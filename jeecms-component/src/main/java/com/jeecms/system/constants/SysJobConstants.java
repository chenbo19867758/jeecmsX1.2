/*
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.constants;

import com.jeecms.common.util.MyDateUtils;
import com.jeecms.system.job.CronTypeChannelJob;
import com.jeecms.system.job.CronTypeContentJob;
import com.jeecms.system.job.CronTypeDataBackupJob;
import com.jeecms.system.job.CronTypeIndexJob;

import java.util.Calendar;
import java.util.Date;

/**
 * 定时任务常量
 *
 * @author: tom
 * @date: 2018年8月23日 上午9:35:04
 */
public class SysJobConstants {

	/**
	 * 分组名-微信留言
	 */
	public static final String GROUP_NAME_WECHAT_COMMENT = "WechatCommentJobGroup";
	/**
	 * 分组名-定时群发
	 */
	public static final String GROUP_NAME_WECHAT_SEND_RECEIVE = "WechatSendReceiveJobGroup";

	/**
	 * 分组名-日志统计
	 */
	public static final String GROUP_NAME_LOG_STATISTICS = "logStatisticsJobGroup";

	/**
	 * 分组名-访问统计
	 */
	public static final String GROUP_NAME_ACCESS_STATISTICS = "accessStatisticsJobGroup";
	/**
	 * 分组名-日志告警
	 */
	public static final String GROUP_NAME_LOG_ALARM = "logAlarmGroup";
	/**
	 * 分组名-日志预警
	 */
	public static final String GROUP_NAME_LOG_ALERT = "logAlertGroup";
	/**
	 * 分组名-数据采集
	 */
	public static final String GROUP_NAME_COLLECT_DATA = "collectDataJobGroup";
	/**
	 * 分组名-解除用户禁用
	 */
	public static final String GROUP_NAME_USER_RELEASE_LOCK = "UserReleaseLockJobGroup";
	/**
	 * 分组名-问卷发布
	 */
	public static final String GROUP_NAME_QUESTIONNAIRE_PUBLISH = "groupNameQuestionnairePublish";
	/**
	 * 分组名-问卷结束
	 */
	public static final String GROUP_NAME_QUESTIONNAIRE_FINISH = "groupNameQuestionnaireFinish";

	/**
	 * 定时任务名字前缀-解除用户禁用
	 */
	public static final String NAME_PREFIX_USER_RELEASE_LOCK = "UserReleaseLockJob_";

	/**
	 * 类路径-解除用户禁用
	 */
	public static final String CLASS_PATH_USER_RELEASE_LOCK = "com.jeecms.system.job.UserReleaseLockJob";

	/**
	 * 分组名-首页静态化
	 */
	public static final String GROUP_INDEX_STATIC_PAGE = "IndexStaticPageJobGroup";
	/**
	 * 分组名-栏目静态化
	 */
	public static final String GROUP_CHANNEL_STATIC_PAGE = "IndexStaticPageJobGroup";
	/**
	 * 分组名-内容静态化
	 */
	public static final String GROUP_CONTENT_STATIC_PAGE = "IndexStaticPageJobGroup";
	/**
	 * 分组名-数据库备份
	 */
	public static final String GROUP_DATA_BACKUP = "DataBackupJobGroup";

	/**
	 * 分组名-工作流自动驳回
	 */
	public static final String GROUP_NAME_FLOW_AUTO_REJECT = "FlowAutoRejectJobGroup";

	/**
	 * 定时任务名字前缀-工作流自动驳回
	 */
	public static final String NAME_PREFIX_FLOW_AUTO_REJECT = "FlowAutoRejectJob_";

	/**
	 * 类路径-工作流自动驳回
	 */
	public static final String CLASS_PATH_FLOW_AUTO_REJECT = "com.jeecms.system.job.FlowAutoRejectJob";

	/**
	 * 类路径-网站统计
	 */
	public static final String CLASS_SITE_ACCESS_STATISTICS = "com.jeecms.system.job.SiteStatisticsJob";

	/**
	 * 类路径-日志统计
	 */
	public static final String CLASS_LOG_STATISTICS = "com.jeecms.system.job.StatisticsLogJob";

	/**
	 * 类路径-日志告警
	 */
	public static final String CLASS_LOG_ALARM = "com.jeecms.system.job.LogAlarmJob";

	/**
	 * 类路径-日志预警
	 */
	public static final String CLASS_LOG_ALERT = "com.jeecms.system.job.LogAlertJob";

	/**
	 * 分组名-工作流超时提醒
	 */
	public static final String GROUP_NAME_FLOW_REMIND = "FlowRemindJobGroup";

	/**
	 * 定时任务名字前缀-工作流超时提醒
	 */
	public static final String NAME_PREFIX_FLOW_REMIND = "FlowRemindJob_";

	/**
	 * 类路径-工作流超时提醒
	 */
	public static final String CLASS_PATH_FLOW_REMIND = "com.jeecms.system.job.FlowOvertimeReminderJob";

	/**
	 * 类路径-工作流超时提醒
	 */
	public static final String CLASS_PATH_QUESTIONNAIRE_PUBLISH = "com.jeecms.system.job.QuestionnairePublishJob";

	/**
	 * 类路径-工作流超时提醒
	 */
	public static final String CLASS_PATH_QUESTIONNAIRE_FINISH = "com.jeecms.system.job.QuestionnaireFinishJob";

	/**
	 * 执行周期类型：分
	 */
	public static final int INTERVAL_TYPE_MINUTE = 1;
	/**
	 * 执行周期类型：时
	 */
	public static final int INTERVAL_TYPE_HOUR = 2;
	/**
	 * 执行周期类型：天
	 */
	public static final int INTERVAL_TYPE_DAY = 3;

	/**
	 * 任务类型：首页静态化
	 */
	public static final int CRON_TYPE_INDEX = 1;
	/**
	 * 任务类型：栏目静态化
	 */
	public static final int CRON_TYPE_CHANNEL = 2;
	/**
	 * 任务类型：内容静态化
	 */
	public static final int CRON_TYPE_CONTENT = 3;
	/**
	 * 任务类型：数据备份
	 */
	public static final int CRON_TYPE_DATA_BACKUP = 4;

	/**
	 * 执行周期类型 1-设置类型
	 */
	public static final Integer EXEC_CYCLE_TYPE_TIME = 1;
	/**
	 * 执行周期类型 2-cron表达式
	 */
	public static final Integer EXEC_CYCLE_TYPE_CRON = 2;

	/**
	 * 根据执行周期转换为cron表达式
	 *
	 * @param intervalType 执行周期类型
	 * @param intervalNum  执行周期
	 * @return cron表达式
	 */
	public static String conversionCron(int intervalType, Integer intervalNum, Date time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);
		int hour = MyDateUtils.getHour(calendar);
		int minute = MyDateUtils.getMinute(calendar);
		int second = MyDateUtils.getSecond(calendar);
		String cronExpression = "";
		switch (intervalType) {
			case INTERVAL_TYPE_MINUTE:
				cronExpression = second + " 0/" + intervalNum + " * * * ? *";
				break;
			case INTERVAL_TYPE_HOUR:
				cronExpression = second + " " + minute + " 0/" + intervalNum + " * * ? *";
				break;
			case INTERVAL_TYPE_DAY:
				cronExpression = second + " " + minute + " " + hour + " 1/" + intervalNum + " * ? *";
				break;
			default:
				break;
		}
		return cronExpression;
	}

	public static String getClassPath(int cronType) {
		String classPath = "";
		switch (cronType) {
			case CRON_TYPE_INDEX:
				classPath = CronTypeIndexJob.class.getName();
				break;
			case CRON_TYPE_CHANNEL:
				classPath = CronTypeChannelJob.class.getName();
				break;
			case CRON_TYPE_CONTENT:
				classPath = CronTypeContentJob.class.getName();
				break;
			case CRON_TYPE_DATA_BACKUP:
				classPath = CronTypeDataBackupJob.class.getName();
				break;
			default:
				break;
		}
		return classPath;
	}

	/**
	 * 根据任务类型获取任务分组名
	 *
	 * @param cronType 任务类型
	 * @return 任务分组名
	 */
	public static String geiJobGroupName(int cronType) {
		String jobGroupName = "";
		switch (cronType) {
			case CRON_TYPE_INDEX:
				jobGroupName = GROUP_INDEX_STATIC_PAGE;
				break;
			case CRON_TYPE_CHANNEL:
				jobGroupName = GROUP_CHANNEL_STATIC_PAGE;
				break;
			case CRON_TYPE_CONTENT:
				jobGroupName = GROUP_CONTENT_STATIC_PAGE;
				break;
			case CRON_TYPE_DATA_BACKUP:
				jobGroupName = GROUP_DATA_BACKUP;
				break;
			default:
				break;
		}
		return jobGroupName;
	}
}
