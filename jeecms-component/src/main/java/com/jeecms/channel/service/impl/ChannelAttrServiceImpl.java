/*  
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.channel.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecms.channel.constants.ChannelConstant;
import com.jeecms.channel.dao.ChannelAttrDao;
import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.domain.ChannelAttr;
import com.jeecms.channel.domain.ChannelAttrRes;
import com.jeecms.channel.service.ChannelAttrService;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.constants.CmsModelConstant;
import com.jeecms.content.domain.CmsModelItem;
import com.jeecms.content.domain.ContentAttr;
import com.jeecms.content.service.CmsModelItemService;
import com.jeecms.resource.service.ResourcesSpaceDataService;
import com.jeecms.system.domain.Area;
import com.jeecms.system.domain.GlobalConfig;
import com.jeecms.system.service.AreaService;
import com.jeecms.system.service.CmsOrgService;

/**
 * 栏目内容属性service实现类
 * 
 * @author: chenming
 * @date: 2019年6月28日 下午5:30:04
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ChannelAttrServiceImpl extends BaseServiceImpl<ChannelAttr, ChannelAttrDao, Integer>
		implements ChannelAttrService {

	@Override
	public List<ChannelAttr> splic(List<ChannelAttr> channelAttrs, Integer channelId,
			GlobalConfig globalConfig) throws GlobalException {
		for (ChannelAttr channelAttr : channelAttrs) {
			channelAttr.setChannelId(channelId);
			List<ChannelAttrRes> channelAttrRes = channelAttr.getChannelAttrRes();
			if (channelAttrRes != null && channelAttrRes.size() > 0) {
				for (ChannelAttrRes newRes : channelAttrRes) {
					newRes.setChannelAttr(channelAttr);
					// 当字段为多图上传时，将附件密级必须置空
					if (CmsModelConstant.MANY_CHART_UPLOAD.equals(channelAttr.getAttrType())) {
						newRes.setSecretId(null);
					}
					// 当字段为附件时，描述必须置空，但是必须判断其是否开启了加密如果开启则直接抛出异常，传入的数据错误
					if (CmsModelConstant.ANNEX_UPLOAD.equals(channelAttr.getAttrType())) {
//						if (globalConfig.getConfigAttr().getOpenAttachmentSecurity()) {
//							if (newRes.getSecretId() == null) {
//								throw new GlobalException(new SystemExceptionInfo(
//										ContentErrorCodeEnum.ATTACHMENTS_MUST_BE_ENCRYPTED.getDefaultMessage(),
//										ContentErrorCodeEnum.ATTACHMENTS_MUST_BE_ENCRYPTED.getCode()));
//							}
//						}
						newRes.setDescription(null);
					}
				}
			}
			channelAttr.setChannelAttrRes(channelAttrRes);
		}
		return channelAttrs;
	}

	@Override
	public List<ChannelAttr> init(JSONObject ject, Integer modelId,Integer channelId,String groupType) throws GlobalException {
		List<CmsModelItem> modelItems = cmsModelItemService.findByModelId(modelId);
		// 取出自定义字段(只需要处理自定义字段，默认字段已处理)
		List<CmsModelItem> items = modelItems.stream().filter(item -> item.getIsCustom() && groupType.equals(item.getGroupType())).collect(Collectors.toList());
		List<ChannelAttr> attrs = new ArrayList<ChannelAttr>();
		for (CmsModelItem cmsModelItem : items) {
			String field = cmsModelItem.getField();
			String dateType = cmsModelItem.getDataType();
			ChannelAttr attr = new ChannelAttr(field, dateType);
			Integer resId = null;
			JSONArray array = null;

			List<ChannelAttrRes> ress = null;
			switch (dateType) {
			case CmsModelConstant.SINGLE_CHART_UPLOAD:
				resId = ject.getInteger(field);
				attr.setResId(resId);
				if (resId != null) {
					attr.setResourcesSpaceData(resourcesSpaceDataService.findById(resId));
				}
				attrs.add(attr);
				continue;
			case CmsModelConstant.MANY_CHART_UPLOAD:
				array = ject.getJSONArray(field);
				ress = new ArrayList<ChannelAttrRes>();
				if (array != null) {
					for (int i = 0; i < array.size(); i++) {
						JSONObject json = array.getJSONObject(i);
						if (json.getInteger("resId") != null) {
							ChannelAttrRes res = new ChannelAttrRes(json.getInteger("resId"), null,
									json.getString("description"));
							res.setResourcesSpaceData(resourcesSpaceDataService.findById(json.getInteger("resId")));
							ress.add(res);
						} else {
							continue;
						}
					}
					if (ress.size() > 0) {
						attr.setChannelAttrRes(ress);
					}
					attrs.add(attr);
				}
				continue;
			case CmsModelConstant.VIDEO_UPLOAD:
				resId = ject.getInteger(field);
				attr.setResId(resId);
				if (resId != null) {
					attr.setResourcesSpaceData(resourcesSpaceDataService.findById(resId));
				}
				attrs.add(attr);
				continue;
			case CmsModelConstant.AUDIO_UPLOAD:
				resId = ject.getInteger(field);
				attr.setResId(resId);
				if (resId != null) {
					attr.setResourcesSpaceData(resourcesSpaceDataService.findById(resId));
				}
				attrs.add(attr);
				continue;
			case CmsModelConstant.ANNEX_UPLOAD:
				array = ject.getJSONArray(field);
				ress = new ArrayList<ChannelAttrRes>();
				if (array != null) {
					for (int i = 0; i < array.size(); i++) {
						JSONObject json = array.getJSONObject(i);
						if (json.getInteger("resId") != null) {
							ChannelAttrRes res = new ChannelAttrRes(json.getInteger("resId"), json.getInteger("secretId"),
									null);
							res.setResourcesSpaceData(resourcesSpaceDataService.findById(json.getInteger("resId")));
							ress.add(res);
						}
					}
					if (ress.size() > 0) {
						attr.setChannelAttrRes(ress);
					}
					attrs.add(attr);
				}
				continue;
			case CmsModelConstant.RICH_TEXT:
				// 如果是富文本过滤掉，其它地方会处理
				continue;
			case CmsModelConstant.TISSUE:
				String org = ject.getString(field);
				if (StringUtils.isNotBlank(org)) {
					Integer orgId = Integer.valueOf(org);
					if (orgId != null) {
						attr.setOrgId(orgId);
						attr.setCmsOrg(cmsOrgService.findById(orgId));
						attrs.add(attr);
					}
				}
				continue;
			case CmsModelConstant.ADDRESS:
				JSONObject json = ject.getJSONObject(field);
				if (json != null) {
					String provinceCode = json.getString(ContentAttr.PROVINCE_CODE_NAME);
					this.initAreaObject(attr, provinceCode,Area.AREA_TYPE_PROVINCE);
					String cityCode = json.getString(ContentAttr.CITY_CODE_NAME);
					this.initAreaObject(attr, cityCode,Area.AREA_TYPE_CITY);
					String areaCode = json.getString(ContentAttr.AREA_CODE_NAME);
					this.initAreaObject(attr, areaCode,Area.AREA_TYPE_REGION);
					attr.setAttrValue(json.getString(ContentAttr.ADDRESS_NAME));
					attrs.add(attr);
				}
				continue;
			case CmsModelConstant.CITY:
				JSONObject cityJson = ject.getJSONObject(field);
				if (cityJson != null) {
					String provinceCode = cityJson.getString(ContentAttr.PROVINCE_CODE_NAME);
					this.initAreaObject(attr, provinceCode,Area.AREA_TYPE_PROVINCE);
					String cityCode = cityJson.getString(ContentAttr.CITY_CODE_NAME);
					this.initAreaObject(attr, cityCode,Area.AREA_TYPE_CITY);
					attrs.add(attr);
				}
				continue;
			default:
				break;
			}
			attr.setAttrValue(ject.getString(field));
			attrs.add(attr);
		}
		attrs = this.initOldAttrs(attrs, channelId, modelItems, groupType);
		return attrs;
	}
	
	private void initAreaObject(ChannelAttr attr,String areaCode,String type) throws GlobalException {
		if (StringUtils.isNotBlank(areaCode)) {
			Area area = this.findByAreaCode(areaCode);
			switch (type) {
				case Area.AREA_TYPE_PROVINCE:
					attr.setProvinceCode(areaCode);
					if (area != null) {
						attr.setProvince(area);
					}
					break;
				case Area.AREA_TYPE_CITY:
					attr.setCityCode(areaCode);
					if (area != null) {
						attr.setCity(area);
					}
					break;
				case Area.AREA_TYPE_REGION:
					attr.setAreaCode(areaCode);
					if (area != null) {
						attr.setArea(area);
					}
					break;
				default:
					break;
			}
		}
	}
	
	private Area findByAreaCode(String areaCode) throws GlobalException {
		List<Area> areas = areaService.findByAreaCode(areaCode);
		if (areas != null && areas.size() > 0) {
			return areas.get(0);
		}
		return null;
	}
	
	private List<ChannelAttr> initOldAttrs(List<ChannelAttr> attrs,Integer channelId,List<CmsModelItem> modelItems,String groupType) throws GlobalException {
		/**
		 * 因为前端修改是用选项卡的形式，所以可能存在只会传入部分数据
		 * 所以自定义数据此处是如此处理，即：取出数据库中该模型的所有字段，如果当前传入的字段中不帮在所有字段中，说明该字段已经被删除，那么我们也删除
		 * 取出该修改的块中的所有字段，如果传入的字段在该块中，说明该字段需要由前台传入的值进行修改，如果是的话，删除该字段
		 */
		List<ChannelAttr> oldAttrs = dao.findByChannelId(channelId);
		List<ChannelAttr> removeAttrs = new ArrayList<ChannelAttr>();
		List<String> fileAlls = modelItems.stream().map(CmsModelItem::getField).collect(Collectors.toList());
		List<String> groupFiles = modelItems.stream().filter(item -> groupType.equals(item.getGroupType())).map(CmsModelItem::getField).collect(Collectors.toList());
		for (ChannelAttr chanenlAttr : oldAttrs) {
			if (!fileAlls.contains(chanenlAttr.getAttrName())) {
				removeAttrs.add(chanenlAttr);
			}
			if (groupFiles.contains(chanenlAttr.getAttrName())) {
				removeAttrs.add(chanenlAttr);
			}
		}
		super.physicalDeleteInBatch(removeAttrs);
		if (removeAttrs.size() > 0) {
			oldAttrs.removeAll(removeAttrs);
		}
		if (oldAttrs.size() > 0) {
			attrs.addAll(oldAttrs);
		}
		return attrs;
	}
	
	@Override
	public List<ChannelAttr> findByChannelId(Integer channelId) {
		List<ChannelAttr> attrs = dao.findByChannelId(channelId);
		if (attrs != null && attrs.size() > 0) {
			return attrs;
		} else {
			attrs = new ArrayList<ChannelAttr>();
			return attrs;
		}
	}

	@Override
	public List<ChannelAttr> splicDefValue(Integer modelId, List<ChannelAttr> attrs, boolean isUpdate, Channel channel)
			throws GlobalException {
		/**
		 * 默认值需要处理的只有：1.多选框、2.下拉框、3.城市、4.地址
		 */
		List<CmsModelItem> modelItemList = cmsItemService.findByModelId(modelId);
		modelItemList = modelItemList.stream()
				.filter(item -> StringUtils.equalsAny(item.getDataType(), CmsModelConstant.MANY_CHOOSE,
						CmsModelConstant.DROP_DOWN, CmsModelConstant.CITY, CmsModelConstant.ADDRESS))
				.collect(Collectors.toList());
		Map<String, String> itemTypeMap = modelItemList.stream()
				.collect(Collectors.toMap(CmsModelItem::getField, CmsModelItem::getDataType));
		// 此处不可以使用Controller.toMap，如果使用toMap会直接抛出异常，因为value有极大可能为Null
		Map<String, String> itemDefValueMap = modelItemList.stream().filter(a -> a.getIsCustom().equals(true))
				.collect(HashMap::new, (m, v) -> m.put(v.getField(), v.getDefValue()), HashMap::putAll);
		if (isUpdate) {
			for (ChannelAttr channelAttr : attrs) {
				String attrType = channelAttr.getAttrType();
				String defValue = itemDefValueMap.get(channelAttr.getAttrName());
				switch (attrType) {
				case CmsModelConstant.CITY:
					if (!StringUtils.isNoneBlank(channelAttr.getProvinceCode(),channelAttr.getCityCode(),channelAttr.getAreaCode())) {
						break;
					}
				case CmsModelConstant.ADDRESS:
					if (!StringUtils.isNoneBlank(channelAttr.getProvinceCode(),channelAttr.getCityCode(),channelAttr.getAreaCode(),channelAttr.getAttrValue())) {
						break;
					}
				default:
					if (channelAttr.getAttrValue() == null && StringUtils.isNotBlank(defValue)) {
						channelAttr = this.initAttrDate(channelAttr, defValue);
					}
					break;
				}
			}
		} else {
			for (String field : itemTypeMap.keySet()) {
				String defValue = itemDefValueMap.get(field);
				if (StringUtils.isNotBlank(defValue)) {
					ChannelAttr attr = new ChannelAttr();
					attr.setAttrName(field);
					attr.setAttrType(itemTypeMap.get(field));
					attr.setChannel(channel);
					attr.setChannelId(channel.getId());
					attr = this.initAttrDate(attr, defValue);
					attrs.add(attr);
				}
			}
		}
		return attrs;
	}

	private ChannelAttr initAttrDate(ChannelAttr attr, String value) {
		attr.setAttrValue(value);
		switch (attr.getAttrType()) {
		case CmsModelConstant.CITY:
			JSONObject json = JSONObject.parseObject(value);
			attr.setAttrValue(null);
			attr.setProvinceCode(json.getString(ChannelConstant.ATTR_PROVINCE_CODE_NAME));
			attr.setCityCode(json.getString(ChannelConstant.ATTR_CITY_CODE_NAME));
			break;
		case CmsModelConstant.ADDRESS:
			JSONObject ject = JSONObject.parseObject(value);
			attr.setProvinceCode(ject.getString(ChannelConstant.ATTR_PROVINCE_CODE_NAME));
			attr.setCityCode(ject.getString(ChannelConstant.ATTR_CITY_CODE_NAME));
			attr.setAreaCode(ject.getString(ChannelConstant.ATTR_AREA_CODE_NAME));
			attr.setAttrValue(ject.getString(ChannelConstant.ATTR_ADDRESS_NAME));
			break;
		}
		return attr;
	}

	@Autowired
	private CmsModelItemService cmsModelItemService;
	@Autowired
	private CmsModelItemService cmsItemService;
	@Autowired
	private ResourcesSpaceDataService resourcesSpaceDataService;
	@Autowired
	private CmsOrgService cmsOrgService;
	@Autowired
	private AreaService areaService;
}