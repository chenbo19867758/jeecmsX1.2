/*
 *  * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.system.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.domain.MessageTplDetails;

/**
 * 模版详细信息service接口
 * @author: wulongwei
 * @version 1.0
 * @date:   2019年4月26日 下午1:41:09
 */
public interface MessageTplDetailsService extends IBaseService<MessageTplDetails, Integer> {

	/**
	 * 保存消息模板
	 * @Title: saveTpl  
	 * @param entities 模板详情
	 * @return
	 * @throws GlobalException 程序异常     
	 * @return: MessageTplDetails
	 */
	MessageTplDetails saveTpl(MessageTplDetails entities) throws GlobalException;


	/**
	 * 获取指定模板标识中指定消息类型的模板详情
	 * @Title: findByCodeAndType  
	 * @param mesCode 
	 * @param mesType 类型
	 * @param detailMesType 详情类型
	 * @return
	 * @throws GlobalException 程序异常     
	 * @return: MessageTplDetails
	 */
	MessageTplDetails findByCodeAndType(String mesCode, Short mesType, Short detailMesType) throws GlobalException;

}
