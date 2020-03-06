package com.jeecms.channel.domain.dto;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

/**
 * 还原栏目扩展dto
 * @author: chenming
 * @date:   2019年8月6日 下午5:09:08
 */
public class ChannelReductionDto {
	/** 还原栏目id集合*/
	private List<Integer> ids;
	/** 是否同时还原内容*/
	private Boolean isContent;
	/** 站点id*/
	private Integer siteId;

	@NotNull
	@Size(min = 1, max = 10000)
	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

	@NotNull
	public Boolean getIsContent() {
		return isContent;
	}

	public void setIsContent(Boolean isContent) {
		this.isContent = isContent;
	}

	@Null
	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
}
