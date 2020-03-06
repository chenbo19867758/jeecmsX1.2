/**
** @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.audit.dao;

import com.jeecms.common.base.dao.IBaseDao;

import java.util.List;

import com.jeecms.audit.domain.AuditModelSet;

/**
 * 模型设置Dao
* @author ljw
* @version 1.0
* @date 2019-12-16
*/
public interface AuditModelSetDao extends IBaseDao<AuditModelSet, Integer> {
	
	/**
	 * 通过内容模型id查询出未被删除的 审核模型设置
	 * @Title: findByModelIdAndHasDeleted
	 * @param modelId		模型id
	 * @param hasDeleted	逻辑删除表示
	 * @return: List
	 */
	List<AuditModelSet> findByModelIdAndHasDeleted(Integer modelId,Boolean hasDeleted);
	
}
