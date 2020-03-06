package com.jeecms.common.wechat.bean.response.mp.material.common;

/**
 * 
 * TODO
 * @author: tom
 * @date:   2019年3月8日 下午4:42:12
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class MaterialItems {
	
	private String mediaId;
	
	private String name;
	
	private String updateTime;
	
	private String url;

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public MaterialItems(String mediaId, String name, String updateTime, String url) {
		super();
		this.mediaId = mediaId;
		this.name = name;
		this.updateTime = updateTime;
		this.url = url;
	}

	public MaterialItems() {
		super();
	}
	
	
}
