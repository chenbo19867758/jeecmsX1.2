package com.jeecms.common.configuration;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * WebSocketConfig
 * 
 * @author: tom
 * @date: 2018年12月17日 下午5:03:28
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Configurable
public class WebSocketConfig {
	/**
	 * 判断当前容器没有用户自己定义ConfigurableServletWebServerFactory：嵌入式的Servlet容器工厂；作用：
	 * 创建嵌入式的Servlet容器 自动注册使用了@ServerEndpoint注解声明的Websocket endpoint.
	 * 注意，如果使用独立的servlet容器，而不是直接使用springboot的内置容器，
	 * 就不要注入ServerEndpointExporter，因为它将由容器自己提供和管理。
	 * 
	 * @Title: serverEndpointExporter
	 * @return: ServerEndpointExporter ServerEndpointExporter中文
	 */
//	@Bean
//	@ConditionalOnBean(value = ConfigurableServletWebServerFactory.class, search = SearchStrategy.CURRENT)
//	public ServerEndpointExporter serverEndpointExporter() {
//		return new ServerEndpointExporter();
//	}

}
