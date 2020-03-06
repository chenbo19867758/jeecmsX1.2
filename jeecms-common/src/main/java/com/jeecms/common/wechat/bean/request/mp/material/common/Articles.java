package com.jeecms.common.wechat.bean.request.mp.material.common;

/**
 * 
 * TODO
 * 
 * @author: tom
 * @date: 2019年3月8日 下午4:38:07
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class Articles {
	/** 标题 */
	private String title;
	/** 图文消息的封面素材id(必须是永久mediaID) */
	private String thumbMediaId;
	/** 作者 */
	private String author;
	/** 图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空，如果本字段为没有填写，则默认抓取正文前64个字 */
	private String digest;
	/** 是否显示封面，0位false，即不显示，1为true，即显示 */
	private Integer showCoverPic;
	/**
	 * 图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS,涉及图片url必须来源
	 * "上传图文消息内的图片获取URL"接口获取。外部图片url将被过滤
	 */
	private String content;
	/** 图文消息的原文地址，即点击"阅读原文"后的URL */
	private String contentSourceUrl;
	/** 是否打开评论，0不打开，1打开 */
	private Integer needOpenComment;
	/** 是否粉丝才可评论，0所有人可评论，1粉丝才可评论 */
	private Integer onlyFansCanComment;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getThumbMediaId() {
		return thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public Integer getShowCoverPic() {
		return showCoverPic;
	}

	public void setShowCoverPic(Integer showCoverPic) {
		this.showCoverPic = showCoverPic;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentSourceUrl() {
		return contentSourceUrl;
	}

	public void setContentSourceUrl(String contentSourceUrl) {
		this.contentSourceUrl = contentSourceUrl;
	}

	public Integer getNeedOpenComment() {
		return needOpenComment;
	}

	public void setNeedOpenComment(Integer needOpenComment) {
		this.needOpenComment = needOpenComment;
	}

	public Integer getOnlyFansCanComment() {
		return onlyFansCanComment;
	}

	public void setOnlyFansCanComment(Integer onlyFansCanComment) {
		this.onlyFansCanComment = onlyFansCanComment;
	}

	public Articles(String title, String thumbMediaId, String author, String digest, Integer showCoverPic,
			String content, String contentSourceUrl, Integer needOpenComment, Integer onlyFansCanComment) {
		super();
		this.title = title;
		this.thumbMediaId = thumbMediaId;
		this.author = author;
		this.digest = digest;
		this.showCoverPic = showCoverPic;
		this.content = content;
		this.contentSourceUrl = contentSourceUrl;
		this.needOpenComment = needOpenComment;
		this.onlyFansCanComment = onlyFansCanComment;
	}

	public Articles() {
		super();
	}

}
