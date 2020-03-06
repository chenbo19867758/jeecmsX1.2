package com.jeecms.content.domain.dto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.jeecms.common.util.MyDateUtils;
import com.jeecms.content.constants.CmsModelConstant;
import com.jeecms.content.domain.CmsModelItem;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.ContentAttr;
import com.jeecms.content.domain.ContentAttrRes;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.system.domain.ContentMark;
import com.jeecms.system.domain.SysSecret;

/**
 * 校验改动Dto
 * 
 * @author: chenming
 * @date: 2019年5月23日 上午9:07:43
 */
public class CheckUpdateDto {

	/** 字段 */
	private String field;
	/** 名称 */
	private String itemLabel;
	/** 值 */
	private String value;
	/** 是否是正文*/
	private Boolean isTxt = false;
	/** 是否是多资源*/ 
	private Boolean isResources = false;
	/** 是否是多图上传*/
	private Boolean isPhones = false;
	/** 多资源之：图片的描述*/
	private String phoneDeptict;
	/** 资源别名：单图的资源别名或者是多图附件的资源别名*/
	private String attrResValue;
	/** 字段为附件上传中的密级*/
	private String secret;
	
	public String getItemLabel() {
		return itemLabel;
	}

	public void setItemLabel(String itemLabel) {
		this.itemLabel = itemLabel;
	}


	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Boolean getIsTxt() {
		return isTxt;
	}

	public void setIsTxt(Boolean isTxt) {
		this.isTxt = isTxt;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Boolean getIsResources() {
		return isResources;
	}

	public void setIsResources(Boolean isResources) {
		this.isResources = isResources;
	}

	public String getAttrResValue() {
		return attrResValue;
	}

	public void setAttrResValue(String attrResValue) {
		this.attrResValue = attrResValue;
	}

	public Boolean getIsPhones() {
		return isPhones;
	}

	public void setIsPhones(Boolean isPhones) {
		this.isPhones = isPhones;
	}

	public String getPhoneDeptict() {
		return phoneDeptict;
	}

	public void setPhoneDeptict(String phoneDeptict) {
		this.phoneDeptict = phoneDeptict;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public static List<CheckUpdateDto> spliceCheckUpdateDtos(List<CmsModelItem> modelItems,
			SpliceCheckUpdateDto checkUpdateDto) {
		modelItems = modelItems.stream().filter(
				CmsModelItem -> 
				!CmsModelItem.getField().equals(CmsModelConstant.FIELD_SYS_CONTENT_RELEASE_TERRACE)
						&& 
				!CmsModelItem.getField().equals(CmsModelConstant.FIELD_SYS_CONTENT_OUTLINK))
				.collect(Collectors.toList());
		List<CheckUpdateDto> dtos = new ArrayList<CheckUpdateDto>();
		Map<String, Object> contentMap = checkUpdateDto.getContentMap();
		Map<String, Object> contentExtMap = checkUpdateDto.getContentExtMap();
		// tag词集合
		List<String> contentTags = checkUpdateDto.getContentTagKeys();
		String tags = null;
		if (contentTags != null && contentTags.size() > 0) {
			tags = contentTags.stream().collect(Collectors.joining(","));
		}
		List<ContentAttr> contentAttrs = checkUpdateDto.getContentAttrs();
		Map<String, ContentAttr> contentAttrMap = null;
		if (CollectionUtils.isEmpty(contentAttrs)) {
			contentAttrMap = new LinkedHashMap<String, ContentAttr>();
		} else {
			contentAttrMap = contentAttrs.stream()
					.collect(Collectors.toMap(ContentAttr::getAttrName, a -> a));
		}
		// 内容正文
		Map<String, String> contentTxts = checkUpdateDto.getContentTxtMap();
		List<CmsModelItem> defaultItem = modelItems.stream()
				.filter(CmsModelItem -> CmsModelItem.getIsCustom().equals(false))
				.collect(Collectors.toList());
		for (CmsModelItem cmsModelItem : defaultItem) {
			CheckUpdateDto dto = new CheckUpdateDto();
			String field = cmsModelItem.getField();
			dto.setField(field);
			dto.setItemLabel(cmsModelItem.getItemLabel());
			Object val = contentMap.get(field);
			if (val == null) {
				val = contentExtMap.get(field);
			}
			dto.setValue(String.valueOf(val));
			if (CmsModelConstant.FIELD_SYS_CONTENT_CONTENTTAG.equals(field)) {
				dto.setValue(tags);
			}
			if (CmsModelConstant.FIELD_SYS_CONTENT_POST_CONTENT.equals(field)) {
				StringBuffer buffer = new StringBuffer();
				ContentMark sueOrg = checkUpdateDto.getSueOrg();
				ContentMark sueYear = checkUpdateDto.getSueYear();
				if (sueOrg != null) {
					buffer.append("," + sueOrg.getMarkName());
					buffer.append("," + sueYear.getMarkName());
					buffer.append("," + checkUpdateDto.getIssueNum());
				}
				String value = buffer.toString();
				dto.setValue(value.length() > 0 ? value : null);
			}
			if (CmsModelConstant.FIELD_SYS_CHANNEL.equals(field)) {
				dto.setValue(checkUpdateDto.getChannelName());
			}
			if (CmsModelConstant.FIELD_SYS_CONTENT_SOURCE.equals(field)) {
				dto.setValue(null);
			}
			if (CmsModelConstant.FIELD_SYS_CONTENT_SECRET.equals(field)) {
				dto.setValue(checkUpdateDto.getSecretName());
			}
			if (CmsModelConstant.FIELD_SYS_CONTENT_RESOURCE.equals(field)) {
				ResourcesSpaceData spaceData = checkUpdateDto.getSpaceData();
				if (spaceData != null) {
					dto.setValue(String.valueOf(spaceData.getId()));
					dto.setAttrResValue(spaceData.getAlias());
				} else {
					dto.setValue(null);
					dto.setAttrResValue(null);
				}
			}
			if (CmsModelConstant.FIELD_SYS_TEXTLIBRARY.equals(field)) {
				ResourcesSpaceData docResource = checkUpdateDto.getDocResource();
				if (docResource != null) {
					dto.setValue(String.valueOf(docResource.getId()));
					dto.setAttrResValue(docResource.getAlias());
				} else {
					dto.setValue(null);
					dto.setAttrResValue(null);
				}
			}
			if (CmsModelConstant.FIELD_SYS_VIEW_CONTROL.equals(field)) {
				dto.setValue(Content.VIEW_DEPICT.get(Integer.valueOf(checkUpdateDto.getViewControl())));
			}
			if (CmsModelConstant.FIELD_SYS_COMMENT_CONTROL.equals(field)) {
				dto.setValue(Content.COMMENT_DEPICT.get(checkUpdateDto.getCommentControl()));
			}
			if (CmsModelConstant.FIELD_SYS_RELEASE_TIME.equals(field)) {
				dto.setValue(MyDateUtils.formatDate(checkUpdateDto.getReleaseTime()));
			}
			if (CmsModelConstant.FIELD_SYS_OFFLINE_TIME.equals(field)) {
				if (checkUpdateDto.getOfflineTime() != null) {
					dto.setValue(MyDateUtils.formatDate(checkUpdateDto.getOfflineTime()));
				}
			}
			dtos.add(dto);

		}
		List<CmsModelItem> customizeItem = modelItems.stream()
				.filter(CmsModelItem -> CmsModelItem.getIsCustom().equals(true))
				.collect(Collectors.toList());
		for (CmsModelItem cmsModelItem : customizeItem) {
			CheckUpdateDto dto = new CheckUpdateDto();
			String field = cmsModelItem.getField();
			dto.setField(field);
			dto.setItemLabel(cmsModelItem.getItemLabel());
			Object val = null;
			ContentAttr contentAttr = contentAttrMap.get(field);
			if (contentAttr != null) {
				val = contentAttrMap.get(field).getAttrValue();
				dto.setValue(String.valueOf(val));
				String attrType = contentAttr.getAttrType();
				if (CmsModelConstant.MANY_CHART_UPLOAD.equals(attrType) 
						|| 
					CmsModelConstant.ANNEX_UPLOAD.equals(attrType)) {
					dto.setValue(null);
					dto.setIsResources(true);
					dto.setValue(
							CheckUpdateDto.initResourcesCheckUpdateDto(
									contentAttr.getContentAttrRes(), 3));
					dto.setAttrResValue(
							CheckUpdateDto.initResourcesCheckUpdateDto(
									contentAttr.getContentAttrRes(), 2));
				}
				switch (contentAttr.getAttrType()) {
					case CmsModelConstant.MANY_CHART_UPLOAD:
						dto.setIsPhones(true);
						dto.setPhoneDeptict(
								CheckUpdateDto.initResourcesCheckUpdateDto(
										contentAttr.getContentAttrRes(), 1));
						break;
					case CmsModelConstant.ANNEX_UPLOAD:
						dto.setSecret(CheckUpdateDto.initResourcesCheckUpdateDto(
								contentAttr.getContentAttrRes(), 4));
						break;
					case CmsModelConstant.ADDRESS:
						dto.setValue(contentAttr.getProvince().getAreaName() + "," 
								+ contentAttr.getCity().getAreaName() + ","
								+ contentAttr.getArea().getAreaName() + "," 
								+ contentAttr.getAttrValue());
						break;
					case CmsModelConstant.CITY:
						dto.setValue(
								contentAttr.getProvinceCode() 
								+ "," + contentAttr.getCityCode());
						break;
					case CmsModelConstant.TISSUE:
						dto.setValue(contentAttr.getCmsOrg().getName());
						break;
					default:
						break;
				}
			}
			if (CmsModelConstant.CONTENT_TXT.equals(cmsModelItem.getDataType())) {
				dto.setIsTxt(true);
				dto.setValue(contentTxts.get(field));
			}
//			if (CmsModelConstant.FIELD_SYS_CONTENT_CONTXT.equals(field)) {
//				dto.setIsTxt(true);
//				dto.setValue(contentTxts.get(field));
//			}
			// 此处过滤掉修改前不存在的字段(即修改前模型被修改)
			dtos.add(dto);
		}
		dtos = CheckUpdateDto.saveOtherCheckUpdateDto(dtos, checkUpdateDto.getStatus());
		return dtos;
	}
	
	/**
	 * 初始化多资源字段的checkUpdateDto的操作 status -> 1. 获取到多图资源的对于图片的描述 2. 获取到资源别名 3. 获取到资源id
	 * 4. 获取到资源密级
	 */
	public static String initResourcesCheckUpdateDto(List<ContentAttrRes> dtos, Integer status) {
		StringBuffer buffer = new StringBuffer();
		if (dtos != null) {
			for (ContentAttrRes contentAttrRes : dtos) {
				ResourcesSpaceData spaceData = contentAttrRes.getResourcesSpaceData();
//			if (spaceData == null && contentAttrRes.getResId() != null) {
//				spaceData = resourcesSpaceDataService.findById(contentAttrRes.getResId());
//			}
				if (spaceData != null) {
					switch (status) {
					case 1:
						buffer.append("," + contentAttrRes.getDescription());
						break;
					case 2:
						buffer.append("," + spaceData.getAlias());
						break;
					case 3:
						buffer.append("," + spaceData.getId());
						break;
					case 4:
						// 此处放到资源！=null下面是因为，资源为空所有的一切都不存在意义
						SysSecret secret = contentAttrRes.getSecret();
						if (secret != null) {
							buffer.append("," + secret.getName());
						}
						break;
					default:
						break;
					}
				}
			}
		}
		String returnStr = buffer.toString();
		return returnStr.length() > 0 ? returnStr.substring(1) : null;
	}

	/**
	 * 新增其它不在模型字段中的字段的改动校验对象
	 */
	public static List<CheckUpdateDto> saveOtherCheckUpdateDto(List<CheckUpdateDto> dtos, Integer status) {
		CheckUpdateDto dto = new CheckUpdateDto();
		dto.setItemLabel("内容状态");
		dto.setField("contentStatus");
		dto.setValue(Content.STATUS_DEPICT.get(status));
		dtos.add(dto);
		return dtos;
	}
	

}
