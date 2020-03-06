package com.jeecms.common;

import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.jeecms.front.config.FrontConfig;

/**
 * 注册前端Dispatcher
 * 
 * @author: tom
 * @date: 2019年1月5日 下午2:06:38
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Component
public class FrontDispatcherConfig {

	/**
	 * 前端 DispatcherServlet bean
	 * @Title: frontDispatcherServlet
	 * @return: DispatcherServlet
	 */
	@Bean(name = "frontDispatcherServlet")
	public DispatcherServlet frontDispatcherServlet() {
		AnnotationConfigWebApplicationContext servletAppContext = new AnnotationConfigWebApplicationContext();
		/*** 使用FrontConfig作为配置类 */
		servletAppContext.register(FrontConfig.class);
		DispatcherServlet dispatcherServlet = new DispatcherServlet(servletAppContext);
		return dispatcherServlet;
	}

	/**
	 * 注册 前端 DispatcherServlet 
	 * @Title: dispatcherServletRegistration
	 * @param multipartConfigProvider MultipartConfigElement
	 * @return: ServletRegistrationBean  ServletRegistrationBean
	 */
	@Bean(name = "frontDispatcherServletRegistration")
	public ServletRegistrationBean<DispatcherServlet> dispatcherServletRegistration(
			ObjectProvider<MultipartConfigElement> multipartConfigProvider) {
		ServletRegistrationBean<DispatcherServlet> registration = 
				new ServletRegistrationBean<DispatcherServlet>(
				frontDispatcherServlet());
		// 必须指定启动优先级，否则无法生效
		registration.setLoadOnStartup(1);
		registration.setName("frontDispatcherServlet");
		// 注册上传配置对象，否则后台不能处理上传
		MultipartConfigElement multipartConfig = multipartConfigProvider.getIfAvailable();
		if (multipartConfig != null) {
			registration.setMultipartConfig(multipartConfig);
		}
		return registration;
	}
    
}
