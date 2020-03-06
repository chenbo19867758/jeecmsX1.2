package com.jeecms.common.wechat.bean.request.open;

/**
 * {@link}https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info?component_access_token=xxxx
 * 
 * @Description:获取授权方的帐号基本信息,请求结构参数
 * @Title:GetAuthorizerInfo
 * @author wulongwei
 * @date 2018年7月27日
 */
public class AuthorizerInfoRequest {

	/** 第三方平台appid */
	private String componentAppid;

	/** 授权方appid */
	private String authorizerAppid;
	
	public AuthorizerInfoRequest(String componentAppid,String authorizerAppid) {
		this.componentAppid = componentAppid;
		this.authorizerAppid = authorizerAppid;
	}

	public String getComponentAppid() {
		return componentAppid;
	}

	public void setComponentAppid(String componentAppid) {
		this.componentAppid = componentAppid;
	}

	public String getAuthorizerAppid() {
		return authorizerAppid;
	}

	public void setAuthorizerAppid(String authorizerAppid) {
		this.authorizerAppid = authorizerAppid;
	}
	

}
