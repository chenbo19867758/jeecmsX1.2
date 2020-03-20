package com.jeecms.auth.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;
import com.jeecms.common.base.domain.AbstractIdDomain;
import com.jeecms.common.base.domain.RequestLoginTarget;

/**
 * LoginToken
 * 前后端分离，前端系统访问后端，返回的 JWT(json web token)
 * @author: tom
 * @date: 2019年3月5日 下午4:48:37
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Entity
@Table(name = "jc_sys_api_token")
@NamedQuery(name = "LoginToken.findAll", query = "SELECT l FROM LoginToken l")
public class LoginToken extends AbstractIdDomain<Integer> implements Serializable {

        @Transient
        @JSONField(serialize = false)
        public static RequestLoginTarget getRequestLoginTarget(Short target) {
                if (LOGIN_TARGET_PLATFORM.equals(target)) {
                        return RequestLoginTarget.admin;
                } else {
                        return RequestLoginTarget.member;
                }
        }

        @Transient
        @JSONField(serialize = false)
        public static String getRequestLoginTargetString(Short target) {
                if (LOGIN_TARGET_PLATFORM.equals(target)) {
                        return RequestLoginTarget.admin.toString();
                } else {
                        return RequestLoginTarget.member.toString();
                }
        }

        // 基础平台 登录标志
        public static Short LOGIN_TARGET_PLATFORM = 1;
        // 商城 登录标志
        public static Short LOGIN_TARGET_STORE = 2;
        // 会员 登录标志
        public static Short LOGIN_TARGET_MEMBER = 3;

        private static final long serialVersionUID = 1L;
        private Integer id;
        private String token;
        private Date expireTime;
        private String username;
        private Short loginTarget;

        public LoginToken() {
        }

        @Id
        @Column(name = "id", unique = true, nullable = false)
        @TableGenerator(name = "jc_sys_login_token", pkColumnValue = "jc_sys_login_token", initialValue = 1, allocationSize = 10)
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_login_token")
        @Override
        public Integer getId() {
                return this.id;
        }

        @Override
        public void setId(Integer id) {
                this.id = id;
        }

        @Column(name = "login_token", nullable = false, length = 255)
        public String getToken() {
                return this.token;
        }

        public void setToken(String token) {
                this.token = token;
        }

        @Column(name = "expire_time", nullable = false)
        public Date getExpireTime() {
                return expireTime;
        }

        public void setExpireTime(Date expireTime) {
                this.expireTime = expireTime;
        }

        @Column(name = "username", nullable = false)
        public String getUsername() {
                return username;
        }

        public void setUsername(String username) {
                this.username = username;
        }

        @Column(name = "login_target", nullable = false)
        public Short getLoginTarget() {
                return loginTarget;
        }

        public void setLoginTarget(Short loginTarget) {
                this.loginTarget = loginTarget;
        }

}