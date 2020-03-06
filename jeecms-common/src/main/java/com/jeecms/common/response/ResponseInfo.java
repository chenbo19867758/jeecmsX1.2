package com.jeecms.common.response;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.annotation.JSONField;
import com.jeecms.common.exception.SystemExceptionEnum;
import com.jeecms.common.web.springmvc.MessageResolver;

/**
 * 封装了响应信息，用于统一响应结果
 * 
 * @author: tom
 * @date: 2018年12月26日 上午10:13:54
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class ResponseInfo {

	/** 响应代码。 */
	private int code;

	/** 响应提示信息。 */
	private String message;

	/** 当前请求的URL。 */
	private String requestUrl;

	/** 需转向（重定向）的URL，默认为空。 */
	private String redirectUrl = "";

	/** 响应时间戳。 */
	private Date timestamp = new Date();

	/** 响应数据。 */
	private Object data;
	/** 更新的token */
	private String token;

	/**
	 * Description: 根据当前请求的对象，需转向（重定向）的URL和响应数据构建响应信息对象。注意，该构造方法仅响应请求成功的情况。
	 *
	 * 
	 * 
	 * @param request
	 *            当前请求的对象。
	 * 
	 * @param redirectUrl
	 *            需转向（重定向）的URL。
	 * 
	 * @param data
	 *            响应数据。
	 */
	public ResponseInfo(HttpServletRequest request, String redirectUrl, Object data) {
		this.code = Integer.parseInt(SystemExceptionEnum.SUCCESSFUL.getCode());
		this.message = MessageResolver.getMessage(SystemExceptionEnum.SUCCESSFUL.getCode(),
				SystemExceptionEnum.SUCCESSFUL.getDefaultMessage());
		this.requestUrl = request.getRequestURI();
		this.redirectUrl = redirectUrl;
		this.data = data;
	}

	/**
	 * Description: 根据需转向（重定向）的URL和响应数据构建响应信息对象。注意，该构造方法仅响应请求成功的情况。
	 *
	 * 
	 * 
	 * @param redirectUrl
	 *            需转向（重定向）的URL。
	 * 
	 * @param data
	 *            响应数据。
	 */
	public ResponseInfo(String redirectUrl, Object data) {
		this.code = Integer.parseInt(SystemExceptionEnum.SUCCESSFUL.getCode());
		this.message = MessageResolver.getMessage(SystemExceptionEnum.SUCCESSFUL.getCode(),
				SystemExceptionEnum.SUCCESSFUL.getDefaultMessage());
		this.redirectUrl = redirectUrl;
		this.data = data;
	}

	/**
	 * Description: 根据响应数据构建响应信息对象。注意，该构造方法仅响应请求成功且无返回参数的情况。
	 *
	 * 
	 * 
	 */
	public ResponseInfo() {
		this.code = Integer.parseInt(SystemExceptionEnum.SUCCESSFUL.getCode());
		this.message = MessageResolver.getMessage(SystemExceptionEnum.SUCCESSFUL.getCode(),
				SystemExceptionEnum.SUCCESSFUL.getDefaultMessage());
		this.data = new Object();
	}

	/**
	 * Description: 根据响应数据构建响应信息对象。注意，该构造方法仅响应请求成功的情况。
	 *
	 * 
	 * 
	 * @param data
	 *            响应数据。
	 */
	public ResponseInfo(Object data) {
		this.code = Integer.parseInt(SystemExceptionEnum.SUCCESSFUL.getCode());
		this.message = MessageResolver.getMessage(SystemExceptionEnum.SUCCESSFUL.getCode(),
				SystemExceptionEnum.SUCCESSFUL.getDefaultMessage());
		this.data = data;
	}

	/**
	 * 构造器
	 * 
	 * @param data
	 *            数据对象
	 * @param token
	 *            token值
	 */
	public ResponseInfo(Object data, String token) {
		this.code = Integer.parseInt(SystemExceptionEnum.SUCCESSFUL.getCode());
		this.message = MessageResolver.getMessage(SystemExceptionEnum.SUCCESSFUL.getCode(),
				SystemExceptionEnum.SUCCESSFUL.getDefaultMessage());
		this.data = data;
		this.token = token;
	}

	/**
	 * Description:
	 * 根据响应码、响应提示信息、当前请求对象、需转向（重定向）URL、响应数据构建响应信息对象。注意，该构造方法响应请求成功和失败（出现异常）两种情况。
	 *
	 * 
	 * 
	 * @param code
	 *            响应码。
	 * 
	 * @param message
	 *            响应提示信息。
	 * 
	 * @param request
	 *            当前请求对象。
	 * 
	 * @param redirectUrl
	 *            需转向（重定向）URL。
	 * 
	 * @param data
	 *            响应数据。
	 */
	public ResponseInfo(String code, String message, HttpServletRequest request, String redirectUrl, Object data) {
		this.code = Integer.parseInt(code);
		this.message = MessageResolver.getMessage(code, message);
		this.requestUrl = request.getRequestURI();
		this.redirectUrl = redirectUrl;
		this.data = data;
	}

	/**
	 * Description:
	 * 根据响应码、响应提示信息、需转向（重定向）URL、响应数据构建响应信息对象。注意，该构造方法响应请求成功和失败（出现异常）两种情况。
	 *
	 * 
	 * 
	 * @param code
	 *            响应码。
	 * 
	 * @param message
	 *            响应提示信息。
	 * 
	 * @param redirectUrl
	 *            需转向（重定向）URL。
	 * 
	 * @param data
	 *            响应数据。
	 */
	public ResponseInfo(String code, String message, String redirectUrl, Object data) {
		this.code = Integer.parseInt(code);
		this.message = MessageResolver.getMessage(code, message);
		this.redirectUrl = redirectUrl;
		this.data = data;
	}

	/**
	 * Description: 根据响应码、响应提示信息、响应数据构建响应信息对象。注意，该构造方法响应请求成功和失败（出现异常）两种情况。
	 * 
	 * 
	 * 
	 * @param code
	 *            响应码。
	 * 
	 * @param message
	 *            响应提示信息。
	 * 
	 * @param data
	 *            响应数据。
	 */
	public ResponseInfo(String code, String message, Object data) {
		this.code = Integer.parseInt(code);
		this.message = MessageResolver.getMessage(code, message);
		this.data = data;
	}

	/**
	 * Description: 根据响应码、响应提示信息、响应数据构建响应信息对象。注意，该构造方法响应请求成功和失败（出现异常）两种情况。
	 * 
	 * 
	 * 
	 * @param code
	 *            响应码。
	 * 
	 * @param message
	 *            响应提示信息。
	 */
	public ResponseInfo(String code, String message) {
		this.code = Integer.parseInt(code);
		this.message = MessageResolver.getMessage(code, message);
	}

	/**
	 * Description: 获取响应代码。
	 * 
	 * 
	 * 
	 * @return 响应代码。
	 */
	public Integer getCode() {
		return code;
	}

	/**
	 * Description: 设置响应代码。
	 * 
	 * 
	 * 
	 * @param code
	 *            响应代码。
	 */
	public void setCode(Integer code) {
		this.code = code;
	}

	/**
	 * Description: 获取响应提示信息。
	 * 
	 * 
	 * 
	 * @return 响应提示信息。
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Description: 设置响应提示信息。
	 * 
	 * 
	 * 
	 * @param message
	 *            响应提示信息。
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Description: 获取当前请求的URL。
	 * 
	 * 
	 * 
	 * @return 当前请求的URL。
	 */
	public String getRequestUrl() {
		return requestUrl;
	}

	/**
	 * Description: 设置当前请求的URL。
	 * 
	 * 
	 * 
	 * @param requestUrl
	 *            当前请求的URL。
	 */
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	/**
	 * Description: 获取需转向（重定向）的URL。
	 * 
	 * 
	 * 
	 * @return 需转向（重定向）的URL。
	 */
	public String getRedirectUrl() {
		return redirectUrl;
	}

	/**
	 * Description: 设置需转向（重定向）的URL。
	 * 
	 * 
	 * 
	 * @param redirectUrl
	 *            需转向（重定向）的URL。
	 */
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	/**
	 * Description: 获取响应时间戳。
	 * 
	 * 
	 * 
	 * @return 响应时间戳。
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")  
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * Description: 获取响应数据。
	 * 
	 * 
	 * 
	 * @return 响应数据。
	 */
	public Object getData() {
		return data;
	}

	/**
	 * Description: 设置响应数据。
	 * 
	 * 
	 * 
	 * @param data
	 *            响应数据。
	 */
	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * 获取更新的token
	 * @Title: getToken
	 * @return: String
	 */
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
