package com.jeecms.content.domain.vo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.content.constants.CmsModelConstant;
import com.jeecms.content.constants.ContentConstant;
import com.jeecms.content.domain.*;
import com.jeecms.system.domain.*;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 查询返回VO
 * 
 * @author: chenming
 * @date: 2019年5月21日 下午4:33:47
 */
public class ContentFindVo {
	/** 渲染字段数据 */
	private JSONObject renderingField;
	/** 字段值数据 */
	private JSONObject dataField;
	/** 内容id*/
	private Integer id;
	/** 内容模型*/
	private Integer modelId;
	/** 内容模型名称*/
	private String modelName;
	/** 内容类型关联 **/
	private List<ContentType> contentTypes;
	/** 浏览URL*/
	private String url;
	/** 预览URL*/
	private String previewUrl;
	/**当前支持的审核动作 */
	private List<ContentFlowActionVo> actions;
	/**是否支持撤回*/
	private Boolean revokeSupport;
	/** 该内容权限集*/
	private ContentPurviewVo purview;
	/** 是佛置顶*/
	private Boolean top;
	/** 违禁内容JSON*/
	private JSONObject banJson;
	/** 内容错误信息(如:参数错误、余量不足)*/
	private String checkErrorContent;

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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public List<ContentType> getContentTypes() {
		return contentTypes;
	}

	public void setContentTypes(List<ContentType> contentTypes) {
		this.contentTypes = contentTypes;
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

	public List<ContentFlowActionVo> getActions() {
		return actions;
	}

	public void setActions(List<ContentFlowActionVo> actions) {
		this.actions = actions;
	}

	public Boolean getRevokeSupport() {
		return revokeSupport;
	}

	public void setRevokeSupport(Boolean revokeSupport) {
		this.revokeSupport = revokeSupport;
	}

	public ContentPurviewVo getPurview() {
		return purview;
	}

	public void setPurview(ContentPurviewVo purview) {
		this.purview = purview;
	}
        
	public Boolean getTop() {
		return top;
	}

	public void setTop(Boolean top) {
		this.top = top;
	}
	
	/**
	 * 组装contentFinVo数据
	 */
	public ContentFindVo spliceContentFindVo(
			Content content, GlobalConfig globalConfig,CmsModel model, List<CmsModelItem> modelItems, 
			ContentSource contentSource,Map<Integer,CmsOrg> cmsOrgMap,ContentCheckDetail checkDetail,JSONObject checkBanContentJson) throws GlobalException {
		ContentFindVo findVo = new ContentFindVo();
		findVo.setRenderingField(model.getEnableJson());
		List<ContentTxt> txts = content.getContentTxts();
		Map<String,String> txtMap = new HashMap<String, String>();
		if (txts != null && txts.size() > 0) {
			for (ContentTxt contentTxt : txts) {
				txtMap.put(contentTxt.getAttrKey(), contentTxt.getAttrTxt());
			}
		}
		// 默认字段
		Set<CmsModelItem> defaultModelItems = modelItems.stream()
				.filter(modelItem -> !modelItem.getIsCustom()).collect(Collectors.toSet());
		JSONObject defaultJson = this.initDefaultModelItems(defaultModelItems, content,txtMap,contentSource);
		defaultJson.put(ContentConstant.CONTENT_STATUS_NAME, content.getStatus());
		// 自定义字段
		Set<CmsModelItem> customModelItems = modelItems.stream()
				.filter(modelItem -> modelItem.getIsCustom()).collect(Collectors.toSet());
		JSONObject customJson = this.initCustomModelItems(customModelItems, content.getContentAttrs(), txtMap, cmsOrgMap);
		// 创建一个新的JSON，将默认字段和自定义字段拼接出的JSON组装成一个JSON
		JSONObject dataFieldJson = new JSONObject();
		dataFieldJson.putAll(defaultJson);
		dataFieldJson.putAll(customJson);
		findVo.setDataField(dataFieldJson);
		findVo.setId(content.getId());
		findVo.setModelId(model.getId());
		findVo.setModelName(model.getModelName());
		findVo.setContentTypes(content.getContentTypes());
		findVo.setUrl(content.getUrlWhole());
		findVo.setPreviewUrl(content.getPreviewUrl());
		findVo.setPurview(ContentPurviewVo.initContentPurviewVo(content, new ContentPurviewVo()));
		findVo.setTop(content.getTop());
		if (content.getStatus() == ContentConstant.STATUS_SMART_AUDIT_SUCCESS
				|| content.getStatus() == ContentConstant.STATUS_SMART_AUDIT_FAILURE) {
			if (checkDetail != null) {
				findVo.setBanJson(checkBanContentJson);
				findVo.setCheckErrorContent(checkDetail.getCheckErrorContent());
			}
		}
		return findVo;
	}
	
	/**
	 * 初始化默认字段
	 */
	private JSONObject initDefaultModelItems(Set<CmsModelItem> items,Content content,
			Map<String,String> txtMap, ContentSource contentSource) {
		JSONObject json = new JSONObject();
		ContentExt contentExt = content.getContentExt();
		for (CmsModelItem cmsModelItem : items) {
			/**
			 * tag词、评论设置、正文、发布平台、单图上传、发文字号单独处理
			 *来源没有单独处理，来源返回就是返回一个id值，发布时间、下线时间
			 */
			String field = cmsModelItem.getField();
			if (CmsModelConstant.FIELD_SYS_KEY_WORD.equals(field)) {
				String keyword = contentExt.getKeyWord();
				json.put(field, keyword);
				continue;
			}
			if (CmsModelConstant.FIELD_SYS_TPL_PC.equals(field)) {
				json.put(field, contentExt.getTplPc());
				continue;
			}
			if (CmsModelConstant.FIELD_SYS_TPL_MOBILE.equals(field)) {
				json.put(field, contentExt.getTplMobile());
				continue;
			}
			if (CmsModelConstant.FIELD_SYS_CONTENT_SECRET.equals(field)) {
				json.put(field, content.getContentSecretId());
				continue;
			}
			if (CmsModelConstant.FIELD_SYS_VIEW_CONTROL.equals(field)) {
				json.put(field,content.getViewControl());
				continue;
			}
			if (CmsModelConstant.FIELD_SYS_CHANNEL.equals(field)) {
				json.put(field, content.getChannelId());
				continue;
			}
			if (CmsModelConstant.FIELD_SYS_COMMENT_CONTROL.equals(field)) {
 				json.put(field, Integer.valueOf(content.getCommentControl()));
				continue;
			}
			if (CmsModelConstant.FIELD_SYS_AUTHOR.equals(field)) {
				json.put(field, contentExt.getAuthor());
				continue;
			}
			if (CmsModelConstant.FIELD_SYS_DESCRIPTION.equals(field)) {
				json.put(field, contentExt.getDescription());
				continue;
			}
			if (CmsModelConstant.FIELD_SYS_CONTENT_CONTENTTAG.equals(field)) {
				String tags = null;
				// tag词进行单独处理将list对象处理成string
				List<ContentTag> contentTags = content.getContentTags();
				if (contentTags != null) {
					tags = contentTags.stream().map(ContentTag::getTagName)
							.collect(Collectors.joining(","));
				}
				json.put(field, tags);
				continue;
			}
			if (CmsModelConstant.FIELD_SYS_CONTENT_OUTLINK.equals(field)) {
				JSONObject outLinkJson = new JSONObject();
				outLinkJson.put(field, contentExt.getOutLink());
				outLinkJson.put(ContentExt.IS_NEW_TARGET_NAME, contentExt.getIsNewTarget());
				json.put(field, outLinkJson);
				continue;
			}
			if (CmsModelConstant.FIELD_SYS_CONTENT_RELEASE_TERRACE.equals(field)) {
				List<String> teeraces = new ArrayList<String>();
				if (content.getReleasePc()) {
					teeraces.add(Content.RELEASE_PC_NAME);
				}
				if (content.getReleaseWap()) {
					teeraces.add(Content.RELEASE_WAP_NAME);
				}
				if (content.getReleaseApp()) {
					teeraces.add(Content.RELEASE_APP_NAME);
				}
				if (content.getReleaseMiniprogram()) {
					teeraces.add(Content.RELEASE_MINIPROGRAM_NAME);
				}
				json.put(field, teeraces);
				continue;
			}
			if (CmsModelConstant.FIELD_SYS_CONTENT_RESOURCE.equals(field)) {
				json.put(field, contentExt.getReData());
				continue;
			}
			if (CmsModelConstant.FIELD_SYS_CONTENT_POST_CONTENT.equals(field)) {
				JSONObject teeraceJson = new JSONObject();
				teeraceJson.put(ContentExt.SUE_ORG_NAME, contentExt.getIssueOrg());
				teeraceJson.put(ContentExt.SUE_YEAR_NAME, contentExt.getIssueYear());
				teeraceJson.put(ContentExt.SUE_NUM_NAME, contentExt.getIssueNum());
				json.put(field, teeraceJson);
				continue;
			}
			if (CmsModelConstant.FIELD_SYS_CONTENT_SOURCE.equals(field)) {
				JSONObject contentSourceJson = new JSONObject();
				Integer sourceId = contentExt.getContentSourceId();
				if (sourceId != null) {
					if (contentSource != null) {
						contentSourceJson.put(ContentExt.SOURCE_NAME, 
								contentSource.getSourceName());
						contentSourceJson.put(ContentExt.SOURCE_LINK, 
								contentSource.getSourceLink());
						json.put(field, contentSourceJson);
					} else {
                        contentSourceJson.put(ContentExt.SOURCE_NAME,
                                "");
                        contentSourceJson.put(ContentExt.SOURCE_LINK,
                                "");
                        json.put(field, contentSourceJson);
                    }
				} else {
                    contentSourceJson.put(ContentExt.SOURCE_NAME,
                            "");
                    contentSourceJson.put(ContentExt.SOURCE_LINK,
                            "");
                    json.put(field, contentSourceJson);
                }
				continue;
			}
			if (CmsModelConstant.FIELD_SYS_TITLE.equals(field)) {
				JSONObject titleJson = new JSONObject();
				titleJson.put(field, content.getTitle());
				titleJson.put(Content.TITLE_IS_BOLD_NAME, content.getTitleIsBold());
				titleJson.put(Content.TITLE_COLOR_NAME, content.getTitleColor());
				json.put(field, titleJson);
				continue;
			}
			if (CmsModelConstant.FIELD_SYS_SHORT_TITLE.equals(field)) {
				json.put(field, content.getShortTitle());
				continue;
			}
			if (CmsModelConstant.FIELD_SYS_RELEASE_TIME.equals(field)) {
				json.put(field, MyDateUtils.formatDate(content.getReleaseTime(), MyDateUtils.COM_Y_M_D_H_M_S_PATTERN));
				continue;
			}
			if (CmsModelConstant.FIELD_SYS_OFFLINE_TIME.equals(field)) {
				if (content.getOfflineTime() != null) {
					json.put(field, MyDateUtils.formatDate(content.getOfflineTime(), MyDateUtils.COM_Y_M_D_H_M_S_PATTERN));
				}
				continue;
			}
			if (CmsModelConstant.FIELD_SYS_TEXTLIBRARY.equals(field)) {
				if (content.getContentExt().getDocResource() != null) {
					JSONArray array = new JSONArray();
					JSONObject docResourceJson = new JSONObject();
					docResourceJson.put("resourcesSpaceData", content.getContentExt().getDocResource());
					array.add(docResourceJson);
					json.put(field, array);
					continue;
				}
			}
			// 老数据中可能出现"正文"字段为默认字段但是名称不为content，所以此处为了兼容大部分情况
			if (CmsModelConstant.FIELD_SYS_CONTENT_CONTXT.equals(cmsModelItem.getDataType())) {
				if (StringUtils.isNotBlank(txtMap.get(field))) {
					json.put(field, txtMap.get(field));
					continue;
				} 
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
	private JSONObject initCustomModelItems(
			Set<CmsModelItem> items,List<ContentAttr> contentAttrs,Map<String,String> txtMap,
			Map<Integer,CmsOrg> cmsOrgMap) {
		JSONObject json = new JSONObject();
		Map<String,ContentAttr> attrMap = new HashMap<String, ContentAttr>();
		if (contentAttrs != null && contentAttrs.size() > 0) {
			for (ContentAttr contentAttr : contentAttrs) {
				attrMap.put(contentAttr.getAttrName(), contentAttr);
			}
		}
		for (CmsModelItem cmsModelItem : items) {
			String field = cmsModelItem.getField();
			ContentAttr attr = attrMap.get(field);
			// 默认值
			Object defaultValue = JSONObject.parseObject(cmsModelItem.getContent())
					.getJSONObject(CmsModelConstant.MODEL_ITEM_CONTENT_VALUE)
					.get(CmsModelConstant.MODEL_ITEM_CONTENT_DEFAULT_VALUE);
			/**
			 * 特殊处理字段：单图上传、多图上传、视频上传、音频上传、附件上传、富文本、组织、所在地、城市、多选框、下拉框、单选
			 */
			if (attr != null) {
				String value = attr.getAttrValue();
				switch (attr.getAttrType()) {
					case CmsModelConstant.SINGLE_CHART_UPLOAD:
						if (attr.getResourcesSpaceData() != null) {
							json.put(field, attr.getResourcesSpaceData());
						}
						continue;
					case CmsModelConstant.MANY_CHART_UPLOAD:
						if (attr.getContentAttrRes() != null) {
							json.put(field, attr.getContentAttrRes());
						}
						continue;
					case CmsModelConstant.VIDEO_UPLOAD:
						if (attr.getResourcesSpaceData() != null) {
							json.put(field, attr.getResourcesSpaceData());
						}
						continue;
					case CmsModelConstant.AUDIO_UPLOAD:
						if (attr.getResourcesSpaceData() != null) {
							json.put(field, attr.getResourcesSpaceData());
						}
						continue;
					case CmsModelConstant.ANNEX_UPLOAD:
						if (attr.getContentAttrRes() != null) {
							json.put(field, attr.getContentAttrRes());
						}
						continue;
					case CmsModelConstant.TISSUE:
						if (attr.getOrgId() != null) {
							CmsOrg cmsOrg = cmsOrgMap.get(attr.getOrgId());
							if (cmsOrg != null) {
								json.put(field, cmsOrg.getNodeIds());
							}
						}
						continue;
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
							json.put(field, JSONObject.parseObject(value));
							continue;
						}
					default:
						break;
				}
				if (value != null) {
					json.put(field, value);
				} else {
					json.put(field, defaultValue);
				}
			} else {
				// "正文"为自定义字段
				if (CmsModelConstant.FIELD_SYS_CONTENT_CONTXT.equals(cmsModelItem.getDataType())) {
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

	public JSONObject getBanJson() {
		return banJson;
	}

	public void setBanJson(JSONObject banJson) {
		this.banJson = banJson;
	}

	public String getCheckErrorContent() {
		return checkErrorContent;
	}

	public void setCheckErrorContent(String checkErrorContent) {
		this.checkErrorContent = checkErrorContent;
	}
	
}
