package com.jeecms.common.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.StringUtils;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import com.jeecms.common.annotation.ParameterModel;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 自定义参数装载
 * 
 * @author: tom
 * @date: 2018年12月27日 上午9:27:14
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class CustomerArgumentResolver implements HandlerMethodArgumentResolver {
	/**
	 * 日志对象
	 */
	private Logger logger = LoggerFactory.getLogger(CustomerArgumentResolver.class);

	/**
	 * 该方法返回true时调用resolveArgument方法执行逻辑 spring家族的架构设计万变不离其宗啊，在之前event &
	 * listener也是用到了同样的方式
	 * 
	 * @param methodParameter
	 *            MethodParameter
	 * @return
	 */
	@Override
	public boolean supportsParameter(MethodParameter methodParameter) {
		return methodParameter.hasParameterAnnotation(ParameterModel.class);
	}

	/**
	 * 装载参数
	 * 
	 * @param methodParameter
	 *            方法参数
	 * @param modelAndViewContainer
	 *            返回视图容器
	 * @param nativeWebRequest
	 *            本次请求对象
	 * @param webDataBinderFactory
	 *            数据绑定工厂
	 * @throws Exception
	 *             Exception
	 */
	@Override
	public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
			NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
		String parameterName = methodParameter.getParameterName();
		logger.info("参数名称：{}", parameterName);
		/**
		 * 目标返回对象 如果Model存在该Attribute时从module内获取并设置为返回值
		 * 如果Model不存在该Attribute则从request parameterMap内获取并设置为返回值
		 */
		if (modelAndViewContainer != null) {
			Object target = modelAndViewContainer.containsAttribute(parameterName)
					? modelAndViewContainer.getModel().get(parameterName)
					: createAttribute(parameterName, methodParameter, webDataBinderFactory, nativeWebRequest);
			/**
			 * 返回内容，这里返回的内容才是最终装载到参数的值
			 */
			return target;
		}
		return new Object();
	}

	/**
	 * 根据参数attributeName获取请求的值
	 * 
	 * @param attributeName
	 *            请求参数
	 * @param parameter
	 *            method 参数对象
	 * @param binderFactory
	 *            数据绑定工厂
	 * @param request
	 *            请求对象
	 * @throws Exception
	 *             Exception
	 */
	protected Object createAttribute(String attributeName, MethodParameter parameter,
			WebDataBinderFactory binderFactory, NativeWebRequest request) throws Exception {
		/**
		 * 获取attributeName的值
		 */
		String value = getRequestValueForAttribute(attributeName, request);

		/**
		 * 如果存在值
		 */
		if (value != null) {
			/**
			 * 进行类型转换 检查请求的类型与目标参数类型是否可以进行转换
			 */
			Object attribute = convertAttributeToParameterValue(value, attributeName, parameter, binderFactory,
					request);
			/**
			 * 如果存在转换后的值，则返回
			 */
			if (attribute != null) {
				return attribute;
			}
		} else {
			/**
			 * 检查request parameterMap 内是否存在以attributeName作为前缀的数据
			 * 如果存在则根据字段的类型来进行设置值、集合、数组等
			 */
			Object attribute = putParameters(parameter, request);
			if (attribute != null) {
				return attribute;
			}
		}
		/**
		 * 如果以上两种条件不符合，直接返回初始化参数类型的空对象
		 */
		return BeanUtils.instantiateClass(parameter.getParameterType());
	}

	/**
	 * 将attribute的值转换为parameter参数值类型
	 * 
	 * @param sourceValue
	 *            源请求值
	 * @param attributeName
	 *            参数名
	 * @param parameter
	 *            目标参数对象
	 * @param binderFactory
	 *            数据绑定工厂
	 * @param request
	 *            请求对象
	 * @throws Exception
	 *             Exception
	 */
	protected Object convertAttributeToParameterValue(String sourceValue, String attributeName,
			MethodParameter parameter, WebDataBinderFactory binderFactory, NativeWebRequest request) throws Exception {
		/**
		 * 获取类型转换业务逻辑实现类
		 */
		DataBinder binder = binderFactory.createBinder(request, null, attributeName);
		ConversionService conversionService = binder.getConversionService();
		if (conversionService != null) {
			/**
			 * 源类型描述
			 */
			TypeDescriptor source = TypeDescriptor.valueOf(String.class);
			/**
			 * 根据目标参数对象获取目标参数类型描述
			 */
			TypeDescriptor target = new TypeDescriptor(parameter);
			/**
			 * 验证是否可以进行转换
			 */
			if (conversionService.canConvert(source, target)) {
				/**
				 * 返回转换后的值
				 */
				return binder.convertIfNecessary(sourceValue, parameter.getParameterType(), parameter);
			}
		}
		return null;
	}

	/**
	 * 从request parameterMap集合内获取attributeName的值
	 * 
	 * @param attributeName
	 *            参数名称
	 * @param request
	 *            请求对象
	 * @return
	 */
	protected String getRequestValueForAttribute(String attributeName, NativeWebRequest request) {
		/**
		 * 获取PathVariables参数集合
		 */
		Map<String, String> variables = getUriTemplateVariables(request);
		/**
		 * 如果PathVariables参数集合内存在该attributeName 直接返回相对应的值
		 */
		if (StringUtils.hasText(variables.get(attributeName))) {
			return variables.get(attributeName);
		} else if (StringUtils.hasText(request.getParameter(attributeName))) {
			/**
			 * 如果request parameterMap内存在该attributeName 直接返回相对应的值
			 */
			return request.getParameter(attributeName);
		} else {
			// 不存在时返回null
			return null;
		}
	}

	/**
	 * 获取指定前缀的参数：包括uri varaibles 和 parameters 是否截取掉namePrefix的前缀
	 * 
	 * @param namePrefix
	 *            前缀
	 * @param request
	 *            NativeWebRequest
	 * @return
	 */
	protected Map<String, String[]> getPrefixParameterMap(String namePrefix, NativeWebRequest request,
			boolean subPrefix) {
		Map<String, String[]> result = new HashMap<>(10);
		/**
		 * 从PathVariables内获取该前缀的参数列表
		 */
		Map<String, String> variables = getUriTemplateVariables(request);

		int namePrefixLength = namePrefix.length();
		for (String name : variables.keySet()) {
			if (name.startsWith(namePrefix)) {

				// page.pn 则截取 pn
				if (subPrefix) {
					char ch = name.charAt(namePrefix.length());
					// 如果下一个字符不是 数字 . _ 则不可能是查询 只是前缀类似
					if (illegalChar(ch)) {
						continue;
					}
					result.put(name.substring(namePrefixLength + 1), new String[] { variables.get(name) });
				} else {
					result.put(name, new String[] { variables.get(name) });
				}
			}
		}

		/**
		 * 从request parameterMap集合内获取该前缀的参数列表
		 */
		Iterator<String> parameterNames = request.getParameterNames();
		while (parameterNames.hasNext()) {
			String name = parameterNames.next();
			if (name.startsWith(namePrefix)) {
				// page.pn 则截取 pn
				if (subPrefix) {
					char ch = name.charAt(namePrefix.length());
					// 如果下一个字符不是 数字 . _ 则不可能是查询 只是前缀类似
					if (illegalChar(ch)) {
						continue;
					}
					result.put(name.substring(namePrefixLength + 1), request.getParameterValues(name));
				} else {
					result.put(name, request.getParameterValues(name));
				}
			}
		}

		return result;
	}

	/**
	 * 验证参数前缀是否合法
	 * 
	 * @param ch
	 *            字符
	 * @return
	 */
	private boolean illegalChar(char ch) {
		return ch != '.' && ch != '_' && !(ch >= '0' && ch <= '9');
	}

	/**
	 * 获取PathVariables集合
	 * 
	 * @param request
	 *            请求对象
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected final Map<String, String> getUriTemplateVariables(NativeWebRequest request) {
		Map<String, String> variables = (Map<String, String>) request
				.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
		return (variables != null) ? variables : Collections.emptyMap();
	}

	/**
	 * 从request内获取parameter前缀的所有参数 并根据parameter的类型将对应字段的值设置到parmaeter对象内并返回
	 * 
	 * @param parameter
	 *            MethodParameter
	 * @param request
	 *            NativeWebRequest
	 * @return
	 */
	protected Object putParameters(MethodParameter parameter, NativeWebRequest request) {
		/**
		 * 根据请求参数类型初始化空对象
		 */
		Object object = BeanUtils.instantiateClass(parameter.getParameterType());
		/**
		 * 获取指定前缀的请求参数集合
		 */
		String paramName = parameter.getParameterName();
		Map<String, String[]> parameters;
		if (org.apache.commons.lang3.StringUtils.isNoneBlank(paramName)) {
			parameters = getPrefixParameterMap(paramName, request, true);
			Iterator<String> iterator = parameters.keySet().iterator();
			while (iterator.hasNext()) {
				// 字段名称
				String fieldName = iterator.next();
				// 请求参数值
				String[] parameterValue = parameters.get(fieldName);
				try {
					Field field = object.getClass().getDeclaredField(fieldName);
					field.setAccessible(true);

					// 字段的类型
					Class<?> fieldTargetType = field.getType();

					/**
					 * List（ArrayList、LinkedList）类型 将数组类型的值转换为List集合对象
					 */
					if (List.class.isAssignableFrom(fieldTargetType)) {
						field.set(object, Arrays.asList(parameterValue));
					} else if (Object[].class.isAssignableFrom(fieldTargetType)) {
						/**
						 * Object数组类型，直接将数组值设置为目标字段的值
						 */
						field.set(object, parameterValue);
					} else {
						/**
						 * 单值时获取数组索引为0的值
						 */
						field.set(object, parameterValue[0]);
					}
				} catch (Exception e) {
					logger.error(java.text.Normalizer.normalize(
							String.format("Set Field：%s Value Error，In %s", fieldName, object.getClass().getName()),
							java.text.Normalizer.Form.NFKD));
					continue;
				}
			}
		}
		return object;
	}
}
