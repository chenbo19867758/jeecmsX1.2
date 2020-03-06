/**
 * 
 */

package com.jeecms.common.exception;

/**   
 * 参数加密类异常
 * @author: tom
 * @date:   2018年6月21日 下午10:41:24     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class ContentSecurityExceptionInfo implements ExceptionInfo {

	/** 参数解密类异常的代码。 */
	private String code = SystemExceptionEnum.CONTENT_SECURITY_NEED_ERROR.getCode();

	/** 参数解密类异常对应的默认提示信息。 */
	private String defaultMessage = SystemExceptionEnum.CONTENT_SECURITY_NEED_ERROR.getDefaultMessage();

	/** 参数解密类异常对应的原始提示信息。 */
	private String originalMessage;

	/** 当前请求的URL。 */
	private String requestUrl;

	/** 默认的转向（重定向）的URL，默认为空。 */
	private String defaultRedirectUrl = "";

	/** 参数解密类异常对应的响应数据。 */
	private Object data = new Object();
	
	/**
	 * Description: 通过参数解密类的默认提示信息、响应数据构建一个参数解密类信息对象。
	 *
	 * @param defaultMessage
	 *            参数解密类的默认提示信息。
	 * 
	 * @param code
	 *            参数解密类的响应编码
	 */
	public ContentSecurityExceptionInfo(String defaultMessage, String code) {
		this.defaultMessage = defaultMessage;
		this.code = code;
	}
	
	public ContentSecurityExceptionInfo() {
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
