package com.jeecms.statistics.service;

import java.util.Date;
import java.util.List;

/**
 * 时间切割service接口
 * @author: chenming
 * @date:   2019年7月4日 下午2:10:02
 */
public interface TimeCuttingService {
	
	/**
	 * 根据分钟切割时间
	 * @Title: getIntervalTimeList  
	 * @param startDate
	 * @param endDate
	 * @param interval
	 * @return: List
	 */
	List<Date> getIntervalTimeList(Date startDate, Date endDate, int interval);
}
