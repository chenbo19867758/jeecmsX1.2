/**
 *  @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service;

import java.util.List;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.domain.SiteModelTpl;

/**
 * 站点配置Service
* @author ljw
* @version 1.0
* @date 2019-04-23
*/
public interface SiteModelTplService extends IBaseService<SiteModelTpl, Integer> {

	/**
	 * 根据站点ID获取到内容
	 * @Title: findBySiteId  
	 * @param siteId	站点Id
	 * @throws GlobalException     全局异常 
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
