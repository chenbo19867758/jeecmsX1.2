package com.jeecms.system.dao.ext;

import java.util.List;

import com.jeecms.common.page.Paginable;
import com.jeecms.system.domain.CmsDataPerm;

/**
 * 数据权限扩展dao接口
 * 
 * @author: tom
 * @date: 2018年11月5日 下午1:56:08
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface CmsDataPermDaoExt {

	/**
	 * 查询
	 * 
	 * @Title: findList
	 * @param orgId
	 *            组织ID
	 * @param roleId
	 *            角色ID
	 * @param userId
	 *            用户ID
	 * @param siteId
	 *            站点ID
	 * @param dataType
	 *            数据模块类型（1站点 2栏目 3文档 4组织 5可管理站点）
	 * @param operation 操作
	 * @param dataId 数据ID
	 * @param paginable
	 *            取数数据
	 * @return List
	 */
	List<CmsDataPerm> findList(Integer orgId, Integer roleId, Integer userId, Integer siteId,
	                Short dataType,Short operation,Integer dataId,  Paginable paginable);
}
