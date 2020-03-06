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
import com.jeecms.system.dao.StatisticsLogOperateDao;
import com.jeecms.system.domain.StatisticsLogOperate;
import com.jeecms.system.service.StatisticsLogOperateService;
import com.jeecms.system.service.SysLogService;
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
 * 日志操作类型统计Service实现类
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-06-17
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StatisticsLogOperateServiceImpl extends BaseServiceImpl<StatisticsLogOperate, StatisticsLogOperateDao, Integer> implements StatisticsLogOperateService {

	@Autowired
	private SysLogService logService;

	@Override
	public JSONObject statisticsList(Integer cycle, Date startTime, Date endTime) {
		JSONObject json = new JSONObject();
		List<StatisticsLogOperate> operates = dao.getList(startTime, endTime);
		Map<String, List<StatisticsLogOperate>> map = operates.parallelStream().filter(o -> o.getOperateType() != null)
				.collect(Collectors.groupingBy(o -> LogConstants.operate(Integer.parseInt(o.getOperateType()))));
		List<String> list = Arrays.asList("查询", "新增", "修改", "删除", "导出", "导入", "上传", "下载");
		//横坐标列表
		List<String> fieldList;
		LogConstants.LogGroup logGroup = LogConstants.LogGroup.values()[cycle == null ? 1 : cycle];
		//如果是年，获取年的横坐标
		if (LogConstants.LogGroup.YEAR.equals(logGroup)) {
			Map<String, List<StatisticsLogOperate>> timeMap = operates.parallelStream().filter(o -> o.getStatisticsYear() != null)
					.collect(Collectors.groupingBy(StatisticsLogOperate::getStatisticsYear));
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
			//操作类型存在
			if (set.contains(s)) {
				List<StatisticsLogOperate> sysLogs = map.get(s);
				Map<String, List<StatisticsLogOperate>> collect = getMap(sysLogs, logGroup);
				Set<String> timeSet = collect.keySet();
				for (String time : fieldList) {
					if (timeSet.contains(time)) {
						object.put(time, collect.get(time).parallelStream().mapToInt(StatisticsLogOperate::getCounts).sum());
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
		List list = logService.hourlyStatistics(StatisticsType.OPERATIONTYPE);
		List<StatisticsLogOperate> logOperates = new ArrayList<>(list.size());
		for (Object obj : list) {
			String json = JSONObject.toJSONString(obj).replaceAll("\"", "");
			String[] arr = json.substring(1, json.length() - 1).split(",");
			StatisticsLogOperate logOperate = new StatisticsLogOperate();
			logOperate.setCounts(Integer.parseInt(arr[1]));
			logOperate.setOperateType(arr[0]);
			Date date = Calendar.getInstance().getTime();
			logOperate.setStatisticsYear(MyDateUtils.formatDate(date, MyDateUtils.COM_Y_PATTERN));
			logOperate.setStatisticsMonth(MyDateUtils.formatDate(date, MyDateUtils.COM_Y_M_PATTERN));
			logOperate.setStatisticsDay(MyDateUtils.formatDate(date, MyDateUtils.COM_Y_M_D_PATTERN));
			logOperates.add(logOperate);
		}
		super.saveAll(logOperates);
	}

	/**
	 * 根据时间
	 *
	 * @param sysLogs  数据
	 * @param logGroup 天， 月， 年
	 * @return
	 */
	private Map<String, List<StatisticsLogOperate>> getMap(List<StatisticsLogOperate> sysLogs, LogConstants.LogGroup logGroup) {
		Map<String, List<StatisticsLogOperate>> map;
		switch (logGroup) {
			case DAY:
				map = sysLogs.parallelStream().filter(o -> o.getStatisticsDay() != null)
						.sorted(Comparator.comparing(StatisticsLogOperate::getStatisticsDay))
						.collect(Collectors.groupingBy(StatisticsLogOperate::getStatisticsDay));
				break;
			case MONTH:
				map = sysLogs.parallelStream().filter(o -> o.getStatisticsMonth() != null)
						.sorted(Comparator.comparing(StatisticsLogOperate::getStatisticsMonth))
						.collect(Collectors.groupingBy(StatisticsLogOperate::getStatisticsMonth));
				break;
			case YEAR:
				map = sysLogs.parallelStream().filter(o -> o.getStatisticsYear() != null)
						.sorted(Comparator.comparing(StatisticsLogOperate::getStatisticsYear))
						.collect(Collectors.groupingBy(StatisticsLogOperate::getStatisticsYear));
				break;
			default:
				map = sysLogs.parallelStream().filter(o -> o.getStatisticsDay() != null)
						.sorted(Comparator.comparing(StatisticsLogOperate::getStatisticsDay))
						.collect(Collectors.groupingBy(StatisticsLogOperate::getStatisticsDay));
				break;
		}
		return map;
	}
}