/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.domain.StatisticsLogOperate;
import com.jeecms.common.base.service.IBaseService;

import java.util.Date;

/**
 * 日志操作类型统计Service
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-06-17
 */
public interface StatisticsLogOperateService extends IBaseService<StatisticsLogOperate, Integer> {

	/**
	 * 日志操作类型统计数据
	 *
	 * @param cycle     0小时 1天 2月 3年
	 * @param startTime 开始事件
	 * @param endTime   结束事件
	 * @return JSONObject
	 */
	JSONObject statisticsList(Integer cycle, Date startTime, Date endTime);

	/**
	 * 添加统计数据
	 *
	 * @throws GlobalException 异常
	 */
	void save() throws GlobalException;

}
