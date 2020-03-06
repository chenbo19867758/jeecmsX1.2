package com.jeecms.common.web;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * ApplicationContext 工具类
 * 
 * @author: tom
 * @date: 2018年12月27日 上午9:25:03
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {
	/**
	 * 上下文对象实例
	 */
	private static ApplicationContext ctx;

	public static ApplicationContext getCtx() {
		return ctx;
	}

	public static void setCtx(ApplicationContext ctx) {
		ApplicationContextProvider.ctx = ctx;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		setCtx(applicationContext);
	}

	/**
	 * 获取applicationContext
	 * 
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return getCtx();
	}

	/**
	 * 通过name获取 Bean.
	 * 
	 * @param name
	 *            名称
	 * @return
	 */
	public static Object getBean(String name) {
		if(getApplicationContext()!=null) {
			return getApplicationContext().getBean(name);
		}
		return null;
	}

	/**
	 * 通过class获取Bean.
	 * 
	 * @param clazz
	 *            类型
	 * @return
	 */
	public static <T> T getBean(Class<T> clazz) {
		if(getApplicationContext()!=null) {
			return getApplicationContext().getBean(clazz);
		}
		return null;
	}

	/**
	 * 通过name,以及Clazz返回指定的Bean
	 * 
	 * @param name
	 *            名称
	 * @param clazz
	 *            类型
	 * @return
	 */
	public static <T> T getBean(String name, Class<T> clazz) {
		if(getApplicationContext()!=null) {
			return getApplicationContext().getBean(name, clazz);
		}
		return null;
	}

	public static <T> Map<String, T> getBeansOfType(Class<T> type) {
		return getApplicationContext().getBeansOfType(type);
	}

}
