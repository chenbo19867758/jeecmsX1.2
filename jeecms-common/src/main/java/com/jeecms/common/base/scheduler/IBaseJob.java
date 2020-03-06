package com.jeecms.common.base.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 定时任务接口
 * 
 * @author: tom
 * @date: 2018年6月11日 下午6:31:25
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface IBaseJob extends Job {
	/**
	 * 执行任务
	 * 
	 * @param context
	 *            JobExecutionContext任务上下文
	 * @throws JobExecutionException
	 *             任务异常
	 * @return: void
	 */
	public void execute(JobExecutionContext context) throws JobExecutionException;
}
