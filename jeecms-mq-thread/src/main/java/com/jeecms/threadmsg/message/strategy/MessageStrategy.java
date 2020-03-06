package com.jeecms.threadmsg.message.strategy;

import java.util.concurrent.Callable;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.threadmsg.common.MessageInfo;
import com.jeecms.threadmsg.common.MessageResult;
import com.jeecms.threadmsg.message.task.TaskPipeline;

/**
 * @Description: 消息策略
 * @author: ztx
 * @date: 2019年1月21日 下午4:08:34
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface MessageStrategy extends Callable<MessageResult> {

	/**
	 * 是否支持
	 * @Title: support  
	 * @param msgInfo 消息体
	 * @return      
	 * @return: boolean true表示支持,false表示不支持
	 */
	boolean support(MessageInfo msgInfo);

	/**
	 * 获取场景操作标识符
	 * @Title: operation  
	 * @return
	 * @throws GlobalException      
	 * @return: int 操作标识符
	 */
	int operation() throws GlobalException;

	/**
	 *  获取任务队列
	 * @return: TaskPipeline 任务队列 {@link TaskPipeline},用于存储任务节点
	 */
	TaskPipeline taskPipeline();

	/**
	 * 构建任务实例
	 * @Title: build  
	 * @return
	 * @throws GlobalException      
	 * @return: MessageStrategy
	 */
	@Deprecated
	MessageStrategy build() throws GlobalException;

}
