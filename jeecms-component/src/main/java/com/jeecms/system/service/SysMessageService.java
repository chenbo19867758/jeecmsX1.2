package com.jeecms.system.service;

import org.springframework.data.domain.Pageable;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.SysMessage;
import com.jeecms.system.domain.dto.SysMessageDto;

/**
* @author ljw
* @version 1.0
* @date 2018-07-02
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/
public interface SysMessageService extends IBaseService<SysMessage, Integer>{

	/**
	 * 获取列表信息
	 * @Description:
	 * @Title: getMessagePage  
	 * @param pageable
	 * @return      
	 * @return: ResponseInfo
	 */
	ResponseInfo getMessagePage(Pageable pageable);
	
	/**
	 * 删除消息
	 * @Description:
	 * @Title: deleteMessage  
	 * @param ids
	 * @return      
	 * @throws GlobalException 全局异常     
	 * @return: ResponseInfo
	 */
	ResponseInfo deleteMessage(Integer[] ids)throws GlobalException;
	
	/**
	 * 发送信息
	 * @Description:
	 * @Title: saveMessage  
	 * @param dto
	 * @param title 信息标题
	 * @param content 信息内容
	 * @param recTargetType 接收对象类型  1-全部  2-全部管理员   3-全部会员   4-组织  
	 * 5-指定管理员  6-会员等级   7-会员组  8-指定会员 (默认5)
	 * @return
	 * @throws GlobalException      
	 * @return: ResponseInfo
	 */
	ResponseInfo saveMessage(SysMessageDto dto) throws GlobalException;
	
}
