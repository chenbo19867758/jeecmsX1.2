/**
 * 
 */

package com.jeecms.front.controller;

import static com.jeecms.common.constants.TplConstants.TPLDIR_CSI;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.util.FrontUtils;

/**
 * 登录页控制器
 * 
 * @author: tom
 * @date: 2018年3月3日 下午3:13:10
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Controller
public class LoginController {

	/**
	 * 登录页
	 * @Title: login
	 * @param request  HttpServletRequest
	 * @param model ModelMap
	 * @param backUrl 登录后返回url
	 * @return: String
	 */
	@RequestMapping(value = WebConstants.LOGIN_URL, method = RequestMethod.GET)
	public String login(HttpServletRequest request, ModelMap model, String backUrl) {
		FrontUtils.frontData(request, model);
		/** 非本站地址给到首页 */
		if (StringUtils.isNoneBlank(backUrl)) {
			if (!backUrl.startsWith(RequestUtils.getServerUrl(request))) {
				backUrl = "/";
			}
			model.put("backUrl", backUrl);
		}
		return FrontUtils.getTplPath(request, null, "login");
	}

	/**
	 * 客户端包含
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param model
	 *            ModelMap
	 * @return
	 */
	@RequestMapping(value = "/login_csi")
	public String csi(HttpServletRequest request, ModelMap model) {
		// 将request中所有参数
		model.putAll(RequestUtils.getQueryParams(request));
		FrontUtils.frontData(request, model);
		return FrontUtils.getTplPath(request, TPLDIR_CSI, "login_csi");
	}
	
	
}
