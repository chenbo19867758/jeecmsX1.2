package com.jeecms.common;

import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.jeecms.common.constants.WebConstants;
import com.jeecms.front.config.MemberConfig;


/**
 * 注册会员 Dispatcher
 * @author: tom
 * @date:   2019年1月5日 下午2:06:55     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Component
public class MemberDispatcherConfig {
	/**
	 * 会员中心DispatcherServlet。使用不同的DispatcherServlet
	 * 
	 * @return
	 */
	@Bean(name = "memberDispatcherServlet")
	public DispatcherServlet memberDispatcherServlet() {
		AnnotationConfigWebApplicationContext servletAppContext = new AnnotationConfigWebApplicationContext();
		// 使用MemberConfig作为配置类
		servletAppContext.register(MemberConfig.class);
		DispatcherServlet dispatcherServlet = new DispatcherServlet(servletAppContext);
		return dispatcherServlet;
	}

	/**
	 * 手动注册会员中心DispatcherServlet。只处理`/member/*`相关请求
	 * 
	 * @param multipartConfigProvider
	 *            获取springboot自动定义的上传配置对象，实现前后台统一的上传配置
	 * @return
	 */
	@Bean(name = "memberDispatcherServletRegistration")
	public ServletRegistrationBean<DispatcherServlet> memberDispatcherServletRegistration(
			ObjectProvider<MultipartConfigElement> multipartConfigProvider) {
		ServletRegistrationBean<DispatcherServlet> registration = 
				new ServletRegistrationBean<DispatcherServlet>(
				memberDispatcherServlet(), WebConstants.MEMBER_PREFIX + "/*");
		// 必须指定启动优先级，否则无法生效
		registration.setLoadOnStartup(2);
		registration.setName("memberDispatcherServlet");
		// 注册上传配置对象，否则后台不能处理上传
		MultipartConfigElement multipartConfig = multipartConfigProvider.getIfAvailable();
		if (multipartConfig != null) {
			registration.setMultipartConfig(multipartConfig);
		}
		return registration;
	}
}
