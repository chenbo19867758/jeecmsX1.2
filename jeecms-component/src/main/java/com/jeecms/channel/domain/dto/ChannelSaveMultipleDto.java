package com.jeecms.channel.domain.dto;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

/**
 * 批量插入controller层传入实体类
 * 
 * @author: chenming
 * @date: 2019年4月28日 上午11:15:00
 */
public class ChannelSaveMultipleDto {
	/** 栏目id */
	private Integer channelParentId;
	/** 模型id */
	private Integer modelId;
	/** 栏目名称 */
	private List<String> channelNames;

	private Integer siteId;

	@NotNull
	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}

	@Null
	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	@NotNull
	public Integer getChannelParentId() {
		return channelParentId;
	}

	public void setChannelParentId(Integer channelParentId) {
		this.channelParentId = channelParentId;
	}

	@Size(min = 1, max = 20)
	public List<String> getChannelNames() {
		return channelNames;
	}

	public void setChannelNames(List<String> channelNames) {
		this.channelNames = channelNames;
	}

}
