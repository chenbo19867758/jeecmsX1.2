package com.jeecms.wechat.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.wechat.domain.WechatReplyContent;

/**
* @author ASUS
* @version 1.0
* @date 2018-08-08
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/
public interface WechatReplyContentService extends IBaseService<WechatReplyContent, Integer>{

	/**
	 * 获取单个关键词
	 * @param id
	 * @return
	 */
	WechatReplyContent getId(Integer id);

	/**
	 * 禁用按钮
	 * @Title: isEnable  
	 * @param id
	 * @param isEnable
	 * @return
	 * @throws GlobalException      
	 * @return: ResponseInfo
	 * @author: wulongwei
	 * @date: 2018年8月31日 下午5:04:15
	 */
	ResponseInfo isEnable(Integer id, Boolean isEnable)throws GlobalException;
	
	/**
	 * 修改关键词
	 * @Title: updateKeyWord  
	 * @param content
	 * @return
	 * @throws GlobalException      
	 * @return: ResponseInfo
	 * @author: wulongwei
	 * @date: 2018年9月1日 下午2:38:07
	 */
	ResponseInfo updateKeyWord(WechatReplyContent content)throws GlobalException;
	
	/**
	 * 删除关键词
	 * 
	 * @param ids
	 * @return
	 * @throws GlobalException
	 */
	ResponseInfo deleteKeyWork(Integer[] ids) throws GlobalException;
	
	Page<WechatReplyContent> getPage(String appId,Pageable pageable,String searchStr) throws GlobalException;
}
