/*  
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.service.impl;

import com.jeecms.channel.domain.Channel;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.component.listener.ChannelListener;
import com.jeecms.component.listener.ContentListener;
import com.jeecms.content.constants.ContentConstant;
import com.jeecms.content.dao.ContentChannelDao;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.ContentChannel;
import com.jeecms.content.service.ContentChannelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 内容栏目关联service实现类
 * 
 * @author: chenming
 * @date: 2019年5月15日 下午5:26:45
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ContentChannelServiceImpl extends BaseServiceImpl<ContentChannel, ContentChannelDao, Integer>
		implements ContentChannelService,ChannelListener,ContentListener {

	/**除引用外类型*/
	public static final List<Integer> types = Arrays.asList(ContentConstant.CONTENT_CREATE_TYPE_ADD,
			ContentConstant.CONTENT_CREATE_TYPE_CONTRIBUTE,ContentConstant.CONTENT_CREATE_TYPE_SITE_PUSH,
			ContentConstant.CONTENT_CREATE_TYPE_COPY,ContentConstant.CONTENT_CREATE_TYPE_COLLECT);

	@Override
	public void beforeChannelDelete(Integer[] ids) throws GlobalException {
		// 栏目删除后，与该栏目关联的清除
		List<ContentChannel> contentChannels = dao.findByChannelIdIn(ids);
		super.physicalDeleteInBatch(contentChannels);
	}

	@Override
	public void afterChannelSave(Channel c) throws GlobalException {
		
	}
	
	@Override
	public void afterChannelChange(Channel c) throws GlobalException {
	         
	}

	@Override
	public void afterChannelRecycle(List<Channel> channels) throws GlobalException {
		// 当栏目加入回收站后，栏目内容关联的也将全部加入回收站
	        Integer[]cids=new Integer[channels.size()];
	        Channel.fetchIds(channels).toArray(cids);
		List<ContentChannel> contentChannels = dao.findByChannelIdIn(cids);
		for (ContentChannel contentChannel : contentChannels) {
			contentChannel.setRecycle(true);
		}
		super.batchUpdate(contentChannels);
	}

	@Override
	public void afterDelete(List<Content> contents) throws GlobalException {
		// 当内容删除后，对应的关联关系也应该全部斩断(删除)
		if (contents.size() > 0) {
			List<Integer> contentIds = contents.stream().map(Content::getId).collect(Collectors.toList());
			List<ContentChannel> contentChannels = dao.findByContentIdIn(contentIds.toArray(new Integer[contentIds.size()]));
			super.physicalDeleteInBatch(contentChannels);
		}
	}

	@Override
	public void afterSave(Content content) throws GlobalException {
		
	}

	@Override
	public Map<String, Object> preChange(Content content) {
		return null;
	}

	@Override
	public void afterChange(Content content, Map<String, Object> map) throws GlobalException {
		
	}

	@Override
	public void afterContentRecycle(List<Integer> contentIds) throws GlobalException {
		// 如果内容加入回收站，则将内容关联的表的数据也加入回收站
		List<ContentChannel> contentChannels = dao.findByContentIdIn(contentIds.toArray(new Integer[contentIds.size()]));
		contentChannels = contentChannels.stream().filter(contentChannel -> !contentChannel.getRecycle()).collect(Collectors.toList());
		for (ContentChannel contentChannel : contentChannels) {
			contentChannel.setRecycle(true);
		}
		super.batchUpdate(contentChannels);
	}
	
	@Override
	public List<ContentChannel> countQuote(Integer channelId, List<Integer> list) throws GlobalException {
		List<ContentChannel> contentChannels = dao.findByChannelIdAndCreateTypeIn(channelId, list);
		return contentChannels;
	}

	@Override
	public void update(Content content) throws GlobalException {
		ContentChannel contentChannel = null;
		List<ContentChannel> contentChannels = content.getContentChannels().stream()
														.filter(
																conChannel -> types.contains(conChannel.getCreateType()))
														.collect(Collectors.toList());
		if (contentChannels.size() > 0) {
			contentChannel = contentChannels.get(0);
		}
		if(contentChannel!=null){
			contentChannel.setId(contentChannel.getRefId());
			contentChannel.setStatus(content.getStatus());
			contentChannel.setContent(content);
			contentChannel.setChannelId(content.getChannelId());
			super.update(contentChannel);
		}
	}

	@Override
	public List<ContentChannel> findByChannelIds(Integer[] channelIds) {
		return dao.findByChannelIdIn(channelIds);
	}
	
}