package com.jeecms.common.configuration;

import java.util.List;
import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.mobile.device.DeviceHandlerMethodArgumentResolver;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.mobile.device.LiteDeviceResolver;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.jeecms.common.constants.SysConstants;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.interceptor.ContentSecurityInterceptor;
import com.jeecms.common.jsonfilter.converter.JsonFilterHttpMessageConverter;
import com.jeecms.common.web.ContentSecurityMethodArgumentResolver;
import com.jeecms.common.web.util.UrlUtil;

/**
 * 全局WebMvcConfigurer 配置 客户端识别 国际化 全局拦截器 FastJson转换器 错误异常页面
 * 
 * @author: tom
 * @date: 2018年12月24日 下午6:00:20
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	/**
	 * 设备识别器，用于识别是否手机访问
	 */
	@Bean
	public LiteDeviceResolver liteDeviceResolver() {
		LiteDeviceResolver liteDeviceResolver = new LiteDeviceResolver();
		return liteDeviceResolver;
	}

	@Bean
	public DeviceResolverHandlerInterceptor deviceResolverHandlerInterceptor() {
		return new DeviceResolverHandlerInterceptor();
	}

	@Bean
	public DeviceHandlerMethodArgumentResolver deviceHandlerMethodArgumentResolver() {
		return new DeviceHandlerMethodArgumentResolver();
	}


	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(deviceResolverHandlerInterceptor()).excludePathPatterns(UrlUtil.getSysresurl(),
				UrlUtil.getResourceurl());
		// 加密传参数拦截器
		registry.addInterceptor(new ContentSecurityInterceptor()).excludePathPatterns(UrlUtil.getSysresurl(),
				UrlUtil.getResourceurl());
		registry.addInterceptor(localeChangeInterceptor()).excludePathPatterns(UrlUtil.getSysresurl(),
				UrlUtil.getResourceurl());
	}

	/**
	 * 修改自定义消息转换器
	 * 
	 * @param converters
	 *            消息转换器列表
	 */
	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.clear();
		JsonFilterHttpMessageConverter jsonFilterHttpMessageConverter = new JsonFilterHttpMessageConverter();
		converters.add(jsonFilterHttpMessageConverter);
	}

	/**
	 * 添加参数装载
	 * 
	 * @param argumentResolvers
	 *            参数
	 */

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		/**
		 * 将自定义的参数装载添加到spring内托管,支持参数加密传输
		 */
		argumentResolvers.add(new ContentSecurityMethodArgumentResolver());
		argumentResolvers.add(deviceHandlerMethodArgumentResolver());
	}

	/**
	 * LocalValidatorFactoryBean
	 * 
	 * @Title: validator
	 * @return: LocalValidatorFactoryBean
	 */
	@Bean
	public LocalValidatorFactoryBean validator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.setValidationMessageSource(this.messageSource);
		return localValidatorFactoryBean;
	}

	/**
	 * 国际化LocaleResolver
	 * 
	 * @Title: localeResolver
	 * @return: LocaleResolver
	 */
	@Bean
	public LocaleResolver localeResolver() {
		CookieLocaleResolver clr = new CookieLocaleResolver();
		clr.setCookieName("localeCookie");
		/** 设置默认区域 */
		clr.setCookieMaxAge(3600);// 设置cookie有效期.
		return clr;
	}

	/**
	 * 国际化变更拦截器
	 * 
	 * @Title: localeChangeInterceptor
	 * @return: LocaleChangeInterceptor
	 */
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}

	@Component
	public class ErrorPageConfig implements ErrorPageRegistrar {

		@Override
		public void registerErrorPages(ErrorPageRegistry errorPageRegistry) {
			// 1、按错误的类型显示错误的网页
			// 错误类型为404，找不到网页的，默认显示404.html网页
			ErrorPage e404 = new ErrorPage(HttpStatus.NOT_FOUND, WebConstants.ERROR_404);
			// 错误类型为500，表示服务器响应错误，默认显示500.html网页
			ErrorPage e500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, WebConstants.ERROR_500);
			errorPageRegistry.addErrorPages(e404, e500);
		}

	}

	@Override
	public Validator getValidator() {
		return validator();
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		/** 跨域设置 */
		registry.addMapping("/**")
				/** 支持所有域名 */
				.allowedOrigins("*")
				/** allowedHeaders 通配* 低版本浏览器不支持，如IE系列，Chrome 49版本或以下版本 */
				.allowedHeaders(
						SysConstants.DEFAULT_ALLOW_HEADERS + tokenHeader + "," + redirectHeader + "," + siteIdHeader)
				/** 支持所有方法 */
				.allowedMethods("PUT", "DELETE", "GET", "POST", "OPTIONS")
				/** 设置针对同一个接口，1小时内，前端不会重复发送option预请求接口 */
				.allowCredentials(true).maxAge(3600);
		WebMvcConfigurer.super.addCorsMappings(registry);
	}
	
	/** 公用调度器 */
	@Bean(name = "commonDispatcherServlet")
	public DispatcherServlet commonDispatcherServlet() {
		AnnotationConfigWebApplicationContext servletAppContext = new AnnotationConfigWebApplicationContext();
		DispatcherServlet dispatcherServlet = new DispatcherServlet(servletAppContext);
		return dispatcherServlet;
	}

	/**
	 * 通用controller调度器
	 * 
	 * @Title: dispatcherServletRegistration DispatcherServlet
	 * @param multipartConfigProvider
	 *            MultipartConfigElement
	 * @return: ServletRegistrationBean
	 */
	@Bean(name = "commonDispatcherServletRegistration")
	public ServletRegistrationBean<DispatcherServlet> dispatcherServletRegistration(
			ObjectProvider<MultipartConfigElement> multipartConfigProvider) {
		ServletRegistrationBean<DispatcherServlet> registration;
		registration = new ServletRegistrationBean<DispatcherServlet>(commonDispatcherServlet(),
				WebConstants.COMMON_PREFIX + "/*");
		// 必须指定启动优先级，否则无法生效
		registration.setLoadOnStartup(0);
		registration.setName("commonDispatcherServlet");
		// 注册上传配置对象，否则不能处理上传
		MultipartConfigElement multipartConfig = multipartConfigProvider.getIfAvailable();
		if (multipartConfig != null) {
			registration.setMultipartConfig(multipartConfig);
		}
		return registration;
	}

	@Autowired
	private MessageSource messageSource;
	@Value("${token.header}")
	private String tokenHeader;
	@Value("${redirect.header}")
	private String redirectHeader;
	@Value("${siteId.header}")
	private String siteIdHeader;
}
