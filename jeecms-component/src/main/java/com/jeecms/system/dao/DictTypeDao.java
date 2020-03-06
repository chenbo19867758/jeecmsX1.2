package com.jeecms.system.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.system.domain.DictType;

/**
 * 
 * @Description:字典类型DAO
 * @author: ztx
 * @date:   2018年6月14日 下午4:51:53     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface DictTypeDao extends IBaseDao<DictType, Integer>{
	
	/**
	 * 根据字典类型名称 查询字典类型数据
	 * @Title: findByDictTypeAndHasDeleted  
	 * @param dictType 字典类型
	 * @param hasDeleted 是否删除
	 * @return      
	 * @return: DictType
	 */
	DictType findByDictTypeAndHasDeleted(String dictType,Boolean hasDeleted);
	
}
