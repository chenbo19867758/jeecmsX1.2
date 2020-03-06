package com.jeecms.common.wechat.bean.response.mp.material.common;

/**
 * 
 * TODO
 * @author: tom
 * @date:   2019年3月8日 下午4:42:21
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class NewsItem {
	private String mediaId;
	
	private Content content;
	
	private String updateTime;

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
	
}
