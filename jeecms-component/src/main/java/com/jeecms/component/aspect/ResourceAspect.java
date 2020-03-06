package com.jeecms.component.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jeecms.common.resource.annotations.ResourceMethod;
import com.jeecms.resource.service.ResourcePushService;

import java.util.List;

/**
 * 统一资源Aop切面定义 根据自定义注解配置自动设置配置的资源类型到指定的字段
 * 
 * @author tom
 */
@Component
@Aspect
public class ResourceAspect {
	/**
	 * logback
	 */
	Logger logger = LoggerFactory.getLogger(ResourceAspect.class);

	/**
	 * 资源处理业务逻辑
	 */
	@Autowired
	@Qualifier("ResourcePushSupport")
	ResourcePushService resourcePushService;

	/**
	 * 资源设置切面方法 拦截配置了@ResourceMethod注解的class method，cglib仅支持class 方法切面，接口切面不支持
	 * 
	 * @param proceedingJoinPoint
	 *            切面方法实例
	 * @param resourceMethod
	 *            方法注解实例
	 * @return
	 * @throws Throwable
	 */
	@SuppressWarnings("unchecked")
	@Around(value = "@annotation(resourceMethod)")
	public Object resourcePutAround(ProceedingJoinPoint proceedingJoinPoint, ResourceMethod resourceMethod)
			throws Throwable {
		/**
		 * 执行方法，获取返回值
		 */
		Object result = proceedingJoinPoint.proceed();
		if (StringUtils.isEmpty(result)) {
			return result;
		}
		/**
		 * 返回值为List集合时
		 */
		if (result instanceof List) {
			List<Object> list = (List<Object>) result;
			resourcePushService.push(list);
		}
		/**
		 * 返回值为单值时
		 */
		else {
			resourcePushService.push(result);
		}
		return result;
	}

}
