/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.system.domain.ContentSource;

/**
 * 来源管理dao层
 * 
 * @author: wulongwei
 * @version 1.0
 * @date: 2019年5月6日 上午11:41:37
 */
public interface ContentSourceDao extends IBaseDao<ContentSource, Integer> {

	/**
	 * 根据来源名称查询来源信息
	 * 
	 * @Title: findBySourceNameAndHasDeleted
	 * @param sourceName 来源名称
	 * @param hasDeleted 是否删除
	 * @return: SysSource
	 */
	ContentSource findBySourceNameAndHasDeleted(String sourceName, Boolean hasDeleted);

	/**
	 * 查询默认的来源信息
	 * 
	 * @Title: findByIsDefaultAndHasDeleted
	 * @param isDefault  是否默认
	 * @param hasDeleted 是否删除
	 * @return: SysSource 来源对象
	 */
	ContentSource findByIsDefaultAndHasDeleted(Boolean isDefault, Boolean hasDeleted);
}
