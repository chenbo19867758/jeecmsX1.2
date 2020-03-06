package com.jeecms.common.wechat.api.mp;

import javax.servlet.http.HttpServletRequest;

import com.jeecms.common.response.ResponseInfo;

/**
 * 自动回复
 * <p>Title:NewsApiService</p>
 * @author wulongwei
 * @date 2018年8月1日
 */
public interface NewsApiService {

	/**
	 * 消息回复
	 * @Title: MessageReply  
	 * @param req
	 * @return      
	 * @return: ResponseInfo
	 */
	ResponseInfo messageReply(HttpServletRequest req);

	/**
	 * 文本消息组装
	 * @Title: initText  
	 * @param toUserName 接受用户名
	 * @param fromUserName 发送用户名
	 * @param content 文本内容
	 * @return      
	 * @return: ResponseInfo
	 */
	ResponseInfo initText(String toUserName, String fromUserName, String content);
	
	/**
	 * 音乐消息组装
	 * @Title: initMusicMessage  
	 * @param toUserName 接受用户名
	 * @param fromUserName 发送用户名
	 * @return      
	 * @return: ResponseInfo
	 */
    ResponseInfo initMusicMessage(String toUserName, String fromUserName);
}
