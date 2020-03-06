package com.jeecms.common.wechat.bean.response.mp.oauth2;

import com.jeecms.common.wechat.bean.response.mp.BaseResponse;

/**
 * 
 * @Description: 微信公众号网页授权获取openId-response
 * @author: chenming
 * @date:   2018年10月16日 下午2:02:22     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class GetOpenIdResponse extends BaseResponse{
	/** 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同*/
	private String accessToken;
	/** access_token接口调用凭证超时时间，单位（秒）*/
	private String expiresIn;
	/** 用户刷新access_token*/
	private String refreshToken;
	/** 用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID*/
	private String openid;
	/** 用户授权的作用域，使用逗号（,）分隔*/
	private String scope;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
	
	
}
