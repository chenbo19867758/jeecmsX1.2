package com.jeecms.auth.dto;

import javax.validation.constraints.NotNull;

import com.jeecms.common.base.domain.RequestLoginTarget;

/**
 * 用户登陆接口参数的实体类
 * 
 * @Author tom
 */
public class RequestLoginUser {

        /**
         * 登录 用户标识
         */
        public static final String LOGIN_IDENTITY = "identity";
        /***
         * 登录  密码
         */
        public static final String LOGIN_P_STR = "pStr";
        /***
         * 登录用户 混淆码
         */
        public static final String LOGIN_SALT = "salt";
        /**
         * 登录  用户验证码
         */
        public static final String LOGIN_CAPTCHA = "captcha";
        /**
         * 登录字符串 推送标识
         */
        public static final String LOGIN_PUSHID = "uniquePushId";

        /**
         * 登录字符串 登录来源
         */
        public static final String LOGIN_TARGET = "target";

        /***
         * 登录用户认证对象
         */
        public static final String LOGIN_USER_DETAIL = "userDetails";
        /***
         * 认证认证结果
         */
        public static final String LOGIN_RESULT = "loginResult";
        /**
         * 用户名、邮箱、手机号
         */
        private String identity;
        /**
         * 密码
         */
        private String pStr;

        /**
         * 登录来源 platform store member
         */
        private RequestLoginTarget target;
        /**
         * 验证码
         */
        private String captcha;
        /**
         * 推送标识
         */
        private String uniquePushId;

        public static RequestLoginUser buildForMemberLogin(String identity, String pStr, RequestLoginTarget target,
                        String captcha, String uniquePushId) {
                return new RequestLoginUser(identity, pStr, target, captcha, uniquePushId);
        }

        public RequestLoginUser(String identity, String pStr, RequestLoginTarget target, String captcha,
                        String uniquePushId) {
                super();
                this.identity = identity;
                this.pStr = pStr;
                this.target = target;
                this.captcha = captcha;
                this.uniquePushId = uniquePushId;
        }

        public RequestLoginUser() {
        }

        @NotNull
        public String getpStr() {
                return pStr;
        }

        public void setpStr(String pStr) {
                this.pStr = pStr;
        }

        public void setPStr(String pStr) {
                setpStr(pStr);
        }

        public RequestLoginTarget getTarget() {
                return target;
        }

        public void setTarget(RequestLoginTarget target) {
                this.target = target;
        }

        public String getCaptcha() {
                return captcha;
        }

        public void setCaptcha(String captcha) {
                this.captcha = captcha;
        }

        @NotNull(message = "{validation.error.identity}")
        public String getIdentity() {
                return identity;
        }

        public void setIdentity(String identity) {
                this.identity = identity;
        }

        public String getUniquePushId() {
                return uniquePushId;
        }

        public void setUniquePushId(String uniquePushId) {
                this.uniquePushId = uniquePushId;
        }

}
