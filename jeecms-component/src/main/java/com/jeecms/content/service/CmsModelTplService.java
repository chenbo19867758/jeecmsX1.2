/*
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved.
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.domain.CmsModelTpl;
import com.jeecms.system.domain.CmsSite;

import java.util.List;

/**
 * 模型配置模板service层
 *
 * @version 1.0
 * @author: wulongwei
 * @date: 2019年4月17日 下午3:16:25
 */
public interface CmsModelTplService extends IBaseService<CmsModelTpl, Integer> {

	/**
	 * 根据模板Id查询模型配置的所有模板
	 *
	 * @param modelId 模板Id
	 * @param siteId  站点Id
	 * @throws GlobalException 全局异常
	 * @Title: findByModelId
	 * @return: List
	 */
	List<CmsModelTpl> findByModelId(Integer modelId, Integer siteId) throws GlobalException;

	/**
	 * 获取模板下拉列表
	 *
	 * @param siteId   站点Id
	 * @param modelId  模板id
	 * @param solution 模板方案名
	 * @throws GlobalException 全局异常
	 * @Title: models
	 * @return: List
	 */
	List<CmsModelTpl> models(Integer siteId, Integer modelId, String solution) throws GlobalException;

	/**
	 * 获取模板适用模型
	 *
	 * @param siteId  站点id
	 * @param tplPath 模板路径
	 * @return List
	 */
	List<CmsModelTpl> findByTplPath(Integer siteId, String tplPath);

	/**
	 * 获取模板适用模型
	 *
	 * @param siteId   站点id
	 * @param tplPath  模板路径
	 * @param solution 模板方案名
	 * @return List
	 */
	List<CmsModelTpl> findByTplPath(Integer siteId, String tplPath, String solution);

	/**
	 * 批量保存模板模型关联
	 *
	 * @param root     保存路径
	 * @param site     站点
	 * @param name     模板路径
	 * @param modelIds 模型id数组
	 * @throws GlobalException 异常
	 */
	void saveBatch(String root, CmsSite site, String name, Integer[] modelIds) throws GlobalException;

	/**
	 * 通过站点、模型id，和模板类型(1栏目模板 2内容模板)查询模板列表
	 * @Title: getList  
	 * @param siteId	站点id
	 * @param modelId	模型id
	 * @param tplType	模板类型(1栏目模板 2内容模板)
	 * @return: List
	 */
	List<CmsModelTpl> getList(Integer siteId, Integer modelId, Short tplType);

}
