/**
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.audit.service;

import java.util.List;

import com.jeecms.audit.domain.AuditModelItem;
import com.jeecms.audit.domain.AuditModelSet;
import com.jeecms.audit.domain.dto.AuditModelDto;
import com.jeecms.audit.domain.vo.AuditModelVo;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;

/**
 * 审核模型设置
* @author ljw
* @version 1.0
* @date 2019-12-16
*/
public interface AuditModelSetService extends IBaseService<AuditModelSet, Integer> {

	/**
	 * 获取内容模型下拉列表
	* @Title: models 
	* @param siteId 站点ID
	* @throws GlobalException 异常
	 */
	List<AuditModelVo> models(Integer siteId) throws GlobalException;
	
	/**
	 * 保存模型设置
	* @Title: saveModelSet 
	* @param dto 传输
	* @throws GlobalException 异常
	 */
	ResponseInfo saveOrUpdateModelSet(AuditModelDto dto) throws GlobalException;
	
	/**
	 * 通过模型id查询出审核模型配置的字段
	 * @Title: findByModelId
	 * @param modelId	内容模型id
	 * @return: List
	 */
	List<AuditModelItem> findByModelId(Integer modelId);
}
