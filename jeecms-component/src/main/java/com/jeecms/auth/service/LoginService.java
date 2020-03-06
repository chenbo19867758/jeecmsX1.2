package com.jeecms.auth.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import com.jeecms.auth.domain.LoginDetail;
import com.jeecms.auth.dto.RequestLoginUser;
import com.jeecms.auth.dto.TokenDetail;
import com.jeecms.common.base.domain.RequestLoginTarget;
import com.jeecms.common.exception.GlobalException;

/**
 * 登录service接口
 * 
 * @Author tom
 */
public interface LoginService {
        /**
         * 获取认证信息
         * 
         * @Title: getUserDetails
         * @param userSource
         *                登录来源
         * @param identity
         *                登录标识
         * @param token
         *                登录产生的token
         * @return: UserDetails
         * @throws GlobalException
         *                 GlobalException
         */
        UserDetails getUserDetails(String userSource, String identity, String token) throws GlobalException;

        /**
         * 查询登录的用户
         * 
         * @param target
         *                RequestLoginTarget {@link RequestLoginTarget}
         * @param identity
         *                用户名\手机号\邮箱号
         * @return LoginDetail
         */
        LoginDetail getLoginDetail(RequestLoginTarget target, String identity);

        /**
         * 生成TOKEN
         * 
         * @param tokenDetail
         *                token
         * @return token字符串
         * @throws GlobalException
         *                 全局异常
         */
        String generateToken(TokenDetail tokenDetail) throws GlobalException;

        /**
         * 登录
         * 
         * @Title: login
         * @param requestLoginUser
         *                RequestLoginUser
         * @param request
         *                HttpServletRequest
         * @param response
         *                HttpServletResponse
         * @throws GlobalException
         *                 GlobalException
         * @return: Map
         */
        Map<String, Object> login(RequestLoginUser requestLoginUser, HttpServletRequest request,
                        HttpServletResponse response) throws GlobalException;

        /**
         * 特殊登录（无密钥登录）
         * 
         * @Title: login
         * @param target
         *                {@RequestLoginTarget}
         * @param identity
         *                用户名、手机号、邮箱
         * @param uniquePushId
         *                推送ID
         * @throws GlobalException
         *                 GlobalException
         * @return: Map
         */
        Map<String, Object> login(RequestLoginTarget target, String identity, String uniquePushId)
                        throws GlobalException;

        /**
         * 退出
         * 
         * @param token
         *                token值
         * @throws GlobalException
         *                 全局异常
         */
        void logout(String token, HttpServletRequest request, HttpServletResponse response) throws GlobalException;

        /**
         * 是否强制退出
         * 
         * @Title: forceLogout
         * @throws GlobalException
         *                 GlobalException
         * @return: boolean
         */
        boolean isForceLogout() throws GlobalException;

        /**
         * 强制退出
         * 
         * @Title: forceLogout
         * @throws GlobalException
         *                 GlobalException
         * @return: void
         */
        void forceLogout(HttpServletRequest request, HttpServletResponse response) throws GlobalException;

        /**
         * 认证用户
         * 
         * @param userDetails
         *                用户
         * @return 认证
         */
        Authentication setSecuriyAuth(UserDetails userDetails);

        /**是否允许同一账号异地登录 ,不允许则 若是已存在登陆用户则踢出之前的登录用户 
         * @Title: tryValidRepeatSession
         * @param request HttpServletRequest
         * @param response HttpServletResponse
         * @return: void
         */
        void tryValidRepeatSession(HttpServletRequest request, HttpServletResponse response);
        
        /**
         * 尝试token登录
         * @Title: tryTokenLogin
         * @param request HttpServletRequest
         * @param response HttpServletResponse
         * @return: void
         */
        void tryTokenLogin(HttpServletRequest request, HttpServletResponse response)  throws GlobalException;
        
        /**
         * 尝试form登录，实现下次登录
         * @Title: tryFormLogin
         * @param request HttpServletRequest
         * @param response HttpServletResponse
         * @return: void
         */
        void tryFormLogin(HttpServletRequest request, HttpServletResponse response);
        
        /**
         * 获取解密的密码
         * @Title: getPStr
         * @param request HttpServletRequest
         * @return: String
         */
        String getPStr(HttpServletRequest request);
}
