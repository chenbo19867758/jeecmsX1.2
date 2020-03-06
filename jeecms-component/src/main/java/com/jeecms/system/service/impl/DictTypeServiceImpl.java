package com.jeecms.system.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.dao.DictTypeDao;
import com.jeecms.system.domain.DictData;
import com.jeecms.system.domain.DictType;
import com.jeecms.system.service.DictTypeService;

/**
 * 
 * @Description:字典类型service实现
 * @author: ztx
 * @date: 2018年6月14日 下午4:51:53
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
@Transactional(rollbackFor = { RuntimeException.class })
public class DictTypeServiceImpl extends BaseServiceImpl<DictType, DictTypeDao, Integer> implements DictTypeService {

	@Override
	public DictType update(DictType dictType) throws  GlobalException {
		DictType bean = super.update(dictType);

		if (bean.getDatas() != null) {
			for (DictData dictData : bean.getDatas()) {
				dictData.setDictType(bean.getDictType());
			}
		}
		return super.update(dictType);
	}

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	@Override
	public DictType findByDictType(String dictType) throws GlobalException {
		DictType type = dao.findByDictTypeAndHasDeleted(dictType, false);
		return type;
	}

	@Override
	public DictType changeSys(Integer id, Boolean isSystem) throws GlobalException {
		DictType type = new DictType();
		type.setId(id);
		type.setIsSystem(isSystem);
		type = super.update(type);
		return type;
	}

}
