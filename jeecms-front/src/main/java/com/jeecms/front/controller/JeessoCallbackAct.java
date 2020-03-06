package com.jeecms.front.controller;

import static com.jeecms.sso.constants.SsoContants.CLIENT_AUTHTOKEN;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.web.cache.CacheConstants;
import com.jeecms.common.web.cache.CacheProvider;
import com.jeecms.common.web.util.CookieUtils;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.util.FrontUtils;
import com.jeecms.util.SystemContextUtils;

@Controller
public class JeessoCallbackAct {
	
	@Autowired
	private CacheProvider cacheProvider;
	
//	/**
//	 * SSO回调函数
//	* @Title: callback 
//	* @param request 请求
//	* @param response 响应
//	* @param AUTHTOKEN 接收SSO认证中心TOKEN
//	* @param model 模型
//	* @return
//	* @throws GlobalException 异常
//	* @throws IOException 异常
//	 */
//	@RequestMapping(value = "/callback", method = RequestMethod.GET)
//	public String callback(HttpServletRequest request, HttpServletResponse response, String authToken, ModelMap model)
//			throws GlobalException, IOException {
//		if (StringUtils.isNotBlank(authToken)) {
//			//设置cookie
//			CookieUtils.addCookie(request, response, CLIENT_AUTHTOKEN, authToken, null, null);
//		}
//		//将参数获取放到model里面
//		model.addAttribute("authToken", authToken);
//		CmsSite site = SystemContextUtils.getSite(request);
//		//暂时使用IP测试
//		model.addAttribute("callUrl", site.getProtocol() + site.getDomain() + "/admin/sso/getInfo");
//		String ssoTpl = FrontUtils.getSysPagePath(request, "sso", "ssocallback");
//		return ssoTpl;
//	}
}
