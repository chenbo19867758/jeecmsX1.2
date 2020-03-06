/*  
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.constants.ContentConstant;
import com.jeecms.content.dao.ContentExtDao;
import com.jeecms.content.domain.ContentExt;
import com.jeecms.content.domain.dto.ContentContributeDto;
import com.jeecms.content.service.ContentExtService;
import com.jeecms.system.domain.ContentSource;
import com.jeecms.system.service.ContentSourceService;

/**
 * 内容扩展service实现类
 * 
 * @author: chenming
 * @date: 2019年5月15日 下午5:27:52
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ContentExtServiceImpl extends BaseServiceImpl<ContentExt, ContentExtDao, Integer>
		implements ContentExtService {

	
	@Override
	public ContentExt initContentExt(ContentExt contentExt, Integer siteId, JSONObject json, 
			Integer channelId, Integer modelId) throws GlobalException {
		contentExt.setOutLink(json.getString(ContentConstant.ITEM_OUT_LINK_NAME));
		contentExt.setIsNewTarget(false);
		// 来源特殊处理
		String sourceName = json.getString(ContentConstant.ITEM_CONTENT_SOURCE_NAME);
		if (StringUtils.isNotBlank(sourceName)) {
			ContentSource contentSource = contentSourceService.findBySourceName(sourceName);
			if (contentSource != null) {
				contentExt.setContentSourceId(contentSource.getId());
				contentExt.setContentSource(contentSource);
			} else {
				contentSource = new ContentSource(sourceName, null, false, false);
			}
		}
		
		contentExt.setDescription(json.getString(ContentConstant.ITEM_DESCRIPTION_NAME));
		Integer commentsNum = json.getInteger(ContentConstant.ITEM_REPEAT_NAME);
		contentExt.setCommentsMonth(commentsNum != null ? commentsNum : 0);
		contentExt.setCommentsWeek(commentsNum != null ? commentsNum : 0);
		contentExt.setCommentsDay(commentsNum != null ? commentsNum : 0);
		Integer upsNum = json.getInteger(ContentConstant.ITEM_PRAISED_NAME);
		contentExt.setUpsMonth(upsNum != null ? upsNum : 0);
		contentExt.setUpsWeek(upsNum != null ? upsNum : 0);
		contentExt.setUpsDay(upsNum != null ? upsNum : 0);
		contentExt.setViewsMonth(0);
		contentExt.setDownloadsMonth(0);
		contentExt.setDownsMonth(0);
		contentExt.setViewsWeek(0);
		contentExt.setDownloadsWeek(0);
		contentExt.setDownsWeek(0);
		contentExt.setViewsDay(0);
		contentExt.setDownloadsDay(0);
		contentExt.setDownsDay(0);
		
		return contentExt;
	}
	
	@Override
	public ContentExt initContributeContentExt(ContentExt contentExt, Integer siteId, ContentContributeDto dto,
			Integer channelId,Integer modelId) throws GlobalException {
		contentExt.setIsNewTarget(false);
		contentExt.setAuthor(dto.getAuthor());
		contentExt.setDescription(dto.getDescription());
		contentExt.setCommentsMonth(0);
		contentExt.setCommentsWeek(0);
		contentExt.setCommentsDay(0);
		contentExt.setUpsMonth(0);
		contentExt.setUpsWeek(0);
		contentExt.setUpsDay(0);
		contentExt.setViewsMonth(0);
		contentExt.setDownloadsMonth(0);
		contentExt.setDownsMonth(0);
		contentExt.setViewsWeek(0);
		contentExt.setDownloadsWeek(0);
		contentExt.setDownsWeek(0);
		contentExt.setViewsDay(0);
		contentExt.setDownloadsDay(0);
		contentExt.setDownsDay(0);
		
		return contentExt;
	}
	@Autowired
	private ContentSourceService contentSourceService;
	
	
}