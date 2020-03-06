package com.jeecms.threadmsg.message;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.message.MqSendMessageService;
import com.jeecms.message.dto.CommonMqConstants.MessageSceneEnum;
import com.jeecms.threadmsg.common.MessageInfo.MessageInfoBuilder;
import com.jeecms.threadmsg.message.thread.MessageQueueThreadPool;

/**
 * @Description:发送消息接口
 * @author: ztx
 * @date: 2019年1月25日 下午2:25:21
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Component
public class MqSendMessageServiceImpl implements MqSendMessageService {

	@Override
	public void sendMemberMsg(Integer targetType, Integer orgId, Integer memberGroupId, Integer memberLevelId,
			List<Integer> receiveIds, String messageCode, MessageSceneEnum scene, String title,
			String textContent, List<String> toPhones, List<String> toEmails, JSONObject data, Short sendType, Integer siteId)
			throws GlobalException {
		MessageInfoBuilder builder = new MessageInfoBuilder(toPhones, toEmails, data, targetType, orgId, memberGroupId, memberLevelId, receiveIds, messageCode, scene, title, textContent, sendType, siteId);
		threadPool.addMsg(builder.build());
	}

	@Override
	public void sendValidateCodeMsg(String messageCode, MessageSceneEnum scene, String title, String textContent,
			String toPhone, String toEmail, Short sendType, Integer siteId, JSONObject data) throws GlobalException {
		List<String> toPhones = null;
		if (StringUtils.isNotBlank(toPhone)) {
			toPhones = new ArrayList<String>();
			toPhones.add(toPhone);
		}
		List<String> toEmails = null;
		if (StringUtils.isNotBlank(toEmail)) {
			toEmails = new ArrayList<String>();
			toEmails.add(toEmail);
		}
		MessageInfoBuilder builder = new MessageInfoBuilder(toPhones, toEmails, data, messageCode, scene, title, textContent, sendType, siteId);
		threadPool.addMsg(builder.build());
	}
	
	@Autowired
	private MessageQueueThreadPool threadPool;

}
