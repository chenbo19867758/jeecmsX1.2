/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.job;

import com.jeecms.common.base.scheduler.IBaseJob;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.web.ApplicationContextProvider;
import com.jeecms.content.service.ContentStaticPageService;
import com.jeecms.system.service.CmsSiteService;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

/**
 * 首页静态化任务
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/6/13 11:12
 */

public class CronTypeIndexJob implements IBaseJob {

	private static final Logger logger = LoggerFactory.getLogger(CronTypeIndexJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap map = context.getMergedJobDataMap();
		logger.info("Running Job name : {} ", map.getString("name"));
		try {
			initService();
			Integer siteId = Integer.parseInt(String.valueOf(map.get("siteId")));
			staticPageService.index(siteService.findById(siteId), false);
			logger.info("静态化首页成功，时间{}" + Calendar.getInstance().getTime());
		} catch (GlobalException e) {
		}
	}

	private ContentStaticPageService staticPageService;
	private CmsSiteService siteService;

	private void initService() {
		staticPageService = ApplicationContextProvider.getBean(ContentStaticPageService.class);
		siteService = ApplicationContextProvider.getBean(CmsSiteService.class);
	}
}
