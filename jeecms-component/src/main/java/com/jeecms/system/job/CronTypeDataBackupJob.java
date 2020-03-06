/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.job;

import com.jeecms.backup.service.DatabaseBackupService;
import com.jeecms.common.base.scheduler.IBaseJob;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.web.ApplicationContextProvider;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据备份任务
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/6/13 11:12
 */

public class CronTypeDataBackupJob implements IBaseJob {

	private static final Logger logger = LoggerFactory.getLogger(CronTypeDataBackupJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap map = context.getMergedJobDataMap();
		try {
			logger.info("Running Job name : {} ", map.getString("name"));
			Integer siteId = Integer.parseInt(String.valueOf(map.get("siteId")));
			initService();
			backupServices.backup(siteId);
		} catch (GlobalException e) {
		}

	}

	private DatabaseBackupService backupServices;

	private void initService() {
		backupServices = ApplicationContextProvider.getBean(DatabaseBackupService.class);
	}
}
