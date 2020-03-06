package com.jeecms.common.wechat.api.open.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.WeChatExceptionInfo;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.annotations.ValidWeChatToken;
import com.jeecms.common.wechat.api.open.OpenPlatformApiService;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.open.AuthorizerAccessTokenRequest;
import com.jeecms.common.wechat.bean.request.open.AuthorizerInfoRequest;
import com.jeecms.common.wechat.bean.request.open.ComponentAccessTokenRequest;
import com.jeecms.common.wechat.bean.request.open.PreauthCodeRequest;
import com.jeecms.common.wechat.bean.request.open.QueryAuthRequest;
import com.jeecms.common.wechat.bean.response.open.AuthorizerAccessTokenResponse;
import com.jeecms.common.wechat.bean.response.open.AuthorizerInfoResponse;
import com.jeecms.common.wechat.bean.response.open.ComponentAccessTokenResponse;
import com.jeecms.common.wechat.bean.response.open.PreauthCodeResponse;
import com.jeecms.common.wechat.bean.response.open.QueryAuthResponse;
import com.jeecms.common.wechat.util.client.HttpUtil;
import com.jeecms.common.wechat.util.serialize.SerializeUtil;

/**
 * 
 * @Description:微信第三方开放平台应用授权流程接口集
 * @author: wangqq
 * @date:   2018年7月25日 下午4:15:46     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
public class OpenPlatformApiServiceImpl implements OpenPlatformApiService {
	
	/** 使用component_verify_ticket刷新component_access_token*/
	public final String API_COMPONENT_TOKEN = Const.DoMain.API_URI.concat("/cgi-bin/component/api_component_token");
	/** 使用authorizer_refresh_token刷新授权公众号调用凭据（authorizer_access_token） */
	public final String API_AUTHORIZER_TOKEN = Const.DoMain.API_URI.concat("/cgi-bin/component/api_authorizer_token");
	/** 获取预授权码pre_auth_code */
	public final String API_CREATE_TOKEN = Const.DoMain.API_URI.concat("/cgi-bin/component/api_create_preauthcode");
	/** 使用授权码换取公众号或小程序的接口调用凭据和授权信息 */
	public final String API_QUERY_AUTH = Const.DoMain.API_URI.concat("/cgi-bin/component/api_query_auth");
	/**获取授权方的帐号基本信息*/
	public final String API_GET_AUTHORIZER_INFO=Const.DoMain.API_URI.concat("/cgi-bin/component/api_get_authorizer_info");
	/** 授权注册页面扫码授权链接 */
	public final String GRANT_AUTH_URL = Const.DoMain.MP_URI.concat("/cgi-bin/componentloginpage?component_appid=%s&pre_auth_code=%s&redirect_uri=%s&auth_type=%s");
	
	public final String COMPONENT_ACCESS_TOKEN = "component_access_token";
	/**授权扫码确认回调地址*/
	public final String GRANT_AUTH_NOTIFY_URL =  "/weChat/authBindNotify/%s";
	
	/**授权的帐号类型 1则商户扫码后，手机端仅展示公众号、2表示仅展示小程序，3表示公众号和小程序都展示*/
	public final int GRANT_AUTH_ACCOUNT_TYPE = 3;

	
	/**
	 * 获取开放平台应用component_token
	 * @throws GlobalException 
	 */
	@Override
	public ComponentAccessTokenResponse getComponentTokenApi(ComponentAccessTokenRequest comAccessTokenATR) throws GlobalException {
		ComponentAccessTokenResponse componentToken =  HttpUtil.postJsonBean(API_COMPONENT_TOKEN, null, 
				SerializeUtil.beanToJson(comAccessTokenATR), ComponentAccessTokenResponse.class);
		if(componentToken.SUCCESS_CODE.equals(componentToken.getErrcode())){
			return componentToken;
		}else{
			throw new GlobalException(new WeChatExceptionInfo(componentToken.getErrcode(), componentToken.getErrmsg()));
		}
	}

	/**
	 * 获取开放平台应用代为公众号或小程序调用authorizer_token
	 */
	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.COMONTENT_ACCESS_TOKEN)
	public AuthorizerAccessTokenResponse getAuthorizerTokenApi(AuthorizerAccessTokenRequest accessTokenATR,ValidateToken validToken)throws GlobalException {
		Map<String, String>  params = new HashMap<String, String>(20);
		params.put(COMPONENT_ACCESS_TOKEN, validToken.getAccessToken());
		AuthorizerAccessTokenResponse authAccessToken =  HttpUtil.postJsonBean(API_AUTHORIZER_TOKEN, params, SerializeUtil.beanToJson(accessTokenATR), AuthorizerAccessTokenResponse.class);
		if(authAccessToken.SUCCESS_CODE.equals(authAccessToken.getErrcode())){
			return authAccessToken;
		}else{
			throw new GlobalException(new WeChatExceptionInfo(authAccessToken.getErrcode(), authAccessToken.getErrmsg()));
		}
	}

	/**
	 * 获取微信公众号及小程序 网页预授权码并返回授权扫码地址
	 * @throws GlobalException 
	 */
	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.COMONTENT_ACCESS_TOKEN)
	public String getPreAuthCodeApi(PreauthCodeRequest preCodeReq,ValidateToken validToken,HttpServletRequest request,String redirectUri) throws GlobalException {
		Map<String, String>  params = new HashMap<String, String>(20);
		params.put(COMPONENT_ACCESS_TOKEN, validToken.getAccessToken());
		PreauthCodeResponse preCodeResponse =  HttpUtil.postJsonBean(API_CREATE_TOKEN, params, SerializeUtil.beanToJson(preCodeReq), PreauthCodeResponse.class);
		if(preCodeResponse.SUCCESS_CODE.equals(preCodeResponse.getErrcode())){
			if(StringUtils.isBlank(redirectUri)){
				redirectUri = EXTERA;
			}
			String authBindNotifyUrl  = RequestUtils.getServerUrl(request)+String.format(GRANT_AUTH_NOTIFY_URL, redirectUri); 
			String url  =  String.format(GRANT_AUTH_URL,preCodeReq.getComponentAppid(), preCodeResponse.getPreAuthCode(),authBindNotifyUrl,GRANT_AUTH_ACCOUNT_TYPE);
			return url;
		}else{
			throw new GlobalException(new WeChatExceptionInfo(preCodeResponse.getErrcode(), preCodeResponse.getErrmsg()));
		}
	}

	/**
	 * 使用授权码换取公众号或小程序的接口调用凭据和授权信息
	 */
	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.COMONTENT_ACCESS_TOKEN)
	public QueryAuthResponse queryAuthApi(QueryAuthRequest authRequest, ValidateToken validToken)
			throws GlobalException {
		Map<String, String>  params = new HashMap<String, String>(20);
		params.put(COMPONENT_ACCESS_TOKEN, validToken.getAccessToken());
		QueryAuthResponse authResponse=HttpUtil.postJsonBean(API_QUERY_AUTH, params, SerializeUtil.beanToJson(authRequest), QueryAuthResponse.class);
		if(authResponse.SUCCESS_CODE.equals(authResponse.getErrcode())){
			return authResponse;
		}else{
			throw new GlobalException(new WeChatExceptionInfo(authResponse.getErrcode(), authResponse.getErrmsg()));
		}
	}


	/**
	 * 获取授权方的帐号基本信息
	 */
	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.COMONTENT_ACCESS_TOKEN)
	public AuthorizerInfoResponse getAuthorizerInfo(AuthorizerInfoRequest authorizerInfoRequest,
			ValidateToken validToken) throws GlobalException {
		Map<String, String>  params = new HashMap<String, String>(20);
		params.put(COMPONENT_ACCESS_TOKEN, validToken.getAccessToken());
		AuthorizerInfoResponse infoResponse=HttpUtil.postJsonBean(API_GET_AUTHORIZER_INFO, params, SerializeUtil.beanToJson(authorizerInfoRequest), AuthorizerInfoResponse.class);
		if(infoResponse.SUCCESS_CODE.equals(infoResponse.getErrcode())){
			return infoResponse;
		}else{
			throw new GlobalException(new WeChatExceptionInfo(infoResponse.getErrcode(), infoResponse.getErrmsg()));
		}
	}
	

}

 