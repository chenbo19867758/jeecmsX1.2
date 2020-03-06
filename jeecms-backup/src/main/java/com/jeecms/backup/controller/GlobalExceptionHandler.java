package com.jeecms.backup.controller;

import com.jeecms.backup.exception.BackupException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 *
 * @author Zhu Kaixiao
 * @version 1.0
 * @date 2019/8/5 9:19
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
	static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(BackupException.class)
    public Map exceptionHandler(BackupException e) {
        log.error("内部错误: {}", e.getMessage());
        Map<String, Object> map = new HashMap<>(3);
        map.put("status", 400);
        map.put("message", e.getMessage());
        return map;
    }


    @ExceptionHandler(Exception.class)
    public Map exceptionHandler(Exception e) {
        log.error("内部错误", e);
        Map<String, Object> map = new HashMap<>(2);
        map.put("status", 500);
        map.put("message", e.getMessage());
        return map;
    }
}
