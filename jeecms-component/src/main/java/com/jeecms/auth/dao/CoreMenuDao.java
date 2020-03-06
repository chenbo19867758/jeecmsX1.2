package com.jeecms.auth.dao;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.QueryHints;

import com.jeecms.auth.dao.ext.CoreMenuDaoExt;
import com.jeecms.auth.domain.CoreMenu;
import com.jeecms.common.base.dao.IBaseDao;

/**
 * 菜单数据访问层接口定义
 *
 * @author: ztx
 * @date: 2018年6月25日 下午7:24:11
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface CoreMenuDao extends IBaseDao<CoreMenu, Integer>, CoreMenuDaoExt {

	/**
	 * 根据菜单标识查找菜单信息
	 *
	 * @param name      菜单标识
	 * @param hasDelete 是否删除
	 * @return CoreMenu
	 */
        @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	CoreMenu findByNameAndHasDeleted(String name, Boolean hasDelete);

	/**
	 * 根据菜单标识查找菜单信息
	 *
	 * @param path      路由地址
	 * @param hasDelete 是否删除
	 * @return CoreMenu
	 */
        @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	CoreMenu findByPathAndHasDeleted(String path, Boolean hasDelete);

        /**
         * 查询可分配的菜单列表
         */
        @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
        List<CoreMenu> findByIsAuthAndHasDeleted(Boolean isAuth, Boolean hasDelete);
}
