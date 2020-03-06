package com.jeecms.wechat.dao;

import java.util.List;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.wechat.domain.WechatReplyClick;

/**
 * 事件触发dao接口
 * 
 * @author: chenming
 * @date: 2019年6月13日 下午3:28:05
 */
public interface WechatReplyClickDao extends IBaseDao<WechatReplyClick, Integer> {

	/**
	 * 通过appId和回复类型进行检索查询
	 * @Title: findByReplyTypeAndAppIdAndHasDeleted
	 * @param replyType	回复类型(1-关注后自动回复 2-默认回复) 
	 * @param appid	公众号appId
	 * @param hasDeleted	删除标识
	 * @return: WechatReplyClick
	 */
	WechatReplyClick findByReplyTypeAndAppIdAndHasDeleted(Integer replyType, String appid, Boolean hasDeleted);
	
	/**
	 * 通过appId进行检索查询
	 * @Title: findByAppIdAndHasDeleted  
	 * @param appId	公众号appid
	 * @param hasDeleted	删除标识
	 * @return: List
	 */
	List<WechatReplyClick> findByAppIdAndHasDeleted(String appId,Boolean hasDeleted);
}
