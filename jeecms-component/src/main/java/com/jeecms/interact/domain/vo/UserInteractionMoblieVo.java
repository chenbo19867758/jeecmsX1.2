/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.interact.domain.vo;

import java.util.Date;

import com.jeecms.content.domain.vo.ContentFrontVo;

/**
 * 我的互动手机VO
 * 
 * @author: ljw
 * @date: 2019年7月20日 下午2:05:05
 */
public class UserInteractionMoblieVo {

	/** 评论ID **/
	private Integer id;
	/** 头像 **/
	private String headImage;
	/** 回复人名称 **/
	private String replyUsername;
	/** 评论时间 **/
	private String commentTime;
	/** 评论时间 **/
	private Date commentTimes;
	/** 文本内容 **/
	private String text;
	/** 互动类型 1.我的评论，2.我的回复，3.回复我的 **/
	private Integer type;
	/** 互动用户名称 **/
	private String username;
	/** 我的点赞数 **/
	private Integer upCount;
	/** 父结点Id **/
	private Integer parentId;
	/** 手机内容VO **/
	private ContentFrontVo mobileContent;
	/**是否点赞**/
	private Boolean like;

	public UserInteractionMoblieVo() {
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getUpCount() {
		return upCount;
	}

	public void setUpCount(Integer upCount) {
		this.upCount = upCount;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public ContentFrontVo getMobileContent() {
		return mobileContent;
	}

	public void setMobileContent(ContentFrontVo mobileContent) {
		this.mobileContent = mobileContent;
	}

	public String getHeadImage() {
		return headImage;
	}

	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}

	public String getReplyUsername() {
		return replyUsername;
	}

	public void setReplyUsername(String replyUsername) {
		this.replyUsername = replyUsername;
	}
	
	public String getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}

	public Date getCommentTimes() {
		return commentTimes;
	}

	public void setCommentTimes(Date commentTimes) {
		this.commentTimes = commentTimes;
	}

	public Boolean getLike() {
		return like;
	}

	public void setLike(Boolean like) {
		this.like = like;
	}

	/** 我的评论 **/
	public static final Integer TYPE_1 = 1;
	/** 我的回复 **/
	public static final Integer TYPE_2 = 2;
	/** 回复我的 **/
	public static final Integer TYPE_3 = 3;
}