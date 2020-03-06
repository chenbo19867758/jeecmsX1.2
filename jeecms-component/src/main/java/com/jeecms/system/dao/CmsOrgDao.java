/**
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.system.dao;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.QueryHints;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.system.dao.ext.CmsOrgDaoExt;
import com.jeecms.system.domain.CmsOrg;

/**
 * 组织管理dao
 * @author: tom
 * @date: 2018年11月5日 下午1:51:08
 */
public interface CmsOrgDao extends IBaseDao<CmsOrg, Integer>,CmsOrgDaoExt {
	
	/**
	 * 依据组织名查询单个对象，dao实现不需要实现该方法
	 * @Title: findByNameAndHasDeleted
	 * @param orgname 组织名
	 * @param delete 是否删除
	 * @return CmsOrg
	 */
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    CmsOrg findByNameAndHasDeleted(String orgname,Boolean delete);
}
