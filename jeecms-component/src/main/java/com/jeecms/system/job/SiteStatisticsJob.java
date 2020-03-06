/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.job;

import com.jeecms.common.base.scheduler.IBaseJob;
import com.jeecms.common.web.ApplicationContextProvider;
import com.jeecms.statistics.service.StatisticsAccessPageService;
import com.jeecms.statistics.service.StatisticsAccessService;
import com.jeecms.statistics.service.StatisticsFlowService;
import com.jeecms.statistics.service.StatisticsSourceService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

/**
 * 网站统计job
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/6 17:36
 */

public class SiteStatisticsJob implements IBaseJob {
	private static final Logger log = LoggerFactory.getLogger(SiteStatisticsJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// JobDetail中的JobDataMap是共用的,从getMergedJobDataMap获取的JobDataMap是全新的对象
		try {
			initService();
			if(accessPageService == null || sourceService == null || flowService == null || accessService == null) {
				return;
			}
			//受访分析统计
			accessPageService.statisticsAccessPage();
			//来源分析统计
			sourceService.statisticsYesterday();
			//趋势分析统计
			flowService.save();
			//忠诚度分析统计
			accessService.countAnalyze();
		} catch (Exception e) {
			log.error("统计分析失败,error:{}, 统计时间：{}", e.getMessage(), Calendar.getInstance().getTime());
		}
		log.info("统计完成,统计时间：{}", Calendar.getInstance().getTime());
	}

	private StatisticsAccessPageService accessPageService;
	private StatisticsSourceService sourceService;
	private StatisticsFlowService flowService;
	private StatisticsAccessService accessService;

	private void initService() {
		accessPageService = ApplicationContextProvider.getBean(StatisticsAccessPageService.class);
		sourceService = ApplicationContextProvider.getBean(StatisticsSourceService.class);
		flowService = ApplicationContextProvider.getBean(StatisticsFlowService.class);
		accessService = ApplicationContextProvider.getBean(StatisticsAccessService.class);
	}
}
