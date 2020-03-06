package com.jeecms.common.wechat.bean.request.applet;

/**
 * 
 * @Description: 微信小程序登录获取openId--request请求
 * @author: chenming
 * @date:   2018年11月12日 下午3:51:15     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class GetAppletOpenIdRequest {
	/** 小程序appid*/
	private String appid;
	/** 小程序的secret*/
	private String secret;
	/** 临时登录获取的code*/
	private String jsCode;
	/** 填写为 authorization_code*/
	private String grantType;

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

	public String getJsCode() {
		return jsCode;
	}

	public void setJsCode(String jsCode) {
		this.jsCode = jsCode;
	}

	public String getGrantType() {
		return grantType;
	}

	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}
	
	
}
