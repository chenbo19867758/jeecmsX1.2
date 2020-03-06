package com.jeecms.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义业务逻辑异常类
 * @author tom
 */
public class GlobalException extends Exception  {

    /**   
	 * @Fields serialVersionUID : 
	 */ 
	private static final long serialVersionUID = 1L;


	/**
     * 日志对象
     */
    private Logger logger = LoggerFactory.getLogger(GlobalException.class);


	/** 异常信息（封装了异常代码和异常提示信息）。 */
	private ExceptionInfo exceptionInfo;

	/**
	 * Description: 通过异常信息对象构建一个全局异常对象。
	 * @param exceptionInfo
	 *            异常信息对象。
	 */
	public GlobalException(ExceptionInfo exceptionInfo) {
		this.exceptionInfo = exceptionInfo;
	}

	/**
	 * Description: 获取异常信息对象。
	 * 
	 * @return 异常信息对象。
	 */
	public ExceptionInfo getExceptionInfo() {
		return exceptionInfo;
	}

	/**
	 * Description: 设置异常信息对象。
	 * 
	 * @param exceptionInfo
	 *            异常信息对象。
	 */
	public void setExceptionInfo(ExceptionInfo exceptionInfo) {
		this.exceptionInfo = exceptionInfo;
		//错误信息
        logger.error("系统遇到如下异常，异常码：{}>>>异常信息：{}", exceptionInfo.getCode(), exceptionInfo.getOriginalMessage());
	}
}
