package com.jeecms.common.wechat.api.open.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.WeChatExceptionInfo;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.annotations.ValidWeChatToken;
import com.jeecms.common.wechat.api.open.OauthApiService;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.open.GetOpenIdRequest;
import com.jeecms.common.wechat.bean.response.open.GetOpenIdResponse;
import com.jeecms.common.wechat.util.client.HttpUtil;

/**
 * 
 * TODO
 * @author: tom
 * @date:   2019年3月8日 下午4:37:38
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
public class OauthApiServiceImpl implements OauthApiService{
	
	/** 通过code换取access_token与openId*/
	public final String ACCESS_TOKEN=Const.DoMain.API_URI.concat("/sns/oauth2/component/access_token");
	public final String COMPONENT_ACCESS_TOKEN = "component_access_token";
	
	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.COMONTENT_ACCESS_TOKEN)
	public GetOpenIdResponse getOpenId(GetOpenIdRequest getOpenIdRequest,ValidateToken validateToke) throws GlobalException {
		Map<String, String> params=new HashMap<>(50);
		// 注意：公众号的appid
		params.put("appid", getOpenIdRequest.getAppid());
		params.put("code", getOpenIdRequest.getCode());
		params.put("grant_type", getOpenIdRequest.getGrantType());
		// 注意：公众号开放平台的appid
		params.put("component_appid", validateToke.getAppId());
		params.put(COMPONENT_ACCESS_TOKEN, validateToke.getAccessToken());
		GetOpenIdResponse getOpenIdRsponse=HttpUtil.getJsonBean(ACCESS_TOKEN, params, GetOpenIdResponse.class);
		if (getOpenIdRsponse.SUCCESS_CODE.equals(getOpenIdRsponse.getErrcode())) {
			return getOpenIdRsponse;
		}else {
			throw new GlobalException(new WeChatExceptionInfo(getOpenIdRsponse.getErrcode(),getOpenIdRsponse.getErrmsg()));
		}
	}

}
