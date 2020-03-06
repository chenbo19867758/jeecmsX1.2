/**
** @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.weibo.dao;

import java.util.List;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.weibo.domain.WeiboInfo;

/**
 * 微博信息DAO
* @author ljw
* @version 1.0
* @date 2019-06-17
*/
public interface WeiboInfoDao extends IBaseDao<WeiboInfo, Integer> {

	/**
	 * 根据Uid和站点ID查询
	* @Title: findByUidAndSiteIdAndHasDeleted 
	* @param uid 微博UID
	* @param hasDeleted 删除标识
	* @return
	 */
	WeiboInfo findByUidAndHasDeleted(String uid, Boolean hasDeleted);
	
	/**
	 * 根据站点ID查询
	* @Title: findByUidAndSiteIdAndHasDeleted 
	* @param siteId 站点ID
	* @param hasDeleted 删除标识
	* @return
	 */
	List<WeiboInfo> findBySiteIdAndHasDeleted(Integer siteId, Boolean hasDeleted);
}
