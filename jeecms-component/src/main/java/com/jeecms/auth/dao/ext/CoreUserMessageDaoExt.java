/**
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.auth.dao.ext;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.domain.vo.MessageVo;

/**
 * 用户消息扩展dao接口
 * 
 * @author: ljw
 * @date: 2018年7月04日 上午9:57:51
 */
public interface CoreUserMessageDaoExt {

	/**
	 * 收件箱（系统消息+私信）列表分页
	 * 
	 * @Description:
	 * @param: status 信件状态
	 * @param: startTime 开始时间
	 * @param: endTime 结束时间
	 * @param: title 标题
	 * @param: content 内容
	 * @param: sendUserName 发件人
	 * @param: pageable 分页对象
	 * @throws GlobalException 异常
	 * @return: ResponseInfo
	 */
	Page<MessageVo> getMessagePage(Pageable pageable, Integer userId, Boolean status, Date startTime, Date endTime,
			String title, String content, String sendUserName) throws GlobalException;

	/**
	 * 管理员未读信息
	 * 
	 * @Title: unreadMessage
	 * @param userId 用户ID
	 * @throws GlobalException 异常
	 */
	Long unreadMessage(Integer userId) throws GlobalException;
}
