package com.jeecms.system.service.impl;

import static com.jeecms.common.exception.error.SettingErrorCodeEnum.EMAIL_TPL_UNCONFIGURED;
import static com.jeecms.common.exception.error.SettingErrorCodeEnum.MESSAGE_TPL_UNCONFIGURED;
import static com.jeecms.common.exception.error.SettingErrorCodeEnum.SMS_TPL_UNCONFIGURED;
import static com.jeecms.system.domain.dto.ValidateCodeConstants.DEFAULT_VALIDATE_CODE_LENGTH;
import static com.jeecms.system.domain.dto.ValidateCodeConstants.STATUS_EXCEEDCOUNT;
import static com.jeecms.system.domain.dto.ValidateCodeConstants.STATUS_EXPIRED;
import static com.jeecms.system.domain.dto.ValidateCodeConstants.STATUS_ILLEGAL;
import static com.jeecms.system.domain.dto.ValidateCodeConstants.STATUS_NEW;
import static com.jeecms.system.domain.dto.ValidateCodeConstants.STATUS_PASS;
import static com.jeecms.system.domain.dto.ValidateCodeConstants.STATUS_UNEXPIRED;
import static com.jeecms.system.domain.dto.ValidateCodeConstants.STATUS_UNTHROUGH;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.service.CoreUserService;
import com.jeecms.common.base.service.BaseCacheServiceImpl;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.exception.ExceptionInfo;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.cache.CacheConstants;
import com.jeecms.common.web.cache.CacheProvider;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.message.MqConstants;
import com.jeecms.message.MqSendMessageService;
import com.jeecms.message.dto.CommonMqConstants;
import com.jeecms.message.dto.CommonMqConstants.MessageSceneEnum;
import com.jeecms.system.dao.SmsDao;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.CmsSiteConfig;
import com.jeecms.system.domain.MessageTpl;
import com.jeecms.system.domain.MessageTplDetails;
import com.jeecms.system.domain.Sms;
import com.jeecms.system.domain.dto.SendValidateCodeDTO;
import com.jeecms.system.domain.dto.ValidateCodeDTO;
import com.jeecms.system.service.CmsSiteService;
import com.jeecms.system.service.MessageTplService;
import com.jeecms.system.service.SmsService;
import com.jeecms.util.SystemContextUtils;

/**
 * 短信服务设置
 * 
 * @author: chenming
 * @date: 2019年4月13日 上午11:35:16
 */
@Service
@Transactional(rollbackFor = Exception.class)
@CacheConfig(cacheNames = "Sms-")
public class SmsServiceImpl extends BaseCacheServiceImpl<Sms, SmsDao, Integer> implements SmsService {

	@Override
	@Transactional(readOnly = true,rollbackFor = Exception.class)
	public Sms findDefault() {
		return dao.findByisGloable(true);
	}

	@Override
	public ResponseInfo sendEmailMsg(SendValidateCodeDTO bean) throws GlobalException {
		// 拼接服务端验证码key
		String attrName = WebConstants.KCAPTCHA_PREFIX + bean.getSecondLevelName() + bean.getTargetNumber();
		int status = this.notValidateSendCountCode(attrName);
		// 小于pass则表示不通过
		if (STATUS_PASS > status) {
			ExceptionInfo exceptionInfo = ValidateCodeDTO.exceptionAdapter(status);
			return new ResponseInfo(exceptionInfo.getCode(), exceptionInfo.getDefaultMessage(), false);
		}
		// 创建验证码
		if (StringUtils.isBlank(bean.getValidateCode())) {
			bean.setValidateCode(StrUtils.getRandStr(DEFAULT_VALIDATE_CODE_LENGTH));
		}
		String msgCode = bean.getMessageTplCode();
		MessageTpl tpl = messageTplService.findByMesCode(msgCode);
		if (tpl == null) {
			return new ResponseInfo(MESSAGE_TPL_UNCONFIGURED.getCode(),
					MESSAGE_TPL_UNCONFIGURED.getDefaultMessage(), false);
		}
		Optional<MessageTplDetails> optional = tpl.getDetails().stream()
				.filter(messageTplDetail -> MessageTplDetails.MESTYPE_MAIL
				.equals(messageTplDetail.getMesType())).findFirst();
		if (!optional.isPresent()) {
			return new ResponseInfo(EMAIL_TPL_UNCONFIGURED.getCode(),
					EMAIL_TPL_UNCONFIGURED.getDefaultMessage(), false);
		}
		MessageTplDetails details = optional.get();
		String toEmail = bean.getTargetNumber();
		ValidateCodeDTO codeDto = new ValidateCodeDTO(bean.getValidateCode(), new Date(), status, toEmail);
		// 服务端存储验证码
		cacheProvider.setCache(CacheConstants.SMS, attrName, JSONObject.toJSONString(codeDto));
		//查询站点
		CoreUser user = coreUserService.findByEmailOrUsername(toEmail);
		Integer siteId = null;
		if (user == null) {
			siteId = SystemContextUtils.getSiteId(RequestUtils.getHttpServletRequest());
		} else {
			siteId = user.getSourceSiteId();
		}
		// 发送消息
		JSONObject data = new JSONObject();
		data.put(CommonMqConstants.EXT_DATA_KEY_EMAIL, bean.getTreeMapParam());
		mqSendMessageService.sendValidateCodeMsg(tpl.getMesCode(), MessageSceneEnum.VALIDATE_CODE, 
				details.getMesTitle(), details.getMesContent(), 
				null, toEmail, MqConstants.SEND_EMAIL, siteId, data);
		return new ResponseInfo();
	}

	@Override
	public ResponseInfo sendPhoneMsg(SendValidateCodeDTO bean) throws GlobalException {
		// 拼接服务端验证码key
		String attrName = WebConstants.KCAPTCHA_PREFIX + bean.getSecondLevelName() + bean.getTargetNumber();
		int status = this.validateCode(attrName, null);
		// 小于0则返回错误信息
		if (STATUS_PASS > status) {
			ExceptionInfo exceptionInfo = ValidateCodeDTO.exceptionAdapter(status);
			return new ResponseInfo(exceptionInfo.getCode(), exceptionInfo.getDefaultMessage());
		}
		// 创建验证码
		if (StringUtils.isBlank(bean.getValidateCode())) {
			bean.setValidateCode(StrUtils.getRandStr(DEFAULT_VALIDATE_CODE_LENGTH));
		}
		String msgCode = bean.getMessageTplCode();
		MessageTpl tpl = messageTplService.findByMesCode(msgCode);
		// 未配置消息模板
		if (tpl == null) {
			return new ResponseInfo(MESSAGE_TPL_UNCONFIGURED.getCode(),
					MESSAGE_TPL_UNCONFIGURED.getDefaultMessage(), false);
		}
		Optional<MessageTplDetails> optional = tpl.getDetails().stream()
				.filter(messageTplDetail -> MessageTplDetails.MESTYPE_PHONE
				.equals(messageTplDetail.getMesType())).findFirst();
		// 未配置短信模板
		if (!optional.isPresent()) {
			return new ResponseInfo(SMS_TPL_UNCONFIGURED.getCode(), 
					SMS_TPL_UNCONFIGURED.getDefaultMessage(), false);
		}
		MessageTplDetails details = optional.get();
		String toPhone = bean.getTargetNumber();
		ValidateCodeDTO codeDto = new ValidateCodeDTO(bean.getValidateCode(), new Date(), status, toPhone);
		// 服务端存储验证码
		cacheProvider.setCache(CacheConstants.SMS, attrName, JSONObject.toJSONString(codeDto));
		//查询站点
		CoreUser user = coreUserService.findByPhone(toPhone);
		Integer siteId = null;
		if (user == null) {
			siteId = SystemContextUtils.getSiteId(RequestUtils.getHttpServletRequest());
		} else {
			siteId = user.getSourceSiteId();
		}
		// 发送消息
		JSONObject data = new JSONObject();
		data.put(CommonMqConstants.EXT_DATA_KEY_SMS, bean.getTreeMapParam());
		mqSendMessageService.sendValidateCodeMsg(tpl.getMesCode(), MessageSceneEnum.VALIDATE_CODE, 
				details.getMesTitle(), details.getMesContent(), 
				toPhone, null, MqConstants.SEND_SMS, siteId, data);
		return new ResponseInfo();
	}

	@Override
	public int validateCode(String attrName, String validateCode) throws GlobalException {
		Serializable targetBean = cacheProvider.getCache(CacheConstants.SMS, attrName);
		int status = STATUS_NEW;
		String targetStr = null;
		ValidateCodeDTO codeDto = null;

		// 服务端不存在对应验证码,如果是前台验证码验证操作的话则代表非法,否则代表获取验证码.
		if (targetBean == null || StringUtils.isBlank(targetStr = targetBean.toString())) {
			return StringUtils.isNotBlank(validateCode) ? STATUS_ILLEGAL : status;
		}
		codeDto = JSONObject.parseObject(targetStr, ValidateCodeDTO.class);

		// 验证码需要验证时.
		if (StringUtils.isNotBlank(validateCode)) {
			if (codeDto.isExpire()) {
				status = STATUS_EXPIRED;
			} else if (!validateCode.equals(codeDto.getCode())) {
				status = STATUS_UNTHROUGH;
			} else {
				status = STATUS_PASS;
			}
			return status;
		}

		// 获取验证码时.
		if (!codeDto.isSendExpire()) {
			status = STATUS_UNEXPIRED;
		} else if (codeDto.isExceedCount()) {
			status = STATUS_EXCEEDCOUNT;
		} else {
			status = codeDto.getCount() + 1;
			cacheProvider.clearCache(CacheConstants.SMS, attrName);
		}
		return status;
	}

	@Override
	public int notValidateSendCountCode(String attrName) throws GlobalException {
		Serializable targetBean = cacheProvider.getCache(CacheConstants.SMS, attrName);

		int status = STATUS_NEW;
		String targetStr = null;
		ValidateCodeDTO codeDto = null;

		// 获取验证码.
		if (targetBean == null || StringUtils.isBlank(targetStr = targetBean.toString())) {
			return status;
		}
		codeDto = JSONObject.parseObject(targetStr, ValidateCodeDTO.class);

		// 获取验证码时.
		if (!codeDto.isSendExpire()) {
			status = STATUS_UNEXPIRED;
		} else {
			status = codeDto.getCount() + 1;
			cacheProvider.clearCache(CacheConstants.SMS, attrName);
		}
		return status;
	}

	@Override
	public Sms findOnly() throws GlobalException {
		List<Sms> smsList = super.getList(null, null, false);
		if (smsList.size() > 0) {
			return smsList.get(0);
		}
		return null;
	}

	@Override
	public Sms save(Sms sms) throws GlobalException {
		Sms only = this.findOnly();
		/**
		 * 如果进入说明之前一定不存在数据
		 * 存在两种情况：
		 * 		1. 为全局开启	设置
		 * 		2. 非全局开启	不动
		 */
		if (only != null) {
			super.physicalDelete(only);
		}
		Sms bean = super.save(sms);
		if (bean.getIsGloable()) {
			this.cmsSiteConfig(bean);
		}
		return bean;
	}

	@Override
	public Sms update(Sms sms) throws GlobalException {
		/**
		 * 如果进入说明之前一定存在数据
		 * 存在两种情况：
		 * 		1. 为全局开启	之前为非全局	设置
		 * 					之前为全局		设置
		 * 		2. 非全局开启	之前为非全局	不动
		 * 					之前为全局		设置
		 */
		Boolean mark = this.findOnly().getIsGloable();
		Sms bean = super.update(sms);
		if (bean.getIsGloable()) {
			this.cmsSiteConfig(bean);
		} else {
			if (mark) {
				this.cmsSiteConfig(bean);
			}
		}
		return bean;
	}

	private void cmsSiteConfig(Sms sms) throws GlobalException {
		List<CmsSite> cmsSiteList = cmsService.findAll(true);
		Map<String,String> oldCfgMap = null;
		Map<String,String> newCfgMap = null;
		if (sms.getIsGloable() != null && sms.getIsGloable()) {
			for (CmsSite cmsSite : cmsSiteList) {
				oldCfgMap = cmsSite.getCfg();
				newCfgMap = new LinkedHashMap<String, String>();
				for (String key : oldCfgMap.keySet()) {
					newCfgMap.put(key, oldCfgMap.get(key));
				}
				newCfgMap.put(CmsSiteConfig.SERVICE_PROVIDERS, String.valueOf(sms.getServiceProvider()));
				newCfgMap.put(CmsSiteConfig.ACCESSKEY_ID, sms.getAccessKey());
				newCfgMap.put(CmsSiteConfig.ACCESSKEY_SERRET, sms.getAccesskeySecret());
				newCfgMap.put(CmsSiteConfig.MESSAGE_SIGNATURES, sms.getSmsSign());
				cmsSite.setCfg(newCfgMap);
			}
		} else {
			for (CmsSite cmsSite : cmsSiteList) {
				oldCfgMap = cmsSite.getCfg();
				newCfgMap = new LinkedHashMap<String, String>();
				for (String key : oldCfgMap.keySet()) {
					newCfgMap.put(key, oldCfgMap.get(key));
				}
				newCfgMap.put(CmsSiteConfig.SERVICE_PROVIDERS, null);
				newCfgMap.put(CmsSiteConfig.ACCESSKEY_ID, null);
				newCfgMap.put(CmsSiteConfig.ACCESSKEY_SERRET, null);
				newCfgMap.put(CmsSiteConfig.MESSAGE_SIGNATURES, null);
				cmsSite.setCfg(newCfgMap);
			}
		}
		if (cmsSiteList.size() > 0) {
			cmsService.batchUpdateAll(cmsSiteList);
			cmsService.flush();
		}
	}

	@Autowired
	private CmsSiteService cmsService;
	@Autowired
	private CacheProvider cacheProvider;
	@Autowired
	private MessageTplService messageTplService;
	@Autowired
	private MqSendMessageService mqSendMessageService;
	@Autowired
	private CoreUserService coreUserService;
}
