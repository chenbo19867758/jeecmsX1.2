package com.jeecms.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.dao.DictDataDao;
import com.jeecms.system.domain.DictData;
import com.jeecms.system.domain.DictType;
import com.jeecms.system.service.DictDataService;
import com.jeecms.system.service.DictTypeService;

/**
 * 字典数据service实现
 * 
 * @author: ztx
 * @date: 2018年6月14日 下午4:51:53
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
@Transactional(rollbackFor = { RuntimeException.class })
public class DictDataServiceImpl extends BaseServiceImpl<DictData, DictDataDao, Integer> implements DictDataService {

        @Override
        @Transactional(readOnly = true, rollbackFor = Exception.class)
        public DictData findByTypeAndCode(Integer typeId, String code) throws GlobalException {
                return dao.findByDictTypeIdAndDictCodeAndHasDeleted(typeId, code, false);
        }

        @Override
        @Transactional(readOnly = true, rollbackFor = Exception.class)
        public DictData findByTypeKeyAndCode(String typeKey, String code) throws GlobalException {
                return dao.findByDictTypeAndDictCodeAndHasDeleted(typeKey, code, false);
        }

        @Override
        public DictData save(DictData bean) throws GlobalException {
                Integer typeId = bean.getDictTypeId();
                DictType type = dictTypeService.findById(typeId);
                bean.setCoreDictType(type);
                bean = super.save(bean);
                if (type != null) {
                        type.getDatas().add(bean);
                }
                return bean;
        }

        @Override
        public List<DictData> delete(Integer[] ids) throws GlobalException {
                List<DictData> list = super.delete(ids);
                for (Integer id : ids) {
                        DictData dictData;
                        dictData = findById(id);
                        if (dictData != null) {
                                Integer typeId = dictData.getDictTypeId();
                                DictType type = dictTypeService.findById(typeId);
                                if (type != null) {
                                        type.getDatas().remove(dictData);
                                }
                        }
                }
                return list;
        }

        @Override
        public DictData changeSys(Integer id, Boolean isSystem) throws GlobalException {
                DictData data = new DictData();
                data.setId(id);
                data.setIsSystem(isSystem);
                data = super.update(data);
                return data;
        }

        @Autowired
        private DictTypeService dictTypeService;

}
