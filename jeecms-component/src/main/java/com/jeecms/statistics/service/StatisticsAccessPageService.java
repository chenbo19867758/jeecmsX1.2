/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.statistics.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.statistics.domain.StatisticsAccessPage;
import com.jeecms.statistics.domain.vo.StatisticsAccessPageVo;
import com.jeecms.statistics.domain.vo.StatisticsEntryPageVo;
import org.springframework.data.domain.Pageable;

import java.util.Date;

/**
 * 受访分析Service
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-07-02
 */
public interface StatisticsAccessPageService extends IBaseService<StatisticsAccessPage, Integer> {

	/**
	 * 统计昨日受访分析数据
	 *
	 * @throws GlobalException 异常
	 */
	void statisticsAccessPage() throws GlobalException;

	/**
	 * 受访分析（受访页面）
	 *
	 * @param beginTime    开始时间
	 * @param endTime      结束时间
	 * @param siteId       站点id
	 * @param newVisitor   true 新客户 false 老客户
	 * @param url          筛选地址
	 * @param sorceUrlType 来源网站类型 （1-搜索引擎 2-外部链接 3-直接访问）
	 * @param sortType     0 PV正序 1 PV倒序 2 UV正序 3 UV倒序 4 贡献下游浏览量正序 5 贡献下游浏览量倒序 6 平均停留时长正序 7 平均停留时长倒序
	 * @param pageable     分页
	 * @return StatisticsAccessPageVo
	 * @throws IllegalAccessException 异常
	 */
	StatisticsAccessPageVo accessPage(Date beginTime, Date endTime, Integer siteId, Boolean newVisitor,
									  Integer sorceUrlType, int sortType, String url, Pageable pageable) throws IllegalAccessException;

	/**
	 * 受访分析（入口页面）
	 *
	 * @param beginTime 开始时间
	 * @param endTime   结束时间
	 * @param siteId    站点id
	 * @param url       筛选地址
	 * @param sortType  0 PV正序 1 PV倒序 2 UV正序 3 UV倒序 4 贡献下游浏览量正序 5 贡献下游浏览量倒序 6 跳出率正序 7 跳出率倒序 8 平均访问时长正序 9 平均访问时长倒序
	 * @param pageable  分页
	 * @return StatisticsAccessPageVo
	 * @throws IllegalAccessException 异常
	 */
	StatisticsEntryPageVo entrancePage(Date beginTime, Date endTime,
									   Integer siteId, int sortType, String url,
									   Pageable pageable) throws IllegalAccessException;
}
