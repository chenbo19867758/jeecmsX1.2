package com.jeecms.common.wechat.bean.response.open;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 取消授权接收xml(取消授权后会推送一个xml到消息)
 * 
 * @author: chenming
 * @date: 2019年6月3日 下午2:29:31
 */
@XStreamAlias("xml")
public class AuthorizationCancelResponse {

	/** 第三方平台appid */
	@XStreamAlias("AppId")
	private String appId;

	/** 时间戳 */
	@XStreamAlias("CreateTime")
	private String createTime;

	/** component_verify_ticket */
	@XStreamAlias("InfoType")
	private String infoType;

	/** Ticket内容 */
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

	public String getAuthorizerAppid() {
		return authorizerAppid;
	}

	public void setAuthorizerAppid(String authorizerAppid) {
		this.authorizerAppid = authorizerAppid;
	}

}
