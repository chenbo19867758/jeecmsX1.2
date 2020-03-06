/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.system.domain.SysThird;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * 第三方登录设置dao接口
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-18
 */
public interface SysThirdDao extends IBaseDao<SysThird, Integer> {

	/**
	 * 通过code条件进行查询
	 *
	 * @param code 第三方登陆标识(QQ WECHAT WEIBO )
	 * @Title: findByCode
	 * @return: Third
	 */
	@Query(value = "select bean from SysThird bean where bean.hasDeleted = 0"
			+ " and bean.code = ?1")
	@QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	SysThird findByCode(String code);

	/**
	 * 第三方登录设置列表
	 *
	 * @param hasDeleted 是否删除
	 * @Title: getList
	 * @return: List<Third>
	 */
	@Query("select bean from SysThird bean where bean.hasDeleted = ?1")
	@QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	List<SysThird> getList(boolean hasDeleted);
}
