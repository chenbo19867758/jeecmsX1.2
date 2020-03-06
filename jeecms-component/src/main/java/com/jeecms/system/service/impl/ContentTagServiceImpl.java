/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service.impl;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.util.StrUtils;
import com.jeecms.content.domain.Content;
import com.jeecms.system.dao.ContentTagDao;
import com.jeecms.system.domain.ContentTag;
import com.jeecms.system.service.ContentTagService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * tag词service实现类
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-27
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ContentTagServiceImpl extends BaseServiceImpl<ContentTag, ContentTagDao, Integer>
		implements ContentTagService {

	@Override
	public List<ContentTag> saveBatch(ContentTag contentTag, Integer siteId) throws GlobalException {
		String tagName = contentTag.getTagName();
		String[] tagNames = StrUtils.splitAndTrim(tagName, ",", "，");
		Set<String> set = new HashSet<String>(Arrays.asList(tagNames));
		List<ContentTag> list = new ArrayList<ContentTag>(set.size());
		for (String name : set) {
			if (StringUtils.isBlank(name)) {
				continue;
			}
			// 判断是否存在该tag词，存在则跳过不添加
			if (checkTagName(name, null, siteId)) {
				ContentTag tag = new ContentTag();
				// 手动添加tag词时，使用次数默认为0
				tag.setRefCounter(0);
				tag.setTagName(name);
				tag.setSiteId(siteId);
				list.add(tag);
			}
		}
		return saveAll(list);
	}

	@Override
	public boolean checkTagName(String tagName, Integer id, Integer siteId) {
		if (StringUtils.isBlank(tagName)) {
			return true;
		}
		ContentTag contentTag = dao.findByTagName(tagName, siteId);
		if (contentTag == null) {
			return true;
		} else {
			if (!contentTag.getId().equals(id)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public List<ContentTag> initTags(String contentTagStr, Integer siteId) {
		String[] contentTags = StrUtils.splitAndTrim(contentTagStr, ",", "，");
		if (contentTags != null && contentTags.length > 0) {
			List<ContentTag> tags = dao.findBySiteIdAndHasDeleted(siteId, false);
			List<ContentTag> tagList = new ArrayList<ContentTag>();
			if (tags != null && tags.size() > 0) {
				Map<String, ContentTag> tagMap = tags.stream()
						.collect(Collectors.toMap(ContentTag::getTagName, tag -> tag));
				for (String tag : contentTags) {
					ContentTag contentTag = tagMap.get(tag);
					if (contentTag != null) {
						contentTag.setRefCounter(contentTag.getRefCounter() + 1);
						tagList.add(contentTag);
					} else {
						contentTag = new ContentTag();
						contentTag.setTagName(tag);
						contentTag.setRefCounter(1);
						contentTag.setSiteId(siteId);
						tagList.add(contentTag);
					}
				}
			} else {
				for (String tag : contentTags) {
					ContentTag contentTag = new ContentTag();
					contentTag.setTagName(tag);
					contentTag.setRefCounter(1);
					contentTag.setSiteId(siteId);
					tagList.add(contentTag);
				}
			}
			return tagList;
		}
		return null;
	}

	@Override
	public void deleteTagQuote(List<ContentTag> oldTags, Integer siteId, Content content) throws GlobalException {
		List<ContentTag> tags = dao.findBySiteIdAndHasDeleted(siteId, false);
		List<ContentTag> contentDeleteTags = null;
		if (content != null) {
			contentDeleteTags = content.getContentTags();
		}
		Map<String, ContentTag> tagMap = null;
		if (tags != null && tags.size() > 0) {
			//tagMap = tags.stream().collect(Collectors.toMap(ContentTag::getTagName, tag -> tag));
			tagMap = tags.stream().collect(Collectors.toMap(ContentTag::getTagName, tag -> tag, (v1, v2) -> v2));
		}
		List<ContentTag> tagList = new ArrayList<ContentTag>();
		if (contentDeleteTags != null) {
			if (tags != null && tags.size() > 0) {
				for (ContentTag tag : contentDeleteTags) {
					ContentTag contentTag = tagMap.get(tag.getTagName());
					if (contentTag != null) {
						contentTag.setRefCounter(contentTag.getRefCounter() - 1);
						tagList.add(contentTag);
					}
				}
			}
		} else {
			if (oldTags != null && oldTags.size() > 0) {
				if (tags != null && tags.size() > 0) {
					for (ContentTag tag : oldTags) {
						ContentTag contentTag = tagMap.get(tag.getTagName());
						if (contentTag != null) {
							contentTag.setRefCounter(contentTag.getRefCounter() - 1);
							tagList.add(contentTag);
						}
					}
				}
			}
		}
		if (tagList.size() > 0) {
			super.batchUpdateAll(tagList);
			super.flush();
		}
	}

}