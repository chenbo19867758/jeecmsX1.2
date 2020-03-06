package com.jeecms.collect.domain.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.channel.domain.Channel;
import com.jeecms.content.constants.ContentConstant;
import com.jeecms.content.domain.Content;
import com.jeecms.content.util.ContentInitUtils;
import com.jeecms.system.domain.CmsSiteConfig;

/**
 * 采集中将采集信息导出成内容
 * 
 * @author: chenming
 * @date: 2019年7月15日 上午8:48:11
 */
public class CollectExportContentDto {
	/** 栏目名称 */
	private Integer channelId;
	/** 模型名称 */
	private Integer modelId;
	/** 采集内容id */
	private List<Integer> collectContentIds;

	@NotNull
	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	@NotNull
	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}

	@Min(1)
	public List<Integer> getCollectContentIds() {
		return collectContentIds;
	}

	public void setCollectContentIds(List<Integer> collectContentIds) {
		this.collectContentIds = collectContentIds;
	}

	public static Content initContent(Content content, Channel channel, JSONObject json, 
			CmsSiteConfig cmsSiteConfig, Integer siteId) {
		content.setTitle(json.getString(ContentConstant.ITEM_TITLE_NAME));
		content.setTitleIsBold(false);
		content.setTitleColor(ContentConstant.TITLE_DEFAULT_COLOR);
		content.setReleaseTime(new Date());
		content.setChannelId(channel.getId());
		content.setChannel(channel);
		Short viewControl = null;
		if (channel.getChannelExt().getViewControl() != null) {
			viewControl = ContentInitUtils.initViewControl(channel.getChannelExt().getViewControl());
		} else {
			viewControl = ContentInitUtils.initViewControl(
					Short.valueOf(cmsSiteConfig.getChannelVisitLimitType()));
		}
		content.setHasStatic(false);
		content.setViewControl(viewControl);
		content.setStatus(ContentConstant.STATUS_FIRST_DRAFT);
		content.setCreateType(ContentConstant.CONTENT_CREATE_TYPE_SITE_COLLECT);
		// 此处无需判断栏目为空，因为栏目那边会保证其值存在
		content.setCommentControl(
				Integer.valueOf(channel.getChannelExt().getCommentControl() + ""));
		// 如果小于4，则说明其内容状态是待发布之下，所以发布管理员必须置空
		content.setPublishUserId(null);
		content.setSiteId(siteId);
		content = ContentInitUtils.initContentDefault(content);
		content = ContentInitUtils.initContentNum(content);
		content = ContentInitUtils.initTrueContentRelease(content);
		return content;
	}
}
