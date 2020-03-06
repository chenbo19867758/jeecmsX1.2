/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.member.domain.vo.front;

import com.jeecms.content.domain.vo.ContentFrontVo;

/**
 * 我的点赞VO
 * 
 * @author: ljw
 * @date: 2019年7月26日 上午10:38:46
 */
public class MemberMoblieLikeVo {

	/** 1.点赞评论 2.点赞内容 **/
	private Integer type;
	/** 评论类型1.我的评论2.回复我的 **/
	private Integer commentType;
	/** 评论人名称 **/
	private String username;
	/** 头像 **/
	private String headImage;
	/** 回复人名称 **/
	private String replyUsername;
	/** 评论时间 **/
	private String time;
	/** 点赞数 **/
	private Integer ups;
	/** 评论ID **/
	private Integer commentId;
	/** 评论内容 **/
	private String comment;
	/** 手机内容VO **/
	private ContentFrontVo mobileContent;
	/** 评论数 **/
	private Integer commentSum;

	public MemberMoblieLikeVo() {
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public ContentFrontVo getMobileContent() {
		return mobileContent;
	}

	public void setMobileContent(ContentFrontVo mobileContent) {
		this.mobileContent = mobileContent;
	}

	public Integer getCommentType() {
		return commentType;
	}

	public void setCommentType(Integer commentType) {
		this.commentType = commentType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Integer getUps() {
		return ups;
	}

	public void setUps(Integer ups) {
		this.ups = ups;
	}

	public Integer getCommentSum() {
		return commentSum;
	}

	public void setCommentSum(Integer commentSum) {
		this.commentSum = commentSum;
	}

	/** 1.点赞评论 2.点赞内容 **/
	public static final Integer TYPE_1 = 1;
	/** 1.点赞评论 2.点赞内容 **/
	public static final Integer TYPE_2 = 2;
	
	/** 评论类型1.我的评论2.回复我的  **/
	public static final Integer COMMENT_TYPE_1 = 1;
	/** 评论类型1.我的评论2.回复我的  **/
	public static final Integer COMMENT_TYPE_2 = 2;
}
