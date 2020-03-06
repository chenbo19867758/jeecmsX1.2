package com.jeecms.front;

import com.jeecms.common.configuration.ContextConfig;
import com.jeecms.common.configuration.ThreadPoolConfiguration;
import com.jeecms.common.configuration.WebConfig;
import com.jeecms.common.configuration.WebSocketConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.WebApplicationInitializer;

/**
 * 前台application
 * 
 * @author: tom
 * @date: 2019年3月11日 下午1:49:04
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Configuration
@PropertySource({ "classpath:config/spring.jpa.properties" })
@ImportResource({ "classpath:config/context*.xml","classpath:config/**/context*.xml" })
@EnableAutoConfiguration(exclude = { JmxAutoConfiguration.class, ElasticsearchAutoConfiguration.class,
		ElasticsearchDataAutoConfiguration.class, FreeMarkerAutoConfiguration.class})
@Import({ ContextConfig.class, WebConfig.class, WebSocketConfig.class, ThreadPoolConfiguration.class })
@EnableTransactionManagement
public class CmsFrontApplication extends SpringBootServletInitializer implements WebApplicationInitializer {

	static Logger logger = LoggerFactory.getLogger(CmsFrontApplication.class);

	/**
	 * war方式启动的处理方法
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return configureApplication(builder);
	}

	/**
	 * jar方式启动的处理方法
	 * 
	 * @param args
	 *            String
	 * @throws Exception
	 *             Exception
	 */
	public static void main(String[] args) throws Exception {
		configureApplication(new SpringApplicationBuilder()).run(args);
	}

	/**
	 * war方式启动和jar方式启动共用的配置
	 * 
	 * @param builder
	 *            SpringApplicationBuilder
	 */
	private static SpringApplicationBuilder configureApplication(SpringApplicationBuilder builder) {
		return builder.sources(CmsFrontApplication.class)
				.listeners(new ApplicationListener<ApplicationEnvironmentPreparedEvent>() {
					// 在应用环境准备好后执行（Application.properties和PoropertySource已读取），
					// 此时BeanFactory还未准备好（Bean还未创建）
					@SuppressWarnings("unused")
					@Override
					public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
						ConfigurableEnvironment env = event.getEnvironment();
					}
				});
	}

}
