/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.common.local;

/**   
* 线程池 服务
* @author: tom
* @date:   2019年5月28日 下午3:16:50     
*/
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolService {
	/**
	 * 核心线程数
	 */
	private static final int DEFAULT_CORE_SIZE = 4;
	/**
	 *  最大线程数
	 */
	private static final int MAX_QUEUE_SIZE = 8;
	private static final int KEEP_ALIVE_TIME = 60;
	private static final TimeUnit KEEP_ALIVE_TIME_UNIT =  TimeUnit.MINUTES;
	private static  ThreadPoolExecutor executor;

	/**
	 *  获取单例的线程池对象
	 * @Title: getInstance
	 * @return: ThreadPoolExecutor
	 */
	public static ThreadPoolExecutor getInstance() {
		if (executor == null) {
			synchronized (ThreadPoolService.class) {
				if (executor == null) {
					executor = new ThreadPoolExecutor(DEFAULT_CORE_SIZE, MAX_QUEUE_SIZE, 
							KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT,
							new LinkedBlockingQueue<Runnable>(), 
							Executors.defaultThreadFactory(),
							new ThreadPoolExecutor.AbortPolicy());
				}
			}
		}
		return executor;
	}

	/**
	 * 执行线程
	 * @Title: execute
	 * @param runnable 线程对象
	 * @return: void
	 */
	public void execute(Runnable runnable) {
		if (runnable == null) {
			return;
		}
		executor.execute(runnable);
	}

	/**
	 * 从线程队列中移除对象
	 * 
	 * @Title: cancel
	 * @param runnable
	 *            线程对象
	 * @return: void
	 */
	public void cancel(Runnable runnable) {
		if (executor != null) {
			executor.getQueue().remove(runnable);
		}
	}

}