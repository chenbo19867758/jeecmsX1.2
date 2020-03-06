/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.collect.service;

import com.jeecms.collect.domain.CollectContent;
import com.jeecms.common.base.service.IBaseService;

/**
 * 采集内容库Service接口
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-07-08
 */
public interface CollectContentService extends IBaseService<CollectContent, Integer> {

	/**
	 * 删除任务下所有数据
	 *
	 * @param taskId 任务id
	 */
	void deleteAllByTaskId(Integer taskId);


	long countByUsernameUrlTitle(String username, String url, String title);
}
