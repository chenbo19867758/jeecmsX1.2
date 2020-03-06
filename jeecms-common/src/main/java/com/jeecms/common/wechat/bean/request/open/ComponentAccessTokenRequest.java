package com.jeecms.common.wechat.bean.request.open;

/**
 * {@link}https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1453779503&token=&lang=zh_CN
 * @Description: 获取第三方平台component_access_token  请求参数
 * @author: wangqq
 * @date:   2018年7月25日 上午10:07:30     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class ComponentAccessTokenRequest  {

	/**第三方平台appid*/
	private String componentAppid;
	
	/**第三方平台appsecret*/
	private String componentAppsecret;
	
	/**微信后台推送的ticket，此ticket会定时推送*/
	private String componentVerifyTicket;

	public ComponentAccessTokenRequest(String componentAppid,String componentAppsecret, String componentVerifyTicket){
		this.componentAppid = componentAppid;
		this.componentAppsecret = componentAppsecret;
		this.componentVerifyTicket = componentVerifyTicket;
	}
	
	
	public String getComponentAppid() {
		return componentAppid;
	}

	public void setComponentAppid(String componentAppid) {
		this.componentAppid = componentAppid;
	}

	public String getComponentAppsecret() {
		return componentAppsecret;
	}

	public void setComponentAppsecret(String componentAppsecret) {
		this.componentAppsecret = componentAppsecret;
	}

	public String getComponentVerifyTicket() {
		return componentVerifyTicket;
	}

	public void setComponentVerifyTicket(String componentVerifyTicket) {
		this.componentVerifyTicket = componentVerifyTicket;
	}
	
	
	
	
	
}

 