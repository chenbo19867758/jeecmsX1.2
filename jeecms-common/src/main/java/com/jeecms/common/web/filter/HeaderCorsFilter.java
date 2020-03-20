/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.common.web.filter;

/**
 *
* TODO
* @author: tom
* @date:   2019年8月15日 下午5:29:45     
*/
import com.jeecms.common.constants.SysConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 跨域过滤器
 * 可参考：https://www.cnblogs.com/yuansc/p/9076604.html
 * @author: tom
 * @date: 2019年8月15日 下午5:47:04
 */
public class HeaderCorsFilter implements Filter {
	protected final Logger log = LoggerFactory.getLogger(HeaderCorsFilter.class);
	/**
	 * 请求执行开始时间
	 */
	public static final String START_TIME = "_start_time";

	/**
	 * 初始化
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	/**
	 * 过滤
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		long time = System.currentTimeMillis();
		req.setAttribute(START_TIME, time);
		HttpServletResponse resp = (HttpServletResponse) response;
		resp.setHeader("Access-Control-Allow-Origin", "*");
		//  真实请求允许的方法
		resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE,PUT");
		// 预检请求的有效期，单位为秒。有效期内，不会重复发送预检请求
		resp.setHeader("Access-Control-Max-Age", "3600");
		// 服务器允许使用的字段
		resp.setHeader("Access-Control-Allow-Headers",
				SysConstants.DEFAULT_ALLOW_HEADERS + tokenHeader + "," + redirectHeader + "," + siteIdHeader);
		// 是否允许用户发送、处理 cookie
		resp.setHeader("Access-Control-Allow-Credentials", "true");
		chain.doFilter(request, resp);
		time = System.currentTimeMillis() - time;
		log.debug("process in {} ms: {}", time, req.getRequestURI());
	}

	/**
	 * 
	 */
	public void destroy() {
	}

	public HeaderCorsFilter(String tokenHeader, String redirectHeader, String siteIdHeader) {
		super();
		this.tokenHeader = tokenHeader;
		this.redirectHeader = redirectHeader;
		this.siteIdHeader = siteIdHeader;
	}

	private String tokenHeader;
	private String redirectHeader;
	private String siteIdHeader;
}