package com.jeecms.threadmsg.message.task.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.constants.SmsConstants;
import com.jeecms.common.exception.ExceptionInfo;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionEnum;
import com.jeecms.common.exception.error.UserErrorCodeEnum;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.SmsUtils;
import com.jeecms.message.MqConstants;
import com.jeecms.message.dto.CommonMqConstants;
import com.jeecms.system.domain.CmsSiteConfig;
import com.jeecms.system.domain.MessageTplDetails;
import com.jeecms.system.domain.Sms;
import com.jeecms.system.service.CmsSiteService;
import com.jeecms.system.service.MessageTplDetailsService;
import com.jeecms.system.service.SmsService;
import com.jeecms.threadmsg.common.MessageInfo;
import com.jeecms.threadmsg.message.task.Task;

/**
 * @Description: 短信任务
 * @author: ztx
 * @date: 2019年1月21日 下午4:45:29
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Repository("smsTask")
public class SmsTaskImpl implements Task {
	private static final Logger LOGGER = LoggerFactory.getLogger(SmsTaskImpl.class);

	@SuppressWarnings("all")
	@Override
	public boolean exec(Object msg) throws Exception {
 		if (msg != null && msg instanceof MessageInfo) {
			MessageInfo msgInfo = (MessageInfo) msg;
			switch (msgInfo.getSendType()) {
				case MqConstants.SEND_EMAIL:
					return true;
				case MqConstants.SEND_SYSTEM_STATION:
					return true;
				case MqConstants.SEND_SYSTEM_STATION_EMAIL:
					return true;
				default:
					break;
			}
			String mesCode = msgInfo.getMessageCode();
			if (mesCode == null) {
				return true;
			}
			MessageTplDetails smsMesTplDetail = msgTplDetailService.findByCodeAndType(mesCode, MessageTplDetails.MESTYPE_PHONE,
					MessageTplDetails.MESTYPE_PHONE); // 手机消息模板
			if (smsMesTplDetail == null || !smsMesTplDetail.getIsOpen()) {
				LOGGER.warn("No Phone template message is configured!");
				return true;
			}
			Object resData = null;
			ResponseInfo resInfo = null;
			JSONObject data = msgInfo.getData();
			Map<String, String> smsExtParam = data == null ? new HashMap<>()
					: (Map<String, String>) data.get(CommonMqConstants.EXT_DATA_KEY_SMS); // 自定义参数
			Sms smsBean = smsMng.findOnly();
			if (smsBean == null || !smsBean.getIsEnable()) {
				return true;
			}
			if (msgInfo.getSiteId() != null) {
				CmsSiteConfig cmsSiteConfig = cmsSiteService.findById(msgInfo.getSiteId()).getCmsSiteCfg();
				if (cmsSiteConfig == null) {
					return true;
				}
				smsBean = this.initEamil(cmsSiteConfig);
				
			}
			String phoneTplId = smsMesTplDetail.getTplId();
			Short serviceProvider = smsBean.getServiceProvider();
			List<String> phones = msgInfo.getPhones();
			if (phones == null) {
				return true;
			}
			for (String phone : phones) {
				if (StringUtils.isBlank(phone) || phone.equals("null")) {
					continue;
				}
				if (SmsConstants.ALI.equals(serviceProvider)) { // 阿里云
					resInfo = SmsUtils.sendByALi(smsBean.getAccessKey(), smsBean.getAccesskeySecret(), smsBean.getSmsSign(),
							phoneTplId, phone, smsExtParam);
					resData = resInfo.getData();
					if (resData instanceof ExceptionInfo && !Objects.equals(SystemExceptionEnum.SUCCESSFUL.getCode(),
							((ExceptionInfo) resData).getCode())) {
						throw new GlobalException((ExceptionInfo) resData);
					}
				} else if (SmsConstants.TECENT.equals(serviceProvider)) { // 腾讯云
					// 腾讯,由于腾讯针对短信文本内容动态参数使用{1}{2}{3}....方式进行动态占位，
					// 且传递的动态参数也需要和短信内容文本中的动态占位参数的顺序及个数保持一致
					ArrayList<String> params = new ArrayList<String>();
					for (Map.Entry<String, String> entry : smsExtParam.entrySet()) {
						params.add(entry.getValue());
					}
					// 发送
					resInfo = SmsUtils.sendByTecent(smsBean.getAccessKey(), smsBean.getAccesskeySecret(),
							smsBean.getSmsSign(), Integer.valueOf(phoneTplId), phone, params);
					resData = resInfo.getData();
					if (resData instanceof ExceptionInfo && !Objects.equals(SystemExceptionEnum.SUCCESSFUL.getCode(),
							((ExceptionInfo) resData).getCode())) {
						throw new GlobalException((ExceptionInfo) resData);
					}
				}
			}
			return true;
		}
		return false;
	}

	private Sms initEamil(CmsSiteConfig cmsSiteConfig) throws GlobalException { 
		String serviceProviders = cmsSiteConfig.getServiceProviders();
		String accesskeyId = cmsSiteConfig.getAccesskeyId();
		String accesskeySerret = cmsSiteConfig.getAccesskeySecret();
		String messageSignatures = cmsSiteConfig.getMessageSignatures();
		if (StringUtils.isBlank(serviceProviders) || StringUtils.isBlank(accesskeyId)
				|| StringUtils.isBlank(accesskeySerret) || StringUtils.isBlank(messageSignatures)) {
			ExceptionInfo exceptionInfo = UserErrorCodeEnum.SMS_FORM_ERROR;
			throw new GlobalException(exceptionInfo);
		}
		return new Sms(accesskeyId, accesskeySerret, Short.valueOf(serviceProviders), messageSignatures);
	}
	
	@Override
	public int operation() {
		return MqConstants.MESSAGE_QUEUE_SMS;
	}

	@Autowired
	private SmsService smsMng;
	@Autowired
	private MessageTplDetailsService msgTplDetailService;
	@Autowired
	private CmsSiteService cmsSiteService;
}
