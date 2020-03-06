package com.jeecms.wechat.domain.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 修改菜单状态接受前台dto
 * @author: chenming
 * @date:   2019年5月31日 上午11:07:20
 */
public class UpdateMenuStatusDto {
	/** 菜单组id*/
	private Integer id;
	/** 菜单组状态（默认菜单组类型中，只允许一组生效菜单） 1- 生效 2-未生效 */
	private Integer status;

	@NotNull
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@NotNull
	@Min(value = 1, message = "菜单类型必须大于等于1")
	@Max(value = 2, message = "菜单类型必须小于等于2")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
