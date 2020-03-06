package com.jeecms.wechat.service;

import java.util.List;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.wechat.domain.WechatReplyClick;

/**
 * 事件触发service接口
 * @author: chenming
 * @date:   2019年6月13日 下午3:29:34
 */
public interface WechatReplyClickService extends IBaseService<WechatReplyClick, Integer> {

	/**
	 * 通过回复类型和公众号appId进行查询
	 * @Title: findByReplyTypeAndAppId  
	 * @param replyType	回复类型(1-关注后自动回复 2-默认回复)
	 * @param appid	公众号appId
	 * @throws GlobalException   全局异常   
	 * @return: WechatReplyClick
	 */
	WechatReplyClick findByReplyTypeAndAppId(Integer replyType,String appid)throws GlobalException;
	
	/**
	 * 添加默认回复、关注回复关键词
	 * @Title: saveKeyWord  
	 * @param wechatReplyClick	事件触发实体类
	 * @throws GlobalException  全局异常    
	 * @return: void
	 */
	void saveKeyWord(WechatReplyClick wechatReplyClick)throws GlobalException;
	
	/**
	 * 修改默认回复、关注回复关键词
	 * @Title: updateKeyWord  
	 * @param wechatReplyClick	事件触发实体类
	 * @throws GlobalException   全局异常   
	 * @return: void
	 */
	void updateKeyWord(WechatReplyClick wechatReplyClick) throws GlobalException;
	
	List<Integer> findByAppId(String appId);
}
