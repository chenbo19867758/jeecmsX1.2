/**
*@Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.audit.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jeecms.audit.dao.AuditModelItemDao;
import com.jeecms.audit.domain.AuditModelItem;
import com.jeecms.audit.service.AuditModelItemService;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;

/**
 * 审核模型字段实现类
* @author ljw
* @version 1.0
* @date 2019-12-16
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class AuditModelItemServiceImpl extends BaseServiceImpl<AuditModelItem,AuditModelItemDao, Integer>  
		implements AuditModelItemService {

	@Override
	public List<AuditModelItem> findByAuditModelId(Integer auditModelId) throws GlobalException {
		return dao.findByAuditModelId(auditModelId);
	}

 
}