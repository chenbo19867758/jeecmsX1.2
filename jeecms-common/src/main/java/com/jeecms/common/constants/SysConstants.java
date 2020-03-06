package com.jeecms.common.constants;

import java.util.HashMap;
import java.util.Map;

import com.jeecms.common.jsonfilter.advice.JsonFilterResponseBodyAdvice;
import com.jeecms.common.ueditor.ResourceType;
import com.jeecms.common.ueditor.Utils;

/**
 * JSPGOU常量
 * 
 * @author tom
 */
public class SysConstants {
	/**
	 * 时间单位 1天 2时 3分
	 * 
	 * @author: tom
	 * @date: 2019年5月14日 下午5:40:53
	 */
	public enum TimeUnit {
		/**
		 * 1 天
		 */
		day(1),
		/**
		 * 2小时
		 */
		hour(2),
		/**
		 * 3 分
		 */
		minute(3),;
		private static Map<Integer, TimeUnit> units = new HashMap<Integer, TimeUnit>(4);

		static {
			units.put(day.getValue(), day);
			units.put(hour.getValue(), hour);
			units.put(minute.getValue(), minute);
		}

		Integer value;

		private TimeUnit(Integer value) {
			this.value = value;
		}

		public Integer getValue() {
			return value;
		}

		public void setValue(Integer value) {
			this.value = value;
		}

		public static TimeUnit valueOf(final Integer value) {
			TimeUnit rt = units.get(value);
			if (rt == null) {
				rt = units.get(1);
			}
			return rt;
		}

	}

	/**
	 * 微信
	 */
	public static final String TPLDIR_WECHAT = "wechat";

	/**
	 * 微博
	 */
	public static final String TPLDIR_WEIBO = "weibo";

	/**
	 * 系统模板路径
	 */
	public static final String TPL_BASE = "/WEB-INF/sys";
	/**
	 * 系统默认支持Headers
	 */
	public static final String DEFAULT_ALLOW_HEADERS = "Origin,Content-Type,X-Requested-With,accept,"
			+ "Origin,Access-Control-Request-Method,Access-Control-Request-Headers,X_Requested_With,";

	/**
	 * 1.都不需要登录
	 */
	public static final Short VISIT_LOGIN_NO_LIMIT = 1;

	/**
	 * 2.仅内容页需登录
	 */
	public static final Short VISIT_CONTENT_LOGIN = 2;

	/**
	 * 3.都需要登录
	 */
	public static final Short VISIT_ALL_LOGIN = 3;
	
	/**
	 * request存放 原始处理方法的key，目前用于传递原始处理的method得到原始注解，
	 * 抛出异常后再 ${@link JsonFilterResponseBodyAdvice} 中得到原始请求注解
	 * 以解决抛出异常信息需要传递复杂对象存在的循环依赖
	 */
	public static final String HANDLER_METHOD = "handlerMethod";
}
