package com.jeecms.content.domain.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.channel.domain.Channel;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionInfo;
import com.jeecms.common.exception.error.ContentErrorCodeEnum;
import com.jeecms.common.util.MyBeanUtils;
import com.jeecms.content.constants.ContentConstant;
import com.jeecms.content.domain.CmsModel;
import com.jeecms.content.domain.Content;
import com.jeecms.content.util.ContentInitUtils;
import com.jeecms.system.domain.GlobalConfig;
import com.jeecms.system.domain.SysSecret;

/**
 * 接受站群推送
 * 
 * @author: chenming
 * @date: 2019年7月6日 上午11:05:24
 */
public class ContentPushSitesDto {
	/** 内容id */
	private List<Integer> contentIds;
	/** 站点id */
	private Integer siteId;
	/** 栏目id */
	private Integer channelId;
	/** 推送秘钥 */
	private String pushSecret;

	/** 内容对象集合 */
	private List<Content> contents;
	/** 栏目对象 */
	private Channel channel;

	@Size(min = 1, max = 200)
	public List<Integer> getContentIds() {
		return contentIds;
	}

	public void setContentIds(List<Integer> contentIds) {
		this.contentIds = contentIds;
	}

	@NotNull
	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	@NotNull
	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public String getPushSecret() {
		return pushSecret;
	}

	public void setPushSecret(String pushSecret) {
		this.pushSecret = pushSecret;
	}

	@Null
	public List<Content> getContents() {
		return contents;
	}

	public void setContents(List<Content> contents) {
		this.contents = contents;
	}

	@Null
	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public static Content initContent(Content content, GlobalConfig globalConfig, Channel channel, Integer siteId,
			List<CmsModel> models, SysSecret secret, CoreUser user) throws GlobalException {
		Content newContent = new Content();
		newContent = ContentInitUtils.copyContent(content, newContent, user, siteId);
		newContent = ContentInitUtils.clearContentObject(newContent);
		if (channel != null) {
			newContent.setChannelId(channel.getId());
			newContent.setChannel(channel);
		}
		newContent.setContentSecretId(null);
		newContent.setSecret(null);
		// 因为内容密级可能被删除
		if (globalConfig.getConfigAttr().getOpenContentSecurity()) {
			if (content.getContentSecretId() != null) {
				if (secret != null) {
					newContent.setContentSecretId(secret.getId());
					newContent.setSecret(secret);
				}
			}
		}
		newContent.setCreateType(ContentConstant.CONTENT_CREATE_TYPE_SITE_PUSH);
		newContent.setStatus(ContentConstant.STATUS_FIRST_DRAFT);
		newContent.setCopySourceContentId(null);
		List<Channel> channelList = new ArrayList<Channel>();
		channelList.add(channel);
		newContent.setUser(user);
		Integer modelId = content.getModelId();
		if (models != null && models.size() > 0) {
			List<Integer> modelIds = models.stream().map(CmsModel::getId).collect(Collectors.toList());
			if (modelIds.contains(modelId)) {
				newContent.setModelId(modelId);
			} else {
				newContent.setModelId(models.get(0).getId());
			}
		} else {
			throw new GlobalException(
					new SystemExceptionInfo(
							ContentErrorCodeEnum.THE_MODEL_IS_NULL.getDefaultMessage(),
							ContentErrorCodeEnum.THE_MODEL_IS_NULL.getCode()));
		}
		newContent.setSiteId(siteId);
		
		newContent = ContentInitUtils.initContentDefault(newContent);
		newContent = ContentInitUtils.initContentNum(newContent);
		return newContent;
	}
}
