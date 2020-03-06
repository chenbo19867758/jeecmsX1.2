package com.jeecms.common.wechat.bean.request.open;


/**
 * {@link}https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1453779503&token=&lang=zh_CN
 * @Description:获取（刷新）授权公众号或小程序的接口调用凭据（令牌） 请求参数
 * @author: wangqq
 * @date:   2018年7月25日 上午10:05:29     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class AuthorizerAccessTokenRequest  {

	/**第三方平台appid*/
	private String componentAppid;
	
	/**授权方appid*/
	private String authorizerAppid;
	
	/**授权方的刷新令牌*/
	private String authorizerRefreshToken;

	public AuthorizerAccessTokenRequest(String componentAppid,String authorizerAppid, String authorizerRefreshToken){
		this.componentAppid = componentAppid;
		this.authorizerAppid = authorizerAppid;
		this.authorizerRefreshToken = authorizerRefreshToken;
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

	public String getAuthorizerRefreshToken() {
		return authorizerRefreshToken;
	}

	public void setAuthorizerRefreshToken(String authorizerRefreshToken) {
		this.authorizerRefreshToken = authorizerRefreshToken;
	}
	
	
}

 