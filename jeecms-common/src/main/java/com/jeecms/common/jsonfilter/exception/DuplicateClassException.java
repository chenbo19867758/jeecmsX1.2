package com.jeecms.common.jsonfilter.exception;

/**
 * 转换异常类
 * 
 * @Description:
 * @author: wangqq
 * @date: 2018年3月19日 下午3:00:37
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@SuppressWarnings("serial")
public class DuplicateClassException extends RuntimeException {

	public DuplicateClassException() {
	}

	public DuplicateClassException(String message) {
		super(message);
	}
}
