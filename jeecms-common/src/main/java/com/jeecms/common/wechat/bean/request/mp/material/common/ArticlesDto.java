package com.jeecms.common.wechat.bean.request.mp.material.common;

/**
 * 
 * @author: tom
 * @date: 2019年3月8日 下午4:38:17
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class ArticlesDto {
	/** 要更新的文章在图文消息中的位置（多图文消息时，此字段才有意义），第一篇为0*/
	private Integer index;
	/** 要修改的图文消息的id*/
	private Integer mediaId;
	/** 修改的图文素材对象*/
	private Articles articles;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Integer getMediaId() {
		return mediaId;
	}

	public void setMediaId(Integer mediaId) {
		this.mediaId = mediaId;
	}

	public Articles getArticles() {
		return articles;
	}

	public void setArticles(Articles articles) {
		this.articles = articles;
	}

}
