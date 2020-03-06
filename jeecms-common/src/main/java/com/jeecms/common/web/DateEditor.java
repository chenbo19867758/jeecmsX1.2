package com.jeecms.common.web;

import java.beans.PropertyEditorSupport;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

/**
 * 通用的日期编辑器。使用 {@link DateTime#parse(String)} 格式化，支持多种符合ISO标准日期格式。如'yyyy-MM-dd',
 * 'yyyy-MM-ddTHH:mm:ss'等。
 * 一般日期转换必须指定具体某一种格式，比如'yyyy-MM-dd'或'yyyy-MM-dd HH:mm:ss'，如果指定为前一种，则无法处理后一种格式。
 * 
 * @author tom
 * 
 * @see DateTime
 */
public class DateEditor extends PropertyEditorSupport {
	/**
	 * 短类型日期长度
	 */
	public static final int SHORT_DATE = 10;

	/**
	 * 字符串转换日期
	 * @Title: parse  
	 * @param text 时间字符串
	 * @return: Date
	 */
	public static Date parse(String text) {
		if (StringUtils.isBlank(text)) {
			// Treat empty String as null value.
			return null;
		}
		DateTime dt = DateTime.parse(text);
		// 根据日期长度，转换成不同的日期类型

		if (text.length() > SHORT_DATE) {
			return new java.sql.Timestamp(dt.getMillis());
		} else {
			return new java.sql.Date(dt.getMillis());
		}
	}

	/**
	 * 将日期转换成字符串
	 */
	@Override
	public String getAsText() {
		Date date = (Date) getValue();
		String text = "";
		if (date != null) {
			DateTime dt = new DateTime(date.getTime());
			if (date instanceof java.sql.Timestamp) {
				text = ISODateTimeFormat.dateHourMinuteSecond().print(dt);
			} else {
				text = ISODateTimeFormat.date().print(dt);
			}
		}
		return text;
	}

	/**
	 * 将字符串转换成日期
	 */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(parse(text));
	}
}
