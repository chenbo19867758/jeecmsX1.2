package com.jeecms.auth.util;

import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.dto.UserDetailImpl;

/**
 * 用于将 用户对象 转换成 UserDetail对象
 * @Author tom
 */
public class SecurityModelFactory {

        /**
         * 生成用户认证凭据对象 UserDetailImpl
         * @Title: createUserDetail
         * @param user CoreUser
         * @param sessionId sessionId
         * @param token token
         * @return: UserDetailImpl
         */
        public static UserDetailImpl createUserDetail(CoreUser user,String sessionId,String token) {
                Collection<? extends GrantedAuthority> authorities = null;
                
                if (StringUtils.isNoneBlank(user.getAuthorityString())) {
                        authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(user.getAuthorityString());
                }
                Date lastPasswordReset = user.getLastPasswordChange();
                return new UserDetailImpl(user.getUsername(), user.getUsername(), user.getPassword(), lastPasswordReset,
                                authorities, user.getEnabled(),sessionId,token);
        }

}
