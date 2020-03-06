package com.jeecms.channel.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import org.hibernate.validator.constraints.Length;

import com.jeecms.channel.constants.ChannelConstant;
import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.domain.ChannelExt;
import com.jeecms.system.domain.CmsSiteConfig;

/**
 * 栏目新增扩展Dto
 * 
 * @author: chenming
 * @date: 2019年4月17日 下午2:18:10
 */
public class ChannelSaveDto {
	/** 上级栏目ID */
	private Integer channelParentId;
	/** 栏目模型ID */
	private Integer modelId;
	/** 栏目名称 */
	private String channelName;
	/** 栏目路径 */
	private String channelPath;
	/** 站点ID */
	private Integer siteId;
	/** 排序值*/
	private Integer sortNum;

	public Integer getChannelParentId() {
		return channelParentId;
	}

	public void setChannelParentId(Integer channelParentId) {
		this.channelParentId = channelParentId;
	}

	@NotNull
	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}

	@NotBlank
	@Length(max = 50)
	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	@NotBlank
	@Length(max = 50)
	public String getChannelPath() {
		return channelPath;
	}

	public void setChannelPath(String channelPath) {
		this.channelPath = channelPath;
	}
	
	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	@Null
	public Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}
	
	public static Channel initChannel(Channel channel,CmsSiteConfig config,ChannelSaveAllDto dto) {
		channel.setContribute(config.getChannelNormalLimitContribute().equals(ChannelConstant.TRUE));
		channel.setRecycle(false);
		channel.setStaticChannel(false);
		channel.setHasStaticChannel(false);
		channel.setHasStaticContent(false);
		channel.setLinkTarget(false);
		channel.setDisplay(config.getChannelDisplayList());
		
		if (dto.getSortNum() != null) {
			channel.setSortNum(dto.getSortNum());
		} else {
			channel.setSortNum(10);
		}
		
		channel.setParentId(dto.getChannelParentId());
		channel.setPath(dto.getChannelPath());
		channel.setName(dto.getChannelName());
		
		channel.setChannelAttrs(null);
		
		channel.setContentTpls(null);
		return channel;
	}
	
	public static ChannelExt initChannelExt(ChannelExt channelExt,CmsSiteConfig config) {
		channelExt.setIsListChannel(config.getChannelDisplayList());
		channelExt.setCommentControl(Short.valueOf(config.getCommentSet() + ""));
		channelExt.setViewControl(Short.valueOf(config.getChannelVisitLimitType()));
		channelExt.setPageSize(Short.valueOf(config.getChannelDisplayContentNumber()));
		channelExt.setTaskControl(false);
		channelExt.setIsListChannel(false);
		channelExt.setIsOpenIndex(true);
		return channelExt;
	}

	
}
