/**
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
   *      仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.member.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.member.domain.MemberMessage;
import com.jeecms.member.domain.vo.front.MemberMessageVO;

import java.util.List;

import org.springframework.data.domain.Pageable;

/**
 * 用户接收信息状态
 * 
 * @author ljw
 * @version 1.0
 * @date 2018-09-25
 * 
 */
public interface MemberMessageService extends IBaseService<MemberMessage, Integer> {

	/**
	 * 根据会员获取相应系统未读的信息
	 * @param groupId 会员组ID
	 * @param levelId 等级ID
	 * @param pageable 分页参数
	 * @return
	 * @throws GlobalException 全局异常
	 */
	ResponseInfo getSysMessagePage(Integer groupId, Integer levelId, Pageable pageable) throws GlobalException;

	/**
	 * 根据会员获取相应私人未读的信息
	 * @param memberId 会员ID
	 * @param pageable 分页参数
	 * @return
	 * @throws GlobalException
	 */
	ResponseInfo getPriMessagePage(Integer memberId, Pageable pageable) throws GlobalException;

	/**
	 * 根据会员获取相应系统未读的信息数量
	 * @param groupId 会员组ID
	 * @param levelId 等级ID
	 * @return
	 * @throws GlobalException
	 */
	Long unreadNumSys(Integer groupId,Integer levelId) throws GlobalException;

	/**
	 * 根据会员获取相应私人未读的信息数量
	 * 
	 * @param memberId 会员ID
	 * @return
	 * @throws GlobalException
	 */
	Long unreadNumPri(Integer memberId) throws GlobalException;

	/**
	 * 根据messageId查询会员消息集合
	 * 
	 * @param messageId
	 * @return
	 */
	List<MemberMessage> findByMessageId(Integer messageId);

	/**
	 * 根据ID删除
	 * @param memberId
	 * @param ids
	 * @return
	 * @throws GlobalException
	 */
	ResponseInfo delMemberMes(Integer memberId, Integer[] ids) throws GlobalException;

	/**
	 * 会员消息基本信息
	 * @Title: messageInfo  
	 * @param groupId 会员组ID
	 * @param levelId 会员等级ID
	 * @param memberId 会员ID
	 * @return
	 * @throws GlobalException  全局异常    
	 * @return: MemberMessageVO
	 */
	MemberMessageVO messageInfo(Integer groupId,Integer levelId, Integer memberId) throws GlobalException;

}
