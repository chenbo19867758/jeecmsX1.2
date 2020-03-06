package com.jeecms.common;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.jsonfilter.advice.JsonFilterResponseBodyAdvice;
import com.jeecms.common.jsonfilter.bean.JsonFilterObject;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.LoggerUtils;
import com.jeecms.common.util.UserAgentUtils;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.SysLog;
import com.jeecms.system.service.SysLogService;
import com.jeecms.util.SystemContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义ResponseBodyAdvice实现日志拦截记录类，执行顺序在{@link JsonFilterResponseBodyAdvice}}之后
 *
 * @Description:
 * @author: wangqq
 * @date: 2018年3月19日 下午3:00:37
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@SuppressWarnings("rawtypes")
@Order(2)
@ControllerAdvice
public class LogResponseBodyAdvice implements ResponseBodyAdvice {
	static Logger logger = LoggerFactory.getLogger(LogResponseBodyAdvice.class);

	@Override
	public boolean supports(MethodParameter methodParameter, Class aClass) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass,
								  ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
		HttpServletRequest request = RequestUtils.getHttpServletRequest();
		String urlPath = RequestUtils.getRequestUrl(request);
		//当前请求路径非后台路径不进行日志记录
		if (StringUtils.isNotBlank(urlPath) && RequestUtils.isAdminRequest()) {
			if (o != null && o instanceof JsonFilterObject) {
				ResponseInfo info;
				Object obj = ((JsonFilterObject) o).getJsonObject();
				if (obj != null && obj instanceof ResponseInfo) {
					info = (ResponseInfo) obj;
					//调用线程池中的线程进行异步处理
					SysLog log = new SysLog();
					// 设置客户端ip
					log.setClientIp(LoggerUtils.getCliectIp(request));
					// 设置请求方法
					log.setMethod(request.getMethod());
					// 设置请求参数内容json字符串
					String paramData = JSON.toJSONString(request.getParameterMap(),
						SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteMapNullValue);
					log.setParamData(paramData);
					// 设置sessionId
					String sessionId = request.getRequestedSessionId();
					log.setSessionId(sessionId);
					// 请求耗时
					Object sendTime = request.getAttribute(WebConstants.LOGGER_SEND_TIME);
					if (sendTime != null) {
						long time = Long.parseLong(sendTime.toString());
						long currentTime = System.currentTimeMillis();
						log.setTimeConsuming(Integer.valueOf((currentTime - time) + ""));
						//接口返回时间
						log.setReturmTime(currentTime + "");
					}
					//浏览器
					log.setBrowser(UserAgentUtils.getBrowserInfo(request));
					//操作系统
					log.setOs(UserAgentUtils.getClientOS(request));
					//用户代理
					log.setUserAgent(UserAgentUtils.getBrowerUserAgent(request));
					// 设置请求地址
					log.setUri(urlPath);
					String methods = request.getMethod();
					CoreUser user = SystemContextUtils.getCoreUser();
					CmsSite site = SystemContextUtils.getSite(request);
					sysLogService.asyncLog(methods, log, info, user, site);
				}
			}
		}
		return o;
	}


	@Autowired
	private SysLogService sysLogService;
}



