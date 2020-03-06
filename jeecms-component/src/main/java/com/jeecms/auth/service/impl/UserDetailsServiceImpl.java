package com.jeecms.auth.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.dto.RequestLoginUser;
import com.jeecms.auth.dto.UserDetailImpl;
import com.jeecms.auth.service.CoreUserService;
import com.jeecms.auth.service.MyUserDetailsService;
import com.jeecms.auth.util.SecurityModelFactory;
import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.common.web.util.RequestUtils;

/**
 * Spring Security 用于将 数据库中的用户信息转换成 userDetail 对象的服务类的实现类
 * 
 * @Author tom
 */
@Service
public class UserDetailsServiceImpl implements MyUserDetailsService {

        @Autowired
        private CoreUserService userService;
        @Value("${token.header}")
        private String tokenHeader;

        /**
         * 获取 userDetail
         * 
         * @param username
         *                用户名
         * @throws UsernameNotFoundException
         *                 用户未找到异常
         */
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                CoreUser user = userService.findByUsernameAndAuth(username);
                if (user == null) {
                        throw new UsernameNotFoundException(
                                        String.format("No user found with username '%s'.", username));
                } else {
                        HttpServletRequest request = RequestUtils.getHttpServletRequest();
                        UserDetailImpl  userDetails = SecurityModelFactory.createUserDetail(user, session.getSessionId(request), "");
                        /**混淆码设置到request中，在密码认证中获取*/
                        request.setAttribute(RequestLoginUser.LOGIN_SALT, user.getSalt());
                        request.setAttribute(RequestLoginUser.LOGIN_IDENTITY, username);
                        return userDetails;
                }
        }

        @Override
        public UserDetails loadUserByUsername(String username, String token) throws UsernameNotFoundException {
                UserDetails userDetails = loadUserByUsername(username);
                UserDetailImpl userDetail = (com.jeecms.auth.dto.UserDetailImpl) userDetails;
                userDetail.setToken(token);
                return userDetail;
        }

        @Autowired
        private SessionProvider session;

}
