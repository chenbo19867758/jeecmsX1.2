package com.jeecms.threadmsg.message.thread.concrete;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeecms.message.MqConstants;
import com.jeecms.threadmsg.common.MessageResult;
import com.jeecms.threadmsg.message.thread.MessageQueueThreadPool;

/**
 * @author: ztx
 * @date: 2019年1月22日 下午6:06:04
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@SuppressWarnings("all")
public abstract class AbstractMessageQueueThreadPool implements MessageQueueThreadPool {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMessageQueueThreadPool.class);

	public AbstractMessageQueueThreadPool() {
		this(0, 0, null, null, 0, 0);
	}

	public AbstractMessageQueueThreadPool(int nScheduleThreads, int maximumPoolSize, int workQueueSize) {
		this(nScheduleThreads, 0, null, null, maximumPoolSize, workQueueSize);
	}

	/**
	 * 具体参数设置场景参照 {@link ThreadPoolExecutor} 工作方式
	 * 
	 * @param nScheduleThreads
	 *            定时器线程核心数量 {@link ThreadPoolExecutor#corePoolSize}
	 * @param period
	 *            任务执行间隔时间,默认5
	 * @param timeUnit
	 *            任务执行间隔时间单位,默认秒
	 * @param msgQueue
	 *            线程池核心线程满了就往这丢
	 * @param maximumPoolSize
	 *            线程池最大线程数,如果队列是无界的那这个设的就没有意义
	 * @param workQueueSize
	 *            队列大小(无界队列不需要设)
	 */
	public AbstractMessageQueueThreadPool(int nScheduleThreads, int period, TimeUnit timeUnit,
			BlockingQueue<FutureTask<MessageResult>> msgQueue, int maximumPoolSize, int workQueueSize) {
		this.scheduler = Executors.newScheduledThreadPool(
				nScheduleThreads == 0 ? Runtime.getRuntime().availableProcessors() * 2 : nScheduleThreads);
		this.taskHandler = scheduler.scheduleAtFixedRate(accessBufferThread, 0, period == 0 ? 5 : period,
				timeUnit == null ? TimeUnit.SECONDS : timeUnit);
		this.msgQueue = msgQueue == null ? new LinkedTransferQueue<FutureTask<MessageResult>>() : msgQueue;
		this.threadPool = new ThreadPoolExecutor(1, maximumPoolSize == 0 ? 20 : maximumPoolSize,
				MqConstants.INTERVAL_MILS, TimeUnit.SECONDS,
				new LinkedBlockingQueue(workQueueSize == 0 ? MqConstants.WORK_QUEUE_SIZE : workQueueSize));

	}

	/** 轮询队列 */
	protected BlockingQueue<FutureTask<MessageResult>> msgQueue;

	/** 管理数据库访问的线程池 */
	@SuppressWarnings("all")
	protected ThreadPoolExecutor threadPool;

	@Override
	public void shutdownGracefully() {
		threadPool.shutdownNow();
		scheduler.shutdownNow();
		msgQueue.clear();
	};

	/** 自定义拒绝策略,当队列满了就会将消息存放到msgQueue(消息缓冲队列中) */
	RejectedExecutionHandler handler = new RejectedExecutionHandler() {
		@SuppressWarnings("unchecked")
		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			msgQueue.add((FutureTask<MessageResult>) r);
		}
	};

	/** 访问消息缓存的调度线程 */
	final Runnable accessBufferThread = new Runnable() {
		@Override
		public void run() {
			try {
				FutureTask<MessageResult> task = msgQueue.take();
				threadPool.submit(task);
				try {
					MessageResult result = (MessageResult) task.get();
					LOGGER.info("消费结果返回:{}", result);
				} catch (ExecutionException e) {
					LOGGER.error("消息处理失败!!!!!!!!");
					// TODO 消息处理失败 记录 . 重新放回队列里?????
				}
			} catch (InterruptedException e) {
				LOGGER.error("任务被中断: {}", e.getMessage());
			}
		}
	};

	/** 可以看作是轮询 */
	@SuppressWarnings("rawtypes")
	private ScheduledFuture taskHandler;

	/** 定时任务 */
	private ScheduledExecutorService scheduler;

}
