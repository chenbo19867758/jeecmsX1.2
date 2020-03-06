package com.jeecms.threadmsg.message.strategy.concrete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableList;
import com.jeecms.message.dto.CommonMqConstants.MessageSceneEnum;
import com.jeecms.threadmsg.message.task.DefaultTaskPipeline;
import com.jeecms.threadmsg.message.task.Task;
import com.jeecms.threadmsg.message.task.TaskPipeline;

/**
 * @Description: 验证码
 * @author: ztx
 * @date: 2019年1月21日 下午5:51:41
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service("validateCodeMessageStrategy")
@Transactional(rollbackFor = Exception.class)
public class ConcreteValidateCodeMessageStrategy extends AbstractMessageStrategy {

	@Override
	TaskPipeline initTaskPipeline() {
		return new DefaultTaskPipeline(ImmutableList.of(emailTask, smsTask));
	}

	@Override
	MessageSceneEnum getMsgScene() {
		return MessageSceneEnum.VALIDATE_CODE;
	}

	@Autowired
	private Task smsTask;
	@Autowired
	private Task emailTask;
}
