package com.jeecms.common.exception;

import com.jeecms.common.wechat.bean.ReturnCode;

/**
 * Description: 账户无权限信息类，用于支持账户无权限的处理。
 * @author: tom
 * @date:   2019年3月8日 下午4:36:13
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class WeChatExceptionInfo implements ExceptionInfo {

	private String code = "0";

	private String defaultMessage = ReturnCode.get(0);

	private String originalMessage= "";

	/** 当前请求的URL。 */
	private String requestUrl;

	/** 默认的转向（重定向）的URL，默认为空。 */
	private String defaultRedirectUrl = "";

	private Object data = new Object();
	
	public WeChatExceptionInfo(String code ,String defaultMessage) {
		this.code = code;
		this.defaultMessage = defaultMessage;
	}

	public WeChatExceptionInfo(String code ,String defaultMessage, Object data) {
		this.code = code;
		this.defaultMessage = defaultMessage;
		this.data = data;
	}
	
	/**
	 * Description: 通过非法参数异常的默认提示信息、响应数据构建一个非法参数异常信息对象。
	 *
	 * @param defaultMessage
	 *           账户无权限的默认提示信息。
	 * 
	 * @param data
	 *           账户无权限的响应数据。
	 */
	public WeChatExceptionInfo(String defaultMessage, Object data) {
		this.defaultMessage = defaultMessage;
		this.data = data;
	}
	public WeChatExceptionInfo() {
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
