package com.jeecms.common.web;

import java.beans.PropertyEditorSupport;
import java.lang.annotation.Annotation;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.util.WebUtils;

import com.jeecms.common.page.Paginable;
import com.jeecms.common.page.PaginableRequest;

/**
 * Paginable参数对应实现类
 * @author: tom
 * @date: 2018年4月7日 下午2:51:01
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class PaginableArgumentResolver implements HandlerMethodArgumentResolver {
	private static final String DEFAULT_FIRST = "first";
	private static final String DEFAULT_MAX = "max";
	private static final String DEFAULT_PREFIX = "page";
	private static final String DEFAULT_SEPARATOR = "_";
	public static final int DEFAULT_PAGE_SIZE = 20;
	public static final int DEFAULT_MAX_SIZE = 5000;
	private String prefix = DEFAULT_PREFIX;
	private String separator = DEFAULT_SEPARATOR;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(Paginable.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest servletRequest = (HttpServletRequest) webRequest.getNativeRequest();
		String firstStr = servletRequest.getParameter(DEFAULT_FIRST);
		String maxStr = servletRequest.getParameter(DEFAULT_MAX);
		String prefix = getPrefix(parameter);
		Map<String, Object> map = WebUtils.getParametersStartingWith(servletRequest, prefix + separator);
		Integer first = 1;
		Integer max = 0;
		if (StringUtils.isNotBlank(firstStr)) {
			first = NumberUtils.toInt(firstStr, 0);
			if (first < 0) {
				first = 1;
			}
		}
		if (StringUtils.isNotBlank(maxStr)) {
			max = NumberUtils.toInt(maxStr, 0);
			if (max < 0) {
				max = DEFAULT_MAX_SIZE;
			}
		}
		PropertyValues propertyValues = new MutablePropertyValues(map);
		Sort sort = new Sort(Sort.Direction.DESC, "id");
		Paginable paginable = new PaginableRequest(first, max, sort);
		DataBinder binder = new ServletRequestDataBinder(paginable);
		binder.initDirectFieldAccess();
		binder.registerCustomEditor(Sort.class, new SortPropertyEditor("sort_dir", propertyValues));
		binder.bind(propertyValues);
		return paginable;
	}

	private static class SortPropertyEditor extends PropertyEditorSupport {

		private final String orderProperty;
		private final PropertyValues values;

		/**
		 * Creates a new {@link SortPropertyEditor}.
		 * @param orderProperty String
		 * @param values PropertyValues
		 */
		public SortPropertyEditor(String orderProperty, PropertyValues values) {

			this.orderProperty = orderProperty;
			this.values = values;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
		 */
		@Override
		public void setAsText(String text) {
			PropertyValue rawOrder = values.getPropertyValue(orderProperty);
			Direction order = Direction.ASC;
			if (null != rawOrder) {
				Object rawObj = rawOrder.getValue();
				if (rawObj != null) {
					String val = rawObj.toString();
					if (val != null) {
						order = Direction.fromString(val);
					}
				}
			}
			setValue(new Sort(order, text));
		}
	}

	private String getPrefix(MethodParameter parameter) {

		for (Annotation annotation : parameter.getParameterAnnotations()) {
			if (annotation instanceof Qualifier) {
				return new StringBuilder(((Qualifier) annotation).value())
						.append("_").append(prefix).toString();
			}
		}

		return prefix;
	}

}
