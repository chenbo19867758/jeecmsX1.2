/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.statistics.service;

import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.system.domain.SysAccessRecord;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/2 15:33
 */

public class StatisticsServiceUtils {
	/**
	 * 计算UV
	 *
	 * @param list 访问记录列表
	 * @return uv
	 */
	public static int uvCalculation(List<SysAccessRecord> list) {
		//登录用户的uv
		int userNum = list.parallelStream().filter(o -> o.getLoginUserId() != null)
				.collect(Collectors.groupingBy(SysAccessRecord::getLoginUserId)).size();
		//未登录用户的uv
		int cookieNum = list.parallelStream().filter(o -> o.getCookieId() != null)
				.filter(o -> o.getLoginUserId() == null)
				.collect(Collectors.groupingBy(SysAccessRecord::getCookieId)).size();
		return userNum + cookieNum;
	}

	/**
	 * 计算ip数
	 *
	 * @param list 访问记录列表
	 * @return ip数
	 */
	public static int ipCalculation(List<SysAccessRecord> list) {
		return list.parallelStream().collect(Collectors.groupingBy(SysAccessRecord::getAccessIp)).size();
	}

	/**
	 * 平均访问时长
	 *
	 * @param list 访问记录列表
	 * @return 平均访问时长(HH : mm : ss)
	 */
	public static long averageAccessDuration(List<SysAccessRecord> list) {
		//根据session分组
		Map<String, List<SysAccessRecord>> sessionMap = list.parallelStream()
				.filter(o -> o.getSessionId() != null)
				.sorted(Comparator.comparing(AbstractDomain::getCreateTime))
				.collect(Collectors.groupingBy(SysAccessRecord::getSessionId));
		long total = 0;
		for (List<SysAccessRecord> records : sessionMap.values()) {
			if (records.size() > 1) {
				long end = records.get(records.size() - 1).getCreateTime().getTime();
				long begin = records.get(0).getCreateTime().getTime();
				total += Math.abs(end - begin);
			}
		}
		int size = list.size();
		return Math.abs(size == 0 ? 0 : total / size / 1000);
	}

	/**
	 * 时间差转 HH:mm:ss  *
	 *
	 * @param diff 时间差
	 * @return
	 */
	public static String diffTime(long diff) {
		diff = Math.abs(diff);
		long nd = 24 * 60 * 60;
		long nh = 60 * 60;
		long nm = 60;
		//获取相差的小时数
		long hour = diff % nd / nh;
		hour = Math.abs(hour);
		//获取相差的分钟数
		long min = diff % nd % nh / nm;
		min = Math.abs(min);
		//计算相差的秒数
		long sec = diff % nd % nh % nm;
		sec = Math.abs(sec);
		return (hour < 10 ? "0" + hour : hour) + ":"
				+ (min < 10 ? "0" + min : min) + ":"
				+ (sec < 10 ? "0" + sec : sec);
	}
}
