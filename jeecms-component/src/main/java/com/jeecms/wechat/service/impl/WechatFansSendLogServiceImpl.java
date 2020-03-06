/**
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.wechat.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.page.Paginable;
import com.jeecms.wechat.dao.WechatFansSendLogDao;
import com.jeecms.wechat.domain.WechatFansSendLog;
import com.jeecms.wechat.service.WechatFansSendLogService;

/**
 * 微信推送Service实现类
 * 
 * @author ljw
 * @version 1.0
 * @date 2018-08-09
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WechatFansSendLogServiceImpl extends BaseServiceImpl<WechatFansSendLog, WechatFansSendLogDao, Integer>
		implements WechatFansSendLogService {

	@Override
	public Page<WechatFansSendLog> getLogPage(List<String> appids, Integer type, Date startTime, Date endTime,
			String title, Boolean black, String openId, Pageable pageable) throws GlobalException {
		return dao.getLogPage(appids, type, startTime, endTime, title, black, openId, pageable);
	}

	@Override
	public List<WechatFansSendLog> getList(List<String> appids, Integer type, Paginable paginable)
			throws GlobalException {
		return dao.getList(appids, type, paginable);
	}

	@Override
	public List<WechatFansSendLog> findByOpenId(String openId) {
		return dao.findByOpenIdAndHasDeletedFalse(openId);
	}
	
}