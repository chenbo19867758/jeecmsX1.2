package com.jeecms.auth.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

/**
 * 实现了 UserDetails 接口的模型类 拓展了 UserDetails 的属性 可以承载 创建 token 的日期，和 token 过期的日期 用于判断用户传来的 token 是否可用
 * 
 * @Author tom
 */
public class UserDetailImpl implements UserDetails {
        private static final long serialVersionUID = 1L;
        /**
         * 开始定义必要的属性
         */
        private String id;
        private String username;
        private String password;
        private Date lastPasswordReset;
        private Collection<? extends GrantedAuthority> authorities;
        private Boolean enabled;
        private String sessionId;
        private String token;
        
        /**
         * 必要的属性定义完毕
         */
        /**
         * 开始定义 UserDetails 必要的属性，因为不打算启用这些限制条件，所以不对这些条件做限制，全部设置为 true （通过）
         */
        private Boolean accountNonExpired = true;
        private Boolean accountNonLocked = true;
        private Boolean credentialsNonExpired = true;

        /**
         * 结束定义 UserDetails 必要的属性
         */

        public UserDetailImpl() {
                super();
        }

        public UserDetailImpl(String id, String username, String password, Date lastPasswordReset,
                        Collection<? extends GrantedAuthority> authorities, Boolean enabled,
                        String sessionId,String token) {
                this.setId(id);
                this.setUsername(username);
                this.setPassword(password);
                this.setLastPasswordReset(lastPasswordReset);
                this.setAuthorities(authorities);
                this.setEnabled(enabled);
                this.setSessionId(sessionId);
                this.setToken(token);
        }

        public String getId() {
                return this.id;
        }

        public void setId(String id) {
                this.id = id;
        }

        @Override
        public String getUsername() {
                return this.username;
        }

        public void setUsername(String username) {
                this.username = username;
        }

        @Override
        public String getPassword() {
                return this.password;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        public Date getLastPasswordReset() {
                return this.lastPasswordReset;
        }

        public void setLastPasswordReset(Date lastPasswordReset) {
                this.lastPasswordReset = lastPasswordReset;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
                return this.authorities;
        }

        public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
                this.authorities = authorities;
        }

        public Boolean getAccountNonExpired() {
                return this.accountNonExpired;
        }

        public void setAccountNonExpired(Boolean accountNonExpired) {
                this.accountNonExpired = accountNonExpired;
        }

        @Override
        public boolean isAccountNonExpired() {
                return this.getAccountNonExpired();
        }

        public Boolean getAccountNonLocked() {
                return this.accountNonLocked;
        }

        public void setAccountNonLocked(Boolean accountNonLocked) {
                this.accountNonLocked = accountNonLocked;
        }

        @Override
        public boolean isAccountNonLocked() {
                return this.getAccountNonLocked();
        }

        public Boolean getCredentialsNonExpired() {
                return this.credentialsNonExpired;
        }

        public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
                this.credentialsNonExpired = credentialsNonExpired;
        }

        @Override
        public boolean isCredentialsNonExpired() {
                return this.getCredentialsNonExpired();
        }

        public Boolean getEnabled() {
                return this.enabled;
        }

        public void setEnabled(Boolean enabled) {
                this.enabled = enabled;
        }

        @Override
        public boolean isEnabled() {
                return this.getEnabled();
        }
        
        public String getSessionId() {
                return sessionId;
        }
        
        public void setSessionId(String sessionId) {
                this.sessionId = sessionId;
        }

        public String getToken() {
                return token;
        }
        
        public void setToken(String token) {
                this.token = token;
        }

        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((username == null) ? 0 : username.hashCode());
                return result;
        }

        @Override
        public boolean equals(Object obj) {
                if (this == obj) {
                        return true;
                }
                if (obj == null) {
                        return false;
                }
                if (getClass() != obj.getClass()) {
                        return false;
                }
                UserDetailImpl other = (UserDetailImpl) obj;
                if (username == null) {
                        {
                                if (other.username != null) {
                                        return false;
                                }
                        }
                } else if (!username.equals(other.username)) {

                        return false;
                }
                return true;
        }

}
