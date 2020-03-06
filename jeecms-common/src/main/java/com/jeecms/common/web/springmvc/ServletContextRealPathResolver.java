package com.jeecms.common.web.springmvc;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

/**
 * RealPathResolver实现
 * 
 * @author: tom
 * @date: 2018年12月27日 上午9:40:12
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Component
public class ServletContextRealPathResolver implements RealPathResolver, ServletContextAware {
	@Override
	public String get(String path) {
		String realpath = context.getRealPath(path);
		// tomcat8.0获取不到真实路径，通过/获取路径
		if (StringUtils.isBlank(realpath)) {
			realpath = context.getRealPath("/") + path;
		}
		return realpath;
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.context = servletContext;
	}

	private ServletContext context;
}
