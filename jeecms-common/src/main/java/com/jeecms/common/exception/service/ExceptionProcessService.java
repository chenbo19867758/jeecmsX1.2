package com.jeecms.common.exception.service;

import com.jeecms.common.response.ResponseInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异常处理service接口
 * @author: chenming
 * @date:   2019年10月11日 上午9:26:06
 */
public interface ExceptionProcessService {
	
	/**
	 * 处理错误
	 * @Title: process  
	 * @param request	前台传入的request请求
	 * @param exception	错误对象
	 * @return: ResponseInfo
	 */
	ResponseInfo process(HttpServletRequest request, HttpServletResponse response,Exception exception);
}
