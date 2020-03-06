/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.statistics;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.statistics.domain.StatisticsAccessPage;
import com.jeecms.statistics.domain.vo.StatisticsAccessPageVo;
import com.jeecms.statistics.domain.vo.StatisticsEntryPageVo;
import com.jeecms.statistics.domain.vo.StatisticsPageListVo;
import com.jeecms.statistics.service.StatisticsAccessPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;

/**
 * 受访分析
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-07-02
 */
@RequestMapping("/statisticsAccessPage")
@RestController
public class StatisticsAccessPageController extends BaseController<StatisticsAccessPage, Integer> {

	@Autowired
	private StatisticsAccessPageService service;

	/**
	 * 受访分析（受访页面）
	 *
	 * @param beginTime    开始时间
	 * @param endTime      结束时间
	 * @param siteId       站点id
	 * @param newVisitor   true 新客户 false 老客户
	 * @param sorceUrlType 来源网站类型 （1-搜索引擎 2-外部链接 3-直接访问）
	 * @param url          地址
	 * @param sortType     0 PV正序 1 PV倒序 2 UV正序 3 UV倒序 4 贡献下游浏览量正序 5 贡献下游浏览量倒序 6 平均停留时长正序 7 平均停留时长倒序
	 * @return StatisticsAccessPageVo
	 */
	@GetMapping("/accessPage")
	@MoreSerializeField({@SerializeField(clazz = StatisticsAccessPageVo.class,
			includes = {"pvs", "uvs", "pageViews", "average", "averageTime", "page"}),
			@SerializeField(clazz = StatisticsPageListVo.class,
					includes = {"pvs", "uvs", "pageViews", "average", "averageTime", "url", "proportion"})})
	public ResponseInfo accessPage(Long beginTime, Long endTime, Integer siteId,
								   Boolean newVisitor, Integer sorceUrlType, String url,
								   Integer sortType, Pageable pageable) throws IllegalAccessException {
		sortType = sortType == null ? 1 : sortType;
		Date beginDate = Calendar.getInstance().getTime();
		Date endDate = beginDate;
		if (beginTime != null) {
			beginDate = new Date(beginTime);
		}
		if (beginTime != null) {
			endDate = new Date(endTime);
		}
		StatisticsAccessPageVo vo =
				service.accessPage(MyDateUtils.getStartDate(beginDate), MyDateUtils.getFinallyDate(endDate),
						siteId, newVisitor, sorceUrlType, sortType, url, pageable);
		return new ResponseInfo(vo);
	}

	/**
	 * 受访分析（入口页面）
	 *
	 * @param beginTime 开始时间
	 * @param endTime   结束时间
	 * @param siteId    站点id
	 * @param sortType  0 PV正序 1 PV倒序 2 UV正序 3 UV倒序 4 贡献下游浏览量正序 5 贡献下游浏览量倒序 6 跳出率正序 7 跳出率倒序 8 平均访问时长正序 9 平均访问时长倒序
	 * @param url       地址
	 * @return StatisticsAccessPageVo
	 */
	@GetMapping("/entrancePage")
	@MoreSerializeField({@SerializeField(clazz = StatisticsEntryPageVo.class,
			includes = {"pvs", "uvs", "pageViews", "average", "bounce", "averageTime", "page"}),
			@SerializeField(clazz = StatisticsPageListVo.class,
					includes = {"pvs", "uvs", "pageViews", "average", "averageTime", "bounce", "url", "proportion"})})
	public ResponseInfo entrancePage(Long beginTime, Long endTime, Integer siteId, String url,
									 Integer sortType, Pageable pageable) throws IllegalAccessException {
		sortType = sortType == null ? 1 : sortType;
		Date beginDate = Calendar.getInstance().getTime();
		Date endDate = beginDate;
		if (beginTime != null) {
			beginDate = new Date(beginTime);
		}
		if (beginTime != null) {
			endDate = new Date(endTime);
		}
		StatisticsEntryPageVo vo =
				service.entrancePage(MyDateUtils.getStartDate(beginDate),
						MyDateUtils.getFinallyDate(endDate), siteId, sortType, url, pageable);
		return new ResponseInfo(vo);
	}

}



