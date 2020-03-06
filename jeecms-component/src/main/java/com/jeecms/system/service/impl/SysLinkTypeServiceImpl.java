/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service.impl;

import com.jeecms.common.base.domain.DragSortDto;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.dao.SysLinkTypeDao;
import com.jeecms.system.domain.SysLinkType;
import com.jeecms.system.service.SysLinkTypeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 友情链接分类Service实现类
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-06-10
 */
@Service
@Transactional
public class SysLinkTypeServiceImpl extends BaseServiceImpl<SysLinkType, SysLinkTypeDao, Integer> implements SysLinkTypeService {


	@Override
	public SysLinkType save(String typeName, Integer siteId) throws GlobalException {
		SysLinkType linkType = new SysLinkType();
		linkType.setSiteId(siteId);
		linkType.setTypeName(typeName);
		//排序为站点下分类数量加一
		linkType.setSortNum(getAllBySiteIdNum(siteId) + 1);
		return save(linkType);
	}

	@Override
	public SysLinkType update(Integer id, String typeName, Integer siteId) throws GlobalException {
		SysLinkType linkType = new SysLinkType();
		if (id != null) {
			linkType = findById(id);
		}
		linkType.setTypeName(typeName);
		return update(linkType);
	}


	@Override
	public int getAllBySiteIdNum(Integer siteId) {
		List<SysLinkType> linkTypes = dao.findAllBySiteId(siteId);
		return linkTypes != null ? linkTypes.size() : 0;
	}

	@Override
	public boolean checkByTypeName(String typeName, Integer id, Integer siteId) {
		if (StringUtils.isBlank(typeName)) {
			return true;
		}
		SysLinkType linkType = dao.findByNameAndId(typeName, siteId);
		if (linkType == null) {
			return true;
		} else {
			if (id != null) {
				return linkType.getId().equals(id);
			} else {
				return false;
			}
		}
	}

	@Override
	public void dragSort(DragSortDto sort) throws GlobalException {
		SysLinkType fromType = findById(sort.getFromId());
		SysLinkType toType = findById(sort.getToId());
		Integer fromSort = fromType.getSortNum();
		Integer toSort = toType.getSortNum();
		List<SysLinkType> linkTypes = new ArrayList<SysLinkType>(2);
		fromType.setSortNum(toSort);
		toType.setSortNum(fromSort);
		linkTypes.add(fromType);
		linkTypes.add(toType);
		batchUpdateAll(linkTypes);
	}
}