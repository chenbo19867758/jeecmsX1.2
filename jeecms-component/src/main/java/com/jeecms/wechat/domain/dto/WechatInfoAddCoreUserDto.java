package com.jeecms.wechat.domain.dto;

import javax.validation.constraints.NotNull;

/**
 * 公众号或小程序新增管理员dto
 * @author: chenming
 * @date:   2019年6月10日 下午1:54:34
 */
public class WechatInfoAddCoreUserDto {
	/** 公众号或小程序id*/
	private Integer id;
	/** 管理员id数组*/
	private Integer[] userIds;

	@NotNull
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer[] getUserIds() {
		return userIds;
	}

	public void setUserIds(Integer[] userIds) {
		this.userIds = userIds;
	}

	
	
}
