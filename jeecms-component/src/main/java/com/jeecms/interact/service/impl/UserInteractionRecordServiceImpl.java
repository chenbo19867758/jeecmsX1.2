/*  
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.interact.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.interact.dao.UserInteractionRecordDao;
import com.jeecms.interact.domain.UserInteractionRecord;
import com.jeecms.interact.service.UserInteractionRecordService;

/**
 * 用户互动记录service实现类
 * 
 * @author: chenming
 * @date: 2019年5月6日 上午9:05:37
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserInteractionRecordServiceImpl
		extends BaseServiceImpl<UserInteractionRecord, UserInteractionRecordDao, Integer>
		implements UserInteractionRecordService {

}