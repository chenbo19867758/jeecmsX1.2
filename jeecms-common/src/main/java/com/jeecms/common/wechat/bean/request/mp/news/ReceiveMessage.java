package com.jeecms.common.wechat.bean.request.mp.news;
/**
 * 接收微信用户输入的内容
 * <p>Title:ReceiveMessage</p>
 * @author wulongwei
 * @date 2018年8月2日
 */

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml") 
public class ReceiveMessage extends BaseMessage{

	/**接受到的内容*/
	private String content;

	/**接受的事件类型*/
	private String event;
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}
	
	

}
