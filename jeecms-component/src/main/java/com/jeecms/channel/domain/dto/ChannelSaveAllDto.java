package com.jeecms.channel.domain.dto;

import java.util.List;

/**
 * 批量添加实体类
 * 
 * @author: chenming
 * @date: 2019年4月18日 下午2:58:17
 */
public class ChannelSaveAllDto extends ChannelSaveDto {

	/** 子集*/
	private List<ChannelSaveAllDto> children;

	public List<ChannelSaveAllDto> getChildren() {
		return children;
	}

	public void setChildren(List<ChannelSaveAllDto> children) {
		this.children = children;
	}


}
