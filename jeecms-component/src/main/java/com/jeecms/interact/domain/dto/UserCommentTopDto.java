package com.jeecms.interact.domain.dto;

import javax.validation.constraints.NotNull;

/**
 * 评论推荐、取消推荐dto
 * @author: chenming
 * @date:   2019年7月18日 上午8:46:25
 */
public class UserCommentTopDto {
	/** 评论id*/
	private Integer id;
	/** 是否推荐*/
	private Boolean top;

	@NotNull
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@NotNull
	public Boolean getTop() {
		return top;
	}

	public void setTop(Boolean top) {
		this.top = top;
	}
	
	
}
