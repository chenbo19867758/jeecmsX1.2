/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.domain.Content;
import com.jeecms.system.domain.ContentTag;

import java.util.List;

/**
 * tag词service
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-27
 */
public interface ContentTagService extends IBaseService<ContentTag, Integer> {

	/**
	 * 添加多个tag词（分割contentTag.tagName）
	 *
	 * @param contentTag ContentTag
	 * @param siteId     站点Id
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	List<ContentTag> saveBatch(ContentTag contentTag, Integer siteId) throws GlobalException;

	/**
	 * 校验站点下tag词是否可用
	 *
	 * @param tagName tag词名称
	 * @param id      tag词id
	 * @param siteId  站点id
	 * @return true 数据库中不存在，可用 false 数据库中存在，不可用
	 */
	boolean checkTagName(String tagName, Integer id, Integer siteId);
	
	/**
	 * 用于content中初始化tag
	 * @Title: initTags  
	 * @param contentTags	前台传入tag数组
	 * @param siteId	站点id
	 * @return: List
	 */
	List<ContentTag> initTags(String contentTags, Integer siteId);
	
	void deleteTagQuote(List<ContentTag> oldTags, Integer siteId, Content content) throws GlobalException;
}
