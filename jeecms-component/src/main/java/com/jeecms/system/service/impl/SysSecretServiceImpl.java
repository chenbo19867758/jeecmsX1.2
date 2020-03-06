/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service.impl;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.system.dao.SysSecretDao;
import com.jeecms.system.domain.SysSecret;
import com.jeecms.system.service.SysSecretService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 内容/附件密级Service实现类
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-25
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysSecretServiceImpl extends BaseServiceImpl<SysSecret, SysSecretDao, Integer> implements SysSecretService {

	@Autowired
	private SysSecretDao dao;

	@Override
	public boolean checkByName(String name, Integer id, Integer secretType) {
		if (StringUtils.isBlank(name)) {
			return true;
		}
		secretType = secretType == null ? SysSecret.CONTENT_SECRET : secretType;
		SysSecret sysSecret = dao.findByNameAndSecretType(name, secretType);
		if (sysSecret == null) {
			return true;
		} else {
			if (sysSecret.getSecretType().equals(secretType)) {
				if (id == null) {
					return false;
				} else {
					return sysSecret.getId().equals(id);
				}
			}
		}
		return true;
	}

	@Override
	public List<SysSecret> findByType(Integer type) {
		return dao.findByType(type);
	}
}