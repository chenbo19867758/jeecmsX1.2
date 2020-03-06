/*
 *  * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.system.service;

import java.util.List;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.MessageTpl;

/**
 * 
 * 模版信息service接口
 * @author: wulongwei
 * @version 1.0
 * @date:   2019年4月26日 下午1:40:50
 */
public interface MessageTplService extends IBaseService<MessageTpl, Integer> {
	


	/**
	 * 保存
	 * @Title: saveMessageTpl  
	 * @param bean 消息模板
	 * @return
	 * @throws GlobalException 程序异常     
	 * @return: ResponseInfo
	 */
	public ResponseInfo saveMessageTpl(MessageTpl bean) throws GlobalException;

	/**
	 * 批量保存
	 * @Title: saveAll
	 * @param: @param entities
	 * @param: @return
	 * @throws GlobalException 全局异常
	 * @return: List<S>
	 */
	public <S extends MessageTpl> List<S> saveAll(Iterable<S> entities) throws GlobalException;

	/**
	 * 查找模板标识是否存在
	 * @Title: findByMesCode  
	 * @param mesCode
	 * @return      
	 * @return: MessageTpl
	 */
    MessageTpl findByMesCode(String mesCode);
	
    /**
     * 查找消息模板
     * @Title: findByMesCodeAndMesType  
     * @param mesCode
     * @param mesType 模板类型
     * @param hasDeleted 是否删除
     * @return      
     * @return: MessageTpl
     */
  //  MessageTpl findByMesCodeAndMesType(String mesCode , Short mesType,Boolean hasDeleted);

    /**
     * 更新消息模板
     * @Title: updateMessageTpl  
     * @param bean 消息模板
     * @return
     * @throws GlobalException 程序异常     
     * @return: MessageTpl
     */
	public MessageTpl updateMessageTpl(MessageTpl bean)throws GlobalException;
	
	
	/**
	 * 查询手机消息模版是否开启
	 * @Title: findByMesCodeAndMesType  
	 * @return      
	 * @return: Boolean
	 */
	Boolean findByMesCodeAndMesType();
}
