package com.jeecms.system.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.system.dao.ext.DictDataDaoExt;
import com.jeecms.system.domain.DictData;

/**
 * 
 * @Description:字典数据DAO
 * @author: ztx
 * @date: 2018年6月14日 下午4:50:13
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface DictDataDao extends IBaseDao<DictData, Integer>, DictDataDaoExt {

	/**
	 * 根据字典编码和字典类型 获取字典数据信息
	 * TODO
	 * @Title: findByDictTypeIdAndDictCodeAndHasDeleted  
	 * @param typeId 类型ID
	 * @param code 类型
	 * @param hasDeleted 是否删除
	 * @return      
	 * @return: DictData
	 */
	DictData findByDictTypeIdAndDictCodeAndHasDeleted(Integer typeId, String code, Boolean hasDeleted);

	/**
	 * 根据字典编码Key和字典类型 获取字典数据信息
	 * @Title: findByDictTypeAndDictCodeAndHasDeleted  
	 * @param typeKey 编码key
	 * @param code 字典类型
	 * @param hasDeleted 是否删除
	 * @return      
	 * @return: DictData
	 */
	DictData findByDictTypeAndDictCodeAndHasDeleted(String typeKey, String code, Boolean hasDeleted);

}
