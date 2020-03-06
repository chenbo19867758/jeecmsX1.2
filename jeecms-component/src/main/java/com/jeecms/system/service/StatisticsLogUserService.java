/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service;

import com.alibaba.fastjson.JSONArray;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.domain.StatisticsLogUser;

import java.util.Date;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019-06-17
 */
public interface StatisticsLogUserService extends IBaseService<StatisticsLogUser, Integer> {

	/**
	 * 日志操作类型统计数据
	 *
	 * @param isToday   true今日统计 false历史统计
	 * @param startTime 开始事件
	 * @param endTime   结束事件
	 * @return JSONArray
	 */
	JSONArray statisticsList(Boolean isToday, Date startTime, Date endTime);

	/**
	 * 添加统计数据
	 *
	 * @throws GlobalException 异常
	 */
	void save() throws GlobalException;

}
