package com.jeecms.common.web;

import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * HandlerMethodArgumentResolver
 * 
 * @author: tom
 * @date: 2018年12月27日 上午9:25:23
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public abstract class BaseMethodArgumentResolver implements HandlerMethodArgumentResolver {

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
					result.put(name.substring(namePrefixLength + 1), 
							new String[] { variables.get(name) });
				} else {
					result.put(name, new String[] { variables.get(name) });
				}
			}
		}

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
					result.put(name.substring(namePrefixLength + 1), 
							request.getParameterValues(name));
				} else {
					result.put(name, request.getParameterValues(name));
				}
			}
		}

		return result;
	}

	private boolean illegalChar(char ch) {
		return ch != '.' && ch != '_' && !(ch >= '0' && ch <= '9');
	}

	@SuppressWarnings("unchecked")
	protected final Map<String, String> getUriTemplateVariables(NativeWebRequest request) {
		Map<String, String> variables = (Map<String, String>) request
				.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE,
						RequestAttributes.SCOPE_REQUEST);
		return (variables != null) ? variables : Collections.emptyMap();
	}
}
