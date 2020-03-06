package com.jeecms.common.wechat.api.applet.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.WeChatExceptionInfo;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.api.applet.GetOpenIdApiService;
import com.jeecms.common.wechat.bean.request.applet.GetAppletOpenIdRequest;
import com.jeecms.common.wechat.bean.response.applet.GetAppletOpenIdResponse;
import com.jeecms.common.wechat.util.client.HttpUtil;

/**
 * 
 * @Description: 微信小程序登录获取openId
 * @author: chenming
 * @date:   2018年11月12日 下午3:52:59     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
public class GetOpenIdApiServiceImpl implements GetOpenIdApiService{
	
	public final String JSCODE2SESSION=Const.DoMain.API_URI.concat("/sns/jscode2session");
	
	@Override
	public GetAppletOpenIdResponse getOpenId(GetAppletOpenIdRequest request) throws GlobalException {
		Map<String, String> params=new HashMap<>(16);
		params.put("appid", request.getAppid());
		params.put("secret", request.getSecret());
		params.put("js_code", request.getJsCode());
		params.put("grant_type", "authorization_code");
		GetAppletOpenIdResponse response=HttpUtil.getJsonBean(JSCODE2SESSION, params, GetAppletOpenIdResponse.class);
		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		}else {
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(),response.getErrmsg()));
		}
	}
	
	
}
