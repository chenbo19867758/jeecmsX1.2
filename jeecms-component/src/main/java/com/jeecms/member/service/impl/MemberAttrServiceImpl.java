/**
*@Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.member.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.constants.CmsModelConstant;
import com.jeecms.content.domain.CmsModelItem;
import com.jeecms.member.dao.MemberAttrDao;
import com.jeecms.member.domain.MemberAttr;
import com.jeecms.member.service.MemberAttrService;

/**
 * 会员自定义属性实现类
* @author ljw
* @version 1.0
* @date 2019-07-10
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class MemberAttrServiceImpl extends BaseServiceImpl<MemberAttr,MemberAttrDao, Integer>  
		implements MemberAttrService {

	@Override
	public List<MemberAttr> jsonToMemberAttrs(JSONObject obj, Integer memberId, Set<CmsModelItem> items)
			throws GlobalException {
		// 取出自定义字段(只需要处理自定义字段，默认字段已处理)
		items = items.stream().filter(item -> item.getIsCustom()).collect(Collectors.toSet());
		List<MemberAttr> attrs = new ArrayList<MemberAttr>();
		for (CmsModelItem cmsModelItem : items) {
			//取出模型字段标签名称
			String field = cmsModelItem.getField();
			//数据类型
			String dateType = cmsModelItem.getDataType();
			MemberAttr attr = new MemberAttr(field, dateType);
			attr.setMemberId(memberId);
			attr.setAttrValue(obj.getString(field));
			Integer resId = null;
			//根据模型字段的类型去做处理，
			switch (dateType) {
				case CmsModelConstant.SINGLE_CHART_UPLOAD:
					resId = obj.getInteger(field);
					attr.setResId(resId);
					break;
				case CmsModelConstant.ADDRESS:
					JSONObject json = obj.getJSONObject(field);
					if (json != null) {
						attr.setProvinceCode(json.getString(MemberAttr.PROVINCE_CODE_NAME));
						attr.setCityCode(json.getString(MemberAttr.CITY_CODE_NAME));
						attr.setAreaCode(json.getString(MemberAttr.AREA_CODE_NAME));
						attr.setAttrValue(json.getString(MemberAttr.ADDRESS_NAME));
					}
					break;
				case CmsModelConstant.CITY:
					JSONObject cityJson = obj.getJSONObject(field);
					if (cityJson != null) {
						attr.setProvinceCode(
								cityJson.getString(MemberAttr.PROVINCE_CODE_NAME));
						attr.setCityCode(cityJson.getString(MemberAttr.CITY_CODE_NAME));
						attr.setAttrValue("");
					}
					break;
				default:
					break;
			}
			attrs.add(attr);
		}
		return attrs;
	}

	@Override
	public void deleteAttrs(Integer memberId) {
		dao.deleteAttrs(memberId);
	}
 
}