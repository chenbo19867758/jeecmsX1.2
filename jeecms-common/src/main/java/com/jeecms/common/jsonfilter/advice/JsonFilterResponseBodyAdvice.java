package com.jeecms.common.jsonfilter.advice;

import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.MultiSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.jsonfilter.bean.JsonFilterObject;
import com.jeecms.common.jsonfilter.exception.IncludeAndExcludeConflictException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.web.util.RequestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.ServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
/**
 * 自定义ResponseBodyAdvice实现类，重写beforeBodyWrite函数
 * 
 * @Description:
 * @author: wangqq
 * @date: 2018年3月19日 下午3:00:37
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@SuppressWarnings("rawtypes")
@Order(1)
@ControllerAdvice
public class JsonFilterResponseBodyAdvice implements ResponseBodyAdvice {

	private static final String[] REQUEST_URLS = {
		WebConstants.ADMIN_PREFIX  + "/command/productinfo",
		WebConstants.ADMIN_PREFIX  + "/command/change",
		WebConstants.ADMIN_PREFIX  + "/coreUser/routingList",
		WebConstants.ADMIN_PREFIX  + "/logout",
		WebConstants.ADMIN_PREFIX  + WebConstants.LOGIN_URL
	};

	@Override
	public boolean supports(MethodParameter methodParameter, Class aClass) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass,
			ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
		JsonFilterObject jsonFilterObject = new JsonFilterObject();

		if (o == null) {
			return null;
		}
		ServletRequest rq = RequestUtils.getHttpServletRequest();
		/**
		 * 防止会话过期，更换新token
		 */
		Object token = rq.getAttribute(tokenHeader);
		if (token != null && o instanceof ResponseInfo) {
			ResponseInfo info = (ResponseInfo) o;
			info.setToken((String) token);
			o = info;
		}
		// 默认过滤掉page对象中的sort、number/numberOfelements属性
		jsonFilterObject.getExcludes().put(Page.class,
				new HashSet<String>(Arrays.asList("sort", "number", "numberOfElements", "pageable")));

		jsonFilterObject.setJsonObject(o);
		Method method = null;
		if (methodParameter == null) {
			return jsonFilterObject;
		} else {
			method = methodParameter.getMethod();
		}
		// 检查返回对象是否加入注解
		if (method != null) {
			if (!method.isAnnotationPresent(SerializeField.class)
					&& !method.isAnnotationPresent(MultiSerializeField.class)
					&& !method.isAnnotationPresent(MoreSerializeField.class)) {
				return jsonFilterObject;
			}

			if (method.isAnnotationPresent(SerializeField.class)) {
				Object obj = method.getAnnotation(SerializeField.class);
				handleAnnotation(SerializeField.class, obj, jsonFilterObject);
			}
			if (method.isAnnotationPresent(MultiSerializeField.class)) {
				Object obj = method.getAnnotation(MultiSerializeField.class);
				handleAnnotation(MultiSerializeField.class, obj, jsonFilterObject);
			}
			if (method.isAnnotationPresent(MoreSerializeField.class)) {
				MoreSerializeField moreSerializeField = method.getAnnotation(MoreSerializeField.class);
				SerializeField[] serializeFields = moreSerializeField.value();
				if (serializeFields.length > 0) {
					for (int i = 0; i < serializeFields.length; i++) {
						handleAnnotation(SerializeField.class, serializeFields[i], jsonFilterObject);
					}
				}
			}
		}
		return jsonFilterObject;
	}

	private void handleAnnotation(Class clazz, Object obj, JsonFilterObject jsonFilterObject) {
		String[] includes = {};
		String[] excludes = {};
		Class objClass = null;
		if (clazz.equals(SerializeField.class)) {
			SerializeField serializeField = (SerializeField) obj;
			includes = serializeField.includes();
			excludes = serializeField.excludes();
			objClass = serializeField.clazz();
		}
		if (clazz.equals(MultiSerializeField.class)) {
			MultiSerializeField multiSerializeField = (MultiSerializeField) obj;
			includes = multiSerializeField.includes();
			excludes = multiSerializeField.excludes();
			objClass = multiSerializeField.clazz();
		}
		if (includes.length > 0 && excludes.length > 0) {
			throw new IncludeAndExcludeConflictException(
					"Can not use both include field and exclude field in an annotation!");
		} else if (includes.length > 0) {
			jsonFilterObject.getIncludes().put(objClass, new HashSet<String>(Arrays.asList(includes)));
		} else if (excludes.length > 0) {
			List<String> excludeList = new ArrayList<>(Arrays.asList(excludes));
			// excludeList.addAll(Arrays.asList(excludePatter));
			jsonFilterObject.getExcludes().put(objClass, new HashSet<String>(excludeList));
		}
	}


	@Value("${token.header}")
	private String tokenHeader;

}
