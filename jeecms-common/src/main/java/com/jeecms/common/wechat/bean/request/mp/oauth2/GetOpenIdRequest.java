package com.jeecms.common.wechat.bean.request.mp.oauth2;

/**
 * 
 * @Description: 微信公众号网页授权获取openId-request
 * @author: chenming
 * @date:   2018年10月16日 下午2:03:45     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class GetOpenIdRequest {
	/** 公众号的唯一标识*/
	private String appid;
	/** 公众号的appsecret*/
	private String secret;
	/** 填写第一步获取的code参数*/
	private String code;
	/** 填写为authorization_code*/
	private String grantType="authorization_code";

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getGrantType() {
		return grantType;
	}

	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

	public GetOpenIdRequest(String appid, String secret, String code, String grantType) {
		super();
		this.appid = appid;
		this.secret = secret;
		this.code = code;
		this.grantType = grantType;
	}

	public GetOpenIdRequest() {
		super();
	}
	
}
