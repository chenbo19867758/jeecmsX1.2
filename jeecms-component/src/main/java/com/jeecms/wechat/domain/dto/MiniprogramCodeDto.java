package com.jeecms.wechat.domain.dto;

import javax.validation.constraints.NotNull;

/**
 * 小程序草稿箱、模板扩展对象
 * @author: chenming
 * @date:   2019年6月12日 下午4:10:35
 */
public class MiniprogramCodeDto {
	/** 小程序草稿箱、模板id值*/
	private Integer id;

	@NotNull
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}
