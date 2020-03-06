package com.jeecms.front.interceptor;

import com.alibaba.fastjson.JSON;
import com.jeecms.auth.constants.AuthConstant;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.service.CoreUserService;
import com.jeecms.common.constants.SysConstants;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.exception.SystemExceptionEnum;
import com.jeecms.common.local.UserThreadLocal;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.UserAgentUtils;
import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.common.web.util.CookieUtils;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.common.web.util.ResponseUtils;
import com.jeecms.common.web.util.UrlUtil;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.util.SystemContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;

/**
 * 会员拦截器
 * 
 * @author: tom
 * @date: 2018年3月30日 下午21:22:24
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class MemberInterceptor implements HandlerInterceptor {

	/**
	 * 进入SpringMVC的Controller之前开始记录日志实体
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
			Object obj) throws Exception {
		SystemContextUtils.setMobile(false);
		SystemContextUtils.setTablet(false);
		/** 优先以cookie，方便预览url跳转 */
		Cookie deviceCookie = CookieUtils.getCookie(request, WebConstants.COOKIE_PREVIEW_DEVICE);
		if (deviceCookie != null) {
			String deviceVal = deviceCookie.getValue();
			if (StringUtils.isBlank(deviceVal) || deviceVal.equals(WebConstants.PREVIEW_DEVICE_PC)) {
				SystemContextUtils.setPc(true);
			} else {
				SystemContextUtils.setMobile(true);
			}
		} else {
			Device device = deviceResolver.resolveDevice(request);
			if (device.isNormal()) {
				SystemContextUtils.setPc(true);
			}
			/** 是手机客户端访问 */
			if (device.isMobile()) {
				SystemContextUtils.setMobile(true);
			}
			/** 如果是平板访问 */
			if (device.isTablet()) {
				SystemContextUtils.setTablet(true);
			}
		}
		/**
		 * 是否微信H5
		 */
		SystemContextUtils.setWxH5(UserAgentUtils.isWxBrower(request));
		/** 设置用户信息 */
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.isAuthenticated() && !WebConstants.ANONYMOUSUSER.equals(auth.getPrincipal())) {
			Object detailObj = auth.getDetails();
			UserDetails userDetails;
			/**WebAuthenticationDetails 下次登录认证对象放在了的auth principal属性中
			 * token传递的认证对象放在了 details属性中*/
			if (detailObj instanceof WebAuthenticationDetails) {
				detailObj = auth.getPrincipal();
				userDetails = (UserDetails) detailObj;
			} else {
				userDetails = (UserDetails) auth.getDetails();
			}
			CoreUser user = userService.findByUsername(userDetails.getUsername());
			SystemContextUtils.setCoreUser(user);
			/** 设置用户线程变量 */
			UserThreadLocal.setUser(user);
			/**设置最后操作时间*/
			if (user != null) {
				sessionProvider.setAttribute(request, AuthConstant.LAST_OPERATE_TIME, 
						Calendar.getInstance().getTime());
			}
		}
		CmsSite site  = SystemContextUtils.getSite(request);
		String header = RequestUtils.getHeaderOrParam(request, redirectHeader);
		/** 站点关闭且非资源等开放请求URL */
		if (!site.getIsOpen() && !UrlUtil.isOpenRequest(request)) {
			if (StringUtils.isNoneBlank(header) && "false".equals(header.toLowerCase())) {
				String msg = MessageResolver.getMessage(SystemExceptionEnum.SITE_CLOSE.getCode(),
						SystemExceptionEnum.SITE_CLOSE.getDefaultMessage());
				String code = SystemExceptionEnum.SITE_CLOSE.getCode();
				ResponseInfo responseInfo = new ResponseInfo(code, msg, request, request.getRequestURI(), null);
				ResponseUtils.renderJson(response, JSON.toJSONString(responseInfo));
			} else {
				ResponseUtils.redirectToSiteClose(request, response);
			}
			return false;
		}
		/** 会员功能关闭且非资源等开放请求URL */
		if (!site.getGlobalConfig().getMemberOpen() && !UrlUtil.isOpenRequest(request)) {
			if (StringUtils.isNoneBlank(header) && "false".equals(header.toLowerCase())) {
				String msg = MessageResolver.getMessage(SystemExceptionEnum.MEMBER_CLOSE.getCode(),
						SystemExceptionEnum.MEMBER_CLOSE.getDefaultMessage());
				String code = SystemExceptionEnum.MEMBER_CLOSE.getCode();
				ResponseInfo responseInfo = new ResponseInfo(code, msg, request, request.getRequestURI(), null);
				ResponseUtils.renderJson(response, JSON.toJSONString(responseInfo));
			} else {
				ResponseUtils.redirectToUrl("/", request, response);
			}
			return false;
		}
		if (obj instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) obj;
			request.setAttribute(SysConstants.HANDLER_METHOD, handlerMethod.getMethod());
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e)
			throws Exception {
		UserThreadLocal.removeUser();
		SystemContextUtils.resetMobile();
		SystemContextUtils.resetPc();
		SystemContextUtils.resetTablet();
		SystemContextUtils.resetWxH5();
		SystemContextUtils.resetCoreUser();
	}

	@Autowired
	private DeviceResolver deviceResolver;
	@Autowired
	private CoreUserService userService;
	@Autowired
	private SessionProvider sessionProvider;
	@Value("${redirect.header}")
	private String redirectHeader;

}
