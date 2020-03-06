package com.jeecms.common.web.session;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Session提供者 【小程序访问session不同，小程序需要的数据不可用】
 * @author: tom
 * @date:   2018年2月13日 下午15:40:30     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface SessionProvider {
	/**
	 * 获取session 属性
	 * @Title: getAttribute  
	 * @param request HttpServletRequest
	 * @param name 属性名
	 * @return: Serializable
	 */
	public Serializable getAttribute(HttpServletRequest request, String name);

	/**
	 * 设置session 属性
	 * @Title: setAttribute  
	 * @param request HttpServletRequest
	 * @param name 属性名
	 * @param value  属性值 
	 * @return: void
	 */
	public void setAttribute(HttpServletRequest request,
			String name, Serializable value);

	/**
	 * 获取sessionId
	 * @Title: getSessionId  
	 * @param request HttpServletRequest
	 * @return: String
	 */
	public String getSessionId(HttpServletRequest request);

	/**
	 * 退出 令session失效
	 * @Title: logout  
	 * @param request HttpServletRequest
	 * @param response  HttpServletResponse     
	 * @return: void
	 */
	public void logout(HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 移除属性
	 * @Title: removeAttribute  
	 * @param request HttpServletRequest
	 * @param id    属性名  
	 * @return: void
	 */
	public void removeAttribute(HttpServletRequest request, String id);
	
}
