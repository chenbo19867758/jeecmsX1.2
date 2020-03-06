/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.job;

import com.jeecms.common.base.scheduler.IBaseJob;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.ApplicationContextProvider;
import com.jeecms.content.service.ContentStaticPageService;
import com.jeecms.system.service.SysJobService;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 内容静态化任务
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/6/13 11:12
 */

public class CronTypeContentJob implements IBaseJob {

	private static final Logger logger = LoggerFactory.getLogger(CronTypeContentJob.class);

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
			Integer siteId = Integer.parseInt(String.valueOf(map.get("siteId")));
			String channel = map.getString("channelIds");
			List<Integer> channelIds = new ArrayList<Integer>();
			if (StringUtils.isNotBlank(channel)) {
				String[] channelId = StrUtils.splitAndTrim(channel, ",", "，");
				if (channelId != null) {
					for (String aChannelId : channelId) {
						channelIds.add(Integer.valueOf(aChannelId));
					}
				}
			}
			staticPageService.content(siteId, channelIds, false, null, null);
		} catch (Exception e) {
			logger.error("内容静态化定时任务错误:{}", e.getMessage());
		}
	}

	private ContentStaticPageService staticPageService;
	private SysJobService jobService;

	private void initService() {
		staticPageService = ApplicationContextProvider.getBean(ContentStaticPageService.class);
		jobService = ApplicationContextProvider.getBean(SysJobService.class);
	}
}
