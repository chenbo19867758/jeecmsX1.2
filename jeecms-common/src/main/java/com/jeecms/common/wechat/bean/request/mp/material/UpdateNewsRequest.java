package com.jeecms.common.wechat.bean.request.mp.material;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.jeecms.common.wechat.bean.request.mp.material.common.Articles;

/**
 * 
 * @Description: 修改永久图文素材：request
 * @author: chenming
 * @date:   2018年7月30日 下午2:56:38     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class UpdateNewsRequest {
	/** 要修改的图文消息的media_id*/
	private String mediaId;
	/** 要更新的文章在图文消息中的位置（多图文消息时，此字段才有意义），第一篇为0*/
	private Integer index;
	/** 图文素材对象*/
	private Articles articles;
	/** 图文素材封面素材URL*/
	private String thumbMediaUrl;
	
	@NotBlank
	public String getMediaId() {
		return mediaId;
	}
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	@NotNull
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public Articles getArticles() {
		return articles;
	}
	public void setArticles(Articles articles) {
		this.articles = articles;
	}
	
	public UpdateNewsRequest() {
		super();
	}
	
	public UpdateNewsRequest(String mediaId, Integer index, Articles articles) {
		super();
		this.mediaId = mediaId;
		this.index = index;
		this.articles = articles;
	}
	@NotBlank
	public String getThumbMediaUrl() {
		return thumbMediaUrl;
	}
	public void setThumbMediaUrl(String thumbMediaUrl) {
		this.thumbMediaUrl = thumbMediaUrl;
	}
	
}
