package com.jeecms.interact.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import org.hibernate.validator.constraints.Length;

/**
 * 评论管理新增dto
 * @author: chenming
 * @date:   2019年7月17日 上午11:27:54
 */
public class UserCommentSaveDto {
	/** 内容id */
	private Integer contentId;
	/** 评论内容信息 */
	private String commentText;
	/** 评论的父级id */
	private Integer parentId;
	/** 被评论的id值 */
	private Integer userCommentId;
	/** 评论用户id*/
	private Integer userId;

	@NotNull
	public Integer getContentId() {
		return contentId;
	}

	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}

	@NotBlank
	@Length(max = 150)
	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getUserCommentId() {
		return userCommentId;
	}

	public void setUserCommentId(Integer userCommentId) {
		this.userCommentId = userCommentId;
	}

	@Null
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}
