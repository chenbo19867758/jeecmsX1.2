
package com.jeecms.system.dao.ext;

import java.util.List;

import com.jeecms.common.page.Paginable;
import com.jeecms.system.domain.CmsOrg;

/**
 * 组织扩展dao接口
 * @author: tom
 * @date: 2018年11月5日 下午1:55:38
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface CmsOrgDaoExt {
	
	/**
	 * 查询列表
	 * 
	 * @Title: findList
	 * @param parentId 父ID
	 * @param isVirtual 是否虚拟
	 * @param key       组织名，组织编号，负责人
	 * @param paginable 取数
	 * @return List
	 */
	List<CmsOrg> findList(Integer parentId, Boolean isVirtual, String key, Paginable paginable);
	
	/**
	 * 查询列表
	 * 
	 * @Title: findList
	 * @param ids  组织IDs
	 * @param isVirtual 是否虚拟
	 * @param key       组织名，组织编号，负责人
	 * @param paginable 取数
	 * @return List
	 */
	List<CmsOrg> findListByIDS(List<Integer> ids, Boolean isVirtual, String key, Paginable paginable);
}
