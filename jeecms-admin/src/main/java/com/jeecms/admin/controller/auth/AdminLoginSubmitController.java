package com.jeecms.admin.controller.auth;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.auth.base.LoginSubmitController;
import com.jeecms.auth.domain.vo.SortMenuVO;
import com.jeecms.auth.dto.RequestLoginUser;
import com.jeecms.auth.service.CoreUserService;
import com.jeecms.common.annotation.ContentSecurity;
import com.jeecms.common.annotation.ContentSecurityAttribute;
import com.jeecms.common.base.domain.RequestLoginTarget;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionEnum;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.sso.constants.SsoContants;
import com.jeecms.sso.service.CmsSsoService;

/**
 * 登录controller
 * @author: tom
 * @date: 2018年3月3日 下午3:13:10
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@RestController
public class AdminLoginSubmitController extends LoginSubmitController {

	@Autowired
	private CoreUserService coreUserService;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = WebConstants.LOGIN_URL, method = RequestMethod.POST)
	@ContentSecurity
	@MoreSerializeField({ @SerializeField(clazz = SortMenuVO.class, excludes = { "parentId" }) })
	@Override
	public ResponseInfo login(HttpServletRequest request, HttpServletResponse response,
			@ContentSecurityAttribute("requestLoginUser") @Valid RequestLoginUser requestLoginUser)
					throws GlobalException {
		requestLoginUser.setTarget(RequestLoginTarget.admin);
		ResponseInfo info = super.login(request, response, requestLoginUser);
		if (SystemExceptionEnum.SUCCESSFUL.getCode().equals(info.getCode().toString())) {
			Map<String, Object> data = (Map<String, Object>) info.getData();
			/**
			 * 管理返回权限
			 */
			if (requestLoginUser.getTarget().equals(RequestLoginTarget.admin)) {
				data.put(userAuth, coreUserService.routingTree(requestLoginUser.getIdentity(),request));
			}
			return new ResponseInfo(SystemExceptionEnum.SUCCESSFUL.getCode(),
					SystemExceptionEnum.SUCCESSFUL.getDefaultMessage(), data);
		} else {
			return info;
		}
	}

	/**
	 * 登出
	* @Title: logout 
	* @param request 请求
	* @param response 响应
	* @throws GlobalException 异常
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public ResponseInfo logout(HttpServletRequest request, 
			HttpServletResponse response, @RequestBody Map<String, Object> map) throws GlobalException {
		//清除SSO登陆token
		String authToken = map.get(SsoContants.SSO_AUTHTOKEN) != null 
				? map.get(SsoContants.SSO_AUTHTOKEN).toString() : "";
		cmsSsoService.logout(authToken);
		String token = map.get("token") != null 
				? map.get("token").toString() : "";
		return super.logout(token, request, response);
	}

	@Value("${token.header}")
	private String tokenHeader;
	@Autowired
	private CmsSsoService cmsSsoService;
}
