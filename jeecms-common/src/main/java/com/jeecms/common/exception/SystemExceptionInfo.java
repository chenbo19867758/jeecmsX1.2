package com.jeecms.common.exception;

/**
 * Description: 系统通用错误信息类，用于支持系统通用错误的处理。
 * @author: tom
 * @date:   2019年3月8日 下午4:35:05
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class SystemExceptionInfo implements ExceptionInfo {

	/** 系统通用错误的代码。 */
	private String code = SystemExceptionEnum.SITE_NOT_FOUND_ERROR.getCode();

	/** 系统通用错误对应的默认提示信息。 */
	private String defaultMessage = SystemExceptionEnum.SITE_NOT_FOUND_ERROR.getDefaultMessage();

	/** 系统通用错误对应的原始提示信息。 */
	private String originalMessage = SystemExceptionEnum.SITE_NOT_FOUND_ERROR.getDefaultMessage();

	/** 当前请求的URL。 */
	private String requestUrl;

	/** 默认的转向（重定向）的URL，默认为空。 */
	private String defaultRedirectUrl = "";

	/** 系统通用错误对应的响应数据。 */
	private Object data = new Object();

	/**
	 * Description: 通过非法参数异常的默认提示信息、响应数据构建一个非法参数异常信息对象。
	 *
	 * @param defaultMessage
	 *            系统通用错误的默认提示信息。
	 * 
	 * @param data
	 *            系统通用错误的响应数据。
	 */
	public SystemExceptionInfo(String defaultMessage, Object data) {
		this.defaultMessage = defaultMessage;
		this.data = data;
	}

	public SystemExceptionInfo(String defaultMessage, String code) {
		this.defaultMessage = defaultMessage;
		this.code = code;
	}
	
	public SystemExceptionInfo(String defaultMessage, String code, Object data) {
		this.code = code;
		this.defaultMessage = defaultMessage;
		this.data = data;
	}

	public SystemExceptionInfo() {
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getDefaultMessage() {
		return defaultMessage;
	}

	@Override
	public String getOriginalMessage() {
		return originalMessage;
	}

	@Override
	public void setOriginalMessage(String originalMessage) {
		this.originalMessage = originalMessage;
	}

	@Override
	public String getRequestUrl() {
		return requestUrl;
	}

	@Override
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	@Override
	public String getDefaultRedirectUrl() {
		return defaultRedirectUrl;
	}

	@Override
	public void setDefaultRedirectUrl(String defaultRedirectUrl) {
		this.defaultRedirectUrl = defaultRedirectUrl;
	}

	@Override
	public Object getData() {
		return data;
	}

	@Override
	public void setData(Object data) {
		this.data = data;
	}

}
