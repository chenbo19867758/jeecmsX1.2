/*
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.member.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.member.domain.MemberLevel;

/**
 * 会员等级
 * @author: wulongwei
 * @date:   2019年4月15日 下午2:39:42
 */
public interface MemberLevelService extends IBaseService<MemberLevel, Integer> {

	/**
	 * 保存会员等级信息
	 * @Title: saveMemberLevel  
	 * @param memberLevel 会员等级
	 * @throws GlobalException 异常   
	 * @return: MemberLevel 会员等级
	 */
	MemberLevel saveMemberLevel(MemberLevel memberLevel) throws GlobalException;

	/**
	 * 更新会员等级信息
	 * @Title: updateMemberLevel  
	 * @param memberLevel 会员等级
	 * @throws GlobalException 异常       
	 * @return: MemberLevel 会员等级
	 */
	MemberLevel updateMemberLevel(MemberLevel memberLevel) throws GlobalException;

	/**
	 * 根据等级会员名称 获取等级信息
	 * @Title: findByLevelName  
	 * @param levelName 等级名称
	 * @throws GlobalException 全局异常     
	 * @return: MemberLevel 等级对象
	 */
	MemberLevel findByLevelName(String levelName) throws GlobalException;
	
	/**
	 * 根据等级积分获取等级
	 * @Title: findBySecore  
	 * @param secore 积分
	 * @throws GlobalException 全局异常     
	 * @return: MemberLevel 等级对象
	 */
	MemberLevel findBySecore(Integer secore) throws GlobalException;

}
