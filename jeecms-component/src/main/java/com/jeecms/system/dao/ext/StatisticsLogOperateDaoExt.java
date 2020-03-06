/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.dao.ext;

import com.jeecms.system.domain.StatisticsLogOperate;

import java.util.Date;
import java.util.List;


/**
 * 日志操作类型统计扩展Dao接口
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-06-19
 */
public interface StatisticsLogOperateDaoExt {

	/**
	 * 获取数据
	 *
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return
	 */
	List<StatisticsLogOperate> getList(Date startTime, Date endTime);
}
