/**
** @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.weibo.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.weibo.domain.WeiboFans;


/**
 * 微博粉丝Dao
* @author ljw
* @version 1.0
* @date 2019-06-18
*/
public interface WeiboFansDao extends IBaseDao<WeiboFans, Integer> {

	/**
	 * 根据UID删除微博粉丝
	* @Title: deleteAllFans 
	* @param uid 授权账户UID
	 */
	@Query("delete from WeiboFans bean where 1 = 1 and bean.uid =?1")
	@Modifying
	void deleteAllFans(Long uid);
}
