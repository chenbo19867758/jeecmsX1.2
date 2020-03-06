/**
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.dao.SiteModelTplDao;
import com.jeecms.system.domain.SiteModelTpl;
import com.jeecms.system.service.SiteModelTplService;

/**
 * 站点配置Service实现
 * 
 * @author ljw
 * @version 1.0
 * @date 2019-04-23
 * 
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SiteModelTplServiceImpl extends BaseServiceImpl<SiteModelTpl, SiteModelTplDao, Integer>
		implements SiteModelTplService {

	@Override
	public List<SiteModelTpl> findBySiteId(Integer siteId) throws GlobalException {

		return dao.findBySiteId(siteId);
	}

	@Override
	public SiteModelTpl findBySiteIdAndModelId(Integer siteId, Integer modelId) throws GlobalException {
		return dao.findBySiteIdAndModelId(siteId, modelId);
	}

}