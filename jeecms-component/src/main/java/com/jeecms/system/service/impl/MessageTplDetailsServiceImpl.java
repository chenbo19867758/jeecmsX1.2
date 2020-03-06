package com.jeecms.system.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.dao.MessageTplDetailsDao;
import com.jeecms.system.domain.MessageTplDetails;
import com.jeecms.system.service.MessageTplDetailsService;

/**
 * @author: tom
 * @date:   2019年3月5日 下午4:48:37
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
@Transactional(rollbackFor=Exception.class)
public class MessageTplDetailsServiceImpl extends BaseServiceImpl<MessageTplDetails, MessageTplDetailsDao, Integer>
		implements MessageTplDetailsService {

	@Override
	public MessageTplDetails saveTpl(MessageTplDetails entities) throws GlobalException {
		return super.save(entities);
	}
	
	@Override
	public MessageTplDetails findByCodeAndType(String mesCode, Short mesType, Short detailMesType)
			throws GlobalException {
		return dao.findByMesCodeAndType(mesCode, mesType, detailMesType, false);
	}

}
