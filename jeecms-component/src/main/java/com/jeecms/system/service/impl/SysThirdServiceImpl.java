/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service.impl;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.system.dao.SysThirdDao;
import com.jeecms.system.domain.SysThird;
import com.jeecms.system.service.SysThirdService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 第三方登录设置的service实现类
 * 
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-18
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysThirdServiceImpl extends BaseServiceImpl<SysThird, SysThirdDao, Integer> implements SysThirdService {

	@Override
	public SysThird getCode(String code) {
		return dao.findByCode(code);
	}
	
	@Override
	public List<SysThird> getList() {
		return dao.getList(false);
	}

}