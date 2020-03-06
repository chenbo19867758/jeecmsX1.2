/*
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.dao.ext;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.domain.CmsModel;

/**
 * 模型dao层扩展
 * 
 * @author: wulongwei
 * @version 1.0
 * @date: 2019年4月18日 上午10:01:09
 */
public interface CmsModelDaoExt {

	/**
	 * 全站模型信息+本站创建模型信息
	 * 
	 * @Title: getPage
	 * @param tplType
	 * @param isGlobal
	 * @param isEnable
	 * @param modelName
	 * @param siteId
	 * @param pageable
	 * @return
	 * @throws GlobalException
	 * @return: Page<CmsModel>
	 */
	Page<CmsModel> getPage(Short tplType, Short isGlobal, Boolean isEnable, String modelName, Integer siteId,
			Pageable pageable) throws GlobalException;

	/**
	 * 查询模型列表
	 * @Title: getList
	 * @param tplType   模型类型（1-栏目模型 2-内容模型 3-会员模型）
	 * @param isEnable 是否启用
	 * @param siteId    站点ID
	 * @throws GlobalException 全局异常
	 * @return: List
	 */
	List<CmsModel> getList(Short tplType, Boolean isEnable, Integer siteId) throws GlobalException;
	
	/**
	 * 获取不同模型中的最大排序值且最大排序权重对象
	 * @param tplType
	 * @return
	 */
	CmsModel getMaxModelType(Short tplType);
}
