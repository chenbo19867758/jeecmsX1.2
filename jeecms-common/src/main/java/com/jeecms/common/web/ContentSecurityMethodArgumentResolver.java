package com.jeecms.common.web;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.jeecms.common.annotation.ContentSecurityAttribute;
import com.jeecms.common.base.domain.RequestLoginTarget;
import com.jeecms.common.constants.ContentSecurityConstants;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.IllegalParamValidationUtils;

/**
 * 自定义方法参数映射 实现了HandlerMethodArgumentResolver接口内的方法supportsParameter &
 * resolveArgument
 * 通过supportsParameter方法判断仅存在@ContentSecurityAttribute注解的参数才会执行resolveArgument方法实现
 * 
 * @author: tom
 * @date: 2018年6月19日 下午8:32:28
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@SuppressWarnings("rawtypes")
public class ContentSecurityMethodArgumentResolver extends BaseMethodArgumentResolver {

	/**
	 * 判断参数是否配置了@ContentSecurityAttribute注解 如果返回true则执行resolveArgument方法
	 * @param parameter MethodParameter
	 * @return
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(ContentSecurityAttribute.class);
	}

	/**
	 * 执行参数映射
	 * @param parameter 参数对象
	 * @param mavContainer 参数集合
	 * @param request 本地请求对象
	 * @param binderFactory 绑定参数工厂对象
	 * @throws Exception Exception
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest request, WebDataBinderFactory binderFactory) throws Exception {
		// 获取@ContentSecurityAttribute配置的value值，作为参数名称
		if (parameter != null) {
			ContentSecurityAttribute anno = parameter.getParameterAnnotation(
					ContentSecurityAttribute.class);
			String name = null;
			if (anno != null) {
				name = anno.value();
			}
			/**
			 * 获取值 如果请求集合内存在则直接获取 如果不存在则调用createAttribute方法创建
			 */
			if (mavContainer != null) {
				Object target = (mavContainer.containsAttribute(name)) 
						? mavContainer.getModel().get(name)
						: createAttribute(name, parameter, binderFactory, request);
				/**
				 * 创建参数绑定
				 */
				WebDataBinder binder = binderFactory.createBinder(request, target, name);
				// 获取返回值实例
				target = binder.getTarget();
				// 如果存在返回值
				if (target != null) {
					/**
					 * 设置返回值对象内的所有field得值，从request.getAttribute方法内获取
					 */
					bindRequestAttributes(binder, request);
					/**
					 * 调用@Valid验证参数有效性
					 */
					validateIfApplicable(binder, parameter);
					/**
					 * 存在参数绑定异常 抛出异常
					 */
					if (binder.getBindingResult().hasErrors()) {
						/** 初始化非法参数的提示信息。 */
						IllegalParamValidationUtils.initIllegalParamMsg(
								binder.getBindingResult());
						/** 获取非法参数异常信息对象，并抛出异常。 */
						throw new GlobalException(
								IllegalParamValidationUtils
								.getIllegalParamExceptionInfo());
						/*
						 * if (isBindExceptionRequired(binder, parameter)) {
						 * throw new BindException(binder.getBindingResult()); }
						 */
					}

				}
				/**
				 * 转换返回对象
				 */
				target = binder.convertIfNecessary(binder.getTarget(), parameter.getParameterType());
				// 存放到model内
				mavContainer.addAttribute(name, target);
				return target;
			}
		}
		return new Object();
	}

	/**
	 * 绑定请求参数
	 * @param binder WebDataBinder
	 * @param nativeWebRequest NativeWebRequest
	 * @throws Exception Exception
	 */
	protected void bindRequestAttributes(WebDataBinder binder, NativeWebRequest nativeWebRequest) throws Exception {

		/**
		 * 获取返回对象实例
		 */
		Object obj = binder.getTarget();
		/**
		 * 获取返回值类型
		 */
		if (obj != null) {
			Class<?> targetType = obj.getClass();
			/**
			 * 转换本地request对象为HttpServletRequest对象
			 */
			HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
			/**
			 * 获取所有attributes
			 */
			if (request != null) {
				Enumeration attributeNames = request.getAttributeNames();
				/**
				 * 遍历设置值
				 */
				while (attributeNames.hasMoreElements()) {
					// 获取attribute name
					String attributeName = String.valueOf(attributeNames.nextElement());
					/**
					 * 仅处理ContentSecurityConstants.ATTRIBUTE_PREFFIX开头的attribute
					 */
					if (!attributeName.startsWith(ContentSecurityConstants.ATTRIBUTE_PREFFIX)) {
						continue;
					}
					// 获取字段名
					String fieldName = attributeName.replace(
							ContentSecurityConstants.ATTRIBUTE_PREFFIX, "");
					Field field = null;
					try {
						field = targetType.getDeclaredField(fieldName);
					} catch (NoSuchFieldException e) {
						/**
						 * 如果返回对象类型内不存在字段 则从父类读取
						 */
						try {
							field = targetType.getSuperclass().getDeclaredField(fieldName);
						} catch (NoSuchFieldException e2) {
							continue;
						}
						/**
						 * 如果父类还不存在，则直接跳出循环
						 */
						if (StringUtils.isEmpty(field)) {
							continue;
						}
					}
					/**
					 * 设置字段的值
					 */
					field.setAccessible(true);
					String fieldClassName = field.getType().getSimpleName();
					Object attributeObj = request.getAttribute(attributeName);

					if (attributeObj != null) {
						if ("String".equals(fieldClassName)) {
							field.set(obj, attributeObj);
						} else if ("Integer".equals(fieldClassName)) {
							field.setInt(obj,
									Integer.valueOf(String.valueOf(attributeObj)));
						} else if ("RequestLoginTarget".equals(fieldClassName)) {
							field.set(obj, 
									RequestLoginTarget.valueOf(
											attributeObj.toString()));
						} else {
							field.set(obj, attributeObj);
						}
					}
				}
				ServletRequestDataBinder servletBinder = (ServletRequestDataBinder) binder;
				servletBinder.bind(request);
			}
		}
	}

	/**
	 * Whether to raise a {@link BindException} on bind or validation errors.
	 * The default implementation returns {@code true} if the next method
	 * argument is not of type {@link Errors}.
	 *
	 * @param binder
	 *            the data binder used to perform data binding
	 * @param parameter
	 *            the method argument
	 */
	protected boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter parameter) {
		boolean hasBindingResult = false;
		int i = 0;
		Class<?>[] paramTypes = null;
		if (parameter != null) {
			i = parameter.getParameterIndex();
			Method m = parameter.getMethod();
			if (m != null) {
				paramTypes = m.getParameterTypes();
			}
		}
		if (paramTypes != null) {
			hasBindingResult = (paramTypes.length > (i + 1) 
					&& Errors.class.isAssignableFrom(paramTypes[i + 1]));
		}
		return !hasBindingResult;
	}

	/**
	 * Extension point to create the model attribute if not found in the model.
	 * The default implementation uses the default constructor.
	 *
	 * @param attributeName
	 *            the name of the attribute, never {@code null}
	 * @param parameter
	 *            the method parameter
	 * @param binderFactory
	 *            for creating WebDataBinder instance
	 * @param request
	 *            the current request
	 * @return the created model attribute, never {@code null}
	 */
	protected Object createAttribute(String attributeName, MethodParameter parameter,
			WebDataBinderFactory binderFactory, NativeWebRequest request) throws Exception {

		String value = getRequestValueForAttribute(attributeName, request);

		if (value != null) {
			Object attribute = createAttributeFromRequestValue(
					value, attributeName, parameter, binderFactory, request);
			if (attribute != null) {
				return attribute;
			}
		}
		return BeanUtils.instantiateClass(parameter.getParameterType());
	}

	/**
	 * Obtain a value from the request that may be used to instantiate the model
	 * attribute through type conversion from String to the target type.
	 * The default implementation looks for the attribute name to match a URI
	 * variable first and then a request parameter.
	 *
	 * @param attributeName
	 *            the model attribute name
	 * @param request
	 *            the current request
	 * @return the request value to try to convert or {@code null}
	 */
	protected String getRequestValueForAttribute(String attributeName, NativeWebRequest request) {
		Map<String, String> variables = getUriTemplateVariables(request);
		if (StringUtils.hasText(variables.get(attributeName))) {
			return variables.get(attributeName);
		} else if (StringUtils.hasText(request.getParameter(attributeName))) {
			return request.getParameter(attributeName);
		} else {
			return null;
		}
	}

	/**
	 * Create a model attribute from a String request value (e.g. URI template
	 * variable, request parameter) using type conversion.
	 * The default implementation converts only if there a registered
	 * {@link org.springframework.core.convert.converter.Converter} that can
	 * perform the conversion.
	 *
	 * @param sourceValue
	 *            the source value to create the model attribute from
	 * @param attributeName
	 *            the name of the attribute, never {@code null}
	 * @param parameter
	 *            the method parameter
	 * @param binderFactory
	 *            for creating WebDataBinder instance
	 * @param request
	 *            the current request
	 * @return the created model attribute, or {@code null}
	 * @throws Exception Exception
	 */
	protected Object createAttributeFromRequestValue(String sourceValue, String attributeName,
			MethodParameter parameter, WebDataBinderFactory binderFactory, 
			NativeWebRequest request) throws Exception {
		DataBinder binder = binderFactory.createBinder(request, null, attributeName);
		ConversionService conversionService = binder.getConversionService();
		if (conversionService != null) {
			TypeDescriptor source = TypeDescriptor.valueOf(String.class);
			TypeDescriptor target = new TypeDescriptor(parameter);
			if (conversionService.canConvert(source, target)) {
				return binder.convertIfNecessary(sourceValue, parameter.getParameterType(), parameter);
			}
		}
		return null;
	}

	/**
	 * Validate the model attribute if applicable.
	 * The default implementation checks for {@code @javax.validation.Valid}.
	 *
	 * @param binder
	 *            the DataBinder to be used
	 * @param parameter
	 *            the method parameter
	 */
	protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
		Annotation[] annotations = parameter.getParameterAnnotations();
		for (Annotation annot : annotations) {
			if (annot.annotationType().getSimpleName().startsWith("Valid")) {
				Object hints = AnnotationUtils.getValue(annot);
				binder.validate(hints instanceof Object[] ? (Object[]) hints : new Object[] { hints });
			}
		}
	}
}
