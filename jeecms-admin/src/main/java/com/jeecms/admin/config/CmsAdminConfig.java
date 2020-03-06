package com.jeecms.admin.config;

import com.jeecms.admin.interceptor.CmsAdminInterceptor;
import com.jeecms.common.web.ContentSecurityMethodArgumentResolver;
import com.jeecms.common.web.PageableArgumentResolver;
import com.jeecms.common.web.PaginableArgumentResolver;
import com.jeecms.common.web.RegisterCustomEditors;
import com.jeecms.system.interceptor.GlobalInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.*;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 平台管理后端配置类，并非 通用全局配置类， application的WebConfig
 *     通用全局配置类，通用全局配置类只允许存在一个。
 *     所以此类不能被springboot扫描在内当做组件（Configuration配置类注解） 具体的拦截请求处理业务没实现
 * @author: tom
 * @date: 2018年6月20日 下午5:23:42
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Configuration
@ImportResource({ "classpath:config/**/context*.xml" })
@ComponentScan({ "com.jeecms.admin.controller"})
@EnableAspectJAutoProxy
public class CmsAdminConfig extends DelegatingWebMvcConfiguration {
	static Logger logger = LoggerFactory.getLogger(CmsAdminConfig.class);
	/**
	 * 覆盖父类方法，避免调用{@link Application.WebConfig}的配置信息
	 */
	/*
	 * @Override public void setConfigurers(List<WebMvcConfigurer> configurers)
	 * { // do nothing. super.setConfigurers(configurers); }
	 */

	/**
	 * 注册{@lin
     * k
     * }和{@link PageableArgumentResolver}
	 * {@link PaginableArgumentResolver}
	 */
	@Bean
	@Override
	public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
		RequestMappingHandlerAdapter adapter = super.requestMappingHandlerAdapter();
		ConfigurableWebBindingInitializer bindingInitializer = getConfigurableWebBindingInitializer();
		RegisterCustomEditors.registerCustomEditors(bindingInitializer);
		adapter.setWebBindingInitializer(bindingInitializer);
		List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<HandlerMethodArgumentResolver>();
		// 分页Pageable参数解析器
		argumentResolvers.add(new PageableArgumentResolver());
		// Paginable参数解析器
		argumentResolvers.add(new PaginableArgumentResolver());
		argumentResolvers.add(new ContentSecurityMethodArgumentResolver());
		adapter.setCustomArgumentResolvers(argumentResolvers);
		return adapter;
	}

	@Bean
	public CmsAdminInterceptor cmsAdminInterceptor() {
		return new CmsAdminInterceptor();
	}

	@Bean
	public GlobalInterceptor globalInterceptor() {
		return new GlobalInterceptor();
	}

	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
		return new MethodValidationPostProcessor();
	}

	/**
	 * 注册拦截器。
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(globalInterceptor());
		registry.addInterceptor(cmsAdminInterceptor()).addPathPatterns("/**");
		super.addInterceptors(registry);
	}
}
