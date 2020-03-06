package com.jeecms.threadmsg.message.task.impl;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.exception.ExceptionInfo;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.RPCErrorCodeEnum;
import com.jeecms.common.exception.error.UserErrorCodeEnum;
import com.jeecms.common.util.EmailUtils;
import com.jeecms.message.MqConstants;
import com.jeecms.message.dto.CommonMqConstants;
import com.jeecms.message.dto.CommonMqConstants.MessageSceneEnum;
import com.jeecms.system.domain.CmsSiteConfig;
import com.jeecms.system.domain.Email;
import com.jeecms.system.domain.MessageTplDetails;
import com.jeecms.system.service.CmsSiteService;
import com.jeecms.system.service.EmailService;
import com.jeecms.system.service.MessageTplDetailsService;
import com.jeecms.threadmsg.common.MessageInfo;
import com.jeecms.threadmsg.message.task.Task;

/**
 * @Description: 邮件任务
 * @author: ztx
 * @date: 2019年1月21日 下午4:45:17
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Repository("emailTask")
public class EmailTaskImpl implements Task {
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailTaskImpl.class);

	@SuppressWarnings("all")
	@Override
	public boolean exec(Object msg) throws Exception {
		if (msg != null && msg instanceof MessageInfo) {
			MessageInfo msgInfo = (MessageInfo) msg;
			switch (msgInfo.getSendType()) {
				case MqConstants.SEND_SMS:
					return true;
				case MqConstants.SEND_SYSTEM_STATION:
					return true;
				case MqConstants.SEND_SYSTEM_STATION_SMS:
					return true;
				default:
					break;
			}
			boolean isEmailUser = MessageSceneEnum.USER_MESSAGE == msgInfo.getScene();
			String mesCode = msgInfo.getMessageCode();
			MessageTplDetails emailMesTplDetail = null;
			if (mesCode != null) {
				emailMesTplDetail = mesTplDetailService.findByCodeAndType(mesCode, MessageTplDetails.MESTYPE_MAIL,
						MessageTplDetails.MESTYPE_MAIL);
			}
			String title = null;
			String content = null;
			if (emailMesTplDetail == null || !emailMesTplDetail.getIsOpen()) {
				title = msgInfo.getTitle();
				content = msgInfo.getTextContent();
				if (StringUtils.isBlank(title) || StringUtils.isBlank(content)) {
					LOGGER.warn("title or content is not null");
					return false;
				}
			} else {
				JSONObject data = msgInfo.getData();
				if (data != null) {
					data = data.getJSONObject(CommonMqConstants.EXT_DATA_KEY_EMAIL);
					if (data != null) {
						content = emailMesTplDetail.getMesContent();
						Set<String> places = data.keySet();
						for (String p : places) {
							if (p.startsWith("${")) {
								content = content.replace(p, data.getString(p));
							} else {
								content = content.replace("${"+p+"}", data.getString(p));
							}
						}
						
					}
				}
			}
			Email emailInfo = emailService.findDefault();
			if (msgInfo.getSiteId() != null) {
				CmsSiteConfig cmsSiteConfig = cmsSiteService.findById(msgInfo.getSiteId()).getCmsSiteCfg();
				if (cmsSiteConfig == null) {
					return false;
				}
				emailInfo = this.initEamil(cmsSiteConfig);
			}
			HtmlEmail htmlEmail = emailInfo.getHtmlEmail();
			List<String> toAddress = msgInfo.getEmails();
			if (toAddress == null) {
				return true;
			}
			for (String toAddres : toAddress) {
				if (StringUtils.isBlank(toAddres) || toAddress.equals("null")) {
					continue;
				}
				int emailResult = EmailUtils.sendEmail(htmlEmail, toAddres, title, content,
						emailInfo.getIsSsl());
				if (EmailUtils.SEND_EMAIL_OK != emailResult) {
					ExceptionInfo exceptionInfo = RPCErrorCodeEnum.THIRD_PARTY_CALL_ERROR;
					if (EmailUtils.SEND_EMAIL_INVALID_ADDRESSES == emailResult) {
						exceptionInfo = RPCErrorCodeEnum.EMAIL_ADDRESS_INVALID;
					}
					//throw new GlobalException(exceptionInfo);
					/**发邮件通知不应该影响主业务继续运行*/
					LOGGER.error(exceptionInfo.getDefaultMessage());
				}
			}
			return true;
		}
		return false;
	}

	
	private Email initEamil(CmsSiteConfig cmsSiteConfig) throws GlobalException {
		String smtpService = cmsSiteConfig.getSMTPService();
		String smtpPort = cmsSiteConfig.getSMTPPort();
		String sendAccount = cmsSiteConfig.getSendAccount();
		String emailPassword = cmsSiteConfig.getEmailPassword();
		String sslUser = cmsSiteConfig.getSslUse();
		if (StringUtils.isBlank(smtpService) || StringUtils.isBlank(smtpPort)
				|| StringUtils.isBlank(sendAccount) || StringUtils.isBlank(emailPassword) || StringUtils.isBlank(sslUser)) {
			ExceptionInfo exceptionInfo = UserErrorCodeEnum.EMAIL_FORM_ERROR;
			throw new GlobalException(exceptionInfo);
		}
		boolean ssl = Integer.valueOf(sslUser) == 0 ? false:true;
		return new Email(smtpService, smtpPort, sendAccount, emailPassword, ssl);
	}
	
	@Override
	public int operation() {
		return MqConstants.MESSAGE_QUEUE_EMAIL;
	}

	@Autowired
	private MessageTplDetailsService mesTplDetailService;
	@Autowired
	private CmsSiteService cmsSiteService;
	@Autowired
	private EmailService emailService;
}
