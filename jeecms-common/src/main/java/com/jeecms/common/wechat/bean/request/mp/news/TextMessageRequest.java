package com.jeecms.common.wechat.bean.request.mp.news;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 回复文本消息
 * 
 * @author wulongwei
 * @date 2018年7月30日
 */
@XStreamAlias("xml")
public class TextMessageRequest extends BaseMessage {
	
	/**
	 * 文本消息内容
	 */
	@XStreamAlias("Content")
	private String content; 

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
