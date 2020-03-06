/**
** @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.weibo.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.weibo.domain.WeiboAppConfig;

/**
 * 微博应用Dao
* @author ljw
* @version 1.0
* @date 2019-06-15
*/
public interface WeiboAppConfigDao extends IBaseDao<WeiboAppConfig, Integer> {

	/**
	 * 根据站点ID获取应用信息
	* @Title: getBySiteId 
	* @param siteId 站点ID
	* @return WeiboAppConfig
	 */
	WeiboAppConfig findBySiteIdAndHasDeletedFalse(Integer siteId);
}
