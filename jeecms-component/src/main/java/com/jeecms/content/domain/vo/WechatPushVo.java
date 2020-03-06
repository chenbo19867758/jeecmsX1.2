/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.domain.vo;

/**
 * 微信推送预览VO
 * 
 * @author: ljw
 * @date: 2019年7月24日 下午3:32:15
 */
public class WechatPushVo {

	/** 文章标题 **/
	private String title;
	/** 正文内容 **/
	private String content;
	/** 文章封面图片地址url，必传 **/
	private String cover;
	/** 文章封面id **/
	private Integer picId;
	/** 作者 **/
	private String author;
	/** 原创链接地址 **/
	private String sourceUrl;
	/** appid **/
	private String appId;
	/** 群发次数 **/
	private Integer sengNumber;

	public WechatPushVo() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public Integer getPicId() {
		return picId;
	}

	public void setPicId(Integer picId) {
		this.picId = picId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Integer getSengNumber() {
		return sengNumber;
	}

	public void setSengNumber(Integer sengNumber) {
		this.sengNumber = sengNumber;
	}

}
