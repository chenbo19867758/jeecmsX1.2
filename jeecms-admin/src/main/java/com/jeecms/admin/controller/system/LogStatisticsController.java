/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.system;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.system.domain.SysLog;
import com.jeecms.system.service.StatisticsLogClickService;
import com.jeecms.system.service.StatisticsLogOperateService;
import com.jeecms.system.service.StatisticsLogResultService;
import com.jeecms.system.service.StatisticsLogUserService;
import com.jeecms.system.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Date;

/**
 * 日志统计Controller
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/6/17 14:15
 */

@RequestMapping("/log/statistics")
@RestController
public class LogStatisticsController extends BaseController<SysLog, Integer> {

	@Autowired
	private StatisticsLogClickService logClickService;
	@Autowired
	private StatisticsLogOperateService logOperateService;
	@Autowired
	private StatisticsLogResultService logResultService;
	@Autowired
	private StatisticsLogUserService logUserService;
	@Autowired
	private SysLogService service;

	@PostConstruct
	public void init() {
		String[] queryParams = {};
		super.setQueryParams(queryParams);
	}

	/**
	 * 操作结果统计
	 *
	 * @param cycle     0小时 1天 2月 3年
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return ResponseInfo
	 */
	@GetMapping("/result")
	public ResponseInfo operationResult(Integer cycle, Date startTime, Date endTime) {
		JSONObject object;
		boolean isNow = true;
		boolean onlyDay = false;
		if (startTime != null && endTime != null) {
			isNow = MyDateUtils.isInDate(endTime, Calendar.getInstance().getTime());
			onlyDay = MyDateUtils.getDaysBetweenDate(startTime, endTime) != 0;
		}
		/* 1、不是当天走历史记录
		 * 2、不是同一天走历史记录
		 */
		cycle = cycle == null ? 0 : cycle;
		if (!isNow || onlyDay || cycle.equals(2) || cycle.equals(3)) {
			object = logResultService.statisticsList(cycle, startTime, endTime);
		} else {
			object = service.statistics(startTime, endTime, 0, cycle, null);
		}
		return new ResponseInfo(object);
	}

	/**
	 * 事件类型统计
	 *
	 * @param cycle     0小时 1天 2月 3年
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return ResponseInfo
	 */
	@GetMapping("/eventType")
	public ResponseInfo eventType(Integer cycle, Date startTime, Date endTime) {
		JSONObject object;
		boolean isNow = true;
		boolean onlyDay = false;
		if (startTime != null && endTime != null) {
			isNow = MyDateUtils.isInDate(endTime, Calendar.getInstance().getTime());
			onlyDay = MyDateUtils.getDaysBetweenDate(startTime, endTime) != 0;
		}
		/* 1、不是当天走历史记录
		 * 2、不是同一天走历史记录
		 */
		cycle = cycle == null ? 0 : cycle;
		if (!isNow || onlyDay || cycle.equals(3) || cycle.equals(2)) {
			object = logClickService.statisticsList(cycle, startTime, endTime);
		} else {
			object = service.statistics(startTime, endTime, 1, cycle, null);
		}
		return new ResponseInfo(object);
	}

	/**
	 * 操作类型统计
	 *
	 * @param cycle     0小时 1天 2月 3年
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return ResponseInfo
	 */
	@GetMapping("/operationType")
	public ResponseInfo operationType(Integer cycle, Date startTime, Date endTime) {
		JSONObject object;
		boolean isNow = true;
		boolean onlyDay = false;
		if (startTime != null && endTime != null) {
			isNow = MyDateUtils.isInDate(endTime, Calendar.getInstance().getTime());
			onlyDay = MyDateUtils.getDaysBetweenDate(startTime, endTime) != 0;
		}
		/* 1、不是当天走历史记录
		 * 2、不是同一天走历史记录
		 */
		cycle = cycle == null ? 0 : cycle;
		if (!isNow || onlyDay || cycle.equals(2) || cycle.equals(3)) {
			object = logOperateService.statisticsList(cycle, startTime, endTime);
		} else {
			object = service.statistics(startTime, endTime, 2, cycle, null);
		}
		return new ResponseInfo(object);
	}

	/**
	 * 用户名
	 *
	 * @param isToday   true 今日统计 历史统计
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return ResponseInfo
	 */
	@GetMapping("/username")
	public ResponseInfo username(Boolean isToday, Date startTime, Date endTime) {
		JSONArray jsonArray;
		boolean isNow = true;
		boolean onlyDay = false;
		if (startTime != null && endTime != null) {
			isNow = MyDateUtils.isInDate(endTime, Calendar.getInstance().getTime());
			onlyDay = MyDateUtils.getDaysBetweenDate(startTime, endTime) != 0;
		}
		/* 1、不是当天走历史记录
		 * 2、不是同一天走历史记录
		 */
		isToday = isToday == null ? true : isToday;
		if (!isNow || onlyDay || !isToday) {
			jsonArray = logUserService.statisticsList(isToday, startTime, endTime);
		} else {
			JSONObject object = service.statistics(startTime, endTime, 3, 0, isToday);
			jsonArray = object.getJSONArray("data");
		}

		return new ResponseInfo(jsonArray);
	}

}
