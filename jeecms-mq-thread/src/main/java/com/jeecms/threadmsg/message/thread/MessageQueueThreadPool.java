package com.jeecms.threadmsg.message.thread;

import com.jeecms.threadmsg.common.MessageInfo;

/**
 * @Description: 消息队列线程池
 * @author: ztx
 * @date: 2019年1月22日 下午5:54:10
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface MessageQueueThreadPool {

	/**
	 * 添加消息
	 * @Title: addMsg  
	 * @param msg 消息体    
	 * @return: void
	 */
	void addMsg(MessageInfo msg);

	/**
	 * 安全关闭
	 */
	void shutdownGracefully();

}
