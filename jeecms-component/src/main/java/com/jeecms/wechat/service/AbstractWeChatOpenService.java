/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.wechat.service;


import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.wechat.domain.AbstractWeChatOpen;

/**
 * 微信开放平台service层
 * @author: wulongwei
 * @version 1.0
 * @date:   2019年5月7日 下午3:26:59
 */
public interface AbstractWeChatOpenService extends IBaseService<AbstractWeChatOpen, Integer> {
	
	/**
	 * 查找开启配置微信
	 * @Title: findOpenConfig  
	 * @return      
	 * @return: AbstractWeChatOpen
	 */
	AbstractWeChatOpen findOpenConfig();
	
	/**
	 * 保存开放平台设置信息
     * （只存在一条，如果数据库中有数据，则不能做添加，只能做修改操作，防止违法调用接口，直接返回不做添加处理）
	 * @Title: saveAbstractWeChatOpen  
	 * @param abstractWeChatOpen
	 * @return
	 * @throws GlobalException 全局异常     
	 * @return: ResponseInfo
	 */
	ResponseInfo saveAbstractWeChatOpen(AbstractWeChatOpen abstractWeChatOpen) throws GlobalException;
	
	/**
	 * 第三方平台信息修改
	 * @Title: updateAbstractWeChatOpen  
	 * @param abstractWeChatOpen
	 * @return
	 * @throws GlobalException 全局异常     
	 * @return: ResponseInfo
	 */
	ResponseInfo updateAbstractWeChatOpen(AbstractWeChatOpen abstractWeChatOpen) throws GlobalException;
}
