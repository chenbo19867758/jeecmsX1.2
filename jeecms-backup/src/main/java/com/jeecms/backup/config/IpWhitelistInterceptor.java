package com.jeecms.backup.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ip白名单拦截器
 *
 * @author Zhu Kaixiao
 * @version 1.0
 * @date 2019/8/2 14:21
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class IpWhitelistInterceptor extends HandlerInterceptorAdapter {
	static Logger log = LoggerFactory.getLogger(IpWhitelistInterceptor.class);
    private BackupProperties backupProperties;
	
    

    public IpWhitelistInterceptor(BackupProperties backupProperties) {
		super();
		this.backupProperties = backupProperties;
	}


	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String accessIp = getIpAddress(request);
        if (backupProperties.getIpWhitelist().contains(accessIp)) {
            return true;
        } else {
            log.info("已拦截非法访问IP[{}]", accessIp);
            response.sendError(404);
            return false;
        }
    }


    /**
     * 从request中获取请求方IP
     *
     * @param request
     * @return java.lang.String
     * @author Zhu Kaixiao
     * @date 2019/8/2 15:26
     **/
    private static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
