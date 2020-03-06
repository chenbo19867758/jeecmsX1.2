package com.jeecms.system.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.domain.DictType;

/**
 * 
 * @Description:字典类型service
 * @author: ztx
 * @date:   2018年6月14日 下午4:51:53     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface DictTypeService extends IBaseService<DictType, Integer>{
	
	
	/**
	 * 根据字典类型名称查询字典类型信息
	 * @Title: findByDictType  
	 * @param dictType 字典类型
	 * @return
	 * @throws GlobalException 程序异常     
	 * @return: DictType
	 */
	DictType findByDictType(String dictType) throws GlobalException;
	
	/**
	 * 改变是否系统内置状态
	 * @Title: changeSys  
	 * @param id 主键ID
	 * @param isSystem 是否系统内置状态
	 * @return
	 * @throws GlobalException 程序异常     
	 * @return: DictType
	 */
	DictType changeSys(Integer id,Boolean isSystem) throws GlobalException;
}
