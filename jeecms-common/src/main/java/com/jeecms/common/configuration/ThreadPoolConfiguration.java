package com.jeecms.common.configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;


/**
 * 线程池管理配置
 * 
 * @author: wangqq
 * @date: 2019年7月5日 上午9:54:18
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Configuration
@EnableAsync
public class ThreadPoolConfiguration  {
	static final Logger logger = LoggerFactory.getLogger(ThreadPoolConfiguration.class);
	
	@Bean("asyncServiceExecutor")
	public Executor asyncServiceExecutor() {
		logger.info("asyncServiceExecutor start..............");
		ExtendThreadPoolExecutor tpExecutor = new ExtendThreadPoolExecutor();
		//核心线程数
		tpExecutor.setCorePoolSize(Integer.parseInt(corePoolSize));
		//最大线程数
		tpExecutor.setMaxPoolSize(Integer.parseInt(maxPoolSize));
		//线程队列数
		tpExecutor.setQueueCapacity(Integer.parseInt(queneCapacity));
		// rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
		tpExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		//线程池初始化
		tpExecutor.initialize();
		return tpExecutor;
	}
	
	/**以下三个数值，根据实际的线程池中的任务类型进行调整，大致规则为
	 * 1、线程池中以计算型任务较多，则按服务器的cpu个数 * 2
	 * 2、线程池中以IO型任务较多，则按服务器的cpu个数正负1
	 * ps:具体数值还需参考实际运行服务器的配置及运行情况而调整
	*/
	@Value("${spring.threadPool.corePoolSize}")
	private String corePoolSize;
	@Value("${spring.threadPool.maxPoolSize}")
	private String maxPoolSize;
	@Value("${spring.threadPool.queneCapacity}")
	private String queneCapacity;
	
}
