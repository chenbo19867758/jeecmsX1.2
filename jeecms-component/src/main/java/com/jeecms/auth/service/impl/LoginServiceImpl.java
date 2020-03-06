package com.jeecms.auth.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeecms.auth.constants.AuthConstant;
import com.jeecms.auth.constants.AuthConstant.LoginFailProcessMode;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.domain.LoginDetail;
import com.jeecms.auth.domain.LoginToken;
import com.jeecms.auth.dto.RequestLoginUser;
import com.jeecms.auth.dto.TokenDetail;
import com.jeecms.auth.dto.UserDetailImpl;
import com.jeecms.auth.service.*;
import com.jeecms.common.base.domain.RequestLoginTarget;
import com.jeecms.common.captcha.KaptchaConfig;
import com.jeecms.common.constants.ContentSecurityConstants;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.exception.*;
import com.jeecms.common.local.ThreadPoolService;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.DesUtil;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.common.web.cache.CacheConstants;
import com.jeecms.common.web.cache.CacheProvider;
import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.common.web.util.CookieUtils;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.common.web.util.ResponseUtils;
import com.jeecms.common.web.util.UrlUtil;
import com.jeecms.sso.service.CmsSsoService;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.GlobalConfig;
import com.jeecms.system.service.GlobalConfigService;
import com.jeecms.util.SystemContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录service
 * 
 * @author: tom
 * @date: 2019年4月4日 下午6:12:49
 */
@Service
@Transactional
public class LoginServiceImpl implements LoginService {
        static Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

        @Autowired
        protected CoreUserService userService;
        @Autowired
        private TokenService tokenService;
        @Autowired
        private PasswdService passwdService;
        @Autowired
        private GlobalConfigService globalConfigService;
        @Autowired
        private LoginTokenService loginTokenService;
        @Autowired
        private CaptchaService captchaService;
        @Autowired
        private SessionProvider sessionProvider;
        /**
         * Spring Security 的核心操作服务类 在当前类中将使用 UserDetailsService 来获取 userDetails 对象
         */
        @Autowired
        private MyUserDetailsService userDetailsService;
        @Autowired
        private SessionRegistry sessionRegistry;

        @Value("${token.header}")
        private String tokenHeader;

        @Override
        @Transactional(rollbackFor = Exception.class, readOnly = true)
        public UserDetails getUserDetails(String userSource, String identity, String token) throws GlobalException {
                UserDetails userDetails = null;
                if (userSource != null) {
                        userDetails = userDetailsService.loadUserByUsername(identity, token);
                        sessionRegistry.registerNewSession(identity, userDetails);
                }
                return userDetails;
        }

        @Override
        public LoginDetail getLoginDetail(RequestLoginTarget target, String identity) {
                // 会员登录有手机号和邮箱登录
                if (target.equals(RequestLoginTarget.member)) {
                        return userService.findByUsername(identity);
                } else if (target.equals(RequestLoginTarget.admin)) {
                        return userService.findByUsername(identity);
                }
                return null;
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public String generateToken(TokenDetail tokenDetail) throws GlobalException {
                /** 生成token 并保存入库 */
                String token = tokenService.generateToken(tokenDetail);
                LoginToken loginToken = loginTokenService.findByToken(token);
                boolean exist = true;
                if (loginToken == null) {
                        loginToken = new LoginToken();
                        exist = false;
                }
                loginToken.setToken(token);
                loginToken.setExpireTime(tokenService.generateExpirationDate());
                loginToken.setUsername(tokenDetail.getUsername());
                Short loginTarget = LoginToken.LOGIN_TARGET_PLATFORM;
                if (RequestLoginTarget.member.equals(tokenDetail.getUserSource())) {
                        loginTarget = LoginToken.LOGIN_TARGET_MEMBER;
                }
                loginToken.setLoginTarget(loginTarget);
                if (!exist) {
                        loginTokenService.save(loginToken);
                } else {
                        loginTokenService.update(loginToken);
                }
                return token;
        }

        @Override
        public Map<String, Object> login(RequestLoginUser requestLoginUser, HttpServletRequest request,
                        HttpServletResponse response) throws GlobalException {
                /**
                 * 验证用户认证信息是否正确
                 */
                loginValid(requestLoginUser, request, response);
                return login(requestLoginUser.getTarget(), requestLoginUser.getIdentity(),
                                requestLoginUser.getUniquePushId());
        }

        @Override
        public Map<String, Object> login(RequestLoginTarget target, String identity, String uniquePushId)
                        throws GlobalException {
                /**
                 * 获取登录认证信息 LoginDetail
                 */
                LoginDetail loginDetail = getLoginDetail(target, identity);
                TokenDetail tokenDetail = (TokenDetail) loginDetail;
                String token = generateToken(tokenDetail);
                UserDetails userDetails = getUserDetails(target.toString(), identity, token);
                /** 设置security认证信息 */
                setSecuriyAuth(userDetails);
                /**
                 * 验证通过则重置登录错误次数
                 */
                String ip = RequestUtils.getRemoteAddr(RequestUtils.getHttpServletRequest());
                loginAfter(loginDetail, ip, true, uniquePushId, null);
                /**
                 * 认证通过则生成JWT token
                 */
                Map<String, Object> data = new HashMap<>(1);
                CoreUser user = userService.findByUsername(loginDetail.getUsername());
                CmsSite site = SystemContextUtils.getSite(RequestUtils.getHttpServletRequest());
                data.put(tokenHeader, token);
                data.put(AuthConstant.CURR_USER_ORG_ID, user.getOrgId());
                data.put(AuthConstant.CURR_USER_ROLE_ID, user.getRoleIds());
                if(site!=null){
                        data.put("siteId", site.getId());
                        data.put("siteName", site.getName());
                }
                GlobalConfig config = globalConfigService.get();
                /** 管理员登录验证 是否要求强制修改密码、账户安全应用锁定账户 */
                if (RequestLoginTarget.admin.equals(target)) {
                        /** 是否强制要求修改密码 */
                        if (user.getNeedUpdatePassword()) {
                                data.put(AuthConstant.NEED_CHANGE_PASSWORD, true);
                        }
                        /** 长期未登录锁定账号 */
                        boolean needLocked = needLockUser(user, config);
                        if (needLocked) {
                                /** 锁定账户 */
                                user.setEnabled(false);
                                userService.update(user);
                        }
                        /** 从新登录从新拉取用户权限 */
                        user.clearPermCache();
                }
                return data;
        }

        /**
         * 退出删除数据库中存储的token 并清除securityContext
         */
        @Override
        public void logout(String token, HttpServletRequest request, HttpServletResponse response)
                        throws GlobalException {
                if (StringUtils.isNoneBlank(token)) {
                        loginTokenService.physicalDelete(token);
                }
                SecurityContextHolder.clearContext();
                sessionProvider.logout(request, response);
        }

        @Override
        public boolean isForceLogout() throws GlobalException {
                HttpServletRequest request = RequestUtils.getHttpServletRequest();
                GlobalConfig config = globalConfigService.get();
                /** 系统设置开启了自动退出 */
                if (config.getAutoLogoutMinute() > 0) {
                        Object lastOpeTimeObj = sessionProvider.getAttribute(request, AuthConstant.LAST_OPERATE_TIME);
                        Date now = Calendar.getInstance().getTime();
                        Date lastOpeTime = now;
                        if (lastOpeTimeObj != null) {
                                lastOpeTime = (Date) lastOpeTimeObj;
                        }
                        Integer lastOpeMinute = MyDateUtils.getDiffIntMinuteTwoDate(lastOpeTime, now);

                        if (lastOpeMinute >= config.getAutoLogoutMinute()) {
                                return true;
                        }
                }
                return false;

        }

        @Override
        public void forceLogout(HttpServletRequest request, HttpServletResponse response) throws GlobalException {
                boolean isForce = isForceLogout();
                if (isForce) {
                        // 尝试获取请求头的 token
                        String authToken = request.getHeader(this.tokenHeader);
                        if (StringUtils.isBlank(authToken)) {
                                authToken = RequestUtils.getParam(request, this.tokenHeader);
                        }
                        logout(authToken, request, response);
                }
        }

        @Override
        public Authentication setSecuriyAuth(UserDetails userDetails) {
                SecurityContext securityContext = SecurityContextHolder.getContext();
                Authentication auth = securityContext.getAuthentication();
                if (userDetails != null && (auth == null || !auth.isAuthenticated()
                                || WebConstants.ANONYMOUSUSER.equals(auth.getPrincipal()))) {
                        // 生成通过认证
                        auth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                                        userDetails.getAuthorities());
                        // 将权限写入本次会话
                        UsernamePasswordAuthenticationToken userAuth = (UsernamePasswordAuthenticationToken) auth;
                        userAuth.setDetails(userDetails);
                        securityContext.setAuthentication(userAuth);
                        SecurityContextHolder.setContext(securityContext);
                }
                return auth;
        }

        @Override
        public void tryValidRepeatSession(HttpServletRequest request, HttpServletResponse response) {
                SecurityContext sc = SecurityContextHolder.getContext();
                Authentication auth = sc.getAuthentication();
                GlobalConfig config = null;
                try {
                        config = configService.get();
                } catch (GlobalException e2) {
                        logger.error(e2.getMessage());
                }
                if (config != null && !config.getLoginMuti()) {
                        if (auth != null && auth.isAuthenticated()
                                        && !WebConstants.ANONYMOUSUSER.equals(auth.getPrincipal())) {
                                UserDetails userDetails = (UserDetails) auth.getDetails();
                                UserDetailImpl lastLoginPrincipal = (UserDetailImpl) userDetails;
                                String sessionId = sessionProvider.getSessionId(request);
                                /**
                                 * 登录时 getUserDetails 绑定sessionId到UserDetails对象中，若是限制同时登录则把之前的session踢掉
                                 * 这里实现方式是由之前session的请求发起新请求的时候主动退出
                                 */
                                SessionInformation sessionInfo = sessionRegistry
                                                .getSessionInformation(lastLoginPrincipal.getUsername());
                                if (sessionInfo != null) {
                                        lastLoginPrincipal = (UserDetailImpl) sessionInfo.getPrincipal();
                                        if (!sessionId.equals(lastLoginPrincipal.getSessionId())) {
                                                try {
                                                        logout(lastLoginPrincipal.getToken(), request, response);
                                                } catch (GlobalException e) {
                                                        logger.error(e.getMessage());
                                                }
                                        }
                                }
                        }
                }
        }

        private void loginValid(RequestLoginUser requestLoginUser, HttpServletRequest request,
                        HttpServletResponse response) throws GlobalException {
                String ip = RequestUtils.getRemoteAddr(request);
                LoginDetail loginDetail = null;
                /** 登录先退出，防止一个用户一次会话登录多个账户 */
                logout(RequestUtils.getHeaderOrParam(request, tokenHeader), request, response);
                loginDetail = getLoginDetail(requestLoginUser.getTarget(), requestLoginUser.getIdentity());
                if (loginDetail == null) {
                        throw new GlobalException(new AccountCredentialExceptionInfo());
                } else {
                        GlobalConfig config = globalConfigService.get();
                        Integer configLoginErrorCount = config.getLoginErrorCount();
                        JSONObject json = new JSONObject();
                        boolean validCaptcha = false;
                        /** 管理员登录验证 是否要求强制修改密码、账户安全应用锁定账户 */
                        if (RequestLoginTarget.admin.equals(requestLoginUser.getTarget())) {
                                /** 非管理员不可登录后台 */
                                if (!loginDetail.getAdmin()) {
                                        throw new GlobalException(new AccountCredentialExceptionInfo());
                                }
                        }
                        if (RequestLoginTarget.member.equals(requestLoginUser.getTarget())) {
                                /** 会员功能是否关闭，关闭则不可登录 */
                                if (!config.getMemberOpen()) {
                                        String msg = MessageResolver.getMessage(
                                                        SystemExceptionEnum.MEMBER_CLOSE.getCode(),
                                                        SystemExceptionEnum.MEMBER_CLOSE.getDefaultMessage());
                                        String code = SystemExceptionEnum.MEMBER_CLOSE.getCode();
                                        throw new GlobalException(new SystemExceptionInfo(msg, code));
                                }
                                /** 非审核通过的会员不可登录 */
                                if (!loginDetail.getAdmin() && !loginDetail.getChecked()) {
                                        throw new GlobalException(new AccountCredentialExceptionInfo());
                                }
                                /**非审核通过用户不可登录*/
                                if(!loginDetail.getChecked()){
                                        String msg = MessageResolver.getMessage(
                                                MemberExceptionEnum.MEMBER_NEED_CHECKED.getCode(),
                                                MemberExceptionEnum.MEMBER_NEED_CHECKED.getDefaultMessage());
                                        String code = MemberExceptionEnum.MEMBER_NEED_CHECKED.getCode();
                                        throw new GlobalException(new SystemExceptionInfo(msg, code));
                                }
                        }
                        /** 是否禁用的账户 */
                        if (loginDetail.getEnabled() == false) {
                                throw new GlobalException(new AccountLockedExceptionInfo());
                        }
                        String captcha = requestLoginUser.getCaptcha();
                        /**
                         * 登录错误次数超过限定次数，验证码校验或者前端用户输入了验证码则验证码必须要校验
                         */
                        validCaptcha = StringUtils.isNoneBlank(captcha);
                        /** 系统配置登录错误次数超过限制处理方式是需要验证码 */
                        if (!validCaptcha && configLoginErrorCount != 0
                                        && LoginFailProcessMode.needCaptcha.equals(config.getProcessingMode())) {
                                validCaptcha = loginDetail.getLoginErrorCount() >= configLoginErrorCount;
                        }
                        if (validCaptcha) {
                                String captchaSessionid = request.getParameter(KaptchaConfig.PARAM_SESSION_ID);
                                json.put(AuthConstant.NEXT_NEED_CAPTCHA, true);
                                /** 需要验证码的时候如果验证码为空则提示需要验证码，但是code编码 是200 方便第一次加载验证码时候 */
                                if (StringUtils.isBlank(captcha)) {
                                        loginAfter(loginDetail, ip, false, null, config.getProcessingMode());
                                        throw new GlobalException(new CaptchaExceptionInfo(
                                                        SystemExceptionEnum.SUCCESSFUL.getCode(), json));
                                }
                                if (!captchaService.validCaptcha(request, response, captcha, captchaSessionid)) {
                                        loginAfter(loginDetail, ip, false, null, config.getProcessingMode());
                                        throw new GlobalException(new CaptchaExceptionInfo());
                                }
                        	}
						boolean isPasswordValid = passwdService.isPasswordValid(requestLoginUser.getpStr(),
                                     loginDetail.getSalt(), loginDetail.getPassword());
                        if (!isPasswordValid) {
                                boolean nextNeedCaptcha = false;
                                /** 默认无需处理 */
                                LoginFailProcessMode processMode = LoginFailProcessMode.no;
                                /** 检查登录密码最大错误次数 */
                                String username = loginDetail.getUsername();
                                CoreUser user = userService.findByUsername(username);
                                /** 用户登录错误次数超过系统设置错误次数 */
                                if (user.getNeedProcessLoginError(config)) {
                                        processMode = config.getProcessingMode();
                                }
                                if (LoginFailProcessMode.needCaptcha.equals(processMode)) {
                                        nextNeedCaptcha = true;
                                }
                                loginAfter(loginDetail, ip, false, null, processMode);
                                if (RequestLoginTarget.member.equals(loginDetail.getUserSource())) {
                                        if (loginDetail.getLoginErrorCount() >= configLoginErrorCount) {
                                                nextNeedCaptcha = true;
                                        }
                                }
                                json.put(AuthConstant.NEXT_NEED_CAPTCHA, nextNeedCaptcha);
                                throw new GlobalException(new AccountCredentialExceptionInfo(json));
                        }
                }
        }

        @Override
        public void tryTokenLogin(HttpServletRequest request, HttpServletResponse response) throws GlobalException {
                SecurityContext sc = SecurityContextHolder.getContext();
                Authentication auth = sc.getAuthentication();
                /*** 尝试获取请求头的 token */
                String authToken = RequestUtils.getHeaderOrParam(request, this.tokenHeader);
                if (StringUtils.isNoneBlank(authToken)) {
                        Date nowTime = Calendar.getInstance().getTime();
                        // 尝试拿 token 中的 username
                        // 若是没有 token 或者拿 username 时出现异常，那么 username 为 null
                        String username = tokenService.getUsernameFromToken(authToken);
                        String userSource = tokenService.getUserSource(authToken);
                        Date tokenExpireTime = tokenService.getExpirationDateFromToken(authToken);
                        // 如果上面解析 token 成功并且拿到了 username 并且本次会话的权限还未被写入
                        boolean tokenValid = false;
                        // 用 UserDetailsService 从数据库中拿到用户的 UserDetails 类
                        // UserDetails 类是 Spring Security 用于保存用户权限的实体类
                        UserDetails userDetails = null;
                        try {
                                userDetails = getUserDetails(userSource, username, authToken);
                        } catch (GlobalException e1) {
                                logger.error(e1.getMessage());
                        }
                        if (userDetails != null && !userDetails.isEnabled()) {
                                ResponseInfo responseInfo = new ResponseInfo(
                                                SystemExceptionEnum.ACCOUNT_LOCKED.getCode(),
                                                SystemExceptionEnum.ACCOUNT_LOCKED.getDefaultMessage(), request,
                                                request.getRequestURI(), null);
                                ResponseUtils.renderJson((HttpServletResponse) response,
                                                JSON.toJSONString(responseInfo));
                                return;
                        }
                        if (StringUtils.isNoneBlank(username)) {
                                // 检查用户带来的 token 是否有效
                                // 包括 token 和 userDetails 中用户名是否一样， token 是否过期， token
                                // 生成时间是否在最后一次密码修改时间之前
                                // 若是检查通过
                                tokenValid = this.tokenService.validateToken(authToken, userDetails);
                        }
                        /** token是有效的并在兑换期内则生成新的token */
                        if (tokenValid) {
                                Object e = cacheProvider.getCache(CacheConstants.MEMBER, authToken);
                                if (MyDateUtils.getSecondBetweenDate(nowTime, tokenExpireTime) <= tokenExchangeTime
                                                && e == null) {
                                        LoginDetail tokenDetail = getLoginDetail(RequestLoginTarget.valueOf(userSource),
                                                        username);
                                        String newToken = "";
                                        try {
                                                /** 重新生成token 并授权 */
                                                newToken = generateToken(tokenDetail);
                                        } catch (GlobalException ex) {
                                                logger.error(ex.getMessage());
                                        }
                                        request.setAttribute(tokenHeader, newToken);
                                        cacheProvider.setCache(CacheConstants.MEMBER, authToken, true);
                                }
                        } else {
                                /** token无效，且用户处于登录状态,则主动退出 */
                                CoreUser user = SystemContextUtils.getCoreUser();
                                if (user != null) {
                                        logout(authToken, request, response);
                                }
                        }
                        /** token有效并当前未授权或者匿名 */
                        if (tokenValid && StringUtils.isNoneBlank(username)) {
                                if (auth == null || WebConstants.ANONYMOUSUSER.equals(auth.getPrincipal())) {
                                        setSecuriyAuth(userDetails);
                                }
                        }
                }
        }

        @Override
        public void tryFormLogin(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
                Cookie rememberMeCookie = CookieUtils.getCookie(httpRequest, WebConstants.COOKIE_REMEMBER_ME);
                if (rememberMeCookie != null && !UrlUtil.isOpenRequest(httpRequest)) {
                        String identity = RequestUtils.getParam(httpRequest, RequestLoginUser.LOGIN_IDENTITY);
                        String captcha = RequestUtils.getParam(httpRequest, RequestLoginUser.LOGIN_CAPTCHA);
                        String uniquePushId = RequestUtils.getParam(httpRequest, RequestLoginUser.LOGIN_PUSHID);
                        String target = RequestUtils.getParam(httpRequest, RequestLoginUser.LOGIN_TARGET);
                        String pStr = getPStr(httpRequest);
                        if (StringUtils.isBlank(target)) {
                                target = RequestLoginTarget.member.name();
                        }
                        if (StringUtils.isNoneBlank(identity)) {
                                Object loginResult = null;
                                try {
                                        loginResult = login(RequestLoginUser.buildForMemberLogin(identity, pStr,
                                                        RequestLoginTarget.getValueOf(target), captcha, uniquePushId),
                                                        httpRequest, httpResponse);
                                } catch (GlobalException e) {
                                        loginResult = e.getExceptionInfo();
                                }
                                httpRequest.setAttribute(RequestLoginUser.LOGIN_RESULT, loginResult);
                        }
                }
        }

        /**
         * 获取解密的密码
         * 
         * @Title: getPStr
         * @param httpRequest
         *                HttpServletRequest
         * @return: String
         */
        @Override
        public String getPStr(HttpServletRequest httpRequest) {
                /** 加密字符串 */
                String pStrJson = RequestUtils.getParam(httpRequest, ContentSecurityConstants.DES_PARAMETER_NAME);
                /** 解密的密码JSON */
                String decryptStr = DesUtil.decrypt(pStrJson, ContentSecurityConstants.DES_KEY,
                                ContentSecurityConstants.DES_IV);
                String pStr = pStrJson;
                if (StringUtils.isNoneBlank(decryptStr)) {
                        JSONObject passJson = JSONObject.parseObject(decryptStr);
                        Object pObj = passJson.get(RequestLoginUser.LOGIN_P_STR);
                        if (pObj != null) {
                                pStr = (String) pObj;
                        }
                }
                return pStr;
        }


        private void loginAfter(LoginDetail loginDetail, String ip, boolean loginSuccess, String uniquePushId,
                        LoginFailProcessMode processMode) throws GlobalException {
                String username = loginDetail.getUsername();
                ThreadPoolService.getInstance().execute(new Runnable() {
                        @Override
                        public void run() {
                                try {
                                        userService.userLogin(username, ip, loginSuccess, processMode);
                                } catch (GlobalException e) {
                                        logger.error(e.getMessage());
                                }
                        }
                });
        }

        private boolean needLockUser(CoreUser user, GlobalConfig config) {
                Date lastLoginTime = user.getLastLoginTime();
                Date now = Calendar.getInstance().getTime();
                /** 系统开启了长期未登录锁定账号 */
                if (config.getConfigAttr().getSecurityOpen()&&config.getAccountLockAuto() && config.getAccountLockAutoDay() > 0) {
                        if (lastLoginTime != null) {
                                Integer days = MyDateUtils.getDaysBetweenDate(lastLoginTime, now);
                                if (days >= config.getAccountLockAutoDay()) {
                                        return true;
                                }
                        }
                }
                return false;
        }

	@Value("${token.exchangeTime}")
	private Integer tokenExchangeTime;
	@PersistenceContext
	protected EntityManager entityManager;
	@Autowired
	private GlobalConfigService configService;
	@Autowired
	private CacheProvider cacheProvider;
	@Autowired
	private CmsSsoService cmsSsoService;
	
}
