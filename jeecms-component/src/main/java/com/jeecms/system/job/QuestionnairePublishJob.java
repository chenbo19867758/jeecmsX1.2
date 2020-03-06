/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.job;

import com.jeecms.common.base.scheduler.IBaseJob;
import com.jeecms.common.web.ApplicationContextProvider;
import com.jeecms.questionnaire.service.SysQuestionnaireService;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 问卷定时发布任务
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/11/14 9:48
 */
public class QuestionnairePublishJob implements IBaseJob {
	private Logger logger = LoggerFactory.getLogger(QuestionnairePublishJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap map = context.getMergedJobDataMap();
		Integer questionnaireId = null;
		try {
			questionnaireId = Integer.parseInt(String.valueOf(map.get("params")));
			initService();
			service.publish(questionnaireId);
		}
		 catch (Exception e) {
		}
	}

	private SysQuestionnaireService service;

	private void initService() {
		service = ApplicationContextProvider.getBean(SysQuestionnaireService.class);
	}
}
