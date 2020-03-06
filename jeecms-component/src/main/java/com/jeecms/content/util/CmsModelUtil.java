package com.jeecms.content.util;

import com.jeecms.audit.domain.AuditStrategy;
import com.jeecms.content.constants.CmsModelConstant;
import com.jeecms.content.constants.ContentConstant.ContentCheckFieldAndDataType;
import com.jeecms.content.domain.CmsModelItem;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 模型工具类
 * 
 * @author: cm
 * @date: 2019年12月28日 上午11:21:45
 */
public class CmsModelUtil {

	/**
	 * 审核内容过滤模型字段
	 * 
	 * @Title: checkContentCmsModelItem
	 * @param items 查询出的模型字段集合
	 * @param type  内容审核的字段或数据类型枚举
	 * @return: List<CmsModelItem>
	 */
	public static List<CmsModelItem> checkContentCmsModelItem(List<CmsModelItem> items,
			ContentCheckFieldAndDataType type) {
		List<CmsModelItem> returnItems = new ArrayList<CmsModelItem>();
		// 获取到的对应枚举需要的字段集合
		List<String> fields = CmsModelUtil.processFieldAndDataType(type, false);
		// 默认模型字段集合
		List<CmsModelItem> defaultItems = items.stream().filter(item -> !item.getIsCustom())
				.collect(Collectors.toList());
		for (CmsModelItem cmsModelItem : defaultItems) {
			if (fields.contains(cmsModelItem.getField())) {
				returnItems.add(cmsModelItem);
			}
		}
		// 获取到的对应的枚举需要的字段类型
		List<String> types = CmsModelUtil.processFieldAndDataType(type, true);
		// 自定义模型字段集合
		List<CmsModelItem> customItems = items.stream().filter(item -> item.getIsCustom()).collect(Collectors.toList());
		for (CmsModelItem cmsModelItem : customItems) {
			if (types.contains(cmsModelItem.getDataType())) {
				returnItems.add(cmsModelItem);
			}
		}
		return returnItems;
	}

	/**
	 * 处理模型字段名称或者字段类型
	 * 
	 * @Title: processFieldAndDataType
	 * @param enums  内容审核的字段或数据类型枚举
	 * @param isType 是否是数据类型:true->数据类型，false->数字字段
	 * @return: List<String>
	 */
	public static List<String> processFieldAndDataType(ContentCheckFieldAndDataType enums, boolean isType) {
		// 文本名称集合
		List<String> txtFields = Arrays.asList(CmsModelConstant.FIELD_SYS_TITLE, CmsModelConstant.FIELD_SYS_SHORT_TITLE,
				CmsModelConstant.FIELD_SYS_DESCRIPTION, CmsModelConstant.FIELD_SYS_CONTENT_CONTXT,
				CmsModelConstant.FIELD_SYS_CONTENT_CONTENTTAG, CmsModelConstant.FIELD_SYS_CONTENT_SOURCE,
				CmsModelConstant.FIELD_SYS_AUTHOR, CmsModelConstant.FIELD_SYS_KEY_WORD);
		// 图片名称集合
		List<String> imgFields = Arrays.asList(CmsModelConstant.FIELD_SYS_CONTENT_RESOURCE);
		// 文本数据类型集合
		List<String> txtTypes = Arrays.asList(CmsModelConstant.TEXT,CmsModelConstant.TEXTS, CmsModelConstant.MANY_CHART_UPLOAD,
				CmsModelConstant.PHONE, CmsModelConstant.MOBILE, CmsModelConstant.AGE, CmsModelConstant.EMIAL,
				CmsModelConstant.REALNAME, CmsModelConstant.IDENTITY, CmsModelConstant.ADDRESS, CmsModelConstant.FAX);
		// 图片数据类型集合
		List<String> imgTypes = Arrays.asList(CmsModelConstant.SINGLE_CHART_UPLOAD, CmsModelConstant.MANY_CHART_UPLOAD);
		// 视频数据类型集合
		List<String> voideoTypes = Arrays.asList(CmsModelConstant.VIDEO_UPLOAD);
		List<String> returnList = new ArrayList<String>();
		switch (enums) {
		case txt:
			if (isType) {
				returnList = CmsModelUtil.processList(returnList, isType, txtTypes, null, null);
			} else {
				returnList = CmsModelUtil.processList(returnList, isType, txtFields, null, null);
			}
			return returnList;
		case img:
			if (isType) {
				returnList = CmsModelUtil.processList(returnList, isType, null, imgTypes, null);
			} else {
				returnList = CmsModelUtil.processList(returnList, isType, null, imgFields, null);
			}
			return returnList;
		case video:
			if (isType) {
				returnList = CmsModelUtil.processList(returnList, isType, null, null, voideoTypes);
			} else {
				returnList = CmsModelUtil.processList(returnList, isType, null, null, null);
			}
			return returnList;
		case txtAndImg:
			if (isType) {
				returnList = CmsModelUtil.processList(returnList, isType, txtTypes, imgTypes, null);
			} else {
				returnList = CmsModelUtil.processList(returnList, isType, txtFields, imgFields, null);
			}
			return returnList;
		case imgAndVideo:
			if (isType) {
				returnList = CmsModelUtil.processList(returnList, isType, null, imgTypes, voideoTypes);
			} else {
				returnList = CmsModelUtil.processList(returnList, isType, null, imgFields, null);
			}
			return returnList;
		case txtAndVideo:
			if (isType) {
				returnList = CmsModelUtil.processList(returnList, isType, txtTypes, null, voideoTypes);
			} else {
				returnList = CmsModelUtil.processList(returnList, isType, txtFields, null, null);
			}
			return returnList;
		case txtAndImgAndVideo:
			if (isType) {
				returnList = CmsModelUtil.processList(returnList, isType, txtTypes, imgTypes, voideoTypes);
			} else {
				returnList = CmsModelUtil.processList(returnList, isType, txtFields, imgFields, null);
			}
			return returnList;
		}
		return returnList;
	}

	/**
	 * 处理模型字段集合
	 * 
	 * @Title: processList
	 * @param returnList  return出去的集合
	 * @param isType      是否是数据类型:true->数据类型，false->数字字段
	 * @param txtSources  文本源集合
	 * @param imgSources  图片源集合
	 * @param videoSource 视频源集合
	 * @return: List<String>
	 */
	private static List<String> processList(List<String> returnList, boolean isType, List<String> txtSources,
			List<String> imgSources, List<String> videoSource) {
		if (!CollectionUtils.isEmpty(txtSources)) {
			returnList.addAll(txtSources);
		}
		if (!CollectionUtils.isEmpty(imgSources)) {
			returnList.addAll(imgSources);
		}
		if (!CollectionUtils.isEmpty(videoSource)) {
			returnList.addAll(videoSource);
		}
		if (isType) {
			returnList.add(CmsModelConstant.CONTENT_TXT);
		} else {
			returnList.add(CmsModelConstant.FIELD_SYS_CONTENT_CONTXT);
		}
		return returnList;
	}

	/**
	 * 获取 内容审核的字段或数据类型枚举
	 * @Title: getContentCheckFieldAndDataType
	 * @param auditStrategy	审核策略
	 * @return: ContentCheckFieldAndDataType
	 */
	public static ContentCheckFieldAndDataType getContentCheckFieldAndDataType(AuditStrategy auditStrategy) {
		if (auditStrategy.getIsText()) {
			if (auditStrategy.getIsPicture()) {
				return ContentCheckFieldAndDataType.txtAndImg;
			}
			return ContentCheckFieldAndDataType.txt;
		} else {
			if (auditStrategy.getIsPicture()) {
				return ContentCheckFieldAndDataType.img;
			}
		}
		return null;
	}
}
