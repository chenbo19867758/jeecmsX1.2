/**
** @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.audit.dao;

import com.jeecms.audit.domain.AuditChannelSet;
import com.jeecms.common.base.dao.IBaseDao;

import java.util.List;


/**
 * 栏目审核Dao
* @author ljw
* @version 1.0
* @date 2019-12-16
*/
public interface AuditChannelSetDao extends IBaseDao<AuditChannelSet, Integer> {

	/**
	 * 得到策略ID不为传值的栏目设置
	* @Title: findByStrategyIdNotAndHasDeleted 
	* @param strategyId 策略ID
	* @param hasDeleted 是否删除
	* @return
	 */
	List<AuditChannelSet> findByStrategyIdNotAndHasDeleted(Integer strategyId, Boolean hasDeleted);

	/**
	 * 根据策略id查找
	 * @param strategyIds 策略id集合
	 * @return List
	 */
	List<AuditChannelSet> findByStrategyIdIn(Integer[] strategyIds);

	/**
	 * 通过栏目id查询策略对象
	 * @Title: findByChannelIdAndHasDeleted  
	 * @param channelId		栏目id
	 * @param hasDeleted	逻辑删除标识
	 * @return: List
	 */
	List<AuditChannelSet> findByChannelIdAndHasDeleted(Integer channelId,Boolean hasDeleted);
}
