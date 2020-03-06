/**
 *  * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.job;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeecms.common.base.scheduler.IBaseJob;

/**
 * 采集任务job
 * @author: ljw
 * @date: 2019年6月10日 下午6:34:21
 */
public class CollectDataJob implements IBaseJob {

	private Logger logger = LoggerFactory.getLogger(CollectDataJob.class);

	public CollectDataJob() {
	}

	private void initService() {
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		// JobDetail中的JobDataMap是共用的,从getMergedJobDataMap获取的JobDataMap是全新的对象
		// JobDataMap map = context.getMergedJobDataMap();
		initService();
		JobDataMap map = context.getJobDetail().getJobDataMap();
		try {
			map.get("params").toString();
			//TODO 调用开始采集的接口
		} catch (Exception e1) {
			logger.info("{}", e1);
			e1.printStackTrace();
		}
	}
}
