package com.jeecms.system.dao.ext;

import java.util.List;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.domain.SiteModelTpl;

/**
 * 站点模型模板配置dao扩展接口
 * @author: chenming
 * @date:   2019年5月5日 下午1:43:13
 */
public interface SiteModelTplDaoExt {
	
	/**
	 * 通过站点ID查询站点模型配置
	 * @Title: findBySiteId  
	 * @param siteId	站点Id
	 * @throws GlobalException      全局异常
	 * @return: List
	 */
	List<SiteModelTpl> findBySiteId(Integer siteId) throws GlobalException;
	
	/**
	 * 通过站点和模型进行检索查询
	 * @Title: findBySiteIdAndModelId  
	 * @param siteId	站点Id
	 * @param modelId	模型id
	 * @throws GlobalException	全局异常      
	 * @return: SiteModelTpl
	 */
	SiteModelTpl findBySiteIdAndModelId(Integer siteId, Integer modelId) throws GlobalException;
}
