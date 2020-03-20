package com.jeecms.common.web.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.jeecms.common.web.CustomerArgumentResolver;
import org.apache.commons.lang3.StringUtils;

import com.jeecms.common.constants.WebConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * XssFilter
 * 
 * @author: tom
 * @date: 2018年11月26日 下午6:58:23
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class XssFilter implements Filter {

	private Logger logger = LoggerFactory.getLogger(XssFilter.class);


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletRequest xssRequest = req;
		/** 不拦截模板和资源 */
		if (!isExcludeUrl(req)) {
			xssRequest = new XssHttpServletRequestWrapper(req);
		}
		chain.doFilter(xssRequest, response);
	}

	private boolean isExcludeUrl(HttpServletRequest request) {
		String uri = request.getRequestURI();
		String ctx = request.getContextPath();
		if (StringUtils.isNoneBlank(ctx)) {
			uri = uri.substring(ctx.length());
		}


		//logger.debug("请求访问的路径："+uri);

		if (!uri.startsWith("/r/cms/www/") && !uri.startsWith("/jeecms/")) {
			System.out.println("请求访问的路径："+uri);
		}


		if (uri.startsWith(WebConstants.ADMIN_PREFIX + "/template")) {
			return true;
		}
		if (uri.startsWith(WebConstants.ADMIN_PREFIX + "/resource")) {
			return true;
		}
		if (uri.endsWith(WebConstants.DYNAMIC_SUFFIX)) {
			return true;
		}
		return false;
	}

	@Override
	public void destroy() {
	}

}
