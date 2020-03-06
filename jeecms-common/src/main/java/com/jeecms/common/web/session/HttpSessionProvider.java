package com.jeecms.common.web.session;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

/**
 *HttpSession提供类
 * application.properties 全局配置application.sessionProvider  属性 值是http则 生成HttpSessionProvider bean
 * @author: tom
 * @date:   2018年2月22日 下午09:45:30     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Component
public class HttpSessionProvider implements SessionProvider {

	@Override
	public Serializable getAttribute(HttpServletRequest request, String name) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			return (Serializable) session.getAttribute(name);
		} else {
			return null;
		}
	}

	@Override
	public void setAttribute(HttpServletRequest request,
			String name, Serializable value) {
		HttpSession session = request.getSession();
		session.setAttribute(name, value);
	}

	@Override
	public String getSessionId(HttpServletRequest request) {
		return request.getSession().getId();
	}
	

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
	}

	@Override
	public void removeAttribute(HttpServletRequest request, String id) {
		request.getSession().removeAttribute(id);
	}
}
