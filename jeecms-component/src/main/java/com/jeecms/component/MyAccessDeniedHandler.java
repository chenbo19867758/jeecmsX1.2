package com.jeecms.component;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.exception.SystemExceptionEnum;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.common.web.util.ResponseUtils;
import com.jeecms.util.SystemContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 无权限访问时触发
 * 
 * @Author tom
 */
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
        @Override
        public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                        AccessDeniedException e) throws IOException, ServletException {
                // 返回json形式的错误信息
                String msg = MessageResolver.getMessage(SystemExceptionEnum.HAS_NOT_PERMISSION.getCode(),
                                SystemExceptionEnum.HAS_NOT_PERMISSION.getDefaultMessage());
                String code = SystemExceptionEnum.HAS_NOT_PERMISSION.getCode();
                CoreUser user = SystemContextUtils.getCoreUser();
                if (user == null) {
                        msg = MessageResolver.getMessage(SystemExceptionEnum.ACCOUNT_NOT_LOGIN.getCode(),
                                        SystemExceptionEnum.ACCOUNT_NOT_LOGIN.getDefaultMessage());
                        code = SystemExceptionEnum.ACCOUNT_NOT_LOGIN.getCode();
                }
                ResponseInfo responseInfo = new ResponseInfo(code, msg, httpServletRequest,
                                httpServletRequest.getRequestURI(), null);
                ResponseUtils.renderJson(httpServletResponse, JSON.toJSONString(responseInfo));
        }
}
