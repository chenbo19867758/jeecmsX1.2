package com.jeecms.threadmsg.message.strategy.concrete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableList;
import com.jeecms.message.dto.CommonMqConstants.MessageSceneEnum;
import com.jeecms.threadmsg.message.task.DefaultTaskPipeline;
import com.jeecms.threadmsg.message.task.Task;
import com.jeecms.threadmsg.message.task.TaskPipeline;

@Service("userMessageStrategy")
@Transactional(rollbackFor = Exception.class)
public class ConcreteUserMessageStrategy extends AbstractMessageStrategy {

	@Override
	TaskPipeline initTaskPipeline() {
		return new DefaultTaskPipeline(ImmutableList.of(emailTask, smsTask, sysStationTask));
	}

	@Override
	MessageSceneEnum getMsgScene() {
		return MessageSceneEnum.USER_MESSAGE;
	}

	@Autowired
	private Task smsTask;
	@Autowired
	private Task emailTask;
	@Autowired
	private Task sysStationTask;
}