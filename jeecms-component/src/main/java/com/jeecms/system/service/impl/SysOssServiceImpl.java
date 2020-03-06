/*
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.system.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionInfo;
import com.jeecms.common.exception.error.SettingErrorCodeEnum;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.dao.SysOssDao;
import com.jeecms.system.domain.SysOss;
import com.jeecms.system.service.SysOssService;

/**
 * oss云存储service实现
 * @author: wulongwei
 * @date: 2019年4月9日 下午1:57:46
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysOssServiceImpl extends BaseServiceImpl<SysOss, SysOssDao, Integer> implements SysOssService {

	@Override
	public ResponseInfo saveSysOss(SysOss sysOss) throws GlobalException {
		this.ckeckOssInfo(sysOss);
		return new ResponseInfo(super.save(sysOss));
	}

	@Override
	public ResponseInfo updateSysOss(SysOss sysOss) throws GlobalException {
		this.ckeckOssInfo(sysOss);
		return new ResponseInfo(super.update(sysOss));
	}
	
	/**
	 * 校验不同类型下，必填字段是否填写
	 * @Title: ckeckOssInfo  
	 * @param sysOss
	 * @throws GlobalException      
	 * @return: void
	 */
	private void ckeckOssInfo(SysOss sysOss) throws GlobalException {
		switch (sysOss.getOssType()) {
		case SysOss.TENCENT_CLOUD:
			this.checkTencentCloud(sysOss);
			break;
		case SysOss.ALI_CLOUD:
			this.checkAliCloud(sysOss);
			break;
		case SysOss.SEVEN_CATTLE_CLOUD:
			this.checkSevenCattleCloud(sysOss);
			break;
		default:
			break;
		}
	}

	/**
	 * 校验腾讯云存储时必填字段
	 * @Title: checkTencentCloud  
	 * @param sysOss
	 * @throws GlobalException 
	 */
	private void checkTencentCloud(SysOss sysOss) throws GlobalException {
		if(StringUtils.isBlank(sysOss.getAppId())) {
			throw new GlobalException(new SystemExceptionInfo(SettingErrorCodeEnum.APP_ID_NOT_NULL.getDefaultMessage(),
					SettingErrorCodeEnum.APP_ID_NOT_NULL.getCode()));
		} else if(StringUtils.isBlank(sysOss.getBucketArea())) {
			throw new GlobalException(new SystemExceptionInfo(SettingErrorCodeEnum.BUCKET_AREA_NOT_NULL.getDefaultMessage(),
					SettingErrorCodeEnum.BUCKET_AREA_NOT_NULL.getCode()));
		}
	}

	/**
	 * 校验阿里云存储时必填字段
	 * @Title: checkAliCloud  
	 * @param sysOss
	 * @throws GlobalException 
	 */
	private void checkAliCloud(SysOss sysOss) throws GlobalException {
		if(StringUtils.isBlank(sysOss.getAccessDomain())) {
			throw new GlobalException(new SystemExceptionInfo(SettingErrorCodeEnum.ACCESS_DO_MAIN_NOR_NULL.getDefaultMessage(),
					SettingErrorCodeEnum.ACCESS_DO_MAIN_NOR_NULL.getCode()));
		} else if(StringUtils.isBlank(sysOss.getEndPoint())) {
			throw new GlobalException(new SystemExceptionInfo(SettingErrorCodeEnum.END_POINT_NOT_NULL.getDefaultMessage(),
					SettingErrorCodeEnum.END_POINT_NOT_NULL.getCode()));
		}
	}

	/**
	 * 校验七牛云存储时必填字段
	 * @Title: checkSevenCattleCloud  
	 * @param sysOss
	 * @throws GlobalException 
	 */
	private void checkSevenCattleCloud(SysOss sysOss) throws GlobalException {
		if(StringUtils.isBlank(sysOss.getAccessDomain())) {
			throw new GlobalException(new SystemExceptionInfo(SettingErrorCodeEnum.ACCESS_DO_MAIN_NOR_NULL.getDefaultMessage(),
					SettingErrorCodeEnum.ACCESS_DO_MAIN_NOR_NULL.getCode()));
		} 
	}

	
}
