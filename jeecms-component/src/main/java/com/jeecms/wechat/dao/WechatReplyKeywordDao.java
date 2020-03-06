package com.jeecms.wechat.dao;

import java.util.List;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.wechat.dao.ext.WechatReplyKeywordDaoExt;
import com.jeecms.wechat.domain.WechatReplyKeyword;


/**
* @author ASUS
* @version 1.0
* @date 2018-08-08
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/
public interface WechatReplyKeywordDao extends IBaseDao<WechatReplyKeyword, Integer>,WechatReplyKeywordDaoExt {
	
	/**
	 * 根据AppId查询出关键字
	 * @Title: findByAppIdAndHasDeleted  
	 * @param appId
	 * @param hasDeleted 是否删除
	 * @return
	 * @throws GlobalException      
	 * @return: List<WechatReplyKeyword>
	 */
	List<WechatReplyKeyword> findByAppIdAndHasDeleted(String appId,Boolean hasDeleted)throws GlobalException;

	/**
	 * 通过回复内容的ID查询关键词
	 * @Title: findByAppIdAndReplyContentIdAndHasDeleted  
	 * @param appId
	 * @param replyContentId 回复内容ID
	 * @param hasDeleted 是否删除
	 * @author: wulongwei
	 * @date: 2018年9月6日 下午3:57:48
	 * @return
	 * @throws GlobalException 全局异常     
	 * @return: List<WechatReplyKeyword>
	 */
	List<WechatReplyKeyword> findByAppIdAndReplyContentIdAndHasDeleted(String appId,int replyContentId,Boolean hasDeleted)throws GlobalException;
}
