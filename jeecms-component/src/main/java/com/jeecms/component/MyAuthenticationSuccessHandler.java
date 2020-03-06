/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeecms.auth.constants.AuthConstant;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.dto.RequestLoginUser;
import com.jeecms.auth.service.CaptchaService;
import com.jeecms.auth.service.CoreUserService;
import com.jeecms.auth.service.LoginService;
import com.jeecms.common.base.domain.RequestLoginTarget;
import com.jeecms.common.captcha.KaptchaConfig;
import com.jeecms.common.exception.*;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.common.web.util.ResponseUtils;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.GlobalConfig;
import com.jeecms.system.service.CmsSiteService;
import com.jeecms.system.service.GlobalConfigService;
import com.jeecms.auth.constants.AuthConstant.LoginFailProcessMode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义登录成功处理类
 * 
 * @author: tom
 * @date: 2019年7月25日 上午8:49:38
 */
@Component
public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	 	static Logger logger = LoggerFactory.getLogger(MyAuthenticationSuccessHandler.class);
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                        Authentication authentication) throws IOException, ServletException {
        		/**Security认证通过后生成token*/
                String msg = MessageResolver.getMessage(SystemExceptionEnum.SUCCESSFUL.getCode(),
                                SystemExceptionEnum.SUCCESSFUL.getDefaultMessage());
                String code = SystemExceptionEnum.SUCCESSFUL.getCode();
                Object loginResult = new Object();
                try {
                        Object identityObj = request.getAttribute(RequestLoginUser.LOGIN_IDENTITY);
                        if (identityObj != null) {
                                CoreUser user = userService.findByUsername((String) identityObj);
                                GlobalConfig config = globalConfigService.get();
                                Integer configLoginErrorCount = config.getLoginErrorCount();
                                JSONObject json = new JSONObject();
                                boolean validCaptcha = false;
                                String captcha = RequestUtils.getParam(request,RequestLoginUser.LOGIN_CAPTCHA);
                                /**
                                 * 登录错误次数超过限定次数，验证码校验或者前端用户输入了验证码则验证码必须要校验
                                 */
                                validCaptcha = user.getLoginErrorCount() >= configLoginErrorCount;
                                if (validCaptcha) {
                                        String captchaSessionid = request.getParameter(KaptchaConfig.PARAM_SESSION_ID);
                                        String ip = RequestUtils.getRemoteAddr(request);
                                        json.put(AuthConstant.NEXT_NEED_CAPTCHA, true);
                                        /** 需要验证码的时候如果验证码为空则提示需要验证码，但是code编码 是200 方便第一次加载验证码时候 */
                                        if (StringUtils.isBlank(captcha)) {
                                                userService.userLogin(user.getUsername(), ip, false,  config.getProcessingMode());
                                                msg = MessageResolver.getMessage(
                                                        SystemExceptionEnum.CAPTCHA_ERROR.getCode(),
                                                        SystemExceptionEnum.CAPTCHA_ERROR.getDefaultMessage());
                                                code = SystemExceptionEnum.CAPTCHA_ERROR.getCode();
                                                loginService.logout(null, request, response);
                                                user = null;
                                        }
                                        if (!captchaService.validCaptcha(request, response, captcha, captchaSessionid)) {
                                                userService.userLogin(user.getUsername(), ip, false,  config.getProcessingMode());
                                                msg = MessageResolver.getMessage(
                                                        SystemExceptionEnum.CAPTCHA_ERROR.getCode(),
                                                        SystemExceptionEnum.CAPTCHA_ERROR.getDefaultMessage());
                                                code = SystemExceptionEnum.CAPTCHA_ERROR.getCode();
                                                loginService.logout(null, request, response);
                                                user = null;
                                        }
                                }
                                if (user != null) {
                                        /** 会员功能是否关闭，关闭则不可登录 */
                                        if (!config.getMemberOpen()) {
                                                 msg = MessageResolver.getMessage(
                                                        SystemExceptionEnum.MEMBER_CLOSE.getCode(),
                                                        SystemExceptionEnum.MEMBER_CLOSE.getDefaultMessage());
                                                 code = SystemExceptionEnum.MEMBER_CLOSE.getCode();
                                                loginService.logout(null, request, response);
                                        }
                                        /**非审核通过用户不可登录*/
                                        else if (!user.getChecked()) {
                                                msg = MessageResolver.getMessage(
                                                        MemberExceptionEnum.MEMBER_NEED_CHECKED.getCode(),
                                                        MemberExceptionEnum.MEMBER_NEED_CHECKED.getDefaultMessage());
                                                code = MemberExceptionEnum.MEMBER_NEED_CHECKED.getCode();
                                                loginService.logout(null, request, response);
                                        } else if (!user.getEnabled()) {
                                                /** 是否禁用的账户 */
                                                msg = MessageResolver.getMessage(
                                                        SystemExceptionEnum.ACCOUNT_LOCKED.getCode(),
                                                        SystemExceptionEnum.ACCOUNT_LOCKED.getDefaultMessage());
                                                code = SystemExceptionEnum.ACCOUNT_LOCKED.getCode();
                                                loginService.logout(null, request, response);
                                        } else {
                                                loginResult = loginService.login(RequestLoginTarget.member,
                                                                (String) identityObj, null);
                                        }
                                }
                        }
                } catch (GlobalException e) {
                        logger.error(e.getMessage());
                }
                CmsSite site;
                String redirectUrl="";
				try {
					site = siteService.getCurrSite(request, response);
					redirectUrl = site.getMemberRedirectUrl();
				} catch (GlobalException e) {
					logger.error(e.getMessage());
				}
                ResponseInfo responseInfo = new ResponseInfo(code, msg, request, redirectUrl, loginResult);
                ResponseUtils.renderJson(response, JSON.toJSONString(responseInfo));
        }

        @Autowired
        private LoginService loginService;
        @Autowired
        private CoreUserService userService;
        @Autowired
        private CmsSiteService siteService;
        @Autowired
        private GlobalConfigService globalConfigService;
        @Autowired
        private CaptchaService captchaService;
}
