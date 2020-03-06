/*
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved.
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain;

import com.jeecms.auth.constants.AuthConstant.LoginFailProcessMode;
import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.common.base.domain.ISysConfig;
import com.jeecms.common.constants.SysConstants;
import com.jeecms.common.constants.SysConstants.TimeUnit;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.util.MyDateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局设置
 *
 * @author: ztx
 * @date: 2018年7月26日 上午10:54:24+-
 */
@Entity
@Table(name = "jc_sys_config")
public class GlobalConfig extends AbstractDomain<Integer> implements ISysConfig, Serializable {
        private static final long serialVersionUID = 1L;
        public static final String TRUE_STRING = "1";
        public static final String FALSE_STRING = "0";
        private static final Logger log = LoggerFactory.getLogger(GlobalConfig.class);

        /**
         * 初始化数据
         */
        public void init() {
                Map<String, String> source = getAttrs();
                if (StringUtils.isBlank(source.get(GlobalConfigAttr.WECHAT_REPLY_CONTENT))) {
                        source.put(GlobalConfigAttr.WECHAT_REPLY_CONTENT,
                                        "很抱歉，系统无法识别您输入的内容，尝试输入其他内容吧！ [技术支持：<a href=\"http://www.jeecms.com\">金磊科技</a>]");
                }
                log.info("设置数据初始化完毕......");
        }

        private Integer id;

        /**
         * 系统属性集合
         */
        private Map<String, String> attrs = new HashMap<>(GlobalConfigAttr.MAP_INIT_SIZE);

        public GlobalConfig() {
        }

        @Transient
        public String getUploadPath() {
                return WebConstants.UPLOAD_PATH + getConfigAttr().getUploadFolderPath();
        }

        @Id
        @Column(name = "id", nullable = false, length = 11)
        @TableGenerator(name = "jc_sys_config_attr", pkColumnValue = "jc_sys_config_attr", initialValue = 1, allocationSize = 10)
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_config_attr")
        @Override
        public Integer getId() {
                return this.id;
        }

        @Override
        public void setId(Integer id) {
                this.id = id;
        }

        @ElementCollection
        @CollectionTable(name = "jc_sys_config_attr", joinColumns = { @JoinColumn(name = "config_id") })
        @MapKeyColumn(name = "attr_name", length = 150)
        @Column(name = "attr_value", length = 1500)
        public Map<String, String> getAttrs() {
                return attrs;
        }

        public void setAttrs(Map<String, String> attrs) {
                this.attrs = attrs;
        }

        @Transient
        public GlobalConfigAttr getConfigAttr() {
                GlobalConfigAttr configAttr = new GlobalConfigAttr(getAttrs());
                return configAttr;
        }

        @Transient
        public void setConfigAttr(GlobalConfigAttr configAttr) {
                getAttrs().putAll(configAttr.getAttr());
        }

        /**
         * 是否强制要求用户定期修改密码
         *
         * @Title: getPassRegularChange
         * @return: boolean
         */
        @Transient
        public boolean getPassRegularChange() {
                GlobalConfigAttr config = getConfigAttr();
                if (config != null) {
                        if (config.getSecurityOpen() != null && config.getSecurityOpen()
                                        && StringUtils.isNoneBlank(config.getPassRegularChange())) {
                                if (TRUE_STRING.equals(config.getPassRegularChange().toLowerCase())) {
                                        return true;
                                }
                        }
                }
                return false;
        }

        /**
         * 获取最后强制密码修改周期 设置时间
         *
         * @Title: getPassRegularChangeSetTime
         * @return: Date
         */
        @Transient
        public Date getPassRegularChangeSetTime() {
                return getConfigAttr().getPassRegularChangeSetTime();
        }

        /**
         * 登录时密码最大错误次数 0则不限制
         *
         * @Title: getLoginErrorCount
         * @return: Integer
         */
        @Transient
        public Integer getLoginErrorCount() {
                return getConfigAttr().getLoginErrorCount();
        }

        /**
         * 强制修改密码周期 单位天
         *
         * @Title: getPassRegularCycle
         * @return: Integer
         */
        @Transient
        public Integer getPassRegularCycle() {
                return getConfigAttr().getPassRegularCycle();
        }

        /**
         * 首次登录是否强制要求修改密码
         *
         * @Title: getPassFirstNeedUpdate
         * @return: boolean
         */
        @Transient
        public boolean getPassFirstNeedUpdate() {
                GlobalConfigAttr config = getConfigAttr();
                if (config != null) {
                        if (config.getSecurityOpen() != null && config.getSecurityOpen()
                                        && StringUtils.isNoneBlank(config.getPassFirstNeedUpdate())) {
                                if (TRUE_STRING.equals(config.getPassFirstNeedUpdate().toLowerCase())) {
                                        return true;
                                }
                        }
                }
                return false;
        }

        /**
         * 重置密码后首次登录是否强制要求修改密码
         *
         * @Title: getPassResetNeedUpdate
         * @return: boolean
         */
        @Transient
        public boolean getPassResetNeedUpdate() {
                GlobalConfigAttr config = getConfigAttr();
                if (config != null) {
                        if (config.getSecurityOpen() != null && config.getSecurityOpen()
                                        && StringUtils.isNoneBlank(config.getPassResetNeedUpdate())) {
                                if (TRUE_STRING.equals(config.getPassResetNeedUpdate().toLowerCase())) {
                                        return true;
                                }
                        }
                }
                return false;
        }

        /**
         * 获取登录错误次数太多的处理模式
         *
         * @Title: getProcessingMode
         * @return: LoginFailProcessMode
         */
        @Transient
        public LoginFailProcessMode getProcessingMode() {
                GlobalConfigAttr config = getConfigAttr();
                if (config != null && config.getSecurityOpen() != null && config.getSecurityOpen()) {
                        return LoginFailProcessMode.getProcessMode(config.getProcessingMode());
                }
                return LoginFailProcessMode.no;
        }

        /**
         * 获取系统设置的禁止登录时间（根据禁止登录时长和周期来确定时间）
         *
         * @Title: getLoginLimitTime
         * @return: Date
         */
        @Transient
        public Date getLoginLimitTime() {
                GlobalConfigAttr config = getConfigAttr();
                Date now = Calendar.getInstance().getTime();
                Date loginLimitTime = now;
                TimeUnit timeUnit = SysConstants.TimeUnit.valueOf(config.getLoginLimitType());
                if (config != null) {
                        Integer loginLimit = config.getLoginLimit();
                        if (TimeUnit.day.equals(timeUnit)) {
                                loginLimitTime = MyDateUtils.getDayAfterTime(now, loginLimit);
                        } else if (TimeUnit.hour.equals(timeUnit)) {
                                loginLimitTime = MyDateUtils.getHourAfterTime(now, loginLimit);
                        } else if (TimeUnit.minute.equals(timeUnit)) {
                                loginLimitTime = MyDateUtils.getMinuteAfterTime(now, loginLimit);
                        }
                }
                return loginLimitTime;
        }

        /**
         * 获取系统设置的登录错误周期的开始时间（根据登录时密码错误校验周期来确定时间）
         *
         * @Title: getLoginErrorUnitBeginTime
         * @return: Date
         */
        @Transient
        public Date getLoginErrorUnitBeginTime() {
                GlobalConfigAttr config = getConfigAttr();
                Date now = Calendar.getInstance().getTime();
                Date loginErrorUnitTime = now;
                TimeUnit timeUnit = SysConstants.TimeUnit.valueOf(config.getCheckCycleUnit());
                if (config != null) {
                        Integer loginErrorCheckCycle = config.getCheckCycle();
                        if (TimeUnit.day.equals(timeUnit)) {
                                loginErrorUnitTime = MyDateUtils.getDayAfterTime(now, -loginErrorCheckCycle);
                        } else if (TimeUnit.hour.equals(timeUnit)) {
                                loginErrorUnitTime = MyDateUtils.getHourAfterTime(now, -loginErrorCheckCycle);
                        } else if (TimeUnit.minute.equals(timeUnit)) {
                                loginErrorUnitTime = MyDateUtils.getMinuteAfterTime(now, -loginErrorCheckCycle);
                        }
                }
                return loginErrorUnitTime;
        }

        /**
         * 获取自动退出时间 0则不自动退出
         *
         * @Title: getAutoLogoutMinute
         * @return: Integer
         */
        @Transient
        public Integer getAutoLogoutMinute() {
                GlobalConfigAttr config = getConfigAttr();
                if (config != null && config.getSecurityOpen() != null && config.getSecurityOpen()) {
                        return config.getAutoLogoutMinute();
                }
                return 0;
        }

        /**
         * 是否开启 长期未登录锁定账号
         *
         * @Title: getAccountLockAuto
         * @return: boolean true开启了
         */
        @Transient
        public boolean getAccountLockAuto() {
                GlobalConfigAttr config = getConfigAttr();
                if (config != null && config.getSecurityOpen() != null && config.getSecurityOpen()) {
                        return config.getAccountLockAuto();
                }
                return false;
        }

        /**
         * 会员功能是否开启
         * 
         * @Title: getMemberOpen
         * @return: boolean
         */
        @Transient
        public boolean getMemberOpen() {
                GlobalConfigAttr config = getConfigAttr();
                return config.getMemberOpen();
        }

        /**
         * 是否开启会员注册
         * 
         * @Title: getMemberRegister
         * @return: boolean
         */
        @Transient
        public boolean getMemberRegisterOpen() {
                GlobalConfigAttr config = getConfigAttr();
                return config.getIsMemberRegister();
        }

        /**
         * 会员注册是否需要图形验证码
         * 
         * @Title: getMemberRegisterCaptcha
         * @return: boolean
         */
        @Transient
        public boolean getMemberRegisterCaptcha() {
                GlobalConfigAttr config = getConfigAttr();
                String registerCaptcha = config.getIsActivationImage();
                if (GlobalConfigAttr.FALSE_STRING.equals(registerCaptcha)) {
                        return false;
                }
                return true;
        }

        /**
         * 长期未登录锁定账号的天数
         *
         * @Title: getAccountLockAutoDay
         * @return: Integer
         */
        @Transient
        public Integer getAccountLockAutoDay() {
                GlobalConfigAttr config = getConfigAttr();
                return config.getAccountLockAutoDay();
        }

        /**
         * 是否允许多处同时登陆
         *
         * @Title: getLoginMuti
         * @return: Boolean true 允许
         */
        @Transient
        public boolean getLoginMuti() {
                GlobalConfigAttr config = getConfigAttr();
                if (config != null && config.getSecurityOpen() != null && config.getSecurityOpen()) {
                        return config.getLoginMuti();
                }
                return true;
        }

        /**
         * 是否内网 true内网
         *
         * @Title: getIsIntranet
         * @return: Boolean
         */
        @Transient
        public Boolean getIsIntranet() {
                GlobalConfigAttr config = getConfigAttr();
                return config.getIsIntranetSet();
        }

        @Override
        @Transient
        public String getSiteDomain() {
                return getConfigAttr().getSiteDomain();
        }

        @Override
        @Transient
        public String getSiteName() {
                return getConfigAttr().getSiteName();
        }

        @Override
        @Transient
        public Integer getSysState() {
                return getConfigAttr().getSysState();
        }

        @Override
        @Transient
        public Boolean getInBlack() {
                return getConfigAttr().getInBlack();
        }

        @Override
        @Transient
        public String getAuthCode() {
                return getConfigAttr().getAuthCode();
        }

        @Override
        @Transient
        public Boolean getIsLimit() {
                return getConfigAttr().getIsLimit();
        }

        @Override
        @Transient
        public Map<String, String> getConfigMap() {
                return getAttrs();
        }

        @Transient
        public Date getAuthExpiredDate() {
                return getConfigAttr().getAuthExpiredDate();
        }

        @Transient
        public Date getSystemStartDate() {
                return getConfigAttr().getSystemStartDate();
        }

        /**
         * 密码组成规则 1无要求 2必须包含字母和数字 3必须包含大写字母、小写字母、数字 4必须包含字母、数字、特殊字符 5必须包含大写字母、小写字母、数字、特殊字符
         */
        /**
         * 1无要求
         **/
        public static final int NONE_TYPE = 1;
        /**
         * 2必须包含字母和数字
         **/
        public static final int LETTER_NUMBER_TYPE = 2;
        /**
         * 3必须包含大写字母、小写字母、数字
         **/
        public static final int LETTER_UPPER_LOWER_NUMBER_TYPE = 3;
        /**
         * 4必须包含字母、数字、特殊字符
         **/
        public static final int LETTER_NUMBER_STRING_TYPE = 4;
        /**
         * 5必须包含大写字母、小写字母、数字、特殊字符
         **/
        public static final int LETTER_UPPER_LOWER_NUMBER_STRING_TYPE = 5;

        /** 正则表达式 **/
        /**
         * 必须包含字母和数字
         **/
        public static final String REGEX = "^(?=.*?[0-9])(?=.*?[a-zA-Z])[0-9a-zA-Z]+$";
        
        /**
         * 必须包含大，小写字母和数字
         **/
        public static final String REGEXS = "^(?=.*[0-9].*)(?=.*[A-Z].*)(?=.*[a-z].*)[0-9a-zA-Z]+$";
        
        /**
         * 必须包含字母和数字,特殊字符
         **/
        public static final String REGEXMORE = "^(?=.*?[0-9])(?=.*?[a-zA-Z])(?=.*?[\\-\\+\\!\\#\\%\\[\\]\\{\\}\\=\\/])[0-9a-zA-Z\\-\\+\\!\\#\\%\\[\\]\\{\\}\\=\\/]+$";
        
        /**
         * 必须包含大写字母、小写字母、数字、特殊字符
         **/
        public static final String REGEXMORES = "^(?=.*[0-9].*)(?=.*[A-Z].*)(?=.*[a-z].*)(?=.*?[\\-\\+\\!\\#\\%\\[\\]\\{\\}\\=\\/])[0-9a-zA-Z\\-\\+\\!\\#\\%\\[\\]\\{\\}\\=\\/]+$";
       
        /**
         * 验证颜色格式
         **/
        public static final String COLOR = "^#FF[0-9a-fA-F]{6}+$";
        
        /**
         * 数据备份文件存储服务器1本地服务器 2.FTP 3.OSS存储器（默认为1）
         */
        public static final Integer BACKUP_TYPE_LOCAL = 1;
        
        /**
         * 数据备份文件存储服务器1本地服务器 2.FTP 3.OSS存储器（默认为1）
         */
        public static final Integer BACKUP_TYPE_FTP = 2;
        
        /**
         * 数据备份文件存储服务器1本地服务器 2.FTP 3.OSS存储器（默认为1）
         */
        public static final Integer BACKUP_TYPE_OSS = 3;
}