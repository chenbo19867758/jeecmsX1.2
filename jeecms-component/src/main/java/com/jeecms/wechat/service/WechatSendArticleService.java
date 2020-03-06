/**
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.wechat.service;

import com.jeecms.wechat.domain.WechatSendArticle;

import java.util.Date;
import java.util.List;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;

/**
 * 群发成功文章Service
* @author ljw
* @version 1.0
* @date 2019-06-04
*/
public interface WechatSendArticleService extends IBaseService<WechatSendArticle, Integer> {

	/**
	 *根据APPIDs 获取文章列表
	* @Title: getArticles 
	* @param appids 微信公众号IDs
	* @param start 开始时间 
	* @param end 结束时间
	* @return List
	* @throws GlobalException 异常
	 */
	List<WechatSendArticle> getArticles(List<String> appids, Date start, Date end) throws GlobalException;
	
	/**
	 * 根据群发ID获取文章列表
	* @Title: getArticles 
	* @param msgDatas 群发IDs
	* @param start 开始时间 
	* @param end 结束时间
	* @return List
	* @throws GlobalException 异常
	 */
	List<WechatSendArticle> getArticlesBySend(List<String> msgDatas, Date start, Date end) throws GlobalException;

	/**
	 * 查询单个文章
	* @Title: findArticle 
	* @param msgDataId 群发ID
	* @param index 序号
	* @return List
	* @throws GlobalException 异常
	 */
	WechatSendArticle findArticle(String msgDataId, Integer index) throws GlobalException;
	
	/**
	 * 处理已读
	* @Title: findArticle 
	* @param msgDataId 群发ID
	* @param index 序号
	* @throws GlobalException 异常
	 */
	void read(String msgDataId, Integer index) throws GlobalException;
}
