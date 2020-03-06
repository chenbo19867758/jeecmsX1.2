/**
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.auth.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.jeecms.auth.domain.CoreUserMessage;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;

/**
 * 平台接收消息状态Service
 * 
 * @author ljw
 * @version 1.0
 * @date 2019-01-23
 */
public interface CoreUserMessageService extends IBaseService<CoreUserMessage, Integer> {

	/**
	 * 收件箱（系统消息+私信）列表分页 
	 * @Title: getMessagePage 
	 * @param pageable 分页对象
	 * @param userId 用户ID
	 * @param sendUserName 发件人
	 * @param status 是否已读
	 * @param startTime 开始时间 
	 * @param endTime 结束时间
	 * @param title 标题
	 * @param content 内容
	 * @throws GlobalException 异常 
	 */
	ResponseInfo getMessagePage(Pageable pageable, Integer userId, Boolean status, Date startTime, Date endTime,
			String title, String content, String sendUserName) throws GlobalException;

	/**
	 * 根据messageId查询用户消息集合
	 * 
	 * @Title: findByMessageId
	 * @param messageId 消息主键ID
	 * @throws GlobalException 全局异常
	 */
	List<CoreUserMessage> findByMessageId(Integer messageId) throws GlobalException;

	/**
	 * 标记已读
	 * 
	 * @Description:
	 * @Title: saveMessage
	 * @param ids ids
	 * @throws GlobalException 异常
	 */
	ResponseInfo saveMessage(Integer[] ids) throws GlobalException;

	/**
	 * 根据ID删除
	 * 
	 * @param ids 数组
	 * @throws GlobalException 异常
	 */
	ResponseInfo delMessage(Integer[] ids) throws GlobalException;

	/**
	 * 用户未读信息
	 * 
	 * @Description:
	 * @Title: unreadMessage
	 * @param userId 用户ID
	 * @throws GlobalException 异常
	 */
	Long unreadMessage(Integer userId) throws GlobalException;
}
