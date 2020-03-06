/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.member.domain.vo.front;

/**
 * 我的点赞VO
 * 
 * @author: ljw
 * @date: 2019年7月26日 上午10:38:46
 */
public class MemberLikeVo {

	/** 1.点赞评论 2.点赞内容 **/
	private Integer type;
	/** 评论ID **/
	private Integer commentId;
	/** 内容ID **/
	private Integer contentId;
	/** 内容URL **/
	private String contentUrl;
	/** 内容标题 **/
	private String title;
	/** 评论内容 **/
	private String comment;

	public MemberLikeVo() {
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

	public Integer getContentId() {
		return contentId;
	}

	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getContentUrl() {
		return contentUrl;
	}

	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}

	/**1.点赞评论 2.点赞内容 **/
	public static final Integer TYPE_1 = 1;
	/**1.点赞评论 2.点赞内容 **/
	public static final Integer TYPE_2 = 2;
}
