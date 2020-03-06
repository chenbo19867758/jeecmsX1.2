package com.jeecms.channel.domain.dto;

import javax.validation.constraints.NotNull;

/**
 * 栏目排序移动扩展Dto
 * @author: chenming
 * @date:   2019年5月5日 下午1:45:30
 */
public class ChannelSortDto {
	/** 需要移动的栏目*/
	private Integer channelId;
	/** */
	private Integer onId;
	
	private Integer nextId;

	@NotNull
	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public Integer getOnId() {
		return onId;
	}

	public void setOnId(Integer onId) {
		this.onId = onId;
	}

	public Integer getNextId() {
		return nextId;
	}

	public void setNextId(Integer nextId) {
		this.nextId = nextId;
	}

	
}
