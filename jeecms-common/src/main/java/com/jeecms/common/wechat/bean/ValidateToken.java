package com.jeecms.common.wechat.bean;

/**
 * 
 * @Description:验证公众号或小程序token参数bean
 * @author: wangqq
 * @date:   2018年7月25日 上午11:41:45     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class ValidateToken {
    /**appId（含公众号、小程序、开放平台第三方应用）*/
    private String appId;
    /**authorizerAccessToken 或者componentAccessToken 必须*/
    private String accessToken ;
    
    public ValidateToken(){}
    
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
    
}
