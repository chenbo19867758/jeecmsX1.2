package com.jeecms.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.annotation.ContentSecurity;
import com.jeecms.common.constants.ContentSecurityConstants;
import com.jeecms.common.exception.ContentSecurityExceptionInfo;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionEnum;
import com.jeecms.common.util.DesUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * 安全认证拦截器
 * 
 * @author: tom
 * @date: 2018年6月19日 下午8:36:03
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class ContentSecurityInterceptor implements HandlerInterceptor {
	/**
	 * logback
	 */
	private static Logger logger = LoggerFactory.getLogger(ContentSecurityInterceptor.class);

	/**
	 * 请求之前处理加密内容
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param handler
	 *            数据对象
	 * @throws Exception
	 *             Exception
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// 默认可以通过
		boolean isPass = true;
		if (handler instanceof HandlerMethod) {
			/**
			 * 获取请求映射方法对象
			 */
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			/**
			 * 获取访问方法实例对象
			 */
			Method method = handlerMethod.getMethod();
			/**
			 * 检查是否存在内容安全验证注解
			 */
			ContentSecurity security = method.getAnnotation(ContentSecurity.class);
			/**
			 * 存在注解做出不同方式认证处理
			 */
			if (security != null) {
				/** 设置是request加密请求 */
				request.setAttribute(ContentSecurityConstants.SECURITY_REQUEST, "true");
				switch (security.away()) {
					// DES方式内容加密处理
					case DES: {
						isPass = checkDES(request, response);
						break;
					}
					default: {
						isPass = checkDES(request, response);
						break;
					}
				}

			}
		}

		return isPass;
	}

	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object o, Exception e) throws Exception {

	}

	/**
	 * 检查DES方式内容
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return
	 */
	boolean checkDES(HttpServletRequest request, HttpServletResponse response) throws GlobalException {
		// 获取desString加密内容
		String des = request.getParameter(ContentSecurityConstants.DES_PARAMETER_NAME);
		/**
		 * 加密串不存在
		 */
		if (des == null || des.length() == 0) {
			throw new GlobalException(new ContentSecurityExceptionInfo());
		}

		/**
		 * 存在加密串 解密DES参数列表并重新添加到request内
		 */
		try {
			des = DesUtil.decrypt(des, ContentSecurityConstants.DES_KEY, ContentSecurityConstants.DES_IV); 

			if (!StringUtils.isEmpty(des)) {

				JSONObject params = JSON.parseObject(des);

				Iterator<?> it = params.keySet().iterator();
				while (it.hasNext()) {
					/**
					 * 获取请求参数名称
					 */
					String parameterName = it.next().toString();
					/**
					 * 参数名称不为空时将值设置到request对象内 key=>value
					 */
					if (!StringUtils.isEmpty(parameterName)) {
						request.setAttribute(ContentSecurityConstants.ATTRIBUTE_PREFFIX 
								+ parameterName,params.get(parameterName));
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new GlobalException(
					new ContentSecurityExceptionInfo(
							SystemExceptionEnum.CONTENT_DECRYPT_ERROR.getDefaultMessage(),
							SystemExceptionEnum.CONTENT_DECRYPT_ERROR.getCode()));
		}
		return true;
	}
}
