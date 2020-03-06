/**
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.weibo.service;

import com.jeecms.weibo.domain.WeiboInfo;
import com.jeecms.weibo.domain.vo.WeiboTokenVO;

import java.util.List;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;

/**
 * 微博信息Service
* @author ljw
* @version 1.0
* @date 2019-06-17
*/
public interface WeiboInfoService extends IBaseService<WeiboInfo, Integer> {

	/**
	 * 保存微博用户信息
	* @Title: saveWeiboInfo 
	* @param vo 授权成功返回的TOKEN
	* @param siteId 站点ID
	* @throws GlobalException 异常
	 */
	ResponseInfo saveWeiboInfo(WeiboTokenVO vo, Integer siteId) throws GlobalException;
	
	/**
	 * 根据微博UID查询
	* @Title: findWeiboInfo 
	* @param uid 微博UID
	* @throws GlobalException 异常
	 */
	WeiboInfo findWeiboInfo(String uid) throws GlobalException;
	
	/**
	 * 根据站点ID和微博UID查询
	* @Title: findWeiboInfo 
	* @param siteId 站点ID
	* @throws GlobalException 异常
	 */
	List<WeiboInfo> findList(Integer siteId) throws GlobalException;
	
	/**
	 * 检查管理员是否有权限操作该公众号数据，校验逻辑如下（菜单权限在此处不做处理，全局拦截已处理）
	 * 1、在公众号设置管理员情况下，管理员是否为公众号的管理员且是否有菜权限；
	 * 2、在未设置管理员情况下,是否有菜单权限
	 * @param userId  操作用户id
	 * @param id 操作授权微博用户id
	 * @throws GlobalException 异常
	 */
	void checkWeiboAuth(Integer userId,Integer id) throws GlobalException;
}
