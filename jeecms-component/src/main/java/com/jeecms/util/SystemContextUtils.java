package com.jeecms.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.GlobalConfig;
import com.jeecms.system.domain.dto.GlobalConfigDTO;
import com.jeecms.wechat.domain.AbstractWeChatInfo;

/**
 * 提供一些系统中使用到的全局共用方法 比如获得用户信息,获得全局配置信息
 * 
 * @author: tom
 * @date: 2018年3月26日 下午9:18:09
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class SystemContextUtils {
	/**
	 * 是否手机访问线程变量
	 */
	private static ThreadLocal<Boolean> isMobileHolder = new ThreadLocal<Boolean>();
	private static ThreadLocal<Boolean> isTabletHolder = new ThreadLocal<Boolean>();
	private static ThreadLocal<Boolean> isPcHolder = new ThreadLocal<Boolean>();
	private static ThreadLocal<Boolean> isWxH5Holder = new ThreadLocal<Boolean>();
	private static ThreadLocal<CoreUser> userHolder = new ThreadLocal<CoreUser>();

	public static void setMobile(boolean isMobile) {
		isMobileHolder.set(isMobile);
	}

	public static boolean isMobile() {
		return isMobileHolder.get() != null && isMobileHolder.get();
	}

	public static void resetMobile() {
		isMobileHolder.remove();
	}
	
	public static void setWxH5(boolean isWxH5) {
		isWxH5Holder.set(isWxH5);
	}

	public static boolean isWxH5() {
		return isWxH5Holder.get() != null && isWxH5Holder.get();
	}

	public static void resetWxH5() {
		isWxH5Holder.remove();
	}

	public static void setPc(boolean isPc) {
		isPcHolder.set(isPc);
	}

	public static boolean isPc() {
		return isPcHolder.get() != null && isPcHolder.get();
	}

	public static void resetPc() {
		isPcHolder.remove();
	}

	public static void setTablet(boolean isTablet) {
		isTabletHolder.set(isTablet);
	}

	public static boolean isTablet() {
		return isTabletHolder.get() != null && isTabletHolder.get();
	}

	public static void resetTablet() {
		isTabletHolder.remove();
	}

	public static void setCoreUser(CoreUser coreUser) {
		userHolder.set(coreUser);
	}

	public static CoreUser getCoreUser() {
		return userHolder.get();
	}

	public static void resetCoreUser() {
		userHolder.remove();
	}

	/**
	 * 平台用户KEY
	 */
	public static final String USER_KEY = "user_key";
	/**
	 * 公众号信息KEY
	 */
	public static final String WECHAT_KEY = "weChat_key";
	/**
	 * 公众号信息KEY
	 */
	public static final String SMALL_PROGRAM_KEY = "small_program_key";
	/**
	 * 全局配置KEY
	 */
	public static final String GLOBAL_CONFIG_KEY = "_global_config_key";
	/**
	 * 对外输出的配置信息key
	 */
	public static final String RESPONSE_GLOBAL_CONFIG_KEY = "response_global_config_key";
	/**
	 * 站点KEY
	 */
	public static final String SITE_KEY = "_site_key";

	/**
	 * 获得平台管理用户
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return
	 */
	public static CoreUser getUser(HttpServletRequest request) {
		// return (CoreUser) request.getAttribute(USER_KEY);
		return getCoreUser();
	}

	/**
	 * 获得用户ID
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return
	 */
	public static Integer getUserId(HttpServletRequest request) {
		CoreUser user = getUser(request);
		if (user != null) {
			return user.getId();
		} else {
			return null;
		}
	}

	/**
	 * 设置用户
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param user
	 *            CoreUser
	 */
	public static void setUser(HttpServletRequest request, CoreUser user) {
		request.setAttribute(USER_KEY, user);
	}

	/**
	 * 设置公众号信息（平台或商家授权）
	 * 
	 * @param request
	 *            HttpServletRequest
	 */
	public static void setWeChat(HttpServletRequest request, AbstractWeChatInfo weChatInfo) {
		request.setAttribute(WECHAT_KEY, weChatInfo);
	}

	/**
	 * 设置小程序信息（平台或商家授权）
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param weChatInfo
	 *            AbstractWeChatInfo
	 */
	public static void setSmallProgram(HttpServletRequest request, AbstractWeChatInfo weChatInfo) {
		request.setAttribute(SMALL_PROGRAM_KEY, weChatInfo);
	}

	/**
	 * 从springsecurity上下文中获取当前用户名
	 * 
	 * @Title: getCurrentUsername
	 * @return: String
	 */
	public static String getCurrentUsername() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		if (securityContext != null) {
			Authentication auth = securityContext.getAuthentication();
			if (auth != null && auth.isAuthenticated()) {
				Object detailObj = auth.getDetails();
				UserDetails userDetails;
				/**
				 * WebAuthenticationDetails 下次登录认证对象放在了的auth principal属性中
				 * token传递的认证对象放在了 details属性中
				 */
				if (detailObj instanceof WebAuthenticationDetails) {
					detailObj = auth.getPrincipal();
					userDetails = (UserDetails) detailObj;
				} else {
					userDetails = (UserDetails) auth.getDetails();
				}
				if (userDetails != null) {
					return userDetails.getUsername();
				}
			}
		}
		return WebConstants.ANONYMOUSUSER;
	}

	/**
	 * 获得全局配置
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return
	 */
	public static GlobalConfig getGlobalConfig(HttpServletRequest request) {
		return (GlobalConfig) request.getAttribute(GLOBAL_CONFIG_KEY);
	}

	/**
	 * 设置全局配置
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param globalConfig
	 *            GlobalConfig
	 */
	public static void setGlobalConfig(HttpServletRequest request, GlobalConfig globalConfig) {
		request.setAttribute(GLOBAL_CONFIG_KEY, globalConfig);
	}

	/**
	 * 获取公众号信息（平台或商家授权）
	 * 
	 * @param request
	 *            HttpServletRequest
	 */
	public static AbstractWeChatInfo getWeChat(HttpServletRequest request) {
		return (AbstractWeChatInfo) request.getAttribute(WECHAT_KEY);
	}

	/**
	 * 获取小程序信息（平台或商家授权）
	 * 
	 * @param request
	 *            HttpServletRequest
	 */
	public static AbstractWeChatInfo getSmallProgram(HttpServletRequest request) {
		return (AbstractWeChatInfo) request.getAttribute(SMALL_PROGRAM_KEY);
	}

	public static void setResponseConfigDto(HttpServletRequest request, GlobalConfigDTO config) {
		request.setAttribute(RESPONSE_GLOBAL_CONFIG_KEY, config);
	}

	public static GlobalConfigDTO getResponseConfigDto(HttpServletRequest request) {
		return (GlobalConfigDTO) request.getAttribute(RESPONSE_GLOBAL_CONFIG_KEY);
	}

	/**
	 * 获得站点
	 * @param request request
	 * 
	 * @return
	 */
	public static CmsSite getSite(HttpServletRequest request) {
		return (CmsSite) request.getAttribute(SITE_KEY);
	}

	/**
	 * 设置站点
	 * @param request request
	 * @param site
	 *            站点
	 */
	public static void setSite(HttpServletRequest request, CmsSite site) {
		request.setAttribute(SITE_KEY, site);
	}

	/**
	 * 获得站点ID
	 * @param request request
	 * 
	 * @return
	 */
	public static Integer getSiteId(HttpServletRequest request) {
		return getSite(request).getId();
	}

}
