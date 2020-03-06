package com.jeecms.system.exception;

import com.jeecms.common.exception.ExceptionInfo;
import com.jeecms.common.exception.error.SiteErrorCodeEnum;

/**
 * 
 * @Description:订单类错误异常
 * @author: tom
 * @date:   2018年7月18日 下午8:00:18     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class SiteExceptionInfo implements ExceptionInfo {

	/**站点相关错误的代码。 */
	private String code = SiteErrorCodeEnum.CROSS_SITE_DATA_ERROR.getCode();

	/**站点相关错误对应的默认提示信息。 */
	private String defaultMessage = SiteErrorCodeEnum.CROSS_SITE_DATA_ERROR.getDefaultMessage();

	/**站点相关错误对应的原始提示信息。 */
	private String originalMessage= SiteErrorCodeEnum.CROSS_SITE_DATA_ERROR.getDefaultMessage();

	/** 当前请求的URL。 */
	private String requestUrl;

	/** 默认的转向（重定向）的URL，默认为空。 */
	private String defaultRedirectUrl = "";

	/**站点相关错误对应的响应数据。 */
	private Object data = new Object();

	/**
	 * Description: 通过非法参数异常的默认提示信息、响应数据构建一个非法参数异常信息对象。
	 *
	 * @param defaultMessage
	 *           站点相关错误的默认提示信息。
	 * 
	 * @param data
	 *           站点相关错误的响应数据。
	 */
	public SiteExceptionInfo(String defaultMessage, Object data) {
		this.defaultMessage = defaultMessage;
		this.data = data;
	}
	
	public SiteExceptionInfo(String code,String defaultMessage) {
		this.code = code;
		this.defaultMessage=defaultMessage;
	}
	
	public SiteExceptionInfo(String code,String defaultMessage,Object data) {
		this.code = code;
		this.defaultMessage=defaultMessage;
		this.data = data;
	}
	
	public SiteExceptionInfo() {
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
