/**
 *  * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.member.service.impl;

import static com.jeecms.common.exception.error.RPCErrorCodeEnum.THIRD_PARTY_CALL_ERROR;
import static com.jeecms.common.exception.error.RPCErrorCodeEnum.THIRD_PARTY_INFO_UNCONFIGURATION;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.service.LoginService;
import com.jeecms.common.base.domain.RequestLoginTarget;
import com.jeecms.common.base.domain.ThirdPartyResultDTO;
import com.jeecms.common.exception.SystemExceptionEnum;
import com.jeecms.common.exception.error.RPCErrorCodeEnum;
import com.jeecms.common.wechat.util.client.HttpUtil;
import com.jeecms.common.weibo.api.user.WeiboUserService;
import com.jeecms.common.weibo.bean.request.user.WeiboUserRequest;
import com.jeecms.common.weibo.bean.response.user.WeiboUserResponse;
import com.jeecms.member.domain.SysUserThird;
import com.jeecms.member.service.SysUserThirdService;
import com.jeecms.member.service.ThirdPartyUserService;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.SysThird;
import com.jeecms.system.service.CmsSiteService;
import com.jeecms.system.service.SysThirdService;
import com.jeecms.util.SystemContextUtils;
import com.jeecms.weibo.domain.vo.WeiboTokenVO;

/**
 * 第三方用户登录
 * @author: ljw
 * @date: 2019年7月19日 下午4:37:40
 */
@Service
public class ThirdPartyUserServiceImpl implements ThirdPartyUserService {
	
	private Logger logger = LoggerFactory.getLogger(ThirdPartyUserServiceImpl.class);

	@Override
	public ThirdPartyResultDTO loginForWeChatPc(String code, String backUrl, Integer siteId) throws Exception {
		// 根据授权后的code获取access_token
		SysThird thirdInfo = thirdService.getCode(SysThird.WECHAT);
		if (thirdInfo == null || !thirdInfo.getIsEnable()) {
			return new ThirdPartyResultDTO(Integer.valueOf(THIRD_PARTY_INFO_UNCONFIGURATION.getCode()),
					THIRD_PARTY_INFO_UNCONFIGURATION.getDefaultMessage());
		}
		Map<String, String> param = ImmutableMap.of("appid", thirdInfo.getAppId(),
				"secret", thirdInfo.getAppKey(),
				"code", code, "grant_type", "authorization_code");
		JSONObject result = JSON.parseObject(HttpUtil.get("https://api.weixin.qq.com/sns/oauth2/access_token", param));
		String errorCode = result.getString("errcode");
		String errMsg = null;
		if (StringUtils.isNotBlank(errorCode)) {
			errMsg = result.getString("errmsg");
			logger.error("获取微信 access_token失败!响应状态码: {} ,提示信息: {}", errorCode, errMsg);
			return new ThirdPartyResultDTO(Integer
					.valueOf(RPCErrorCodeEnum.THIRD_PARTY_CALL_ERROR.getCode()), 
					errMsg.toString());
		}
		// 接口调用凭证,有效期3个月
		String accToken = result.getString("access_token"); 
		// 授权用户唯一标识
		String openId = result.getString("openid"); 
		// 获取用户个人信息,在用户修改微信头像后，旧的微信头像URL将会失效，
		//因此开发者应该自己在获取用户信息后，将头像图片保存下来，避免微信头像URL失效后的异常情况
		// 开发者最好保存用户unionID信息，以便以后在不同应用中进行用户信息互通。
		param = ImmutableMap.of("access_token", accToken, "openid", openId);
		JSONObject object = JSON.parseObject(HttpUtil.get("https://api.weixin.qq.com/sns/userinfo", param));
		if (StringUtils.isNotBlank(errorCode = result.getString("errcode"))) {
			errMsg = result.getString("errmsg");
			logger.error("获取微信 用户个人信息失败!响应状态码: {} ,提示信息: {}", errorCode, errMsg);
			return new ThirdPartyResultDTO(Integer.valueOf(THIRD_PARTY_CALL_ERROR.getCode()), 
					errMsg.toString());
		}
		// 普通用户昵称
		String nickname = object.getString("nickname");
		//用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
		//String unionId = object.getString("unionid"); 
		JSONObject resultJson = new JSONObject();
		resultJson.put("nickname", nickname);
		//跳转地址
		resultJson.put("redirectUrl", "");
		//判断是否绑定第三方登录
		List<SysUserThird> thirdUser = sysUserThirdService.findByThirdId(openId, SysThird.WECHAT);
		if (thirdUser.isEmpty()) {
			resultJson.put("bind", false);
		} else {
			//如果绑定直接内部登录
			resultJson.put("bind", true);
			SysUserThird fir = thirdUser.get(0);
			CoreUser member = fir.getMember();
			Map<String, Object> authToken = loginService
					.login(RequestLoginTarget.member, member.getUsername(), null);
			resultJson.putAll(authToken);
		}
		//登录类型
		resultJson.put("loginType", SysThird.WECHAT);
		//用户第三方ID
		resultJson.put("uid", openId);
		if (siteId != null) {
			CmsSite site = cmsSiteService.findById(siteId);
			String redirectUrl = site.getMemberRedirectUrl();
			resultJson.put("redirectUrl", redirectUrl);
		}
		return new ThirdPartyResultDTO(Integer.valueOf(SystemExceptionEnum.SUCCESSFUL.getCode()),
				SystemExceptionEnum.SUCCESSFUL.getDefaultMessage(), resultJson.toJSONString());
	}

	@Override
	public ThirdPartyResultDTO loginForQQ(String code, String backUrl, Integer siteId) throws Exception {
		// 获取accessToken
		SysThird third = thirdService.getCode(SysThird.QQ);
		if (third == null || !third.getIsEnable()) {
			return new ThirdPartyResultDTO(Integer.valueOf(THIRD_PARTY_INFO_UNCONFIGURATION.getCode()),
					THIRD_PARTY_INFO_UNCONFIGURATION.getDefaultMessage());
		}
		Map<String, String> param = ImmutableMap.of("grant_type", "authorization_code", 
				"client_id", third.getAppId(),
				"client_secret", third.getAppKey(), "code", code, "redirect_uri",
				backUrl);
		String tokenStr = HttpUtil.get("https://graph.qq.com/oauth2.0/token", param);
		Map<String, Object> result = new HashMap<>();
		for (String it : tokenStr.split("&")) {
			String[] tToken = it.split("=");
			result.put(tToken[0], tToken[1]);
		}
		Object resCode = result.get("code");
		Object resMsg = result.get("msg");
		String strCode = "";
		String strMsg = "";
		if (null != resMsg) {
			strMsg = resMsg.toString();
			strMsg = java.text.Normalizer.normalize(strMsg, java.text.Normalizer.Form.NFKD);
		}
		if (resCode != null) {
			strCode = resCode.toString();
			strCode = java.text.Normalizer.normalize(strCode, java.text.Normalizer.Form.NFKD);
			logger.error("获取QQ 用户OpenID失败!响应状态码: {} ,提示信息: {}", resCode, resMsg);
			return new ThirdPartyResultDTO(Integer.valueOf(THIRD_PARTY_CALL_ERROR.getCode()), 
					resMsg.toString());
		}
		// 根据accessToken获取用户OpenId
		String accessToken = result.get("access_token").toString(); // 授权令牌，Access_Token
		param = ImmutableMap.of("access_token", accessToken);
		String meStr = HttpUtil.get("https://graph.qq.com/oauth2.0/me", param);
		if (StringUtils.isBlank(meStr) || meStr.indexOf("(") == -1) { // 返回的参数不是jsonp格式
			for (String it : meStr.split("&")) {
				String[] tToken = it.split("=");
				result.put(tToken[0], tToken[1]);
			}
			logger.error("获取QQ 用户OpenID失败!响应状态码: {} ,提示信息: {}", strCode, strMsg);
			return new ThirdPartyResultDTO(Integer.valueOf(THIRD_PARTY_CALL_ERROR.getCode()), 
					resMsg.toString());
		}
		meStr = meStr.substring(meStr.indexOf("(") + 1, meStr.lastIndexOf(")"));
		result = JSON.parseObject(meStr).getInnerMap(); // 解析JSON
		String openId = result.get("openid").toString(); // 登录用户openId
		// 获取用户信息
		param = ImmutableMap.of("access_token", accessToken, "oauth_consumer_key", 
				third.getAppId(), "openid", openId);
		
		JSONObject object = JSON.parseObject(HttpUtil
				.get("https://graph.qq.com/user/get_user_info", param));
		String nickname = object.getString("nickname"); // 用户在QQ空间的昵称
		if (!result.get("ret").equals(0)) {
			resMsg = result.get("msg");
			logger.error("获取用户信息失败:{}", resMsg);
			return new ThirdPartyResultDTO(Integer.valueOf(THIRD_PARTY_CALL_ERROR.getCode()), 
					resMsg.toString());
		}
		JSONObject resultJson = new JSONObject();
		resultJson.put("nickname", nickname);
		//跳转地址
		resultJson.put("redirectUrl", "");
		//判断是否绑定第三方登录
		List<SysUserThird> thirdUser = sysUserThirdService.findByThirdId(openId, SysThird.QQ);
		if (thirdUser.isEmpty()) {
			resultJson.put("bind", false);
		} else {
			//如果绑定直接内部登录
			resultJson.put("bind", true);
			SysUserThird fir = thirdUser.get(0);
			CoreUser member = fir.getMember();
			Map<String, Object> authToken = loginService
					.login(RequestLoginTarget.member, member.getUsername(), null);
			resultJson.putAll(authToken);
		}
		//登录类型
		resultJson.put("loginType", SysThird.QQ);
		//用户第三方ID
		resultJson.put("uid", openId);
		if (siteId != null) {
			CmsSite site = cmsSiteService.findById(siteId);
			String redirectUrl = site.getMemberRedirectUrl();
			resultJson.put("redirectUrl", redirectUrl);
		}
		return new ThirdPartyResultDTO(Integer.valueOf(SystemExceptionEnum.SUCCESSFUL.getCode()),
				SystemExceptionEnum.SUCCESSFUL.getDefaultMessage(), resultJson.toJSONString());
	}

	@Override
	public ThirdPartyResultDTO loginForSina(WeiboTokenVO vo, Integer siteId) throws Exception {
		JSONObject object = new JSONObject();
		//得到用户信息
		WeiboUserResponse response = weiboUserService.getWeiboUser(new WeiboUserRequest(vo.getAccessToken(),
				Long.valueOf(vo.getUid())));
		//用户昵称
		object.put("nickname", response.getScreenName());
		//登录类型
		object.put("loginType", SysThird.WEIBO);
		//用户第三方ID
		object.put("uid", response.getIdstr());
		//跳转地址
		object.put("redirectUrl", "");
		//判断是否绑定第三方登录
		List<SysUserThird> third = sysUserThirdService.findByThirdId(vo.getUid(), SysThird.WEIBO);
		if (third.isEmpty()) {
			object.put("bind", false);
		} else {
			//如果绑定直接内部登录
			object.put("bind", true);
			SysUserThird fir = third.get(0);
			CoreUser member = fir.getMember();
			Map<String, Object> authToken = loginService
					.login(RequestLoginTarget.member, member.getUsername(), null);
			object.putAll(authToken);
		}
		if (siteId != null) {
			CmsSite site = cmsSiteService.findById(siteId);
			String redirectUrl = site.getMemberRedirectUrl();
			object.put("redirectUrl", redirectUrl);
		}
		return new ThirdPartyResultDTO(Integer.valueOf(SystemExceptionEnum.SUCCESSFUL.getCode()),
				SystemExceptionEnum.SUCCESSFUL.getDefaultMessage(), object.toJSONString());
	}

	@Autowired
	private SysThirdService thirdService;
	@Autowired
	private WeiboUserService weiboUserService;
	@Autowired
	private SysUserThirdService sysUserThirdService;
	@Autowired
	private LoginService loginService;
	@Autowired
	private CmsSiteService cmsSiteService;
}
