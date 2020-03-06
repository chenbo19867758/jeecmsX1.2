/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.statistics.dao.ext;

import com.jeecms.statistics.domain.StatisticsAccessPage;

import java.util.Date;
import java.util.List;

/**
 * 受访分析扩展Dao
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/5 9:32
 */

public interface StatisticsAccessPageDaoExt {

	/**
	 * 获取受访分析数据
	 *
	 * @param beginTime    开始时间
	 * @param endTime      结束时间
	 * @param siteId       站点id
	 * @param sorceUrlType 访问类型
	 * @param urlType      1 访问页面 2入口页面
	 * @param newVisitor   true 新客户 false 老客户
	 * @return List
	 */
	List<StatisticsAccessPage> getList(Date beginTime, Date endTime, Integer siteId, Integer sorceUrlType,
									   Boolean newVisitor, int urlType);
}
