/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.constants;

import com.jeecms.common.util.MyDateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日志常量
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/5/30 10:18:24
 */

public class LogConstants {

	/**
	 * 系统日志
	 */
	public static final int LOG_CATEGORY_SYSTEM = 1;
	/**
	 * 业务日志
	 */
	public static final int LOG_CATEGORY_BUSINESS = 2;
	/**
	 * 审计日志
	 */
	public static final int LOG_CATEGORY_AUDIT = 3;
	/**
	 * 安全日志
	 */
	public static final int LOG_CATEGORY_SECURITY = 4;
	/**
	 * 告警日志
	 */
	public static final int LOG_CATEGORY_ALARM = 5;

	/**
	 * 查询操作
	 */
	public static final Integer LOG_OPERATE_QUERY = 1;

	/**
	 * 新增操作
	 */
	public static final Integer LOG_OPERATE_ADD = 2;

	/**
	 * 修改操作
	 */
	public static final Integer LOG_OPERATE_UPDATE = 3;
	/**
	 * 删除操作
	 */
	public static final Integer LOG_OPERATE_DELETE = 4;
	/**
	 * 导出操作
	 */
	public static final Integer LOG_OPERATE_EXPORT = 5;
	/**
	 * 导入操作
	 */
	public static final Integer LOG_OPERATE_IMPORT = 6;
	/**
	 * 上传操作
	 */
	public static final Integer LOG_OPERATE_UPLOAD = 7;
	/**
	 * 下载操作
	 */
	public static final Integer LOG_OPERATE_DOWNLOAD = 8;

	/**
	 * 日志级别高
	 */
	public static final Integer LOG_LEVEL_HIGH = 1;
	/**
	 * 日志基本中
	 */
	public static final Integer LOG_LEVEL_MIDDLE = 2;
	/**
	 * 日志基本低
	 */
	public static final Integer LOG_LEVEL_LOW = 3;
	/**
	 * 日志类别信息
	 */
	public static final Integer LOG_TYPE_INFO = 1;
	/**
	 * 日志类别警告
	 */
	public static final Integer LOG_TYPE_CAVEAT = 2;
	/**
	 * 系统事件
	 */
	public static final Integer LOG_EVENT_TYPE_SYSTEM = 1;
	/**
	 * 业务事件
	 */
	public static final Integer LOG_EVENT_TYPE_BUSINESS = 2;

	public static final Integer LOG_STATUS_SUCCESS = 1;
	public static final Integer LOG_STATUS_FAIL = 2;

	public enum LogGroup {
		/**
		 * 小时
		 */
		HOUR,
		/**
		 * 天
		 */
		DAY,
		/**
		 * 月
		 */
		MONTH,
		/**
		 * 年
		 */
		YEAR
	}

	public enum StatisticsType {
		/**
		 * 请求结果
		 */
		OPERATIONRESULT,
		/**
		 * 事件类型
		 */
		EVENTTYPE,
		/**
		 * 操作类型
		 */
		OPERATIONTYPE,
		/**
		 * 用户名
		 */
		USERNAME
	}

	/**
	 * 获取日志分类名称
	 *
	 * @param category 分类
	 * @return 分类名称
	 */
	public static String category(int category) {
		switch (category) {
			case LOG_CATEGORY_SYSTEM:
				return "系统日志";
			case LOG_CATEGORY_BUSINESS:
				return "业务日志";
			case LOG_CATEGORY_AUDIT:
				return "审计日志";
			case LOG_CATEGORY_SECURITY:
				return "安全日志";
			case LOG_CATEGORY_ALARM:
				return "告警日志";
			default:
				return "";
		}
	}

	/**
	 * 获取日志操作类型
	 *
	 * @param operate 操作类型
	 * @return 操作类型
	 */
	public static String operate(Integer operate) {
		if (LOG_OPERATE_QUERY.equals(operate)) {
			return "查询";
		} else if (LOG_OPERATE_ADD.equals(operate)) {
			return "新增";
		} else if (LOG_OPERATE_UPDATE.equals(operate)) {
			return "修改";
		} else if (LOG_OPERATE_DELETE.equals(operate)) {
			return "删除";
		} else if (LOG_OPERATE_EXPORT.equals(operate)) {
			return "导出";
		} else if (LOG_OPERATE_IMPORT.equals(operate)) {
			return "导入";
		} else if (LOG_OPERATE_UPLOAD.equals(operate)) {
			return "上传";
		} else if (LOG_OPERATE_DOWNLOAD.equals(operate)) {
			return "下载";
		}
		return "系统异常";
	}

	/**
	 * 获取事件类型
	 *
	 * @param eventType 事件类型
	 * @return 事件类型
	 */
	public static String eventType(Integer eventType) {
		if (LOG_EVENT_TYPE_SYSTEM.equals(eventType)) {
			return "系统事件";
		} else if (LOG_EVENT_TYPE_BUSINESS.equals(eventType)) {
			return "业务事件";
		}
		return "系统事件";
	}

	/**
	 * 获取请求状态
	 *
	 * @param status 状态
	 * @return 状态
	 */
	public static String status(Integer status) {
		if (LOG_STATUS_SUCCESS.equals(status)) {
			return "成功";
		} else if (LOG_STATUS_FAIL.equals(status)) {
			return "失败";
		}
		return "成功";
	}

	/**
	 * 获取时间列表
	 *
	 * @param type      事件类型 0小时 1天 2月 3年
	 * @param beginTime 开始事件
	 * @param endTime   结束事件
	 * @return
	 */
	public static List<String> getTimeList(LogConstants.LogGroup type, Date beginTime, Date endTime) {
		List<String> list;
		SimpleDateFormat sdf = new SimpleDateFormat(MyDateUtils.COM_Y_M_D_PATTERN);
		int hour = 24;
		switch (type) {
			case HOUR:
				list = new ArrayList<>();
				for (int i = 0; i < hour; i++) {
					list.add(i < 10 ? "0" + i : String.valueOf(i));
				}
				break;
			case DAY:
				if (beginTime == null) {
					beginTime = MyDateUtils.getStartDate(Calendar.getInstance().getTime());
				}
				if (endTime == null) {
					endTime = MyDateUtils.getFinallyDate(Calendar.getInstance().getTime());
				}
				list = MyDateUtils.getDays(sdf.format(beginTime), sdf.format(endTime));
				break;
			case MONTH:
				sdf = new SimpleDateFormat(MyDateUtils.COM_Y_M_PATTERN);
				if (beginTime == null) {
					beginTime = MyDateUtils.getSpecficYearStart(Calendar.getInstance().getTime(), 0);
				}
				if (endTime == null) {
					endTime = MyDateUtils.getSpecficYearEnd(Calendar.getInstance().getTime(), 0);
				}
				list = MyDateUtils.getMonths(sdf.format(beginTime), sdf.format(endTime));
				break;
			default:
				list = new ArrayList<>();
				for (int i = 0; i < hour; i++) {
					list.add(i < 10 ? "0" + i : String.valueOf(i));
				}
				break;
		}
		return list;
	}

}
