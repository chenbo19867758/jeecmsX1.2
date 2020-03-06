package com.jeecms.system.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.dao.SystemMessagePushDao;
import com.jeecms.system.domain.SystemMessagePush;
import com.jeecms.system.service.SystemMessagePushService;

/**
 * @author chenming
 * @version 1.0
 * @date 2019-01-24
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
@Transactional(rollbackFor=Exception.class)
public class SystemMessagePushServiceImpl extends BaseServiceImpl<SystemMessagePush, SystemMessagePushDao, Integer>
		implements SystemMessagePushService {

	@Override
	public SystemMessagePush getDefault() throws GlobalException {
		return super.findById(1);
	}

}