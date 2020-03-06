package com.jeecms.threadmsg.message.thread.concrete;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.threadmsg.common.MessageInfo;
import com.jeecms.threadmsg.message.strategy.MessageStrategy;
import com.jeecms.threadmsg.message.task.Task;

/**
 * @Description: {@link Task} 版线程池
 * @author: ztx
 * @date: 2019年1月22日 下午6:39:13
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MessageQueueTaskThreadPool extends AbstractMessageQueueThreadPool implements DisposableBean {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMessageQueueThreadPool.class);

	public MessageQueueTaskThreadPool() {
	}

	public MessageQueueTaskThreadPool(int nScheduleThreads, int maximumPoolSize, int workQueueSize) {
		super(nScheduleThreads, maximumPoolSize, workQueueSize);
	}

	@Override
	public void addMsg(MessageInfo msg) {
		boolean isSupport = false;
		for (MessageStrategy it : messageStrategies) {
			if (it.support(msg)) {
				threadPool.submit(it);
				isSupport = true;
				break;
			}
		}
		if (!isSupport) {
			LOGGER.info(java.text.Normalizer.normalize(String.format("the message unsupported! messageInfo: %s", msg),
					java.text.Normalizer.Form.NFKD));
		}
	}

    @Override
    public void destroy() throws Exception {
    	super.shutdownGracefully();
    }
	// TODO 可以做监听线程处理结果

	@Autowired
	private List<MessageStrategy> messageStrategies;
}
