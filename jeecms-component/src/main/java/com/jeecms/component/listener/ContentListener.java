/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.component.listener;

import java.util.List;
import java.util.Map;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.domain.Content;

/**
 * 内容监听器
 * 
 * @author: tom
 * @date: 2019年4月8日 上午9:30:09
 */
public interface ContentListener {
	/**
	 * 内容是否发布
	 */
	public static final String CONTENT_PUBLISH = "contentPublish";
	/**
         * 内容是否删除(含删除、回收站、归档)
         */
        public static final String CONTENT_IS_DEL = "contentDelete";

	/**
	 * 内容删除之后
	 * 
	 * @Title: afterDelete
	 * @param content 内容
	 */
	void afterDelete(List<Content> contents) throws GlobalException;

	/**
	 * 内容保存之后
	 * 
	 * @Title: afterSave
	 * @param content 内容
	 */
	void afterSave(Content content) throws GlobalException;

	/**
	 * 修改之前执行
	 * 
	 * @param content 修改前的Content
	 * @return 获取一些需要在afterChange中使用的值。
	 */
	Map<String, Object> preChange(Content content);

	/**
	 * 修改之后执行
	 * 
	 * @param content 修改后的Content
	 * @param map     从{@link #preChange(Content)}方法返回的值。 
	 */
	void afterChange(Content content, Map<String, Object> map) throws GlobalException;
	
	/**
	 * 内容加入回收站之后执行
	 * @Title: afterContentRecycle  
	 * @param contentIds	内容id集合
	 * @throws GlobalException     全局异常 
	 * @return: void
	 */
	void afterContentRecycle(List<Integer> contentIds) throws GlobalException;
}
