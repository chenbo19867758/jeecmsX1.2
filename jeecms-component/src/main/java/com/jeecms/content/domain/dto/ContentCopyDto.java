package com.jeecms.content.domain.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.channel.domain.Channel;
import com.jeecms.content.constants.ContentConstant;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.ContentTxt;
import com.jeecms.content.util.ContentInitUtils;
import com.jeecms.system.domain.GlobalConfig;
import com.jeecms.system.domain.SysSecret;

/**
 * 内容复制dto
 * 
 * @author: chenming
 * @date: 2019年5月25日 下午5:28:28
 */
public class ContentCopyDto {
	/** 内容Id */
	private List<Integer> ids;
	/** 栏目Id */
	private Integer channelId;

	@NotNull
	@Size(min = 1)
	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

	@NotNull
	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	/**
	 * 初始化内容对象
	 */
	public static Content initCopyContent(Content content, GlobalConfig globalConfig,Channel channel,
			SysSecret secret,CoreUser user) {
		Content newContent = new Content();
		newContent = ContentInitUtils.copyContent(content, newContent, user, null);
		newContent = ContentInitUtils.clearContentObject(newContent);
		if (channel != null) {
			newContent.setChannelId(channel.getId());
			newContent.setChannel(channel);
		}
		newContent.setContentSecretId(null);
		newContent.setSecret(null);
		if (globalConfig.getConfigAttr().getOpenContentSecurity()) {
			if (content.getContentSecretId() != null) {
				if (secret != null) {
					newContent.setContentSecretId(secret.getId());
					newContent.setSecret(secret);
				}
			}
		}
		newContent.setCreateType(ContentConstant.CONTENT_CREATE_TYPE_COPY);
		newContent.setStatus(ContentConstant.STATUS_FIRST_DRAFT);
		newContent.setCopySourceContentId(content.getId());
		List<Channel> channelList = new ArrayList<Channel>();
		channelList.add(channel);
		newContent.setUser(user);
		newContent.setModelId(content.getModelId());
		newContent.setModel(content.getModel());
		// 此处只初始化默认字段和初始化内容数值，但是发布平台就无需进行任何初始化
		newContent = ContentInitUtils.initContentDefault(newContent);
		newContent = ContentInitUtils.initContentNum(newContent);
		return newContent;
	}
	
	/**
	 * copy时初始化contentTxt
	 */
	public static List<ContentTxt> copyInitTxt(List<ContentTxt> contentTxts) {
		List<ContentTxt> newContentTxts = new ArrayList<ContentTxt>();
		for (ContentTxt contentTxt : contentTxts) {
			ContentTxt newContentTxt = new ContentTxt();
			newContentTxt.setAttrKey(contentTxt.getAttrKey());
			newContentTxt.setAttrTxt(contentTxt.getAttrTxt());
			newContentTxts.add(newContentTxt);
		}
		return newContentTxts;
	}
	
	
}
