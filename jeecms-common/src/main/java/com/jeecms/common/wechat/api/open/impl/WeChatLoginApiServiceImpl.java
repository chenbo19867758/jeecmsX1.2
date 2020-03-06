package com.jeecms.common.wechat.api.open.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.WeChatExceptionInfo;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.annotations.ValidWeChatToken;
import com.jeecms.common.wechat.api.open.WeChatLoginApiService;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.open.GetOpenIdRequest;
import com.jeecms.common.wechat.bean.response.open.GetOpenIdResponse;
import com.jeecms.common.wechat.util.client.HttpUtil;

/**
 * @author: ztx
 * @date:   2018年10月17日 上午11:00:11     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
public class WeChatLoginApiServiceImpl implements WeChatLoginApiService{

	/** 通过code换取access_token与openId*/
	public final String ACCESS_TOKEN=Const.DoMain.API_URI.concat("/sns/oauth2/component/access_token");
	public final String COMPONENT_ACCESS_TOKEN = "component_access_token";
	
	@SuppressWarnings("unused")
	@Override
	@ValidWeChatToken(value = Const.ValidTokenType.COMONTENT_ACCESS_TOKEN)
	public Map<String, Object> login(GetOpenIdRequest getOpenIdRequest, ValidateToken validateToken)
			throws GlobalException {
		Map<String, String> params=new HashMap<>(20);
		// 注意：公众号的appid
		params.put("appid", getOpenIdRequest.getAppid());
		params.put("code", getOpenIdRequest.getCode());
		params.put("grant_type", getOpenIdRequest.getGrantType());
		// 注意：公众号开放平台的appid
		params.put("component_appid", validateToken.getAppId());
		params.put(COMPONENT_ACCESS_TOKEN, validateToken.getAccessToken());
		GetOpenIdResponse getOpenIdRsponse=HttpUtil.getJsonBean(ACCESS_TOKEN, params, GetOpenIdResponse.class);
		if (!getOpenIdRsponse.SUCCESS_CODE.equals(getOpenIdRsponse.getErrcode())) {
			throw new GlobalException(new WeChatExceptionInfo(getOpenIdRsponse.getErrcode(),getOpenIdRsponse.getErrmsg()));
		}
		
		Map<String, String> param = new HashMap<>(20);
		param.put("access_token", getOpenIdRsponse.getAccessToken());
		param.put("openid", getOpenIdRsponse.getOpenid());
		Map<String, Object> result = JSON.parseObject(HttpUtil.get("https://api.weixin.qq.com/sns/userinfo", param));
		String errcode = "errcode";
		if ((result.get(errcode)) != null) {
			throw new GlobalException(new WeChatExceptionInfo(getOpenIdRsponse.getErrcode(),getOpenIdRsponse.getErrmsg()));
		}
		// 普通用户的标识，对当前开发者帐号唯一
		String uOpenId = result.get("openid").toString(); 
		// 普通用户昵称
		String nickName = result.get("nickname").toString();
		// 普通用户性别，1为男性，2为女性
		Object sex = result.get("sex"); 
		// 普通用户个人资料填写的省份
		Object province = result.get("province"); 
		// 普通用户个人资料填写的城市
		Object city = result.get("city"); 
		// 国家，如中国为CN
		Object country = result.get("country"); 
		// 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
		Object headImgUrl = result.get("headimgurl"); 
		// 用户特权信息，json数组，如微信沃卡用户为（chinaunicom）
		Object privilege = result.get("privilege"); 
		// 用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
		Object unionId = result.get("unionid"); 

		return result;
	}

}
