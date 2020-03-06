package com.jeecms.threadmsg.message.task;

/**
 * @Description: 消息任务
 * @author: ztx
 * @date: 2019年1月21日 下午4:19:22
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface Task {

	/**
	 * 执行任务
	 * @Title: exec  
	 * @param msg 消息体
	 * @return
	 * @throws Exception      
	 * @return: boolean 是否执行成功,true表示成功,false表示失败
	 */
	boolean exec(Object msg) throws Exception;

	/**
	 * 获取操作标识符
	 * @return: int 获取操作标识符
	 */
	int operation();

}
