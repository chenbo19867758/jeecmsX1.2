package com.jeecms.common.wechat.api.mp.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.WeChatExceptionInfo;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.api.mp.WechatOauthApiService;
import com.jeecms.common.wechat.bean.request.mp.oauth2.GetOpenIdRequest;
import com.jeecms.common.wechat.bean.response.mp.oauth2.GetOpenIdResponse;
import com.jeecms.common.wechat.util.client.HttpUtil;

/**
 * 
 * @Description: 微信公众号网页授权获取openId与微信对接service实现类
 * @author: chenming
 * @date:   2018年10月16日 下午2:05:01     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
public class WechatOauthApiServiceImpl implements WechatOauthApiService{
	
	public final String ACCESS_TOKEN=Const.DoMain.API_URI.concat("/sns/oauth2/access_token");
	public final String GETCODE=Const.DoMain.OPEN_URI.concat("/connect/oauth2/authorize");
	
	@Override
	public GetOpenIdResponse getOpenId(GetOpenIdRequest request) throws GlobalException {
		Map<String,String> params=new HashMap<>(50);
		params.put("appid", request.getAppid());
		params.put("secret", request.getSecret());
		params.put("code", request.getCode());
		params.put("grant_type", "authorization_code");
		GetOpenIdResponse response=HttpUtil.getJsonBean(ACCESS_TOKEN, params, GetOpenIdResponse.class);
		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		}else {
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(),response.getErrmsg()));
		}
	}

}
