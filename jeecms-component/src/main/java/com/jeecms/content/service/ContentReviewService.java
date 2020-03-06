package com.jeecms.content.service;

import java.util.List;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.domain.Content;

/**
 * 内容审核service接口
 * @author: tom
 * @date:   2020年1月7日 下午6:01:33
 */
public interface ContentReviewService {
	
	/**
	 * 批量审核内容
	 * @Title: reviewContents
	 * @param contents	内容对象集合
	 * @param userId	用户审核id
	 * @throws GlobalException	全局异常
	 * @return: void
	 */
	void reviewContents(List<Content> contents,Integer userId) throws GlobalException;
	
	/**
	 * 校验该内容是否可以审核
	 * @Title: reviewContentCheck
	 * @param content		内容对象
	 * @param channelId		栏目ID
	 * @param modelId		模型ID
	 * @throws GlobalException	全局异常
	 * @return: boolean
	 */
	boolean reviewContentCheck(Content content,Integer channelId,Integer modelId) throws GlobalException;
	
	/**
	 * 校验用户是否绑定
	 * @Title: checkAppIdOrPhone
	 * @throws GlobalException	全局异常
	 * @return: boolean
	 */
	boolean checkAppIdOrPhone() throws GlobalException;
}
