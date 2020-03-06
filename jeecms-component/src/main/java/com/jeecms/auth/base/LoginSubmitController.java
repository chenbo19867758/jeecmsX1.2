package com.jeecms.auth.base;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.auth.constants.AuthConstant;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.dto.RequestLoginUser;
import com.jeecms.auth.service.LoginService;
import com.jeecms.common.annotation.ContentSecurity;
import com.jeecms.common.annotation.ContentSecurityAttribute;
import com.jeecms.common.base.domain.RequestLoginTarget;
import com.jeecms.common.exception.AccountCredentialExceptionInfo;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionEnum;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.util.SystemContextUtils;

/**
 * 登录controller
 * 
 * @author: tom
 * @date: 2018年3月3日 下午3:13:10
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@RestController
public class LoginSubmitController {

        @Autowired
        private LoginService loginService;

        /**
         * 自定义token head标识符
         */
        @Value("${token.header}")
        protected String tokenHeader;
        @Value("${user.auth}")
        protected String userAuth;

        /**
         * 登录
         * 
         * @Title: login
         * @param request
         *                HttpServletRequest
         * @param response
         *                HttpServletResponse
         * @param requestLoginUser
         *                RequestLoginUser
         * @throws GlobalException
         *                 GlobalException
         */
        @ContentSecurity
        public ResponseInfo login(HttpServletRequest request, HttpServletResponse response,
                        @ContentSecurityAttribute("requestLoginUser") @Valid RequestLoginUser requestLoginUser)
                                        throws GlobalException {
                Map<String, Object> data = null;
                data = loginService.login(requestLoginUser, request, response);
                CmsSite site = SystemContextUtils.getSite(request);
                String redirectUrl = site.getMemberRedirectUrl();
                return new ResponseInfo(SystemExceptionEnum.SUCCESSFUL.getCode(),
                                SystemExceptionEnum.SUCCESSFUL.getDefaultMessage(),  redirectUrl, data);
        }

        /**
         * 退出
         * 
         * @Title: logout
         * @param token
         *                token
         * @param request
         *                HttpServletRequest
         * @param response
         *                HttpServletResponse
         * @throws GlobalException
         *                 GlobalException
         */
        public ResponseInfo logout(String token, HttpServletRequest request, HttpServletResponse response)
                        throws GlobalException {
                loginService.logout(token,request,response);
                return new ResponseInfo(SystemExceptionEnum.SUCCESSFUL.getCode(),
                                SystemExceptionEnum.SUCCESSFUL.getDefaultMessage(), new Object());
        }

        public String getTokenHeader() {
                return tokenHeader;
        }

        public String getUserAuth() {
                return userAuth;
        }

        public void setTokenHeader(String tokenHeader) {
                this.tokenHeader = tokenHeader;
        }

        public void setUserAuth(String userAuth) {
                this.userAuth = userAuth;
        }

        final String[] allowedFields = new String[] { "identity", "desStr", "target", "captcha", "uniquePushId",
                "token" };

        @InitBinder
        public void initBinder(WebDataBinder binder) {
                binder.setAllowedFields(allowedFields);
        }

}
