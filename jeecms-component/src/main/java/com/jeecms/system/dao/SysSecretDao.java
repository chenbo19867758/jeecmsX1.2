/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.system.domain.SysSecret;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;


/**
 * 内容/附件密级dao
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-25
 */
public interface SysSecretDao extends IBaseDao<SysSecret, Integer> {

	/**
	 * 通过密级名称查找密级
	 *
	 * @param name       密级名称
	 * @param secretType 密级类型（1内容密级 2附件密级类型）
	 * @return SysSecret
	 */
	@Query
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	SysSecret findByNameAndSecretType(String name, Integer secretType);

	@Query("select bean from SysSecret bean where 1 = 1 and bean.secretType = ?1 and bean.hasDeleted = false ")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	List<SysSecret> findByType(Integer type);
}
