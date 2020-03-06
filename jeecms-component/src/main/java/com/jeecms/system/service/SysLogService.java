/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.constants.LogConstants;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.SysLog;

import java.util.Date;
import java.util.List;

/**
 * 日志Service
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-05-29 16:53:01
 */
public interface SysLogService extends IBaseService<SysLog, Integer> {


	JSONObject statistics(Date beginTime, Date endTime, int statisticsType, int timeCategory, Boolean isToday);

	/**
	 * 根据时间分组
	 *
	 * @param type          小时HOUR 天DAY  月MONTH  年YEAR
	 * @param requestResult 请求结果
	 * @param eventType     事件类型
	 * @param operateType   操作类型
	 * @return JSONArray
	 * @throws GlobalException 异常
	 */
	JSONArray groupByCreateDate(LogConstants.LogGroup type, Integer requestResult, Integer eventType,
								Integer operateType) throws GlobalException;

	/**
	 * 当前小时数据数统计
	 *
	 * @param type 统计类型
	 * @return List
	 */
	@SuppressWarnings("rawtypes")
	List hourlyStatistics(LogConstants.StatisticsType type);

	/**
	 * 异步记录请求日志
	 *
	 * @param method   请求方法 get post....
	 * @param log
	 * @param info
	 * @param coreUser
	 * @param site
	 */
	void asyncLog(String method, SysLog log, ResponseInfo info, CoreUser coreUser, CmsSite site);
}
