package com.jeecms.channel.domain.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 合并栏目dto
 * @author: chenming
 * @date:   2019年7月6日 下午1:43:51
 */
public class ChannelMergeDto {
	/** 目标栏目id*/
	private Integer id;
	/** 栏目数组*/
	private Integer[] ids;

	@NotNull
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@NotNull
	@Size(min = 1)
	public Integer[] getIds() {
		return ids;
	}

	public void setIds(Integer[] ids) {
		this.ids = ids;
	}
	
	
}
