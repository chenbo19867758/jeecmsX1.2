/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.statistics.dao.ext;

import com.jeecms.statistics.domain.StatisticsSource;

import java.util.Date;
import java.util.List;

/**
 * 来源分析扩展Dao
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/5 14:18
 */

public interface StatisticsSourceDaoExt {

	/**
	 * 获取来源分析历史数据
	 *
	 * @param beginTime         开始时间
	 * @param endTime           结束时间
	 * @param siteId            站点id
	 * @param newVisitor        true 新客户 false 老客户
	 * @param visitorDeviceType 访客设备类型（1-计算机   2-移动设备）
	 * @param sorceUrlType      来源网站类型 （1-搜索引擎  2-外部链接  3-直接访问）
	 * @return List
	 */
	List<StatisticsSource> getList(Date beginTime, Date endTime, Integer siteId,
								   Boolean newVisitor,
								   Integer visitorDeviceType,
								   Integer sorceUrlType);
}
