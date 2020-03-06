/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.weibo.domain.dto;

/**
 * 微博推送文章Dto
 * 
 * @author: ljw
 * @date: 2019年6月19日 上午9:27:50
 */
public class PreviewDto {

	/** 文章标题，限定32个中英文字符以内，必传 **/
	private String title;
	/** 正文内容，限制90000个中英文字符内，需要urlencode，必传 **/
	private String content;
	/** 文章封面图片地址url，必传 **/
	private String cover;
	/** 文章导语 **/
	private String summary;
	/** 与其绑定短微博内容，限制1900个中英文字符内,必传 **/
	private String text;
	/** 内容ID **/
	private Integer contentId;
	/** 微博账户ID **/
	private Integer weiboId;
	/** 站点ID **/
	private Integer siteId;

	public PreviewDto() {}

	public Integer getContentId() {
		return contentId;
	}

	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}

	public Integer getWeiboId() {
		return weiboId;
	}

	public void setWeiboId(Integer weiboId) {
		this.weiboId = weiboId;
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

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

}
