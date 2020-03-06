package com.jeecms.common.constants;

/**
 * Sms常量
 * 
 * @author: tom
 * @date: 2018年12月24日 下午6:12:00
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class SmsConstants {

	/**
	 * 发送短信平台
	 */
	public static final Short ALI = 1;
	public static final Short TECENT = 2;

	/** 验证码动态占位符 */
	public static final String MESSAGE_TPL_VALIDATA_CODE = "${code}";
	/** 验证码有效期动态占位符 */
	public static final String MESSAGE_TPL_CODE_EXPIRE = "${expire}";
	/** 原因动态占位符 */
	public static final String MESSAGE_TPL_VALIDATA_REASON = "${reason}";
	/** 名称动态占位符 */
	public static final String MESSAGE_TPL_VALIDATA_NAME = "${name}";

}
