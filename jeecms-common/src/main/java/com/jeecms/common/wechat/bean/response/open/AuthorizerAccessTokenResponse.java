package com.jeecms.common.wechat.bean.response.open;

import com.jeecms.common.wechat.bean.response.open.BaseOpenResponse;

/**
 * {@link}https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1453779503&token=&lang=zh_CN
 * @Description: 获取（刷新）授权公众号或小程序的接口调用凭据（令牌） 返回参数
 * @author: wangqq
 * @date:   2018年7月25日 上午10:07:00     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class AuthorizerAccessTokenResponse extends  BaseOpenResponse{

	


	/**公众号或小程序authorizer_access_token*/
	private String authorizerAccessToken;
	
	/**有效期*/
	private Long expiresIn;
	
	/**刷新令牌*/
	private String authorizerRefreshToken;

	public String getAuthorizerAccessToken() {
		return authorizerAccessToken;
	}

	public void setAuthorizerAccessToken(String authorizerAccessToken) {
		this.authorizerAccessToken = authorizerAccessToken;
	}

	public Long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Long expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getAuthorizerRefreshToken() {
		return authorizerRefreshToken;
	}

	public void setAuthorizerRefreshToken(String authorizerRefreshToken) {
		this.authorizerRefreshToken = authorizerRefreshToken;
	}
	
}



 