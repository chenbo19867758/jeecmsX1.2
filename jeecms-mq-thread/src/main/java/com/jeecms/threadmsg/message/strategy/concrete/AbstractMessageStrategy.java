package com.jeecms.threadmsg.message.strategy.concrete;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.RPCErrorCodeEnum;
import com.jeecms.message.MqConstants;
import com.jeecms.message.MqConstants.MessageSceneOperationEnum;
import com.jeecms.message.dto.CommonMqConstants.MessageSceneEnum;
import com.jeecms.threadmsg.common.MessageInfo;
import com.jeecms.threadmsg.common.MessageResult;
import com.jeecms.threadmsg.message.strategy.MessageStrategy;
import com.jeecms.threadmsg.message.task.DefaultTaskPipeline;
import com.jeecms.threadmsg.message.task.Task;
import com.jeecms.threadmsg.message.task.TaskPipeline;

/**
 * @Description: 策略接口公共层实现
 * @author: ztx
 * @date: 2019年1月21日 下午4:14:01
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Transactional(rollbackFor = Exception.class)
public abstract class AbstractMessageStrategy implements MessageStrategy, InitializingBean {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMessageStrategy.class);

	protected MessageInfo msgInfo;

	protected TaskPipeline taskPipeline;

	protected int opertation;

	@Override
	public boolean support(MessageInfo msgInfo) {
		int operation;
		try {
			operation = operation();
		} catch (GlobalException e) {
			LOGGER.error("get operation error.");
			return false;
		}
		if (taskPipeline.num() != operation || !Objects.equals(msgInfo.getScene(), getMsgScene())) {
			return false;
		}
		this.msgInfo = msgInfo;
		afterSupport(msgInfo);
		return true;
	}

	@Override
	public MessageResult call() throws Exception {
		try {
			TaskPipeline tPipeline = taskPipeline;
			int pipeOperation = tPipeline.num();
			if ((opertation & pipeOperation) != opertation) {
				// 检查当前taskPipeline的task是否满足场景(Strategy)所需的基本tasks.
				// 替代建造的一个方法
				throw new UnsupportedOperationException("current pipeline operation '" + pipeOperation
						+ "' not supported strategy operation '" + opertation + "'.");
			}
			for (Task it : tPipeline) {
				if (!it.exec(msgInfo)) {
					return new MessageResult(RPCErrorCodeEnum.QUEUE_MESSAGE_HANDLER_FAILED.getCode(),
							RPCErrorCodeEnum.QUEUE_MESSAGE_HANDLER_FAILED.getDefaultMessage());
				}
			}
			tPipeline = null;
		} catch (Exception e) {
			LOGGER.error(java.text.Normalizer.normalize(String.format("消息处理失败,失败信息:%s", e.getMessage()),
					java.text.Normalizer.Form.NFKD));
			e.printStackTrace();
			throw new GlobalException(RPCErrorCodeEnum.QUEUE_MESSAGE_HANDLER_FAILED);
		}
		return new MessageResult();
	}

	@Override
	public int operation() throws GlobalException {
		if (opertation == 0) {
			MessageSceneEnum scene = null;
			if ((scene = getMsgScene()) == null) {
				throw new NullPointerException();
			}
			MessageSceneOperationEnum operationEnum = Enum.valueOf(MqConstants.MessageSceneOperationEnum.class,
					scene.toString());
			if (operationEnum == null) {
				throw new UnsupportedOperationException("MessageScene '" + scene + "' is unsupported");
			}
			opertation = operationEnum.getOperation();
		}
		return opertation;
	}

	@Override
	public TaskPipeline taskPipeline() {
		return taskPipeline;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.taskPipeline = initTaskPipeline();
	}

	@Override
	public MessageStrategy build() throws GlobalException {
		// int currOperation = operation();
		// TaskPipeline taskPipeline = taskPipeline();
		// int pipeOperation = taskPipeline.getOperation();
		// if ((pipeOperation & currOperation) == 0) {
		// logger.warn("can't build ConcreteStrategy,because currOperation '" +
		// currOperation + "' is dissatisfy.");
		// return null;
		// }
		// return new ConcreteMemberMessageStrategy();
		// TODO 这个方法没想到怎么和Spring整合,返回Spring不允许createBean返回null.
		// 不过可以想办法标识失败的bean. 建造方法使用于当前场景
		return null;
	}

	public MessageInfo getMsgInfo() {
		return msgInfo;
	}

	protected void afterSupport(MessageInfo msgInfo) {
		// after support MessageScene
	}

	/**
	 * 初始化 {@link DefaultTaskPipeline}
	 * @Title: initTaskPipeline  
	 * @return      
	 * @return: TaskPipeline
	 */
	abstract TaskPipeline initTaskPipeline();

	/**
	 * 获取策略
	 * @Title: getMsgScene  
	 * @return      
	 * @return: MessageSceneEnum
	 */
	abstract MessageSceneEnum getMsgScene();

}
