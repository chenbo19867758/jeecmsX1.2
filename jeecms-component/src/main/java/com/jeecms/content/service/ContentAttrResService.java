/**
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.content.service;

import com.jeecms.content.domain.ContentAttrRes;

import java.util.List;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;

/**
 * 内容自定义属性-多资源表Service
* @author ljw
* @version 1.0
* @date 2019-05-15
*/
public interface ContentAttrResService extends IBaseService<ContentAttrRes, Integer> {
	
	/**
	 * 通过自内容自定义属性集合删除该集合对应的多资源对象集合
	 * @Title: deleteByContentAttr  
	 * @param ids	内容自定义属性Id集合
	 * @throws GlobalException      全局异常
	 * @return: void
	 */
	void deleteByContentAttrs(List<Integer> ids) throws GlobalException;

	/**
	 * 根据资源ID查询密级ID
	* @Title: getSecretByRes 
	* @param resId 资源ID
	* @throws GlobalException 异常
	 */
	List<Integer> getSecretByRes(Integer resId) throws GlobalException;
}
