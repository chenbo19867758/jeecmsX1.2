/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.system.constants.LogConstants;
import com.jeecms.system.constants.LogConstants.StatisticsType;
import com.jeecms.system.dao.StatisticsLogClickDao;
import com.jeecms.system.domain.StatisticsLogClick;
import com.jeecms.system.service.StatisticsLogClickService;
import com.jeecms.system.service.SysLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 日志事件类型统计Service实现
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-06-17
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StatisticsLogClickServiceImpl extends BaseServiceImpl<StatisticsLogClick, StatisticsLogClickDao, Integer> implements StatisticsLogClickService {

	private static Logger logger = LoggerFactory.getLogger(StatisticsLogClickServiceImpl.class);

	@Autowired
	private SysLogService logService;

	@Override
	public JSONObject statisticsList(Integer cycle, Date startTime, Date endTime) {
		JSONObject json = new JSONObject();
		List<StatisticsLogClick> operates = dao.getList(startTime, endTime);
		Map<String, List<StatisticsLogClick>> map = operates.parallelStream().filter(o -> o.getClickType() != null)
				.collect(Collectors.groupingBy(o -> LogConstants.eventType(o.getClickType())));
		List<String> list = Arrays.asList("系统事件", "业务事件");
		//横坐标列表
		List<String> fieldList;
		LogConstants.LogGroup logGroup = LogConstants.LogGroup.values()[cycle == null ? 1 : cycle];
		//如果是年，获取年的横坐标
		if (LogConstants.LogGroup.YEAR.equals(logGroup)) {
			Map<String, List<StatisticsLogClick>> timeMap = operates.parallelStream().filter(o -> o.getStatisticsYear() != null)
					.collect(Collectors.groupingBy(StatisticsLogClick::getStatisticsYear));
			fieldList = new ArrayList<String>(timeMap.keySet());
			if (fieldList.size() <= 0) {
				fieldList.add(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
			}
		} else {
			fieldList = LogConstants.getTimeList(logGroup, startTime, endTime);
		}
		Set<String> set = map.keySet();
		JSONArray array = new JSONArray();
		//遍历操作类型
		for (String s : list) {
			JSONObject object = new JSONObject();
			object.put("name", s);
			//操作结果存在
			if (set.contains(s)) {
				List<StatisticsLogClick> sysLogs = map.get(s);
				Map<String, List<StatisticsLogClick>> collect = getMap(sysLogs, logGroup);
				Set<String> timeSet = collect.keySet();
				for (String time : fieldList) {
					if (timeSet.contains(time)) {
						object.put(time, collect.get(time).parallelStream().mapToInt(StatisticsLogClick::getCounts).sum());
					} else {
						object.put(time, 0);
					}
				}
			} else {
				for (String time : fieldList) {
					object.put(time, 0);
				}
			}
			array.add(object);
		}
		json.put("data", array);
		json.put("fields", fieldList.toArray());
		return json;
	}

	@Override
	public void save() throws GlobalException {
		List list = logService.hourlyStatistics(StatisticsType.EVENTTYPE);
		List<StatisticsLogClick> logClicks = new ArrayList<>(list.size());
		for (Object obj : list) {
			String json = JSONObject.toJSONString(obj).replaceAll("\"", "");
			String[] arr = json.substring(1, json.length() - 1).split(",");
			StatisticsLogClick logClick = new StatisticsLogClick();
			logClick.setCounts(Integer.parseInt(arr[1]));
			logClick.setClickType(Integer.parseInt(arr[0]));
			Date date = Calendar.getInstance().getTime();
			logClick.setStatisticsYear(MyDateUtils.formatDate(date, MyDateUtils.COM_Y_PATTERN));
			logClick.setStatisticsMonth(MyDateUtils.formatDate(date, MyDateUtils.COM_Y_M_PATTERN));
			logClick.setStatisticsDay(MyDateUtils.formatDate(date, MyDateUtils.COM_Y_M_D_PATTERN));
			logClicks.add(logClick);
		}
		super.saveAll(logClicks);
	}

	/**
	 * 根据时间
	 *
	 * @param sysLogs  数据
	 * @param logGroup 天， 月， 年
	 * @return
	 */
	private Map<String, List<StatisticsLogClick>> getMap(List<StatisticsLogClick> sysLogs, LogConstants.LogGroup logGroup) {
		Map<String, List<StatisticsLogClick>> map;
		switch (logGroup) {
			case DAY:
				map = sysLogs.parallelStream().filter(o -> o.getStatisticsDay() != null)
						.sorted(Comparator.comparing(StatisticsLogClick::getStatisticsDay))
						.collect(Collectors.groupingBy(StatisticsLogClick::getStatisticsDay));
				break;
			case MONTH:
				map = sysLogs.parallelStream().filter(o -> o.getStatisticsMonth() != null)
						.sorted(Comparator.comparing(StatisticsLogClick::getStatisticsMonth))
						.collect(Collectors.groupingBy(StatisticsLogClick::getStatisticsMonth));
				break;
			case YEAR:
				map = sysLogs.parallelStream().filter(o -> o.getStatisticsYear() != null)
						.sorted(Comparator.comparing(StatisticsLogClick::getStatisticsYear))
						.collect(Collectors.groupingBy(StatisticsLogClick::getStatisticsYear));
				break;
			default:
				map = sysLogs.parallelStream().filter(o -> o.getStatisticsDay() != null)
						.sorted(Comparator.comparing(StatisticsLogClick::getStatisticsDay))
						.collect(Collectors.groupingBy(StatisticsLogClick::getStatisticsDay));
				break;
		}
		return map;
	}
}