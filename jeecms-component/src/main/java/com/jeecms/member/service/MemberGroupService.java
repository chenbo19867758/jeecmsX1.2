/*
 *  * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.member.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.member.domain.MemberGroup;

/**
 * 会员组service层
 * 
 * @author: wulongwei
 * @date: 2019年4月15日 上午10:03:25
 */
public interface MemberGroupService extends IBaseService<MemberGroup, Integer> {

	/**
	 * 添加会员组信息
	 * 
	 * @Title: saveMemberGroupInfo
	 * @param memberGroup 用户组对象
	 * @throws GlobalException 异常
	 */
	ResponseInfo saveMemberGroupInfo(MemberGroup memberGroup) throws GlobalException;

	/**
	 * 修改会员组信息
	 * 
	 * @Title: updateMemberGroupInfo
	 * @param memberGroup 用户组对象
	 * @throws GlobalException 异常
	 */
	ResponseInfo updateMemberGroupInfo(MemberGroup memberGroup) throws GlobalException;
}
