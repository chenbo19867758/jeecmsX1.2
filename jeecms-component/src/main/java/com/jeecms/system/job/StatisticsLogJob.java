/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.job;

import com.jeecms.common.base.scheduler.IBaseJob;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.web.ApplicationContextProvider;
import com.jeecms.system.service.StatisticsLogClickService;
import com.jeecms.system.service.StatisticsLogOperateService;
import com.jeecms.system.service.StatisticsLogResultService;
import com.jeecms.system.service.StatisticsLogUserService;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 每小时统计日志定时任务
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/6/19 9:25
 */

public class StatisticsLogJob implements IBaseJob {

	private static final Logger logger = LoggerFactory.getLogger(StatisticsLogJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap map = context.getMergedJobDataMap();
		logger.info("Running Job name : {} ", map.getString("name"));
		try {
			initService();
		} catch (Exception e) {
			return;
		}
		try {
			logClickService.save();
		} catch (Exception e) {
			logger.error("日志事件类型统计失败：{}", e.getMessage());
		}
		try {
			logOperateService.save();
		} catch (Exception e) {
			logger.error("日志操作类型统计失败：{}", e.getMessage());
		}
		try {
			logResultService.save();
		} catch (Exception e) {
			logger.error("日志结果统计失败：{}", e.getMessage());
		}
		try {
			logUserService.save();
		} catch (Exception e) {
			logger.error("日志用户分类统计失败：{}", e.getMessage());
		}

	}

	private StatisticsLogClickService logClickService;
	private StatisticsLogOperateService logOperateService;
	private StatisticsLogResultService logResultService;
	private StatisticsLogUserService logUserService;

	private void initService() {
		logClickService = ApplicationContextProvider.getBean(StatisticsLogClickService.class);
		logOperateService = ApplicationContextProvider.getBean(StatisticsLogOperateService.class);
		logResultService = ApplicationContextProvider.getBean(StatisticsLogResultService.class);
		logUserService = ApplicationContextProvider.getBean(StatisticsLogUserService.class);
	}
}
