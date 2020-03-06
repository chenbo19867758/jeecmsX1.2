/*
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.content.dao;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.QueryHints;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.content.dao.ext.CmsModelDaoExt;
import com.jeecms.content.domain.CmsModel;

/**
 * 模型dao层
 * @author: wulongwei
 * @version 1.0
 * @date:   2019年4月17日 下午3:11:04
 */
public interface CmsModelDao extends IBaseDao<CmsModel, Integer>, CmsModelDaoExt {

	/**
	 * 获取不同状态的模型列表
	 * @Title: findByIsEnableAndSiteIdAndHasDeleted  
	 * @param isEnable 是否启用
	 * @param siteId 站点ID
	 * @param hasDeleted 已删除
	 * @return      
	 * @return: List<CmsModel>
	 */
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	List<CmsModel> findByIsEnableAndSiteIdAndHasDeleted(Boolean isEnable, Integer siteId, Boolean hasDeleted);
	
	/**
	 * 通过模型类型及模型名称查询
	 * @Title: findByModelNameAndIsGlobalAndHasDeleted  
	 * @param modelName
	 * @param tplType
	 * @param hasDeleted
	 * @return      
	 * @return: CmsModel
	 */
	CmsModel findByModelNameAndTplTypeAndHasDeleted(String modelName, Short tplType, Boolean hasDeleted);
	
	/**
	 * 通过模型类型获取模型对象
	 * @param tplType  模型类型  1:栏目模型  2:内容模型  3:会员模型
	 * @param hasDeleted
	 * @return List<CmsModel>
	 */
	List<CmsModel> findByTplTypeAndHasDeleted(Short tplType, Boolean hasDeleted);
}
