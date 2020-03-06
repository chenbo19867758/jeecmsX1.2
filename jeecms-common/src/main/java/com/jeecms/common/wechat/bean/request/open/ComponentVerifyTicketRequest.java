package com.jeecms.common.wechat.bean.request.open;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * {@link}https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1453779503&token=&lang=zh_CN
 * @Description: 推送component_verify_ticket协议 ，解密之后之后xml数据
 * @author: wangqq
 * @date:   2018年7月25日 上午10:07:00     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@XStreamAlias("xml")
public class ComponentVerifyTicketRequest{

	/**第三方平台appid*/
	@XStreamAlias("AppId")  
	private String appId;
	
	/**时间戳*/
	@XStreamAlias("CreateTime")  
	private String createTime;
	
	/**component_verify_ticket*/
	@XStreamAlias("InfoType")  
	private String infoType;
	
	/**Ticket内容*/
	@XStreamAlias("ComponentVerifyTicket")  
	private String componentVerifyTicket;

	/** 取消授权的公众号appId */
	@XStreamAlias("AuthorizerAppid")
	private String authorizerAppid;
	
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getInfoType() {
		return infoType;
	}

	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}

	public String getComponentVerifyTicket() {
		return componentVerifyTicket;
	}

	public void setComponentVerifyTicket(String componentVerifyTicket) {
		this.componentVerifyTicket = componentVerifyTicket;
	}

	public String getAuthorizerAppid() {
		return authorizerAppid;
	}

	public void setAuthorizerAppid(String authorizerAppid) {
		this.authorizerAppid = authorizerAppid;
	}

	
}



 