/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.dao.ext;

import java.util.List;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.domain.CmsModelTpl;

/**
 * 模板扩展Dao
 * 
 * @author: ljw
 * @date: 2019年4月24日 上午11:46:15
 */
public interface CmsModelTplDaoExt {

	/**
	 * 获取模板下拉列表
	 * 
	 * @Title: models
	 * @param siteId   站点Id
	 * @param modelId  模板id
	 * @param solution 模板方案名
	 * @throws GlobalException 全局异常
	 * @return: List
	 */
	List<CmsModelTpl> models(Integer siteId, Integer modelId, String solution) throws GlobalException;
	
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
