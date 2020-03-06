package com.jeecms.common.wechat.bean.response.open;

import com.jeecms.common.wechat.bean.response.open.BaseOpenResponse;

/**
 * {@link}https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1453779503&token=&lang=zh_CN
 * @Description: 获取第三方平台component_access_token  返回参数
 * @author: wangqq
 * @date:   2018年7月25日 上午10:07:30     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class ComponentAccessTokenResponse extends  BaseOpenResponse{
	

	/**第三方平台access_token*/
	private String componentAccessToken;
	
	/**有效期*/
	private Long expiresIn;


	public String getComponentAccessToken() {
		return componentAccessToken;
	}

	public void setComponentAccessToken(String componentAccessToken) {
		this.componentAccessToken = componentAccessToken;
	}

	public Long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Long expiresIn) {
		this.expiresIn = expiresIn;
	}
	
	
}



 