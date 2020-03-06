/**
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved.
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.audit.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.audit.domain.AuditStrategy;

/**
 * 审核策略Dao
 *
 * @author ljw
 * @version 1.0
 * @date 2019-12-16
 */
public interface AuditStrategyDao extends IBaseDao<AuditStrategy, Integer> {

	/**
	 * 通过id，站点查找审核策略
	 *
	 * @param id         策略id
	 * @param siteId     站点id
	 * @param hasDeleted true 已删除， false未删除
	 * @return AuditStrategy
	 */
	AuditStrategy findByIdAndSiteIdAndHasDeleted(Integer id, Integer siteId, Boolean hasDeleted);

	/**
	 * 通过策略名称，站点查找审核策略
	 *
	 * @param name       策略名称
	 * @param siteId     站点id
	 * @param hasDeleted true 已删除， false未删除
	 * @return AuditStrategy
	 */
	AuditStrategy findByNameAndSiteIdAndHasDeleted(String name, Integer siteId, Boolean hasDeleted);
}
