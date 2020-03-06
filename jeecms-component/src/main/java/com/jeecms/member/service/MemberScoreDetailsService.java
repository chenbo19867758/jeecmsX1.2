/**
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.member.service;

import com.jeecms.member.domain.MemberScoreDetails;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;

/**
 * 会员积分详情Service
* @author ljw
* @version 1.0
* @date 2019-09-23
*/
public interface MemberScoreDetailsService extends IBaseService<MemberScoreDetails, Integer> {

	/**
	 * 新增积分详情
	* @Title: addMemberScore 
	* @param type 积分途径类型 1-投稿   2-评论  3-注册  4-完善信息,常量地址：MemberScoreDetails
	* @param userId 用户ID
	* @param commentId 用户评论ID
	* @throws GlobalException 异常
	 */
	void addMemberScore(int type, Integer userId, Integer siteId, Integer commentId) throws GlobalException;
	
}
