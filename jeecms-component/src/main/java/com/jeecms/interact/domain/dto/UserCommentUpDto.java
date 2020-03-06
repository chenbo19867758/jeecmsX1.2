package com.jeecms.interact.domain.dto;

import javax.validation.constraints.NotNull;

/**
 * 评论点赞、取消点赞操作
 * @author: chenming
 * @date:   2019年9月16日 下午2:46:30
 */
public class UserCommentUpDto {
	/** 评论ID值*/
	private Integer commentId;

	@NotNull
	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}
	
}
