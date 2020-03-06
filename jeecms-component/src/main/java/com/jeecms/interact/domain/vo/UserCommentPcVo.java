package com.jeecms.interact.domain.vo;

import java.util.List;

import org.springframework.data.domain.Page;

import com.jeecms.interact.domain.UserComment;

public class UserCommentPcVo {

	private Page<UserComment> page;

	private Integer count;
	
	private List<UserComment> list;

	public UserCommentPcVo(Page<UserComment> page, Integer count) {
		super();
		this.page = page;
		this.count = count;
	}

	public UserCommentPcVo(List<UserComment> list, Integer count) {
		super();
		this.list = list;
		this.count = count;
	}
	
	public Page<UserComment> getPage() {
		return page;
	}

	public void setPage(Page<UserComment> page) {
		this.page = page;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public List<UserComment> getList() {
		return list;
	}

	public void setList(List<UserComment> list) {
		this.list = list;
	}

}
