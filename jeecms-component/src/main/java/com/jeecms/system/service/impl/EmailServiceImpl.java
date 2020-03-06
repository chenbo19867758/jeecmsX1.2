package com.jeecms.system.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.base.service.BaseCacheServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.dao.EmailDao;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.CmsSiteConfig;
import com.jeecms.system.domain.Email;
import com.jeecms.system.service.CmsSiteService;
import com.jeecms.system.service.EmailService;

/**
 * email的service实现类
 * 
 * @author: chenming
 * @date: 2019年4月12日 下午6:18:19
 */
@Service
@Transactional(rollbackFor = Exception.class)
@CacheConfig(cacheNames = "Email-")
public class EmailServiceImpl extends BaseCacheServiceImpl<Email, EmailDao, Integer> implements EmailService {

	@Override
	public Email findDefault() throws GlobalException {
		return dao.findByisGloable(true);
	}

	@Override
	public Email findOnly() throws GlobalException {
		if (super.findAll(true).size() > 0) {
			return super.findAll(true).get(0);
		}
		return null;
	}

	@Override
	public Email save(Email email) throws GlobalException {
		Email only = this.findOnly();
		/**
		 * 如果进入说明之前一定不存在数据
		 * 存在两种情况：
		 * 		1. 为全局开启	设置
		 * 		2. 非全局开启	不动
		 */
		if (only != null) {
			super.physicalDelete(only);
		}
		Email bean = super.save(email);
		if (bean.getIsGloable()) {
			this.cmsSiteConfig(bean);
		}
		return bean;
	}

	@Override
	public Email update(Email email) throws GlobalException {
		/**
		 * 如果进入说明之前一定存在数据
		 * 存在两种情况：
		 * 		1. 为全局开启	之前为非全局	设置
		 * 					之前为全局		设置
		 * 		2. 非全局开启	之前为非全局	不动
		 * 					之前为全局		设置
		 */
		Boolean mark = this.findOnly().getIsGloable();
		Email bean = super.update(email);
		if (bean.getIsGloable()) {
			this.cmsSiteConfig(bean);
		} else {
			if (mark) {
				this.cmsSiteConfig(bean);
			}
		}
		return bean;
	}

	private void cmsSiteConfig(Email email) throws GlobalException {
		List<CmsSite> cmsSiteList = cmsService.findAll(true);
		Map<String,String> oldCfgMap = null;
		Map<String,String> newCfgMap = null;
		if (email.getIsGloable()) {
			for (CmsSite cmsSite : cmsSiteList) {
				oldCfgMap = cmsSite.getCfg();
				newCfgMap = new LinkedHashMap<String, String>();
				for (String key : oldCfgMap.keySet()) {
					newCfgMap.put(key, oldCfgMap.get(key));
				}
				newCfgMap.put(CmsSiteConfig.SMTP_SERVICE, email.getSmtpService());
				newCfgMap.put(CmsSiteConfig.SMTP_PORT, email.getSmtpPort());
				newCfgMap.put(CmsSiteConfig.SEND_ACCOUNT, email.getEmailName());
				newCfgMap.put(CmsSiteConfig.EMAIL_PASSWORD, email.getEmailPassword());
				newCfgMap.put(CmsSiteConfig.SSL_USE, email.getIsSsl() ? "1" : "0");
				cmsSite.setCfg(newCfgMap);
			}
		} else {
			for (CmsSite cmsSite : cmsSiteList) {
				oldCfgMap = cmsSite.getCfg();
				newCfgMap = new LinkedHashMap<String, String>();
				for (String key : oldCfgMap.keySet()) {
					newCfgMap.put(key, oldCfgMap.get(key));
				}
				newCfgMap.put(CmsSiteConfig.SMTP_SERVICE, null);
				newCfgMap.put(CmsSiteConfig.SMTP_PORT, null);
				newCfgMap.put(CmsSiteConfig.SEND_ACCOUNT, null);
				newCfgMap.put(CmsSiteConfig.EMAIL_PASSWORD, null);
				newCfgMap.put(CmsSiteConfig.SSL_USE, null);
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
}
