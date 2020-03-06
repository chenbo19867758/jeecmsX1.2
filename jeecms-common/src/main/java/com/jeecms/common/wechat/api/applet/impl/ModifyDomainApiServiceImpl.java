package com.jeecms.common.wechat.api.applet.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.WeChatExceptionInfo;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.annotations.ValidWeChatToken;
import com.jeecms.common.wechat.api.applet.ModifyDomainApiService;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.applet.ModifyDomainRequest;
import com.jeecms.common.wechat.bean.response.applet.ModifyDomainData;
import com.jeecms.common.wechat.bean.response.applet.ModifyDomainResponse;
import com.jeecms.common.wechat.util.client.HttpUtil;

/**
 * 
 * TODO
 * @author: tom
 * @date:   2019年3月8日 下午4:37:27
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
public class ModifyDomainApiServiceImpl implements ModifyDomainApiService{

public final String GET_CATEGORY=Const.DoMain.API_URI.concat("/wxa/modify_domain");
	
	public final String ACCESS_TOKEN="access_token";
	public final String GET="get";
	public final String SET="set";
	@Override
	@ValidWeChatToken(value = Const.ValidTokenType.ACCESS_TOKEN)
	public ModifyDomainData getModifyDomain(ValidateToken validateToken) throws GlobalException {
		Map<String, String> params=new HashMap<>(500);
		params.put(ACCESS_TOKEN, validateToken.getAccessToken());
		ModifyDomainRequest postData=new ModifyDomainRequest();
		postData.setAction(GET);
		ModifyDomainResponse response = HttpUtil.postJsonBean(GET_CATEGORY, params, JSONObject.toJSONString(postData), ModifyDomainResponse.class);
		ModifyDomainData data=new ModifyDomainData();
		data.setDownloaddomain(response.getDownloaddomain());
		data.setRequestdomain(response.getRequestdomain());
		data.setUploaddomain(response.getUploaddomain());
		data.setWsrequestdomain(response.getWsrequestdomain());
		if(response.SUCCESS_CODE.equals(response.getErrcode())) {
			return data;
		}else {
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(),response.getErrmsg()));
		}
		
	}

	@Override
	@ValidWeChatToken(value = Const.ValidTokenType.ACCESS_TOKEN)
	public ModifyDomainData setModifyDomain(ValidateToken validateToken , ModifyDomainRequest request) throws GlobalException {
		Map<String, String> params=new HashMap<>(500);
		params.put(ACCESS_TOKEN, validateToken.getAccessToken());
		ModifyDomainRequest postData=new ModifyDomainRequest();
		postData.setAction(SET);
		postData.setDownloaddomain(request.getDownloaddomain());
		postData.setRequestdomain(request.getRequestdomain());
		postData.setUploaddomain(request.getUploaddomain());
		postData.setWsrequestdomain(request.getWsrequestdomain());
		ModifyDomainResponse response = HttpUtil.postJsonBean(GET_CATEGORY, params, JSONObject.toJSONString(postData), ModifyDomainResponse.class);
		ModifyDomainData data=new ModifyDomainData();
		data.setDownloaddomain(response.getDownloaddomain());
		data.setRequestdomain(response.getRequestdomain());
		data.setUploaddomain(response.getUploaddomain());
		data.setWsrequestdomain(response.getWsrequestdomain());
		if(response.SUCCESS_CODE.equals(response.getErrcode())) {
			return data;
		}else {
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(),response.getErrmsg()));
		}
	}

}
