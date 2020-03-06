package com.jeecms.channel.domain.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.domain.ChannelAttr;
import com.jeecms.channel.domain.ChannelAttrRes;
import com.jeecms.channel.domain.ChannelExt;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.constants.CmsModelConstant;
import com.jeecms.content.domain.ChannelTxt;
import com.jeecms.content.domain.CmsModel;
import com.jeecms.content.domain.CmsModelItem;
import com.jeecms.content.domain.ContentAttr;
import com.jeecms.system.domain.CmsOrg;
import com.jeecms.system.domain.GlobalConfig;

/**
 * 查询栏目vo
 * 
 * @author: chenming
 * @date: 2019年6月28日 下午2:03:38
 */
public class ChannelFindVo {
	/** 渲染字段数据 */
	private JSONObject renderingField;
	/** 字段值数据 */
	private JSONObject dataField;
	/** 栏目id */
	private Integer id;
	/** 模型id */
	private Integer modelId;
	/** 模型名称 */
	private String modelName;
	/** 浏览URL */
	private String url;
	/** 预览URL */
	private String previewUrl;

	public JSONObject getRenderingField() {
		return renderingField;
	}

	public void setRenderingField(JSONObject renderingField) {
		this.renderingField = renderingField;
	}

	public JSONObject getDataField() {
		return dataField;
	}

	public void setDataField(JSONObject dataField) {
		this.dataField = dataField;
	}

	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPreviewUrl() {
		return previewUrl;
	}

	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
	}

	/**
	 * 初始化栏目查询对象：channelFindVo
	 * @Title: splicChannelFindVo  
	 * @param channel	栏目对象
	 * @param globalConfig	全局设置
	 * @param model		栏目模型对象
	 * @param modelItems	栏目模型字段集合
	 * @param attr		栏目自定义字段
	 * @param cmsOrgMap	栏目所有自定义中Map<组织ID:组织对象>
	 */
	public ChannelFindVo splicChannelFindVo(Channel channel, GlobalConfig globalConfig, CmsModel model,
			List<CmsModelItem> modelItems, List<ChannelAttr> attr,Map<Integer,CmsOrg> cmsOrgMap) throws GlobalException {
		ChannelFindVo findVo = new ChannelFindVo();
		findVo.setRenderingField(model.getEnableJson());
		List<ChannelTxt> txts = channel.getTxts();
		Map<String, String> txtMap = new HashMap<String, String>();
		if (txts != null && txts.size() > 0) {
			for (ChannelTxt channelTxt : txts) {
				txtMap.put(channelTxt.getAttrKey(), channelTxt.getAttrTxt());
			}
		}
		// 默认字段
		List<CmsModelItem> defaultModelItems = modelItems.stream().filter(modelItem -> !modelItem.getIsCustom())
				.collect(Collectors.toList());
		
		JSONObject defaultJson = this.initDefaultModelItems(defaultModelItems, channel, txtMap);
		// 自定义字段
		List<CmsModelItem> customModelItems = modelItems.stream().filter(modelItem -> modelItem.getIsCustom())
				.collect(Collectors.toList());
		JSONObject customJson = this.initCustomModelItems(customModelItems, attr, txtMap,cmsOrgMap);
		// 创建一个新的JSON，将默认字段和自定义字段拼接出的JSON组装成一个JSON
		JSONObject dataFieldJson = new JSONObject();
		dataFieldJson.putAll(defaultJson);
		dataFieldJson.putAll(customJson);
		findVo.setDataField(dataFieldJson);
		findVo.setId(channel.getId());
		findVo.setModelId(model.getId());
		findVo.setModelName(model.getModelName());
		findVo.setUrl(channel.getUrlWhole());
		findVo.setPreviewUrl(channel.getPreviewUrl());
		return findVo;
	}

	/**
	 * 初始化默认字段
	 */
	private JSONObject initDefaultModelItems(List<CmsModelItem> items, Channel channel, Map<String, String> txtMap) {
		JSONObject json = new JSONObject();
		ChannelExt channelExt = channel.getChannelExt();
		for (CmsModelItem cmsModelItem : items) {
			String field = cmsModelItem.getField();
			if (CmsModelConstant.FIELD_CHANNEL_PARENT_ID.equals(field)) {
				if (channel.getParentId() != null) {
					json.put(field, channel.getParentId());
				} else {
					json.put(field, 0);
				}
				continue;
			}
			if (CmsModelConstant.FIELD_CHANNEL_NAME.equals(field)) {
				json.put(field, channel.getName());
				continue;
			}
			if (CmsModelConstant.FIELD_CHANNEL_PATH.equals(field)) {
				json.put(field, channel.getPath());
				continue;
			}
			if (CmsModelConstant.FIELD_CHANNEL_CONTENT_TPLS.equals(field)) {
				if (channel.getContentTpls() != null) {
					json.put(field, channel.getContentTpls());
					continue;
				}
			}
			if (CmsModelConstant.FIELD_CHANNEL_LINK.equals(field)) {
				JSONObject linkJson = new JSONObject();
				if (channel.getLink() != null) {
					linkJson.put(field, channel.getLink());
				}
				linkJson.put(Channel.LINK_TARGET_NAME, channel.getLinkTarget());
				json.put(field, linkJson);
				continue;
			}
			if (CmsModelConstant.FIELD_CHANNEL_RESOURCE_ID.equals(field)) {
				if (channelExt.getResourcesSpaceData() != null) {
					json.put(field, channelExt.getResourcesSpaceData());
					continue;
				}
			}
			if (CmsModelConstant.FIELD_CHANNEL_DISPLAY.equals(field)) {
				json.put(field, channel.getDisplay());
				continue;
			}
			if (CmsModelConstant.FIELD_CHANNEL_CONTRIBUTE.equals(field)) {
				json.put(field, channel.getContribute());
				continue;
			}
			if (CmsModelConstant.FIELD_SYS_CHANNEL_LIST.equals(field)) {
				json.put(field, channel.getIsListChannel());
				continue;
			}
			if (CmsModelConstant.FIELD_CHANNEL_COMMENT_CONTROL.equals(field)) {
				json.put(field, channelExt.getCommentControl());
				continue;
			}
			if (CmsModelConstant.FIELD_CHANNEL_VIEW_CONTROL.equals(field)) {
				json.put(field, channelExt.getViewControl());
				continue;
			}
			if (CmsModelConstant.FIELD_CHANNEL_WORKFLOWID.equals(field)) {
				json.put(field, channel.getWorkflowId());
				continue;
			}
			if (CmsModelConstant.FIELD_CHANNEL_PAGE_SIZE.equals(field)) {
				json.put(field, channelExt.getPageSize());
				continue;
			}
			if (CmsModelConstant.FIELD_CHANNEL_TXT.equals(field)) {
				json.put(field, channelExt.getTxt());
				continue;
			}
			if (CmsModelConstant.FIELD_CHANNEL_SEO_TITLE.equals(field)) {
				json.put(field, channelExt.getSeoTitle());
				continue;
			}
			if (CmsModelConstant.FIELD_CHANNEL_SEO_KEYWORD.equals(field)) {
				json.put(field, channelExt.getSeoKeywork());
				continue;
			}
			if (CmsModelConstant.FIELD_CHANNEL_SEO_DESCRIPTION.equals(field)) {
				json.put(field, channelExt.getSeoDescription());
				continue;
			}

			if (CmsModelConstant.FIELD_CHANNEL_TPL_PC.equals(field)) {
				json.put(field, channel.getTplPc());
				continue;
			}
			if (CmsModelConstant.FIELD_CHANNEL_TPL_MOBILE.equals(field)) {
				json.put(field, channel.getTplMobile());
				continue;
			}
			// 默认值
			Object defaultValue = JSONObject.parseObject(cmsModelItem.getContent())
					.getJSONObject(CmsModelConstant.MODEL_ITEM_CONTENT_VALUE)
					.get(CmsModelConstant.MODEL_ITEM_CONTENT_DEFAULT_VALUE);
			json.put(field, defaultValue);
		}
		return json;
	}

	/**
	 * 初始化自定义字段
	 */
	private JSONObject initCustomModelItems(List<CmsModelItem> items, List<ChannelAttr> channelAttrs,
			Map<String, String> txtMap, Map<Integer,CmsOrg> cmsOrgMap) {
		JSONObject json = new JSONObject();
		Map<String, ChannelAttr> attrMap = new HashMap<String, ChannelAttr>();
		if (channelAttrs != null && channelAttrs.size() > 0) {
			for (ChannelAttr channelAttr : channelAttrs) {
				attrMap.put(channelAttr.getAttrName(), channelAttr);
			}
		}
		List<ChannelAttrRes> attrRes = null;
		for (CmsModelItem cmsModelItem : items) {
			String field = cmsModelItem.getField();
			ChannelAttr attr = attrMap.get(field);
			// 默认值
			Object defaultValue = JSONObject.parseObject(cmsModelItem.getContent())
					.getJSONObject(CmsModelConstant.MODEL_ITEM_CONTENT_VALUE)
					.get(CmsModelConstant.MODEL_ITEM_CONTENT_DEFAULT_VALUE);
			/**
			 * 特殊处理字段：单图上传、多图上传、视频上传、音频上传、附件上传、富文本、组织、所在地、城市、多选、下拉框、单选
			 */
			if (attr != null) {
				String value = attr.getAttrValue();
				switch (attr.getAttrType()) {
				case CmsModelConstant.SINGLE_CHART_UPLOAD:
					if (attr.getResourcesSpaceData() != null) {
						json.put(field, attr.getResourcesSpaceData());
						continue;
					}
				case CmsModelConstant.MANY_CHART_UPLOAD:
					attrRes = attr.getChannelAttrRes();
					if (attrRes != null) {
						json.put(field, attrRes);
						continue;
					}
				case CmsModelConstant.VIDEO_UPLOAD:
					if (attr.getResourcesSpaceData() != null) {
						json.put(field, attr.getResourcesSpaceData());
						continue;
					}
				case CmsModelConstant.AUDIO_UPLOAD:
					if (attr.getResourcesSpaceData() != null) {
						json.put(field, attr.getResourcesSpaceData());
						continue;
					}
				case CmsModelConstant.ANNEX_UPLOAD:
					attrRes = attr.getChannelAttrRes();
					if (attrRes != null) {
						json.put(field, attrRes);
						continue;
					}
				case CmsModelConstant.TISSUE:
					if (attr.getOrgId() != null) {
						CmsOrg cmsOrg = cmsOrgMap.get(attr.getOrgId());
						if (cmsOrg != null) {
							json.put(field, cmsOrg.getNodeIds());
						}
						continue;
					}
				case CmsModelConstant.ADDRESS:
					JSONObject addressJson = new JSONObject();
					addressJson.put(ContentAttr.PROVINCE_CODE_NAME, attr.getProvinceCode());
					addressJson.put(ContentAttr.CITY_CODE_NAME, attr.getCityCode());
					addressJson.put(ContentAttr.AREA_CODE_NAME, attr.getAreaCode());
					addressJson.put(ContentAttr.ADDRESS_NAME, attr.getAttrValue());
					json.put(field, addressJson);
					continue;
				case CmsModelConstant.CITY:
					JSONObject cityJson = new JSONObject();
					cityJson.put(ContentAttr.PROVINCE_CODE_NAME, attr.getProvinceCode());
					cityJson.put(ContentAttr.CITY_CODE_NAME, attr.getCityCode());
					json.put(field, cityJson);
					continue;
				case CmsModelConstant.MANY_CHOOSE:
					if (StringUtils.isNotBlank(value)) {
						json.put(field, JSONObject.parseObject(value));
						continue;
					}
				case CmsModelConstant.DROP_DOWN:
					if (StringUtils.isNotBlank(value)) {
						json.put(field, JSONObject.parseObject(value));
						continue;
					}
				case CmsModelConstant.SINGLE_CHOOSE:
					if (StringUtils.isNotBlank(value)) {
						json.put(field, JSONObject.parseObject(value));
						continue;
					}
				case CmsModelConstant.SEX:
					if (StringUtils.isNotBlank(value)) {
						json.put(field, JSONObject.parse(value));
						continue;
					}
				default:
					break;
				}

				if (StringUtils.isNotBlank(value)) {
					json.put(field, value);
				} else {
					json.put(field, defaultValue);
				}
			} else {
				if (CmsModelConstant.RICH_TEXT.equals(cmsModelItem.getDataType())) {
					if (StringUtils.isNotBlank(txtMap.get(field))) {
						json.put(field, txtMap.get(field));
						continue;
					}
				}
				json.put(field, defaultValue);
			}
		}
		return json;
	}

}
