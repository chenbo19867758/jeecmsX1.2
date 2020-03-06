package com.jeecms.content.domain.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.content.constants.CmsModelConstant;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.ContentExt;

import javax.validation.Valid;

/**
 * 修改内容Dto
 * @author: chenming
 * @date:   2019年5月22日 下午5:06:04
 */
@Valid
public class ContentUpdateDto extends ContentSaveDto {
	/** 内容Id*/
	private Integer id;
	
	private CoreUser user;

	private Boolean force;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public CoreUser getUser() {
		return user;
	}

	public void setUser(CoreUser user) {
		this.user = user;
	}
	
	public static JSONObject checkContentToJson(ContentUpdateDto dto) {
		// 此处单独处理的主要是默认字段，但是单独处理的"正文"字段是无论是默认还是自定义字段都是放入到dto的json对象中
		JSONObject json = new JSONObject();
		json.put(CmsModelConstant.FIELD_SYS_TITLE, dto.getTitle().getString(Content.TITLE_NAME));
		String sortTitle = dto.getShortTitle();
		if (StringUtils.isNotBlank(sortTitle)) {
			json.put(CmsModelConstant.FIELD_SYS_SHORT_TITLE, sortTitle);
		}
		String description = dto.getDescription();
		if (StringUtils.isNotBlank(description)) {
			json.put(CmsModelConstant.FIELD_SYS_DESCRIPTION, description);
		}
		String contentTag = dto.getContentTag();
		if (StringUtils.isNoneBlank(contentTag)) {
			json.put(CmsModelConstant.FIELD_SYS_CONTENT_CONTENTTAG, contentTag);
		}
		JSONObject contentSourceJson = dto.getContentSourceId();
		if (contentSourceJson != null) {
			List<String> contentSources = new ArrayList<String>();
			String sourceName = contentSourceJson.getString(ContentExt.SOURCE_NAME);
			if (StringUtils.isNotBlank(sourceName)) {
				contentSources.add(sourceName);
			}
			String sourceLink = contentSourceJson.getString(ContentExt.SOURCE_LINK);
			if (StringUtils.isNotBlank(sourceLink)) {
				contentSources.add(sourceLink);
			}
			if (!CollectionUtils.isEmpty(contentSources)) {
				json.put(CmsModelConstant.FIELD_SYS_CONTENT_SOURCE, contentSources);
			}
		}
		String author = dto.getAuthor();
		if (StringUtils.isNotBlank(author)) {
			json.put(CmsModelConstant.FIELD_SYS_AUTHOR, author);
		}
		String keyword = dto.getKeyword();
		if (StringUtils.isNotBlank(keyword)) {
			json.put(CmsModelConstant.FIELD_SYS_KEY_WORD, keyword);
		}
		Integer resourceId = dto.getResource();
		if (resourceId != null) {
			json.put(CmsModelConstant.FIELD_SYS_CONTENT_RESOURCE, resourceId);
		}
		// 传入的dto的自定义字段的JSON
		JSONObject sourceJson = dto.getJson();
		if (sourceJson != null) {
			for (String field : sourceJson.keySet()) {
				String value = sourceJson.getString(field);
				// TODO 将数据转换成string然后进行为空或者是传入的是一个空的数组判断，然后删选出有值得情况，此处只判断此两种情况其它的未判断
				if (StringUtils.isNotBlank(value) && !value.equals("[]") && !value.equals("null")) {
					json.put(field, sourceJson.get(field));
				}
			}
		}
		return json;
	}

	public Boolean getForce() {
		return force;
	}

	public void setForce(Boolean force) {
		this.force = force;
	}

}
