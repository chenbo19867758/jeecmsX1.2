/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.job;

import com.jeecms.common.base.scheduler.IBaseJob;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.ApplicationContextProvider;
import com.jeecms.content.service.ContentStaticPageService;
import com.jeecms.system.service.CmsSiteService;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 栏目静态化任务
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/6/13 11:12
 */

public class CronTypeChannelJob implements IBaseJob {

	private static final Logger logger = LoggerFactory.getLogger(CronTypeChannelJob.class);

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
			if (channel != null) {
				String[] channelId = StrUtils.splitAndTrim(channel, ",", "，");
				if (channelId != null) {
					for (String aChannelId : channelId) {
						channelIds.add(Integer.valueOf(aChannelId));
					}
				}
			}
			staticPageService.channel(siteId, channelIds, false, null, null);
		} catch (Exception e) {
			logger.error("栏目静态化定时任务错误:{}", e.getMessage());
		}
	}

	private ContentStaticPageService staticPageService;
	private CmsSiteService siteService;

	private void initService() {
		staticPageService = ApplicationContextProvider.getBean(ContentStaticPageService.class);
		siteService = ApplicationContextProvider.getBean(CmsSiteService.class);
	}
}
