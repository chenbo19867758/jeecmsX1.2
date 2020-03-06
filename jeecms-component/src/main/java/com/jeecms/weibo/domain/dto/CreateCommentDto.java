/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.weibo.domain.dto;

import javax.validation.constraints.NotNull;

/**
 * 发表评论DTO
 * 
 * @author: ljw
 * @date: 2019年6月20日 下午1:50:29
 */
public class CreateCommentDto {

	/** 评论内容，必须做URLencode，内容不超过140个汉字。必填 **/
	private String comment;
	/** 需要评论的微博ID。必填 **/
	private Long weiboId;
	/** 微博账户ID **/
	private Integer weiboUserId;
	/** 评论ID **/
	private Long commentId;

	public CreateCommentDto(){}
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Long getWeiboId() {
		return weiboId;
	}

	public void setWeiboId(Long weiboId) {
		this.weiboId = weiboId;
	}

	@NotNull
	public Integer getWeiboUserId() {
		return weiboUserId;
	}

	public void setWeiboUserId(Integer weiboUserId) {
		this.weiboUserId = weiboUserId;
	}

	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}

}
