package com.jeecms.system.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.domain.DictData;

/**
 * 
 * @Description:字典数据service
 * @author: ztx
 * @date: 2018年6月14日 下午4:51:53
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface DictDataService extends IBaseService<DictData, Integer> {

	/**
	 * 根据字典类型id和code值 获取字典数据信息
	 * @Title: findByTypeAndCode  
	 * @param typeId 类型ID
	 * @param code 值
	 * @return
	 * @throws GlobalException 程序异常     
	 * @return: DictData
	 */
	DictData findByTypeAndCode(Integer typeId, String code) throws GlobalException;

	/***
	 * 根据字典类型key和code值 获取字典数据信息
	 * 
	 * @Title: findByTypeKeyAndCode
	 * @param typeKey
	 * @param code
	 * @return
	 * @throws GlobalException
	 * @return: DictData
	 */
	DictData findByTypeKeyAndCode(String typeKey, String code) throws GlobalException;

	/**
	 * 改变是否系统内置状态
	 * @Title: changeSys  
	 * @param id 主键ID
	 * @param isSystem 是否系统内置状态
	 * @return
	 * @throws GlobalException 程序异常      
	 * @return: DictData
	 */
	DictData changeSys(Integer id, Boolean isSystem) throws GlobalException;

}
