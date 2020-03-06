/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.auth.constants;

/**
 * 权限相关常量
 * 
 * @author: tom
 * @date: 2019年5月13日 下午7:22:54
 */
public class AuthConstant {
        /**
         * 用户最后操作时间
         */
        public static final String LAST_OPERATE_TIME = "lastOperateTime";
        /** 下次登录是否需要验证码 */
        public static final String NEXT_NEED_CAPTCHA = "nextNeedCaptcha";
        /** 需要修改密码 */
        public static final String NEED_CHANGE_PASSWORD = "needChangePassword";
        /**当前操作用户名*/
        public static final String CURR_USERNAME = "username";
        /** 当前登录用户组织ID */
        public static final String CURR_USER_ORG_ID = "orgId";
        /** 当前登录用户角色ID */
        public static final String CURR_USER_ROLE_ID = "roleIds";
        /** 用户菜单权限key */
        public static final String SESSION_OWNER_MENU = "ownerMenu";
        /** 用户站群权限key */
        public static final String SESSION_OWNER_SITE = "ownerSite";
        /** 用户站点类数据权限key */
        public static final String SESSION_OWNER_SITE_DATA_PERM = "ownSiteDataPerm";
        /** 用户栏目类数据权限key */
        public static final String SESSION_OWNER_CHANNEL_DATA_PERM = "ownChannelDataPerm";
        /** 用户内容类数据权限key */
        public static final String SESSION_OWNER_CONTENT_DATA_PERM = "ownContentDataPerm";

        /**
         * 登录错误次数对账户处理模式
         * 
         * @author: tom
         * @date: 2019年5月14日 下午5:40:53
         */
        public  enum LoginFailProcessMode {
                /**
                 * 0不处理
                 */
                no(0),
                /**
                 * 1显示验证码
                 */
                needCaptcha(1),
                /**
                 * 锁定账号(禁用)
                 */
                lock(2),
                /**
                 * 3 一定时间内禁止登录
                 */
                forbidden(3),;

                /**
                 * 根据数值获取 账户处理模式枚举
                 * @Title: getProcessMode
                 * @param value  数值
                 * @return: LoginFailProcessMode
                 */
                public static LoginFailProcessMode getProcessMode(Integer value) {
                        if (value != null) {
                                if (value == 1) {
                                        return needCaptcha;
                                } else if (value == 2) {
                                        return lock;
                                } else if (value == 3) {
                                        return forbidden;
                                }
                        }
                        return no;
                }

                Integer value;

                private LoginFailProcessMode(Integer value) {
                        this.value = value;
                }

                public Integer getValue() {
                        return value;
                }

                public void setValue(Integer value) {
                        this.value = value;
                }

        }
}
