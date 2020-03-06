/**
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.auth.dao.ext;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.page.Paginable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * 管理员DAO扩展层
 * 
 * @author: tom
 * @date: 2018年3月1日 下午8:48:23
 */
public interface CoreUserDaoExt {

	/**
	 * {@docRoot} 管理员列表分页
	 * 
	 * @Title pageUser
	 * @param enabled     是否启用
	 * @param orgids      组织ID集合
	 * @param roleids     角色ID集合
	 * @param key         关键字
	 * @param isAdmin     是否管理员
	 * @param checkStatus 审核状态
	 * @param groupId     会员组ID
	 * @param levelId     用户等级ID
	 * @param userSecretId 人员密级ID
	 * @param sourceSiteId 来源站点ID
	 * @param pageable    分页对象
	 * @return ResponseInfo
	 * @see com.jeecms.auth.domain.CoreUser
	 */
	Page<CoreUser> pageUser(Boolean enabled, List<Integer> orgids, List<Integer> roleids,
			String key, Boolean isAdmin, Short checkStatus, Integer groupId, Integer levelId, 
			Integer userSecretId, List<Integer> sourceSiteId,
			Pageable pageable);

	/**
	 * {@docRoot} 管理员列表
	 * 
	 * @Title findList
	 * @param enabled     是否启用
	 * @param orgids      组织ID集合
	 * @param roleids     角色ID集合
	 * @param key         关键字
	 * @param isAdmin     是否管理员
	 * @param checkStatus 审核状态
	 * @param groupId     会员组ID
	 * @param levelId     用户等级ID
	 * @param secretId    用户密级ID
	 * @param sourceSiteIds 站点来源IDs
	 * @param paginable   取条数对象
	 * @see com.jeecms.auth.domain.CoreUser
	 * @return List
	 */
	List<CoreUser> findList(Boolean enabled, List<Integer> orgids, List<Integer> roleids,
			String key, Boolean isAdmin, Short checkStatus, Integer groupId, Integer levelId, 
			Integer secretId, List<Integer> sourceSiteIds,
			Paginable paginable);

	/**
	 * 微信端查询管理员列表分页
	 * 
	 * @Title: pageWechat
	 * @param orgId    组织id
	 * @param roleid   角色id
	 * @param username 用户名或真实姓名
	 * @param pageable 分页对象
	 * @param notIds   用户集合(不包含的用户)
	 * @return: Page
	 */
	Page<CoreUser> pageWechat(Integer orgId, Integer roleid, String username, 
			Pageable pageable, List<Integer> notIds);
    
	/**
	 * 微信端查询管理员列表分页
	 * 
	 * @Title: pageWechat
	 * @param ids      用户id集合
	 * @param orgId    组织id
	 * @param roleid   角色id
	 * @param username 用户名或真实姓名
	 * @return: Page
	 */
	List<CoreUser> listWechat(List<Integer> ids, Integer orgId, Integer roleid, String username);

	/**
	 * 获取会员数
	 *
	 * @param beginTime   开始时间
	 * @param endTime     结束时间
	 * @param checkStatus 用户审核状态(1审核通过、2审核不通过 0待审核)
	 * @param siteId      站点id
	 * @return 会员数
	 */
	long getUserSum(Date beginTime, Date endTime, Integer siteId, Short checkStatus);
}
