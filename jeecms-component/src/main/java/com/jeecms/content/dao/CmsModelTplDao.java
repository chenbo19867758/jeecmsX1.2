/*
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved.
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.content.dao.ext.CmsModelTplDaoExt;
import com.jeecms.content.domain.CmsModelTpl;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * 模型配置模板dao层
 *
 * @version 1.0
 * @author: wulongwei
 * @date: 2019年4月17日 下午3:12:01
 */
public interface CmsModelTplDao extends IBaseDao<CmsModelTpl, Integer>, CmsModelTplDaoExt {

	/**
	 * 查询该站点该模型配置的模板集合
	 *
	 * @param modelId 模型Id
	 * @param siteId  站点Id
	 * @Title: findByModelId
	 * @return: List
	 */
	@Query("select bean from CmsModelTpl bean where 1 = 1 and bean.modelId = ?1 and bean.siteId = ?2")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	List<CmsModelTpl> findByModelId(Integer modelId, Integer siteId);

	/**
	 * 查询该站点模型的适用范围
	 *
	 * @param siteId  站点id
	 * @param tplPath 模型路径
	 * @return List
	 */
	@Query("select bean from CmsModelTpl bean where 1=1 and bean.siteId = ?1 and bean.tplPath = ?2")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	List<CmsModelTpl> findByTplPath(Integer siteId, String tplPath);

	/**
	 * 查询该站点模型的适用范围
	 *
	 * @param siteId  站点id
	 * @param tplPath 模型路径
	 * @param tplType 模板类型(1栏目模板 2内容模板)
	 * @param tplSolution  模板方案名
	 * @return List
	 */
	@Query("select bean from CmsModelTpl bean where 1=1 and bean.siteId = ?1 and bean.tplPath = ?2 and "
			+ " bean.tplSolution = ?3")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	List<CmsModelTpl> findByTplPath(Integer siteId, String tplPath, String tplSolution);
}
