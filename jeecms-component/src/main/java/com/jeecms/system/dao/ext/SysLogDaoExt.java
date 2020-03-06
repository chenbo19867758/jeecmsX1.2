/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.dao.ext;

import com.jeecms.system.constants.LogConstants.LogGroup;
import com.jeecms.system.constants.LogConstants.StatisticsType;
import com.jeecms.system.domain.vo.SysLogSelectVo;

import java.util.Date;
import java.util.List;

/**
 * 日志扩展Dao接口
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/6/18 15:52
 */
public interface SysLogDaoExt {

	/**
	 * 时间分组
	 *
	 * @param em            小时HOUR 天DAY  月MONTH  年YEAR
	 * @param requestResult 请求结果
	 * @param eventType     时间类型
	 * @param operateType   操作类型
	 * @return list
	 */
	List groupByCreateDate(LogGroup em, Integer requestResult, Integer eventType, Integer operateType);

	/**
	 * 每小时统计
	 *
	 * @param type
	 * @return list
	 */
	List hourlyStatistics(StatisticsType type);

	/**
	 * 获取日志
	 *
	 * @param beginTime 开始时间
	 * @param endTime   结束时间
	 * @return 日志列表
	 */
	List<SysLogSelectVo> getList(Date beginTime, Date endTime);
}
