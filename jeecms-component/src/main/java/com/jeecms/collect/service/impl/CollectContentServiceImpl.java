/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.collect.service.impl;

import com.jeecms.collect.dao.CollectContentDao;
import com.jeecms.collect.domain.CollectContent;
import com.jeecms.collect.service.CollectContentService;
import com.jeecms.common.base.service.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 采集内容库Service接口实现
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-07-08
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CollectContentServiceImpl extends BaseServiceImpl<CollectContent, CollectContentDao, Integer>
		implements CollectContentService {

	@Override
	public void deleteAllByTaskId(Integer taskId) {
		dao.deleteAllByTaskId(taskId);
	}

	@Override
	public long countByUsernameUrlTitle(String username, String url, String title) {
		Map<String, String[]> par = new LinkedHashMap<>(4);
		par.put("EQ_createUser_String", new String[]{username});
		par.put("EQ_url_String", new String[]{url});
		par.put("EQ_title_String", new String[]{title});
		return count(par);
	}
}