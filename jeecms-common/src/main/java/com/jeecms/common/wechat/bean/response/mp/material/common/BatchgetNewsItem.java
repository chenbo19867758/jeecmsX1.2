package com.jeecms.common.wechat.bean.response.mp.material.common;

/**
 * 
 * TODO
 * @author: tom
 * @date:   2019年3月8日 下午4:41:51
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class BatchgetNewsItem {
	
	private String mediaId;
	
	private BatchgetContent content;
	
	private String updateTime;

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public BatchgetContent getContent() {
		return content;
	}

	public void setContent(BatchgetContent content) {
		this.content = content;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
	
}
