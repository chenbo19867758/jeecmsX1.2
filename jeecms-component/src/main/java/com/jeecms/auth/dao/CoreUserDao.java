/**
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.auth.dao;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.QueryHints;

import com.jeecms.auth.dao.ext.CoreUserDaoExt;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.base.dao.IBaseDao;

/**
 * 管理员DAO层
 * @author: tom
 * @date: 2018年3月1日 下午8:50:32
 */
public interface CoreUserDao extends IBaseDao<CoreUser, Integer>, CoreUserDaoExt {

	/**
	 * 依据用户名查询单个对象，dao实现不需要实现该方法
	 * 
	 * @param  username 用户名
	 * @return CoreUser
	 * @see com.jeecms.auth.domain.CoreUser
	 */
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	CoreUser findByUsernameAndHasDeletedFalse(String username);

	/**
	 * 根据邮箱查询单个对象
	 * 
	 * @param  email 邮箱
	 * @return CoreUser
	 * @see com.jeecms.auth.domain.CoreUser
	 */
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	CoreUser findByEmailAndHasDeletedFalse(String email);
	
	/**
	 * 根据手机号查询单个对象
	 * 
	 * @param usePhone 手机号
	 * @return CoreUser
	 * @see com.jeecms.auth.domain.CoreUser
	 */
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	CoreUser findByTelephoneAndHasDeletedFalse(String usePhone);

	/**
	 * 根据邮箱和用户名查询单个对象
	 * 
	 * @param  email 邮箱
	 * @param  username 用户名
	 * @return CoreUser
	 * @see com.jeecms.auth.domain.CoreUser
	 */
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	CoreUser findByEmailOrUsernameAndHasDeletedFalse(String email, String username);
}
