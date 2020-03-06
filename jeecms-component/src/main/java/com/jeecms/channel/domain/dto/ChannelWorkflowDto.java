package com.jeecms.channel.domain.dto;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.jeecms.channel.domain.Channel;

/**
 * 栏目操作工作流dto
 * 
 * @author: chenming
 * @date: 2019年8月20日 下午4:53:51
 */
public class ChannelWorkflowDto {
	/** 源栏目id */
	private Integer channelId;
	/** 目标栏目id数组 */
	private List<Integer> ids;
	/** 是否选择了全部 */
	private Boolean all;
	
	/** 需要修改的栏目集合*/
	private List<Channel> channels;

	@NotNull
	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

	@NotNull
	public Boolean getAll() {
		return all;
	}

	public void setAll(Boolean all) {
		this.all = all;
	}

	@Null
	public List<Channel> getChannels() {
		return channels;
	}

	public void setChannels(List<Channel> channels) {
		this.channels = channels;
	}

}
