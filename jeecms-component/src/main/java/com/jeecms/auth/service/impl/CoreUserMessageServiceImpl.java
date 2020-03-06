/**
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.auth.service.impl;

import com.jeecms.auth.dao.CoreUserMessageDao;
import com.jeecms.auth.domain.CoreUserMessage;
import com.jeecms.auth.service.CoreUserMessageService;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.util.SystemContextUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 管理员接收信息状态ServiceImpl
 * 
 * @author ljw
 * @version 1.0
 * @date 2019-01-23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CoreUserMessageServiceImpl extends BaseServiceImpl<CoreUserMessage, CoreUserMessageDao, Integer>
		implements CoreUserMessageService {

	@Override
	public ResponseInfo getMessagePage(Pageable pageable, Integer userId, Boolean status, 
			Date startTime, Date endTime,
			String title, String content, String sendUserName) throws GlobalException {
		return new ResponseInfo(dao.getMessagePage(pageable, userId, status,
				startTime, endTime, title, content, sendUserName));
	}

	@Override
	public List<CoreUserMessage> findByMessageId(Integer messageId) throws GlobalException {

		return dao.findByMessageId(messageId);
	}

	@Override
	public ResponseInfo saveMessage(Integer[] ids) throws GlobalException {
		Integer userId = SystemContextUtils.getUserId(RequestUtils.getHttpServletRequest());
		for (Integer integer : ids) {
			CoreUserMessage bean = new CoreUserMessage();
			List<CoreUserMessage> list = dao.findByMessageId(integer);
			// 已读的消息不做操作
			if (list.isEmpty()) {
				// 标记已读
				bean.setStatus(1);
				bean.setUserId(userId);
				bean.setMessageId(integer);
				super.save(bean);
			} 
		}
		return new ResponseInfo();
	}

	@Override
	public ResponseInfo delMessage(Integer[] ids) throws GlobalException {
		Integer userId = SystemContextUtils.getUserId(RequestUtils.getHttpServletRequest());
		for (Integer integer : ids) {
			CoreUserMessage bean = new CoreUserMessage();
			List<CoreUserMessage> list = dao.findByMessageId(integer);
			// 已读的标记删除
			if (list != null && !list.isEmpty()) {
				for (CoreUserMessage systemMessage : list) {
					systemMessage.setStatus(2);
					super.update(systemMessage);
				}
			} else {
				// 标记删除
				bean.setStatus(2);
				bean.setUserId(userId);
				bean.setMessageId(integer);
				super.save(bean);
			}
		}
		return new ResponseInfo();
	}

	@Override
	public Long unreadMessage(Integer userId) throws GlobalException {
		return dao.unreadMessage(userId);
	}

}