package com.jeecms.statistics.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.statistics.service.TimeCuttingService;

/**
 * 时间切割service实现类
 * @author: chenming
 * @date:   2019年7月4日 下午2:10:36
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TimeCuttingServiceImpl implements TimeCuttingService{

	public List<Date> getIntervalTimeList(Date startDate, Date endDate, int interval) {
		List<Date> list = new ArrayList<>();
		Date oldStartDate = startDate;
		while (startDate.getTime() <= endDate.getTime()) {
			if (!startDate.equals(oldStartDate)) {
				list.add(startDate);
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);
			calendar.add(Calendar.MINUTE, interval);
			if (calendar.getTime().getTime() > endDate.getTime()) {
				if (!startDate.equals(endDate)) {
					list.add(endDate);
				}
				startDate = calendar.getTime();
			} else {
				startDate = calendar.getTime();
			}
		}
		return list;
	}
}
