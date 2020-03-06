/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.system.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.system.domain.SysBlackList;

/**
 * 黑名单service接口
 * @author: chenming
 * @date:   2019年5月6日 上午8:57:59
 */
public interface SysBlackListService extends IBaseService<SysBlackList, Integer> {
	
	/**
	 * 检验评论
	 * @Title: checkUserComment  
	 * @param siteId	站点Id必填
	 * @param userId	用户Id
	 * @param ip	用户ip
	 * @return: boolean	如果没有重复的返回true，如果有重复的返回false
	 */
	boolean checkUserComment(Integer siteId, Integer userId, String ip);
	
	/**
	 * 根据站点Id查询出所有的黑名单列表
	 * @Title: findBySiteId  
	 * @param siteId	站点Id
	 * @param type	状态
	 * @return: List
	 */
	List<SysBlackList> findBySiteId(Integer siteId, Integer type);
	
	/**
	 * 通过条件查询出黑名单分页信息
	 * @Title: getPage  
	 * @param status	状态：true->userId不为空，false->ip不为空
	 * @param userName	用户名进行模糊查询
	 * @param ip	黑名单ip进行模糊查询
	 * @param siteId	站点Id
	 * @param pageable	分页信息
	 * @return: Page
	 */
	Page<SysBlackList> getPage(boolean status, String userName, String ip, Integer siteId, Pageable pageable);
	
	/**
	 * 根据Id查询单个黑名单信息(并将评论集合放入到黑名单对象中)
	 * @Title: findCommentList  
	 * @param id	黑名单id
	 * @return: SysBlackList
	 */
	SysBlackList findCommentList(Integer id);
	
	/**
	 * 根据siteId以及userId或者ip进行精准查询
	 * @Title: findByUserIdByIp  
	 * @param siteId	站点Id
	 * @param userId	用户Id
	 * @param ip	用户ip
	 * @return: SysBlackList
	 */
	SysBlackList findByUserIdByIp(Integer siteId, Integer type, Integer userId, String ip);
}
