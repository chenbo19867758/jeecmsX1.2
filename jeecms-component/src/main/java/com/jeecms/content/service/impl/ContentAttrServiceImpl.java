/**
*@Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.content.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.constants.CmsModelConstant;
import com.jeecms.content.dao.ContentAttrDao;
import com.jeecms.content.domain.CmsModelItem;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.ContentAttr;
import com.jeecms.content.domain.ContentAttrRes;
import com.jeecms.content.service.CmsModelItemService;
import com.jeecms.content.service.ContentAttrResService;
import com.jeecms.content.service.ContentAttrService;
import com.jeecms.resource.service.ResourcesSpaceDataService;
import com.jeecms.system.domain.Area;
import com.jeecms.system.domain.GlobalConfig;
import com.jeecms.system.service.AreaService;
import com.jeecms.system.service.CmsOrgService;
import com.jeecms.system.service.SysSecretService;

/**
 * 内容自定义属性service实现类
 * 
 * @author ljw
 * @version 1.0
 * @date 2019-05-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ContentAttrServiceImpl extends BaseServiceImpl<ContentAttr, ContentAttrDao, Integer>
		implements ContentAttrService {

	@Override
	public void deleteByContent(Integer contentId) throws GlobalException {
		List<ContentAttr> contentAttrs = dao.findByContentId(contentId);
		super.physicalDeleteInBatch(contentAttrs);
		contentAttrResService
				.deleteByContentAttrs(
						contentAttrs.stream().map(ContentAttr::getId)
						.collect(Collectors.toList()));
	}

	/**
	 * 下方的两个方法有诸多类似，但是不聚合成一个是因为两个方法遍历的对象不同，如果聚合成一个，那个方法的代码将及其冗余
	 */

	@Override
	public List<ContentAttr> initContentAttr(List<ContentAttr> contentAttrs, Integer contentId,
			GlobalConfig globalConfig) throws GlobalException {
		for (ContentAttr contentAttr : contentAttrs) {
			contentAttr.setContentId(contentId);
			List<ContentAttrRes> contentAttrRes = contentAttr.getContentAttrRes();
			if (contentAttrRes != null && contentAttrRes.size() > 0) {
				for (ContentAttrRes newRes : contentAttrRes) {
//					newRes.setContentAttr(contentAttr);
					// 当字段为多图上传时，将附件密级必须置空
					if (CmsModelConstant.MANY_CHART_UPLOAD.equals(contentAttr.getAttrType())) {
						newRes.setSecretId(null);
					}
					// 当字段为附件时，描述必须置空，但是必须判断其是否开启了加密如果开启则直接抛出异常，传入的数据错误
					if (CmsModelConstant.ANNEX_UPLOAD.equals(contentAttr.getAttrType())) {
						newRes.setDescription(null);
					}
				}
			}
		}
		return contentAttrs;
	}

	@Override
	public List<ContentAttr> copyInitContentAttr(List<ContentAttr> contentAttrs, Integer contentId) throws GlobalException {
		/**
		 * 底下无需如上方一样判断因为这是copy，copy如果出现上面的未判断导致出错的问题说明我save、update处理有问题，此处修改了也会有问题
		 */
		List<ContentAttr> newContentAttrs = new ArrayList<ContentAttr>();
		for (ContentAttr contentAttr : contentAttrs) {
			ContentAttr newContentAttr = new ContentAttr();
			newContentAttr = this.copyContentAttr(contentAttr, newContentAttr);
			newContentAttr.setContentId(contentId);
			List<ContentAttrRes> contentAttrRes = contentAttr.getContentAttrRes();
			if (contentAttrRes != null && contentAttrRes.size() > 0) {
				List<ContentAttrRes> newContentAttrRes = new ArrayList<ContentAttrRes>();
				for (ContentAttrRes attrRes : contentAttrRes) {
					ContentAttrRes newAttrRes = new ContentAttrRes();
					newAttrRes = this.copyContentAttRes(attrRes, newAttrRes);
					newAttrRes.setContentAttr(newContentAttr);
					newContentAttrRes.add(newAttrRes);
				}
				newContentAttr.setContentAttrRes(newContentAttrRes);
			}
			newContentAttrs.add(newContentAttr);
		}
		return newContentAttrs;
	}

	private ContentAttrRes copyContentAttRes(ContentAttrRes res,ContentAttrRes newRes) throws GlobalException {
		newRes.setResId(res.getResId());
		newRes.setSecretId(res.getSecretId());
		newRes.setDescription(res.getDescription());
		if (newRes.getResId() != null) {
			newRes.setResourcesSpaceData(resourcesSpaceDataService.findById(newRes.getResId()));
		}
		if (newRes.getSecretId() != null) {
			newRes.setSecret(secretService.findById(newRes.getSecretId()));
		}
		return newRes;
	}
	
	private ContentAttr copyContentAttr(ContentAttr contentAttr,ContentAttr newContentAttr) throws GlobalException {
		newContentAttr.setAttrName(contentAttr.getAttrName());
		newContentAttr.setAttrValue(contentAttr.getAttrValue());
		newContentAttr.setAttrType(contentAttr.getAttrType());
		newContentAttr.setResId(contentAttr.getResId());
		newContentAttr.setOrgId(contentAttr.getOrgId());
		newContentAttr.setProvinceCode(contentAttr.getProvinceCode());
		newContentAttr.setCityCode(contentAttr.getCityCode());
		newContentAttr.setAreaCode(contentAttr.getAreaCode());
		newContentAttr.setValue(contentAttr.getValue());
		if (newContentAttr.getResId() != null) {
			newContentAttr.setResourcesSpaceData(resourcesSpaceDataService.findById(newContentAttr.getResId()));
		}
		if (newContentAttr.getOrgId() != null) {
			newContentAttr.setCmsOrg(cmsOrgService.findById(newContentAttr.getOrgId()));
		}
		List<Area> list = null;
		if (StringUtils.isNotBlank(newContentAttr.getAreaCode())) {
			list = areaService.findByAreaCode(newContentAttr.getAreaCode());
			if (list != null && list.size() > 0) {
				newContentAttr.setArea(list.get(0));
			}
		}
		list = null;
		if (StringUtils.isNotBlank(newContentAttr.getProvinceCode())) {
			list = areaService.findByAreaCode(newContentAttr.getProvinceCode());
			if (list != null && list.size() > 0) {
				newContentAttr.setProvince(list.get(0));
			}
		}
		list = null;
		if (StringUtils.isNotBlank(newContentAttr.getCityCode())) {
			list = areaService.findByAreaCode(newContentAttr.getCityCode());
			if (list != null && list.size() > 0) {
				newContentAttr.setCity(list.get(0));
			}
		}
		return newContentAttr;
	}
	
	@Override
	public List<ContentAttr> pushSiteInitContentAttr(List<ContentAttr> contentAttrs, Integer contentId,
			List<String> itemFiles, GlobalConfig globalConfig) throws GlobalException {
		List<ContentAttr> newContentAttrs = new ArrayList<ContentAttr>();
		for (ContentAttr contentAttr : contentAttrs) {
			if (itemFiles.contains(contentAttr.getAttrName())) {
				ContentAttr newContentAttr = new ContentAttr();
				newContentAttr = this.copyContentAttr(contentAttr, newContentAttr);
				newContentAttr.setContentId(contentId);
				List<ContentAttrRes> contentAttrRes = contentAttr.getContentAttrRes();
				if (contentAttrRes != null && contentAttrRes.size() > 0) {
					List<ContentAttrRes> newContentAttrRes = new ArrayList<ContentAttrRes>();
					for (ContentAttrRes attrRes : contentAttrRes) {
						ContentAttrRes newAttrRes = new ContentAttrRes();
						newAttrRes = this.copyContentAttRes(attrRes, newAttrRes);
						newAttrRes.setContentAttr(newContentAttr);
						newContentAttrRes.add(newAttrRes);
					}
					newContentAttr.setContentAttrRes(newContentAttrRes);
				}
				newContentAttrs.add(newContentAttr);
			}
		}
		return newContentAttrs;
	}

	@Override
	public List<ContentAttr> initContentAttr(JSONObject json, Integer modelId) throws GlobalException {
		List<CmsModelItem> items = cmsModelItemService.findByModelId(modelId);
		// 取出自定义字段(只需要处理自定义字段，默认字段已处理)
		items = items.stream().filter(item -> item.getIsCustom()).collect(Collectors.toList());
		List<ContentAttr> attrs = new ArrayList<ContentAttr>();
		for (CmsModelItem cmsModelItem : items) {
			String field = cmsModelItem.getField();
			String dateType = cmsModelItem.getDataType();
			ContentAttr attr = new ContentAttr(field, dateType);
			Integer resId = null;
			JSONArray array = null;

			List<ContentAttrRes> ress = null;
			switch (dateType) {
				case CmsModelConstant.SINGLE_CHART_UPLOAD:
					resId = json.getInteger(field);
					attr.setResId(resId);
					if (resId != null) {
						attr.setResourcesSpaceData(resourcesSpaceDataService.findById(resId));
					}
					attrs.add(attr);
					continue;
				case CmsModelConstant.MANY_CHART_UPLOAD:
					array = json.getJSONArray(field);
					ress = new ArrayList<ContentAttrRes>();
					if (array != null) {
						for (int i = 0; i < array.size(); i++) {
							JSONObject ject = array.getJSONObject(i);
							if (ject.getInteger("resId") != null) {
								ContentAttrRes res = new ContentAttrRes(
										ject.getInteger("resId"), null,
										ject.getString("description"));
								res.setResourcesSpaceData(resourcesSpaceDataService.findById(ject.getInteger("resId")));
								ress.add(res);
							} else {
								continue;
							}
						}
						if (ress.size() > 0) {
							attr.setContentAttrRes(ress);
						}
						attrs.add(attr);
					}
					continue;
				case CmsModelConstant.VIDEO_UPLOAD:
					resId = json.getInteger(field);
					attr.setResId(resId);
					if (resId != null) {
						attr.setResourcesSpaceData(resourcesSpaceDataService.findById(resId));
					}
					attrs.add(attr);
					continue;
				case CmsModelConstant.AUDIO_UPLOAD:
					resId = json.getInteger(field);
					attr.setResId(resId);
					if (resId != null) {
						attr.setResourcesSpaceData(resourcesSpaceDataService.findById(resId));
					}
					attrs.add(attr);
					continue;
				case CmsModelConstant.ANNEX_UPLOAD:
					array = json.getJSONArray(field);
					ress = new ArrayList<ContentAttrRes>();
					if (array != null) {
						for (int i = 0; i < array.size(); i++) {
							JSONObject ject = array.getJSONObject(i);
							if (ject.getInteger("resId") != null) {
								ContentAttrRes res = new ContentAttrRes(
										ject.getInteger("resId"), 
										ject.getInteger("secretId"),
										null);
								res.setResourcesSpaceData(resourcesSpaceDataService.findById(ject.getInteger("resId")));
								ress.add(res);
							}
						}
						if (ress.size() > 0) {
							attr.setContentAttrRes(ress);
						}
						attrs.add(attr);
					}
					continue;
				case CmsModelConstant.FIELD_SYS_CONTENT_CONTXT:
					// 如果是内容正文过滤掉，其它地方会处理
					continue;
				case CmsModelConstant.TISSUE:
					Integer orgId = json.getInteger(field);
					if (orgId != null) {
						attr.setOrgId(orgId);
						attr.setCmsOrg(cmsOrgService.findById(orgId));
						attrs.add(attr);
					}
					continue;
				case CmsModelConstant.ADDRESS:
					JSONObject ject = json.getJSONObject(field);
					if (ject != null) {
						String provinceCode = ject.getString(ContentAttr.PROVINCE_CODE_NAME);
						this.initAreaObject(attr, provinceCode,Area.AREA_TYPE_PROVINCE);
						String cityCode = ject.getString(ContentAttr.CITY_CODE_NAME);
						this.initAreaObject(attr, cityCode, Area.AREA_TYPE_CITY);
						String areaCode = ject.getString(ContentAttr.AREA_CODE_NAME);
						this.initAreaObject(attr, areaCode, Area.AREA_TYPE_REGION);
						attr.setAttrValue(ject.getString(ContentAttr.ADDRESS_NAME));
						attrs.add(attr);
					}
					continue;
				case CmsModelConstant.CITY:
					JSONObject cityJson = json.getJSONObject(field);
					if (cityJson != null) {
						String provinceCode = cityJson.getString(ContentAttr.PROVINCE_CODE_NAME);
						this.initAreaObject(attr, provinceCode,Area.AREA_TYPE_PROVINCE);
						String cityCode = cityJson.getString(ContentAttr.CITY_CODE_NAME);
						this.initAreaObject(attr, cityCode, Area.AREA_TYPE_CITY);
						attrs.add(attr);
					}
					continue;
				default:
					break;
			}
			attr.setAttrValue(json.getString(field));
			attrs.add(attr);
		}
		return attrs;
	}
	
	private void initAreaObject(ContentAttr attr,String areaCode,String type) throws GlobalException {
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

	@Override
	public Content copySaveContentAttr(List<ContentAttr> contentAttrs,Content bean) throws GlobalException {
		List<ContentAttr> newContentAttrs = new ArrayList<ContentAttr>();
		for (ContentAttr contentAttr : contentAttrs) {
			ContentAttr attrBean = super.save(contentAttr);
			super.flush();
			List<ContentAttrRes> attrRes = contentAttr.getContentAttrRes();
			if (attrRes != null && attrRes.size() > 0) {
				for (ContentAttrRes attrRe : attrRes) {
					attrRe.setContentAttr(attrBean);
					attrRe.setContentAttrId(attrBean.getId());
				}
				List<ContentAttrRes> newAttrRes = contentAttrResService.saveAll(attrRes);
				contentAttrResService.flush();
				attrBean.setContentAttrRes(newAttrRes);
			}
			newContentAttrs.add(attrBean);
		}
		if (newContentAttrs.size() > 0) {
			bean.setContentAttrs(newContentAttrs);
		}
		return bean;
	}
	
	@Autowired
	private ContentAttrResService contentAttrResService;
	@Autowired
	private CmsModelItemService cmsModelItemService;
	@Autowired
	private ResourcesSpaceDataService resourcesSpaceDataService;
	@Autowired
	private CmsOrgService cmsOrgService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private SysSecretService secretService;
	

}