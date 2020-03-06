/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.system.constants.LogConstants.StatisticsType;
import com.jeecms.system.dao.StatisticsLogUserDao;
import com.jeecms.system.domain.StatisticsLogUser;
import com.jeecms.system.service.StatisticsLogUserService;
import com.jeecms.system.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019-06-17
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StatisticsLogUserServiceImpl extends BaseServiceImpl<StatisticsLogUser, StatisticsLogUserDao, Integer> implements StatisticsLogUserService {

	@Autowired
	private SysLogService logService;

	@Override
	public JSONArray statisticsList(Boolean isToday, Date startTime, Date endTime) {
		JSONArray jsonArray = new JSONArray();
		List list = dao.statisticsList(startTime, endTime, isToday);
		for (Object obj : list) {
			JSONObject jsonObject = new JSONObject();
			String s = JSONObject.toJSONString(obj).replaceAll("\"", "");
			String[] arr = s.substring(1, s.length() - 1).split(",");
			jsonObject.put("type", arr[0]);
			jsonObject.put("日志条数", Integer.valueOf(arr[1]));
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}

	@Override
	public void save() throws GlobalException {
		List list = logService.hourlyStatistics(StatisticsType.USERNAME);
		List<StatisticsLogUser> logUsers = new ArrayList<>(list.size());
		for (Object obj : list) {
			String json = JSONObject.toJSONString(obj).replaceAll("\"", "");
			String[] arr = json.substring(1, json.length() - 1).split(",");
			StatisticsLogUser logUser = new StatisticsLogUser();
			logUser.setCounts(Integer.parseInt(arr[1]));
			logUser.setOperateUser(arr[0]);
			Date date = Calendar.getInstance().getTime();
			logUser.setStatisticsYear(MyDateUtils.formatDate(date, MyDateUtils.COM_Y_PATTERN));
			logUser.setStatisticsMonth(MyDateUtils.formatDate(date, MyDateUtils.COM_Y_M_PATTERN));
			logUser.setStatisticsDay(MyDateUtils.formatDate(date, MyDateUtils.COM_Y_M_D_PATTERN));
			logUsers.add(logUser);
		}
		super.saveAll(logUsers);
	}
}