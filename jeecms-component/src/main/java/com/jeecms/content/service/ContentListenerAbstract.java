/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.service;


import com.jeecms.common.exception.GlobalException;
import com.jeecms.component.listener.ContentListener;
import com.jeecms.content.domain.Content;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ContentListener的抽象实现
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/31
 */
public class ContentListenerAbstract implements ContentListener {

	@Override
	public void afterDelete(List<Content> contents) throws GlobalException {

	}

	@Override
	public void afterSave(Content content) throws GlobalException {

	}

	@Override
	public Map<String, Object> preChange(Content content) {
		Map<String, Object> map = new HashMap<String, Object>(1);
		map.put(CONTENT_PUBLISH, content.isPublish());
		map.put(CONTENT_IS_DEL, content.isDelete());
		return map;
	}

	@Override
	public void afterChange(Content content, Map<String, Object> map) throws GlobalException {

	}

	@Override
	public void afterContentRecycle(List<Integer> contentIds) throws GlobalException {
		
	}
}
