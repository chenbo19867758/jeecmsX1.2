package com.jeecms.wechat.service;

import java.util.List;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.wechat.bean.request.mp.news.ReqMessage;
import com.jeecms.wechat.domain.WechatReplyKeyword;

/**
 * 关键词service接口
 * 
 * @author: chenming
 * @date: 2019年6月3日 上午9:43:52
 */
public interface WechatReplyKeywordService extends IBaseService<WechatReplyKeyword, Integer> {

	/**
	 * 消息回复
	 * @Title: getMessageReply  
	 * @param reqMessage	用户发送的对象
	 * @param appId		公众号appId
	 * @throws GlobalException    全局异常  
	 * @return: String
	 */
	String getMessageReply(ReqMessage reqMessage, String appId) throws GlobalException;
	
	/**
	 * 新增关键字回复对象
	 * @Title: saveKeyWork  
	 * @param keyword	回复对象
	 * @throws GlobalException   全局异常   
	 * @return: ResponseInfo
	 */
	ResponseInfo saveKeyWork(WechatReplyKeyword keyword) throws GlobalException;

	/**
	 * 修改关键字回复对象
	 * @Title: updateKeyWork  
	 * @param keyword	回复对象
	 * @throws GlobalException   全局异常   
	 * @return: void
	 */
	void updateKeyWork(WechatReplyKeyword keyword) throws GlobalException;

	/**
	 * 通过公众号appId查询添加的所有关键字回复对象
	 * @Title: findByAppId  
	 * @param appId	公众号appid
	 * @throws GlobalException 	全局异常     
	 * @return: List
	 */
	List<WechatReplyKeyword> findByAppId(String appId) throws GlobalException;
}
