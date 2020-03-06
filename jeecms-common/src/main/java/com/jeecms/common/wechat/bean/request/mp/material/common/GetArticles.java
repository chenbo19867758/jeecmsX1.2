package com.jeecms.common.wechat.bean.request.mp.material.common;

/**
 * 
 * TODO
 * 
 * @author: tom
 * @date: 2019年3月8日 下午4:38:25
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class GetArticles extends Articles {

	/** 图文页的URL */
	private String url;
	/** 图文消息的封面图片素材id（必须是永久mediaID） */
	private String thumbMediaUrl;

	public GetArticles(String title, String thumbMediaId, String author, String digest, Integer showCoverPic,
			String content, String contentSourceUrl, Integer needOpenComment, Integer onlyFansCanComment, String url,
			String thumbMediaUrl) {
		super(title, thumbMediaId, author, digest, showCoverPic, content, contentSourceUrl, needOpenComment,
				onlyFansCanComment);
		this.url = url;
		this.thumbMediaUrl = thumbMediaUrl;
	}

	public GetArticles() {
		super();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getThumbMediaUrl() {
		return thumbMediaUrl;
	}

	public void setThumbMediaUrl(String thumbMediaUrl) {
		this.thumbMediaUrl = thumbMediaUrl;
	}

}
