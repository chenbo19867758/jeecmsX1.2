/**
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.auth.dao;

import com.jeecms.auth.dao.ext.CoreRoleDaoExt;
import com.jeecms.auth.domain.CoreRole;
import com.jeecms.common.base.dao.IBaseDao;

/**
 * 角色Dao
 * 
 * @author: tom
 * @date: 2018年3月1日 下午8:50:32
 * 
 */
public interface CoreRoleDao extends IBaseDao<CoreRole, Integer>, CoreRoleDaoExt {
}
