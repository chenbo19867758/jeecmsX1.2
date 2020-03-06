/*
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved.
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.service.impl;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.dao.CmsModelTplDao;
import com.jeecms.content.domain.CmsModel;
import com.jeecms.content.domain.CmsModelTpl;
import com.jeecms.content.service.CmsModelService;
import com.jeecms.content.service.CmsModelTplService;
import com.jeecms.system.domain.CmsSite;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 模型配置模板service实现层
 *
 * @version 1.0
 * @author: wulongwei
 * @date: 2019年4月17日 下午3:08:09
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CmsModelTplServiceImpl extends BaseServiceImpl<CmsModelTpl, CmsModelTplDao, Integer>
		implements CmsModelTplService {

	@Override
	public List<CmsModelTpl> findByModelId(Integer modelId, Integer siteId) throws GlobalException {
		return dao.findByModelId(modelId, siteId);
	}

	@Override
	public List<CmsModelTpl> models(Integer siteId, Integer modelId, String solution) throws GlobalException {
		return dao.models(siteId, modelId, solution);
	}

	@Override
	public List<CmsModelTpl> findByTplPath(Integer siteId, String tplPath) {
		return dao.findByTplPath(siteId, tplPath);
	}

	@Override
	public List<CmsModelTpl> findByTplPath(Integer siteId, String tplPath, String solution) {
		return dao.findByTplPath(siteId, tplPath, solution);
	}

	@Override
	public void saveBatch(String root, CmsSite site, String name, Integer[] modelIds) throws GlobalException {
		if (modelIds != null && modelIds.length > 0) {
			String path = root.replace(site.getTplPath() + "/", "") + "/";
			// 模板方案名
			String solution = path.substring(0, path.indexOf("/"));
			//模板方案名不为空
			if (StringUtils.isNotBlank(solution)) {
				name = name.replace(site.getTplPath() + WebConstants.SPT + solution + WebConstants.SPT, "");
			} else {
				name = name.replace(site.getTplPath() + WebConstants.SPT, "");
			}
			List<CmsModelTpl> list = new ArrayList<CmsModelTpl>(modelIds.length);
			List<CmsModel> models = modelService.findAllById(Arrays.asList(modelIds));
			for (CmsModel model : models) {
				CmsModelTpl cmsModelTpl = new CmsModelTpl();
				cmsModelTpl.setModelId(model.getId());
				cmsModelTpl.setSiteId(site.getSiteId());
				cmsModelTpl.setTplPath(name);
				cmsModelTpl.setTplSolution(solution);
				cmsModelTpl.setTplType(model.getTplType());
				list.add(cmsModelTpl);
			}
			saveAll(list);
		}
	}

	@Override
	public List<CmsModelTpl> getList(Integer siteId, Integer modelId, Short tplType) {
		return dao.getList(siteId, modelId, tplType);
	}
	
	@Autowired
	private CmsModelService modelService;

}