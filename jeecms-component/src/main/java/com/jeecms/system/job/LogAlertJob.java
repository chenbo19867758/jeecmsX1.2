/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.job;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.scheduler.IBaseJob;
import com.jeecms.common.constants.MessageTempleEnum;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.ApplicationContextProvider;
import com.jeecms.message.MqConstants;
import com.jeecms.message.MqSendMessageService;
import com.jeecms.message.dto.CommonMqConstants.MessageSceneEnum;
import com.jeecms.system.domain.SysLogConfig;
import com.jeecms.system.service.SysLogConfigService;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;
import java.util.Calendar;

/**
 * 日志容量预警任务
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/6/19 15:45
 */

public class LogAlertJob implements IBaseJob {

	private static final Logger logger = LoggerFactory.getLogger(LoggerFactory.class);

	@Value(value = "${spring.datasource.url}")
	private String url;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap map = context.getMergedJobDataMap();
		logger.info("Running Job name : {} ", map.getString("name"));
		try {
			initService();
			SysLogConfig logConfig = logConfigService.getDefault();
			// 告警阈值
			Integer dangerValue = logConfig.getDangerValue();
			// 需要发送的邮箱
			String emailList = logConfig.getNoticeEmailList();
			// 需要发送的短信
			String smsList = logConfig.getNoticeSmsList();
			// 日志表使用大小
			Float size = 0f;
			String tableSize = logConfigService.getTableSize();
			if (StringUtils.isNotBlank(tableSize)) {
				tableSize = tableSize.replace("M", "");
				size = Float.parseFloat(tableSize);
			}
			if (dangerValue == null) {
				logger.error("日志策略没有设置告警阈值。");
				return;
			}
			if (emailList != null) {
				sendEmail(emailList, dangerValue, size);
			}
			if (smsList != null) {
				sendSMS(smsList, dangerValue, size, logConfig.getDangerSmsTmpId());
			}
			logger.info("日志预警:{}", Calendar.getInstance().getTime());
		} catch (Exception e) {

		}
	}

	private void sendEmail(String emailList, Integer dangerValue, Float size) {
		// 如果告警阈值小于等于使用百分比则发送邮件
		if (dangerValue <= size) {
			String[] emails = StrUtils.splitAndTrim(emailList, ",", "，");
			if (emails != null) {
				try {
					messageService.sendMemberMsg(null, null, null, null, null, null, MessageSceneEnum.USER_MESSAGE,
							MessageTempleEnum.LOG_WARN_TEMP.getTitle(), content(dangerValue), null,
							Arrays.asList(emails), new JSONObject(), MqConstants.SEND_EMAIL, null);
				} catch (GlobalException e) {
					logger.error("预警邮件发送错误，错误{}", e.getMessage());
				}
			}
		}
	}

	private void sendSMS(String smsList, Integer dangerValue, Float size, String dangerSmsTmpId) {
		// 如果告警阈值小于等于使用百分比则发送短信
		if (dangerValue <= size) {
			String[] phones = StrUtils.splitAndTrim(smsList, ",", "，");
			if (phones != null) {
				try {
					messageService.sendMemberMsg(null, null, null, null, null, dangerSmsTmpId,
							MessageSceneEnum.USER_MESSAGE, MessageTempleEnum.LOG_WARN_TEMP.getTitle(),
							content(dangerValue), Arrays.asList(phones), null, new JSONObject(), MqConstants.SEND_SMS,
							null);
				} catch (GlobalException e) {
					logger.error("预警短信发送错误，错误{}，邮箱{}", e.getMessage());
				}
			}
		}
	}

	/**
	 * 获取内容
	 *
	 * @param dangerValue 告警值
	 * @return 内容
	 */
	private String content(Integer dangerValue) {
		String database = "";
		if (url.contains("jdbc:mysql")) {
			String[] sf = url.split("//");
			String sg = sf[1];
			String[] sdString = sg.split("/");
			String sgString = sdString[1];
			int aInteger = sgString.indexOf("?");
			database = sgString.substring(0, aInteger);
		} else if (url.contains("jdbc:oracle")) {
			String[] sdString = url.split(":");
			database = sdString[sdString.length - 1];
		} else if (url.contains("jdbc:sqlserver")) {
			String[] sdString = url.split("DatabaseName");
			database = sdString[sdString.length - 1].substring(1);
		} else if (url.contains("jdbc:dm")) {
			String[] sf = url.split("//");
			String sg = sf[1];
			String[] sdString = sg.split("/");
			database = sdString[1];
		}
		return String.format(MessageTempleEnum.LOG_WARN_TEMP.getContent(), url, database, dangerValue);
	}

	private MqSendMessageService messageService;
	private SysLogConfigService logConfigService;

	private void initService() {
		messageService = ApplicationContextProvider.getBean(MqSendMessageService.class);
		logConfigService = ApplicationContextProvider.getBean(SysLogConfigService.class);
	}
}
