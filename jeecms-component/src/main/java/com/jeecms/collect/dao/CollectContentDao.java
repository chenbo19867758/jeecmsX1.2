/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.collect.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.collect.domain.CollectContent;
import org.springframework.data.jpa.repository.Query;


/**
 * 采集内容库Dao接口
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-07-08
 */
public interface CollectContentDao extends IBaseDao<CollectContent, Integer> {

	/**
	 * 删除任务下所有数据
	 *
	 * @param taskId 任务id
	 */
	@Query("delete from CollectContent bean where bean.taskId = ?1")
	void deleteAllByTaskId(Integer taskId);
}
