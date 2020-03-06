package com.jeecms.common.wechat.bean.request.open;

/**
 * 
 * @Description: 获取openId的Request请求
 * @author: chenming
 * @date:   2018年9月13日 上午9:27:40     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class GetOpenIdRequest {
	/** 公众号的唯一标识*/
	private String appid;
	/** 服务开发方的access_token*/
	private String componentAccessToken;
	/** 服务开发方的appid*/
	private String componentAppid;
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
	public GetOpenIdRequest() {
		super();
	}
	public String getComponentAccessToken() {
		return componentAccessToken;
	}
	public void setComponentAccessToken(String componentAccessToken) {
		this.componentAccessToken = componentAccessToken;
	}
	public String getComponentAppid() {
		return componentAppid;
	}
	public void setComponentAppid(String componentAppid) {
		this.componentAppid = componentAppid;
	}
	public GetOpenIdRequest(String appid, String componentAccessToken, String componentAppid, String code,
			String grantType) {
		super();
		this.appid = appid;
		this.componentAccessToken = componentAccessToken;
		this.componentAppid = componentAppid;
		this.code = code;
		this.grantType = grantType;
	}
	
}
