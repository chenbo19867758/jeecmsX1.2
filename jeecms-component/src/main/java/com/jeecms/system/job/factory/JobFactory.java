package com.jeecms.system.job.factory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.constants.SysConstants.TimeUnit;
import com.jeecms.system.constants.SysJobConstants;
import com.jeecms.system.domain.SysJob;
import com.jeecms.system.job.CollectDataJob;
import com.jeecms.system.job.MassManageJob;
import com.jeecms.system.job.SysJobUtil;
import org.quartz.JobKey;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 定时任务工厂
 *
 * @author: tom
 * @date: 2018年8月23日 上午8:50:30
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class JobFactory {

	/**
	 * 生成定时群发任务类
	 */
	public static SysJob createWechatSendJob(Integer id, Date jobTime) {
		SysJob job = new SysJob();
		job.setStatus(true);
		job.setParams(id.toString());
		job.setCronName(SysJobConstants.GROUP_NAME_WECHAT_SEND_RECEIVE + id);
		job.setGroupName(SysJobConstants.GROUP_NAME_WECHAT_SEND_RECEIVE);
		job.setCron(SysJobUtil.createCron(jobTime));
		job.setCreateTime(jobTime);
		job.setClassPath(MassManageJob.class.getName());
		Date startTime = jobTime;
		startTime.setTime(startTime.getTime() - 1000L);
		job.setStartTime(startTime);
		return job;
	}

	/**
	 * 生成解除用户禁用任务类
	 *
	 * @param userId  用户ID
	 * @param jobTime 任务执行时间
	 * @Title: createOrderCloseJob
	 * @return: SysJob
	 */
	public static SysJob createUserReleaseLock(Integer userId, Date jobTime) {
		SysJob job = new SysJob();
		job.setClassPath(SysJobConstants.CLASS_PATH_USER_RELEASE_LOCK);
		job.setGroupName(SysJobConstants.GROUP_NAME_USER_RELEASE_LOCK);
		job.setCronName(SysJobConstants.NAME_PREFIX_USER_RELEASE_LOCK + userId);
		job.setParams(userId.toString());
		job.setCron(SysJobUtil.createCron(jobTime));
		Date startTime = Calendar.getInstance().getTime();
		startTime.setTime(startTime.getTime() + 1000L);
		job.setStartTime(startTime);
		job.setStatus(true);
		return job;
	}

	/**
	 * 生成采集任务类
	 */
	public static SysJob createCollectDataJob(Integer id, String corn) {
		SysJob job = new SysJob();
		job.setStatus(true);
		job.setParams(id.toString());
		job.setCronName(SysJobConstants.GROUP_NAME_COLLECT_DATA + id);
		job.setGroupName(SysJobConstants.GROUP_NAME_COLLECT_DATA);
		job.setCron(corn);
		job.setClassPath(CollectDataJob.class.getName());
		return job;
	}

	/**
	 * 生成工作流自动驳回类
	 */
	public static SysJob createFlowAutoRejectJob(String instanceId, List<String> taskIds,
												 Integer flowId, Integer dataId, Short dataType,
												 Date jobTime, Integer siteId) {
		SysJob job = new SysJob();
		job.setClassPath(SysJobConstants.CLASS_PATH_FLOW_AUTO_REJECT);
		job.setGroupName(SysJobConstants.GROUP_NAME_FLOW_AUTO_REJECT);
		job.setCronName(SysJobConstants.NAME_PREFIX_FLOW_AUTO_REJECT + instanceId);
		JSONObject paramJson = new JSONObject();
		paramJson.put("instanceId", instanceId);
		JSONArray taskIdArray = new JSONArray();
		for (String tid : taskIds) {
			taskIdArray.add(tid);
		}
		paramJson.put("taskIds", taskIds);
		paramJson.put("flowId", flowId);
		paramJson.put("dataId", dataId);
		paramJson.put("dataType", dataType);
		paramJson.put("siteId", siteId);
		job.setParams(paramJson.toJSONString());
		job.setCron(SysJobUtil.createCron(jobTime));
		job.setStatus(true);
		Date startTime = jobTime;
		startTime.setTime(startTime.getTime() + 1000L);
		job.setStartTime(jobTime);
		return job;
	}

	/**
	 * 构建 自动驳回类任务提醒KEY
	 *
	 * @param instanceId 流程实例ID
	 * @Title: getFlowAutoRejectKey
	 * @return: JobKey
	 */
	public static JobKey getFlowAutoRejectKey(String instanceId) {
		return JobKey.jobKey(SysJobConstants.NAME_PREFIX_FLOW_AUTO_REJECT + instanceId,
			SysJobConstants.GROUP_NAME_FLOW_AUTO_REJECT);
	}

	/**
	 * 生成工作流自动驳回类
	 */
	public static SysJob createFlowRemindJob(String instanceId, Map<String, List<String>> taskUserMap,
											 Date jobBeginTime, Integer jobInterval,
											 Integer siteId, Boolean infoNotify, Boolean emailNotify, Boolean smsNotify) {
		SysJob job = new SysJob();
		job.setClassPath(SysJobConstants.CLASS_PATH_FLOW_REMIND);
		job.setGroupName(SysJobConstants.GROUP_NAME_FLOW_REMIND);
		/** 任务ID 保证任务key唯一 */
		job.setCronName(SysJobConstants.NAME_PREFIX_FLOW_REMIND + instanceId);
		JSONObject paramJson = new JSONObject();
		paramJson.put("instanceId", instanceId);
		paramJson.put("taskUserMap", taskUserMap);
		paramJson.put("infoNotify", infoNotify);
		paramJson.put("emailNotify", emailNotify);
		paramJson.put("smsNotify", smsNotify);
		paramJson.put("siteId", siteId);
		job.setParams(paramJson.toJSONString());
		job.setCron(SysJobUtil.createCron(jobBeginTime, jobInterval, TimeUnit.hour));
		job.setStatus(true);
		Date startTime = jobBeginTime;
		startTime.setTime(startTime.getTime() - 1000L);
		job.setStartTime(jobBeginTime);
		return job;
	}

	/**
	 * 构建 任务提醒KEY
	 *
	 * @param taskId 任务ID
	 * @Title: getFlowRemindJobKey
	 * @return: JobKey
	 */
	public static JobKey getFlowRemindJobKey(String taskId) {
		return JobKey.jobKey(SysJobConstants.NAME_PREFIX_FLOW_REMIND + taskId,
			SysJobConstants.GROUP_NAME_FLOW_REMIND);
	}

	/**
	 * @param jobTime 任务启动时间
	 * @return SysJob
	 */
	public static SysJob createUserSummaryJob(Date jobTime) {
		SysJob job = new SysJob();
		job.setStatus(true);
		job.setCronName("group_name_UserSummaryJob");
		job.setGroupName("group_name_UserSummaryJob");
		Date startTime = jobTime;
		startTime.setTime(startTime.getTime() - 1000L);
		//一小时一次
		job.setCron("0 0 9 * * ? *");
		job.setStartTime(startTime);
		job.setClassPath("com.jeecms.system.job.UserSummaryJob");
		return job;
	}

	/**
	 * 生成日志统计任务
	 *
	 * @param jobTime 任务启动时间
	 * @return SysJob
	 */
	public static SysJob createLogStatisticsJob(Date jobTime) {
		SysJob job = new SysJob();
		job.setStatus(true);
		job.setCronName(SysJobConstants.GROUP_NAME_LOG_STATISTICS);
		job.setGroupName(SysJobConstants.GROUP_NAME_LOG_STATISTICS);
		Date startTime = jobTime;
		startTime.setTime(startTime.getTime() - 1000L);
		//一小时一次
		job.setCron("0 0 * * * ? *");
		job.setStartTime(startTime);
		job.setClassPath(SysJobConstants.CLASS_LOG_STATISTICS);
		return job;
	}

	/**
	 * 生成数据统计任务
	 *
	 * @param jobTime 任务启动时间
	 * @return SysJob
	 */
	public static SysJob createStatisticsAccessJob(Date jobTime) {
		SysJob job = new SysJob();
		job.setStatus(true);
		job.setCronName(SysJobConstants.GROUP_NAME_ACCESS_STATISTICS);
		job.setGroupName(SysJobConstants.GROUP_NAME_ACCESS_STATISTICS);
		Date startTime = jobTime;
		startTime.setTime(startTime.getTime() - 1000L);
		//一天一次
		job.setCron("0 0 0 * * ? *");
		job.setStartTime(startTime);
		job.setClassPath(SysJobConstants.CLASS_SITE_ACCESS_STATISTICS);
		return job;
	}

	/**
	 * 生成日志预警任务
	 *
	 * @param jobTime 任务启动时间
	 * @return SysJob
	 */
	public static SysJob createLogAlertJob(Date jobTime) {
		SysJob job = new SysJob();
		job.setStatus(true);
		job.setCronName(SysJobConstants.GROUP_NAME_LOG_ALERT);
		job.setGroupName(SysJobConstants.GROUP_NAME_LOG_ALERT);
		Date startTime = jobTime;
		startTime.setTime(startTime.getTime() - 1000L);
		//一小时一次
		job.setCron("0 0 * * * ? *");
		job.setStartTime(startTime);
		job.setClassPath(SysJobConstants.CLASS_LOG_ALERT);
		return job;
	}

	/**
	 * 生成日志告警任务
	 *
	 * @param jobTime 任务启动时间
	 * @return SysJob
	 */
	public static SysJob createLogAlarmJob(Date jobTime) {
		SysJob job = new SysJob();
		job.setStatus(true);
		job.setCronName(SysJobConstants.GROUP_NAME_LOG_ALARM);
		job.setGroupName(SysJobConstants.GROUP_NAME_LOG_ALARM);
		Date startTime = jobTime;
		startTime.setTime(startTime.getTime() - 1000L);
		//9点运行
		job.setCron("0 0 9 * * ? ");
		job.setStartTime(startTime);
		job.setClassPath(SysJobConstants.CLASS_LOG_ALARM);
		return job;
	}

	/**
	 * 创建问卷自动发布定时任务
	 *
	 * @param jobTime         结束时间
	 * @param questionnaireId 问卷id
	 * @return SysJob
	 */
	public static SysJob createQuestionnairePublishJob(Date jobTime, Integer questionnaireId) {
		SysJob job = new SysJob();
		job.setStatus(true);
		job.setCronName(SysJobConstants.GROUP_NAME_QUESTIONNAIRE_PUBLISH + questionnaireId);
		job.setGroupName(SysJobConstants.GROUP_NAME_QUESTIONNAIRE_PUBLISH);
		job.setParams(String.valueOf(questionnaireId));
		job.setCron(SysJobUtil.createCron(jobTime));
		job.setClassPath(SysJobConstants.CLASS_PATH_QUESTIONNAIRE_PUBLISH);
		return job;
	}

	/**
	 * 创建问卷自动结束定时任务
	 *
	 * @param jobTime         结束时间
	 * @param questionnaireId 问卷id
	 * @return SysJob
	 */
	public static SysJob createQuestionnaireFinishJob(Date jobTime, Integer questionnaireId) {
		SysJob job = new SysJob();
		job.setStatus(true);
		job.setCronName(SysJobConstants.GROUP_NAME_QUESTIONNAIRE_FINISH + questionnaireId);
		job.setGroupName(SysJobConstants.GROUP_NAME_QUESTIONNAIRE_FINISH);
		job.setCron(SysJobUtil.createCron(jobTime));
		job.setParams(String.valueOf(questionnaireId));
		job.setClassPath(SysJobConstants.CLASS_PATH_QUESTIONNAIRE_FINISH);
		return job;
	}

}
