package com.jeecms.common.configuration;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;


import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 扩展ThreadPoolTaskExecutor线程池对象，其目的监控线程池的运行状况
 * @Async("asyncServiceExecutor") 用注解标注service的时候需要注意bean的循环依赖问题，可采用 @Lazy
 * @author: wangqq
 * @date: 2019年7月5日 上午9:54:18
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class ExtendThreadPoolExecutor extends ThreadPoolTaskExecutor {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(ExtendThreadPoolExecutor.class);

    private void showThreadPoolInfo(String prefix){
    	//获取线程池实例
        ThreadPoolExecutor threadPoolExecutor = getThreadPoolExecutor();
        if(null == threadPoolExecutor) return;
        logger.debug("{}, {},已提交的所有线程数 {}, 已完成的线程数 ：{}, 进行中的线程数： {}, 队列中剩余线程数： {}",
                this.getThreadNamePrefix(),
                prefix,
                threadPoolExecutor.getTaskCount(),
                threadPoolExecutor.getCompletedTaskCount(),
                threadPoolExecutor.getActiveCount(),
                threadPoolExecutor.getQueue().size());
    }

    @Override
    public void execute(Runnable task) {
        showThreadPoolInfo("1 .do execute");
        super.execute(task);
    }

    @Override
    public void execute(Runnable task, long startTimeout) {
        showThreadPoolInfo("2. do execute");
        super.execute(task, startTimeout);
    }

    @Override
    public Future<?> submit(Runnable task) {
        showThreadPoolInfo("1. do submit");
        return super.submit(task);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        showThreadPoolInfo("2. do submit");
        return super.submit(task);
    }

    @Override
    public ListenableFuture<?> submitListenable(Runnable task) {
        showThreadPoolInfo("1. do submitListenable");
        return super.submitListenable(task);
    }

    @Override
    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
        showThreadPoolInfo("2. do submitListenable");
        return super.submitListenable(task);
    }
}
