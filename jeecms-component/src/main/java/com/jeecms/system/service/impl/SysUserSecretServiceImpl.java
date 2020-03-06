/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service.impl;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionInfo;
import com.jeecms.common.exception.error.SysOtherErrorCodeEnum;
import com.jeecms.system.dao.SysUserSecretDao;
import com.jeecms.system.domain.SysSecret;
import com.jeecms.system.domain.SysUserSecret;
import com.jeecms.system.domain.dto.UserSecretDto;
import com.jeecms.system.service.SysSecretService;
import com.jeecms.system.service.SysUserSecretService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 人员密级Service实现类
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-25
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserSecretServiceImpl extends BaseServiceImpl<SysUserSecret, SysUserSecretDao, Integer> implements SysUserSecretService {

	@Autowired
	private SysSecretService sysSecretService;

	@Override
	public SysUserSecret save(UserSecretDto userSecretDto) throws GlobalException {
		if (!checkByName(userSecretDto.getName(), null)) {
			throw new GlobalException(new SystemExceptionInfo(
					SysOtherErrorCodeEnum.USER_SECRET_EXIST_ERROR.getDefaultMessage(),
					SysOtherErrorCodeEnum.USER_SECRET_EXIST_ERROR.getCode()));
		}
		SysUserSecret sysUserSecret = new SysUserSecret();
		sysUserSecret.setName(userSecretDto.getName());
		sysUserSecret.setRemark(userSecretDto.getRemark());
		sysUserSecret.setSortNum(userSecretDto.getSortNum());
		Integer[] contentSecretIds = userSecretDto.getContentSecretIds();
		List<SysSecret> list = new ArrayList<SysSecret>();
		//循环遍历内容密级，添加到list
		for (Integer id : contentSecretIds) {
			list.add(sysSecretService.findById(id));
		}
		Integer[] annexSecretIds = userSecretDto.getAnnexSecretIds();
		//循环遍历附件密级，添加到list
		for (Integer id : annexSecretIds) {
			list.add(sysSecretService.findById(id));
		}
		sysUserSecret.setSysSecrets(list);
		return super.save(sysUserSecret);
	}

	@Override
	public SysUserSecret update(UserSecretDto userSecretDto) throws GlobalException {
		if (!checkByName(userSecretDto.getName(), userSecretDto.getId())) {
			throw new GlobalException(new SystemExceptionInfo(
					SysOtherErrorCodeEnum.USER_SECRET_EXIST_ERROR.getDefaultMessage(),
					SysOtherErrorCodeEnum.USER_SECRET_EXIST_ERROR.getCode()));
		}
		SysUserSecret sysUserSecret = super.findById(userSecretDto.getId());
		sysUserSecret.setName(userSecretDto.getName());
		sysUserSecret.setRemark(userSecretDto.getRemark());
		sysUserSecret.setSortNum(userSecretDto.getSortNum());
		Integer[] contentSecretIds = userSecretDto.getContentSecretIds();
		//添加关联前先清空原有关联
		sysUserSecret.getSysSecrets().clear();
		List<SysSecret> list = new ArrayList<SysSecret>();
		//循环遍历内容密级，添加到list
		for (Integer id : contentSecretIds) {
			list.add(sysSecretService.findById(id));
		}
		Integer[] annexSecretIds = userSecretDto.getAnnexSecretIds();
		//循环遍历附件密级，添加到list
		for (Integer id : annexSecretIds) {
			list.add(sysSecretService.findById(id));
		}
		sysUserSecret.setSysSecrets(list);
		return super.update(sysUserSecret);
	}

	/**
	 * 校验密集名称是否可用
	 *
	 * @param name 密级名称
	 * @param id   密级id
	 * @return boolean
	 */
	@Override
	public boolean checkByName(String name, Integer id) {
		if (StringUtils.isBlank(name)) {
			return true;
		}
		SysUserSecret sysUserSecret = dao.findByName(name);
		if (sysUserSecret == null) {
			return true;
		} else {
			if (!sysUserSecret.getId().equals(id)) {
				return false;
			}
		}
		return true;
	}

}