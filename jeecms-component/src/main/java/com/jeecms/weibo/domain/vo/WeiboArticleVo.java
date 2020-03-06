/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.weibo.domain.vo;

import java.util.Date;

/**
 * 文章推送预览VO
 * 
 * @author: ljw
 * @date: 2019年7月5日 上午11:53:04
 */
public class WeiboArticleVo {

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
	/**微博用户ID**/
	private Integer weiboId;
	/**内容ID**/
	private Integer contentId;
	/** 微博昵称 **/
	private String screenName;
	/** 用户头像地址(中图，50*50) */
	private String profileImageUrl;
	/** 推送时间 **/
	private Date pushDate;
	
	public WeiboArticleVo() {
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

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public Date getPushDate() {
		return pushDate;
	}

	public void setPushDate(Date pushDate) {
		this.pushDate = pushDate;
	}

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

}
