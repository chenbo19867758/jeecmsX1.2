/**
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.audit.service;

import java.util.List;

import com.jeecms.audit.domain.AuditModelItem;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;

/**
 * 模型字段Service
* @author ljw
* @version 1.0
* @date 2019-12-16
*/
public interface AuditModelItemService extends IBaseService<AuditModelItem, Integer> {

	/**
	 * 根据内容模型设置ID查询字段
	* @Title: findByAuditModelId 
	* @param auditModelId 内容模型设置ID
	* @throws GlobalException 异常
	 */
	List<AuditModelItem> findByAuditModelId(Integer auditModelId) throws GlobalException;
}
