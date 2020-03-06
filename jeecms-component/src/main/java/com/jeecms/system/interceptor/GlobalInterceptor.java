package com.jeecms.system.interceptor;

import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionInfo;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.util.CookieUtils;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.common.web.util.ResponseUtils;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.GlobalConfig;
import com.jeecms.system.service.CmsSiteService;
import com.jeecms.system.service.GlobalConfigService;
import com.jeecms.util.SystemContextUtils;

/**
 * 全局拦截器，设置全局通用参数（不分管理平台）
 * 
 * @author: tom
 * @date: 2018年4月14日 下午5:52:34
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Component
public class GlobalInterceptor extends HandlerInterceptorAdapter {
        

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                        throws ServletException, GlobalException {
                ResponseUtils.setHeader(response);
                CmsSite site = siteService.getCurrSite(request, response);
                SystemContextUtils.setSite(request, site);
                GlobalConfig config = configMng.get();
                SystemContextUtils.setGlobalConfig(request, config);
                SystemContextUtils.setResponseConfigDto(request, configMng.filterConfig(request, config));
                return true;
        }

        
        @Autowired
        private GlobalConfigService configMng;
        @Autowired
        private CmsSiteService siteService;

}
