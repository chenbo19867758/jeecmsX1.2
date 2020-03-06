/***
 *  * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.resource.service.TplResourceService;
import com.jeecms.system.dao.CmsSiteExtDao;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.CmsSiteExt;
import com.jeecms.system.service.CmsSiteExtService;
import com.jeecms.system.service.CmsSiteService;

/**
 * 站点扩展Service
 * @author ljw
 * @version 1.0
 * @date 2019-04-11
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CmsSiteExtServiceImpl extends BaseServiceImpl<CmsSiteExt, CmsSiteExtDao, Integer>
		implements CmsSiteExtService {

	@Autowired
	private TplResourceService tplResourceService;
	@Autowired
	private CmsSiteService cmsSiteService;
	
	/**
	 * 创建目录以及复制父站点的目录下的全部文件
	 * 
	 * @Title: saveDir
	 * @param from 来源站点ID
	 * @param to 目标站点ID
	 * @throws IOException 异常
	 */
	@Override
	@Async("asyncServiceExecutor")
	public void saveDir(Integer from, Integer to) throws IOException {
		CmsSite fromSite = cmsSiteService.findById(from);
		CmsSite toSite = cmsSiteService.findById(to);
		tplResourceService.copyTplAndRes(fromSite, toSite);
	}
}