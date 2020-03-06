package com.jeecms.exception.service.impl;

import com.jeecms.auth.dao.CoreApiDao;
import com.jeecms.auth.domain.CoreApi;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.exception.SystemExceptionEnum;
import com.jeecms.common.exception.service.ExceptionProcessService;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.web.springmvc.MessageResolver;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * 异常处理service实现类
 * @author: chenming
 * @date:   2019年10月12日 下午2:28:49
 */
@Service
public class ExceptionProcessServiceImpl implements ExceptionProcessService {
	static Logger logger = LoggerFactory.getLogger(ExceptionProcessServiceImpl.class);

	@Override
	public ResponseInfo process(HttpServletRequest request, HttpServletResponse response, Exception exception) {
		exception.printStackTrace();
		logger.error(exception.getMessage());
		
		Short method = CoreApi.METHODS.get(request.getMethod());
		String requestUrl = request.getRequestURI();
		CoreApi api = null;
		if (StringUtils.isNotBlank(requestUrl)) {
			api = dao.findByApiUrlAndRequestMethodAndHasDeleted(requestUrl, method, false);
			if (api == null) {
				api = dao.findByApiUrlAndRequestMethodAndHasDeleted(requestUrl.substring(0, requestUrl.lastIndexOf("/")), method, false);
			}
		}
		
		// 获取内部服务器错误异常的代码。
		String code = SystemExceptionEnum.INTERNAL_SERVER_ERROR.getCode();
		// 根据内部服务器错误异常的本地化（国际化）提示消息。
		String msg = MessageResolver.getMessage(code, SystemExceptionEnum.INTERNAL_SERVER_ERROR.getDefaultMessage());
		if (exception.getClass().equals(NullPointerException.class)) {
			code = SystemExceptionEnum.DOMAIN_NOT_FOUND_ERROR.getCode();
			msg = MessageResolver.getMessage(code, SystemExceptionEnum.DOMAIN_NOT_FOUND_ERROR.getDefaultMessage());
		} 
		/** 方法级别参数校验失败异常 */
		if (exception.getClass().equals(ConstraintViolationException.class)) {
			code = SystemExceptionEnum.ILLEGAL_PARAM.getCode();
			msg = MessageResolver.getMessage(code, SystemExceptionEnum.ILLEGAL_PARAM.getDefaultMessage());
		}
		// 根据内部服务器错误异常的代码获取其对应的转向（重定向）URL。
		String iseRedirectUrl = MessageResolver.getMessage(code + ".redirectUrl",
				SystemExceptionEnum.INTERNAL_SERVER_ERROR.getDefaultRedirectUrl());
		Map<String, String> omMap = new HashMap<String, String>(1);
		omMap.put("originalMessage", exception.getMessage());
		String errorCode = "-0000X";
		if (api != null) {
			msg = "-"+api.getId()+" 错误，请联系售后";
			errorCode = "-" + api.getId();
		} else {
			msg = "-0000X 未知错误，请联系售后";
		}

		/** 非重定向请求则返回500 异常JSON数据 */
		String header = request.getHeader(redirectHeader);
		if (StringUtils.isNoneBlank(header) && WebConstants.FALSE.equals(header)) {
			return new ResponseInfo(code, msg, request, iseRedirectUrl, omMap);
		}
		try {
			response.sendRedirect("/error/500?msg="+errorCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ResponseInfo(code, msg, request, iseRedirectUrl, omMap);
	}
	
	@Autowired
	private CoreApiDao dao;
	@Value("${redirect.header}")
	private String redirectHeader;

}
