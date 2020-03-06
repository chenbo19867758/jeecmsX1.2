/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.admin.controller.sso;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.service.CoreUserService;
import com.jeecms.auth.service.LoginService;
import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.RequestLoginTarget;
import com.jeecms.common.constants.ContentSecurityConstants;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionEnum;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.DesUtil;
import com.jeecms.common.web.cache.CacheProvider;
import com.jeecms.common.wechat.util.client.HttpUtil;
import com.jeecms.sso.constants.SsoContants;
import com.jeecms.sso.dto.SsoLoginDto;
import com.jeecms.sso.dto.SyncRequestDeleteDto;
import com.jeecms.sso.dto.SyncRequestRegisterDto;
import com.jeecms.sso.dto.request.SyncRequestUser;
import com.jeecms.sso.dto.response.SyncResponseUserVo;
import com.jeecms.sso.service.CmsSsoService;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.GlobalConfig;
import com.jeecms.system.domain.GlobalConfigAttr;
import com.jeecms.system.service.GlobalConfigService;
import com.jeecms.util.SystemContextUtils;

/**   
 * 单点登录配置类
 * @author: ljw
 * @date:   2019年10月26日 上午9:17:22     
 */
@RestController
@RequestMapping("/sso")
public class SsoConfigController extends BaseController<GlobalConfig, Integer> {

	@Autowired
	private CmsSsoService cmsSsoService;
	@Autowired
	private GlobalConfigService globalConfigService;
	@Autowired
	private LoginService loginService;
	@Value("${user.auth}")
    protected String userAuth;
	@Value("${sso.login.url}")
	protected String loginUrl;
	@Value("${sso.app.valid.url}")
	protected String loginValidUrl;
	@Autowired
	private CoreUserService coreUserService;
	@Autowired
	private CacheProvider cacheProvider;
	
	/**
	 * 保存单点登录设置
	 */
	@PostMapping
	public ResponseInfo save(@RequestBody @Valid SsoLoginDto dto, BindingResult result)
			throws GlobalException {
		super.validateBindingResult(result);
		return cmsSsoService.saveSsoConfig(dto);
	}
	
	/**
	 * 单点登录设置详情
	 */
	@GetMapping
	public ResponseInfo get() throws GlobalException {
		JSONObject object = new JSONObject();
		GlobalConfig config = globalConfigService.get();
		config.getConfigAttr().getSsoLoginAppId();
		object.put("appId", config.getConfigAttr().getSsoLoginAppId());
		object.put("openSso", config.getConfigAttr().getSsoLoginAppOpen());
		object.put("appSecret", config.getConfigAttr().getSsoLoginAppSecret());
		return new ResponseInfo(object);
	}
	
	/**
	 * 客户端提供同步新增接口
	 */
	@RequestMapping("/sync")
	@MoreSerializeField({
		@SerializeField(clazz = ResponseInfo.class, includes = { "code", "message", "data", }),
		 })
	public ResponseInfo sync(@RequestBody SyncRequestRegisterDto dto)
			throws GlobalException {
		//使用缓存实现立即刷新的效果
		Serializable open = cacheProvider.getCache(WebConstants.SSO_OPEN, WebConstants.SSO_OPEN);
		if (open != null && open.toString().equals("0")) {
			return new ResponseInfo();
		} else if (open == null) {
			//缓存为空，去数据库查询,没开启不同步
			GlobalConfigAttr attr = globalConfigService.get().getConfigAttr();
			if (!attr.getSsoLoginAppOpen()) {
				return new ResponseInfo();
			}
		}
		return cmsSsoService.saveUserNoAuth(dto);
	}
	
	/**
	 * 客户端提供同步修改接口
	 */
	@RequestMapping("/update")
	@MoreSerializeField({
		@SerializeField(clazz = ResponseInfo.class, includes = { "code", "message", "data", }),
		 })
	public ResponseInfo update(@RequestBody SyncRequestUser dto)
			throws GlobalException {
		// 使用缓存实现立即刷新的效果
		Serializable open = cacheProvider.getCache(WebConstants.SSO_OPEN, WebConstants.SSO_OPEN);
		if (open != null && open.toString().equals("0")) {
			return new ResponseInfo();
		} else if (open == null) {
			//缓存为空，去数据库查询,没开启不同步
			GlobalConfigAttr attr = globalConfigService.get().getConfigAttr();
			if (!attr.getSsoLoginAppOpen()) {
				return new ResponseInfo();
			}
		}
		return cmsSsoService.update(dto);
	}
	
	/**
	 * 客户端提供删除用户接口
	 */
	@RequestMapping("/delete")
	@MoreSerializeField({
		@SerializeField(clazz = ResponseInfo.class, includes = { "code", "message", "data", }),
		 })
	public ResponseInfo delete(@RequestBody SyncRequestDeleteDto dto)
			throws GlobalException {
		// 使用缓存实现立即刷新的效果
		Serializable open = cacheProvider.getCache(WebConstants.SSO_OPEN, WebConstants.SSO_OPEN);
		if (open != null && open.toString().equals("0")) {
			return new ResponseInfo();
		} else if (open == null) {
			//缓存为空，去数据库查询,没开启不同步
			GlobalConfigAttr attr = globalConfigService.get().getConfigAttr();
			if (!attr.getSsoLoginAppOpen()) {
				return new ResponseInfo();
			}
		}
		return cmsSsoService.delete(dto);
	}
	
	/**
	 * 客户端提供获取用户登录数据接口
	* @Title: getInfo 
	* @param authToken 令牌
	* @throws GlobalException 异常
	 */
	@GetMapping(value = "/getInfo")
	public ResponseInfo getInfo(HttpServletRequest request, String authToken)
			throws GlobalException {
		Map<String, Object> data = new HashMap<String, Object>(16);
		SyncResponseUserVo vo = cmsSsoService.userVaild(authToken);
		//得到用户信息，返回客户端Token,需要根据每个应用自行处理
		if (vo != null && vo.getUserType().equals(SsoContants.SSO_USER_TYPE_1)) {
			//查看用户是否存在,发出登出的接口，解决无限循环的Bug
			CoreUser user = coreUserService.findByUsernameAndAuth(vo.getUsername());
			if (user != null) {
				data = loginService.login(RequestLoginTarget.admin, vo.getUsername(), null);
				data.put(userAuth, coreUserService.routingTree(vo.getUsername(), request));
				data.put("userType", SsoContants.SSO_USER_TYPE_1);
			} else {
				cmsSsoService.logout(authToken);
				throw new GlobalException(SystemExceptionEnum.ACCOUNT_NOT_LOGIN);
			}
		} else if (vo != null && vo.getUserType().equals(SsoContants.SSO_USER_TYPE_2)) {
			CoreUser user = coreUserService.findByUsernameAndAuth(vo.getUsername());
			if (user != null) {
				CmsSite site = SystemContextUtils.getSite(request);
				data = loginService.login(RequestLoginTarget.member, vo.getUsername(), null);
				data.put("userType", SsoContants.SSO_USER_TYPE_2);
				data.put("url", site.getUrl());
			} else {
				cmsSsoService.logout(authToken);
				throw new GlobalException(SystemExceptionEnum.ACCOUNT_NOT_LOGIN);
			}
		} else if (vo == null) {
			throw new GlobalException(SystemExceptionEnum.ACCOUNT_NOT_LOGIN);
		}
		return new ResponseInfo(data);
	}
	
	/**
	 *获取是否开启单点登录
	* @Title: status 
	* @throws GlobalException 异常
	 * @throws IOException IO异常
	 */
	@GetMapping(value = "/status")
	public ResponseInfo status(HttpServletRequest request)
			throws GlobalException, IOException {
		JSONObject object = new JSONObject();
		GlobalConfig config = globalConfigService.get();
		GlobalConfigAttr attr = config.getConfigAttr();
		// 判断是否开启单点登录
		Boolean open = attr.getSsoLoginAppOpen();
		if (open) {
			// 判断应用是否有效，需要调应用接口
			String appId = attr.getSsoLoginAppId();
			String appSecret = attr.getSsoLoginAppSecret();
			String secret = DesUtil.encrypt(appSecret, ContentSecurityConstants.DES_KEY, 
					ContentSecurityConstants.DES_IV);
			Map<String, String> params = new HashMap<String, String>();
			params.put("appId", appId);
			params.put("appSecret", secret);
			ResponseInfo info = HttpUtil.getJsonBean(loginValidUrl, params, ResponseInfo.class);
			//应用有效的话
			if (info != null && info.getCode() == 200) {
				object.put("open", true);
				object.put("appId", appId);
				object.put("appSecret", secret);
				object.put("loginUrl", loginUrl);
			} else {
				object.put("open", false);
			}
		} else {
			object.put("open", false);
		}
		return new ResponseInfo(object);
	}
	
}
