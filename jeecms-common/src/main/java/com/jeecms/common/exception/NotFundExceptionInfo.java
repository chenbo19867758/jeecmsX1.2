package com.jeecms.common.exception;

/**
 * 系统错误信息类，用于支持系统错误的处理。
 * 
 * @author: tom
 * @date: 2018年12月25日 上午9:08:01
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class NotFundExceptionInfo implements ExceptionInfo {

	/** 系统错误的代码。 */
	private String code = SystemExceptionEnum.NOT_FOUND.getCode();

	/** 系统错误对应的默认提示信息。 */
	private String defaultMessage = SystemExceptionEnum.NOT_FOUND.getDefaultMessage();

	/** 系统错误对应的原始提示信息。 */
	private String originalMessage = SystemExceptionEnum.NOT_FOUND.getDefaultMessage();

	/** 当前请求的URL。 */
	private String requestUrl;

	/** 默认的转向（重定向）的URL，默认为空。 */
	private String defaultRedirectUrl = "";

	/** 系统错误对应的响应数据。 */
	private Object data = new Object();

	/**
	 * Description: 通过非法参数异常的默认提示信息、响应数据构建一个非法参数异常信息对象。
	 *
	 * @param defaultMessage
	 *            系统错误的默认提示信息。
	 * 
	 * @param data
	 *            系统错误的响应数据。
	 */
	public NotFundExceptionInfo(String defaultMessage, Object data) {
		this.defaultMessage = defaultMessage;
		this.data = data;
	}

	public NotFundExceptionInfo(String defaultMessage, String code) {
		this.defaultMessage = defaultMessage;
		this.code = code;
	}

	public NotFundExceptionInfo() {
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
