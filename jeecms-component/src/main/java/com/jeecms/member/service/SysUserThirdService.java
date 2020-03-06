/**
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.member.service;

import com.jeecms.member.domain.SysUserThird;

import java.util.List;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;

/**
 * 用户第三方账户绑定表Service
* @author ljw
* @version 1.0
* @date 2019-07-19
*/
public interface SysUserThirdService extends IBaseService<SysUserThird, Integer> {

	/**
	 * 删除第三方信息
	* @Title: deleteInfo 
	* @param id 第三方ID
	* @return SysUserThird
	* @throws GlobalException 异常
	 */
	SysUserThird deleteInfo(Integer id) throws GlobalException;

	/**
	 * 根据appid查询第三方平台的信息
	* @Title: getThirds 
	* @param appid 应用APPID
	* @return
	 */
	List<SysUserThird> getThirds(String appid);

	/**
	 * 根据thirdId获取会员第三方信息
	* @Title: findByThirdId 
	* @param thirdId 第三方ID
	* @param typeCode 类型
	* @throws GlobalException 异常
	 */
	List<SysUserThird> findByThirdId(String thirdId, String typeCode) throws GlobalException;

	/**
	 *  获取指定第三方类型的会员第三方信息(多个只返回第一个)
	* @Title: findByTypeCodeAndMember 
	* @param typeCode 类型
	* @param memberId 用户ID
	* @throws GlobalException 异常
	 */
	SysUserThird findByTypeCodeAndMember(String typeCode, Integer memberId) throws GlobalException;

	/**
	 * 获取指定第三方类型的会员第三方信息(返回多个)
	* @Title: listByTypeCodeAndMember 
	* @param typeCode 类型
	* @param memberId 用户ID
	* @throws GlobalException 异常
	 */
	List<SysUserThird> listByTypeCodeAndMember(String typeCode, Integer memberId) throws GlobalException;

	/**
	 * 根据微信公众号(appid)，登录方式(typeCode)，用户Id(memberId)查询出会员第三方配置详情
	 * 
	 * @Title: findByTypeCodeAndMemberAndAppId
	 * @param typeCode
	 *            登录方式
	 * @param memberId
	 *            会员ID
	 * @param appId
	 *            微信公众号唯一标识
	 * @throws GlobalException 异常
	 * @return: MemberThird
	 */
	SysUserThird findByTypeCodeAndMemberAndAppId(String typeCode, Integer memberId, String appId) 
			throws GlobalException;
}
