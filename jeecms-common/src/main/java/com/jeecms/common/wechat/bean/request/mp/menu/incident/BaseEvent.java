package com.jeecms.common.wechat.bean.request.mp.menu.incident;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * TODO
 * @author: tom
 * @date:   2019年3月8日 下午4:39:30
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class BaseEvent {
	
	/** 开发者微信号 */
	@XStreamAlias("ToUserName")
	private String toUserName;
	
	/** 发送方账号(一个OpenID) */
	@XStreamAlias("FromUserName")
	private String fromUserName;
	
	/** 消息创建时间(整形) */
	@XStreamAlias("CreateTime")
	private Integer createTime;
	
	/** 消息类型，event */
	@XStreamAlias("MsgType")
	private String msgType;
	
	/** 事件类型，click */
	@XStreamAlias("Event")
	private String event;
	
	/** 事件Key值，与自定义菜单接口中key值对应 */
	@XStreamAlias("EventKey")
	private String eventKey;

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getEventKey() {
		return eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}
	
	
	
	
}
