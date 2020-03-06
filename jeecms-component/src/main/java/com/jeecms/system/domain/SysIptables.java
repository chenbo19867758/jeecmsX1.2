/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain;

import com.jeecms.common.base.domain.AbstractDomain;

import javax.persistence.*;
import java.io.Serializable;


/**
 * 防火墙配置
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-06-13
 */
@Entity
@Table(name = "jc_sys_iptables")
public class SysIptables extends AbstractDomain<Integer> implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 不限制
         */
        public static final Integer NETWORK_NOT_LIMITED = 1;
        /**
         * 白名单
         */
        public static final Integer NETWORK_WHITELIST = 2;
        /**
         * 黑名单
         */
        public static final Integer NETWORK_BLACKLIST = 3;

        private Integer id;
        /**
         * 是否开启(0-否   1-是)
         */
        private Boolean isEnable;
        /**
         * 限制内网ip模式（1-不限制   2-设置白名单  3-设置黑名单）
         */
        private Integer limitInNetworkModel;
        /**
         * 内网模式限制号段集，json格式,格式为  [{"startIp":"开始号段","endIp":"结束号段"}]
         */
        private String inNetworkIpJson;
        /**
         * 限制外网ip模式（1-不限制   2-设置白名单  3-设置黑名单）
         */
        private Integer limitOutNetworkModel;
        /**
         * 外网模式限制号段集
         */
        private String workNetworkIp;
        /**
         * 限制访问域名
         */
        private String limitDomain;
        /**
         * 允许后台登录时间点,多个用逗号分隔,如：0,1,2,3
         */
        private String allowLoginHours;
        /**
         * 允许后台登录星期，多个是用逗号分隔，如：1,2,3,4,5,6,7
         */
        private String allowLoginWeek;

        public SysIptables() {
        }

        @Id
        @Column(name = "id", nullable = false, length = 11)
        @TableGenerator(name = "jc_sys_iptables", pkColumnValue = "jc_sys_iptables", initialValue = 0, allocationSize = 10)
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_iptables")
        @Override
        public Integer getId() {
                return this.id;
        }

        @Override
        public void setId(Integer id) {
                this.id = id;
        }

        @Column(name = "is_enable", nullable = false, length = 1)
        public Boolean getIsEnable() {
                return isEnable;
        }

        public void setIsEnable(Boolean isEnable) {
                this.isEnable = isEnable;
        }

        @Column(name = "limit_in_network_model", nullable = true, length = 6)
        public Integer getLimitInNetworkModel() {
                return limitInNetworkModel;
        }

        public void setLimitInNetworkModel(Integer limitInNetworkModel) {
                this.limitInNetworkModel = limitInNetworkModel;
        }

        @Column(name = "in_network_ip_json", nullable = true, length = 715827882)
        public String getInNetworkIpJson() {
                return inNetworkIpJson;
        }

        public void setInNetworkIpJson(String inNetworkIpJson) {
                this.inNetworkIpJson = inNetworkIpJson;
        }

        @Column(name = "limit_out_network_model", nullable = true, length = 6)
        public Integer getLimitOutNetworkModel() {
                return limitOutNetworkModel;
        }

        public void setLimitOutNetworkModel(Integer limitOutNetworkModel) {
                this.limitOutNetworkModel = limitOutNetworkModel;
        }

        @Column(name = "work_network_ip", nullable = true, length = 715827882)
        public String getWorkNetworkIp() {
                return workNetworkIp;
        }

        public void setWorkNetworkIp(String workNetworkIp) {
                this.workNetworkIp = workNetworkIp;
        }

        @Column(name = "limit_domain", nullable = true, length = 715827882)
        public String getLimitDomain() {
                return limitDomain;
        }

        public void setLimitDomain(String limitDomain) {
                this.limitDomain = limitDomain;
        }

        @Column(name = "allow_login_hours", nullable = true, length = 150)
        public String getAllowLoginHours() {
                return allowLoginHours;
        }

        public void setAllowLoginHours(String allowLoginHours) {
                this.allowLoginHours = allowLoginHours;
        }

        @Column(name = "allow_login_week", nullable = true, length = 50)
        public String getAllowLoginWeek() {
                return allowLoginWeek;
        }

        public void setAllowLoginWeek(String allowLoginWeek) {
                this.allowLoginWeek = allowLoginWeek;
        }

        @Override
        public boolean equals(Object o) {
                if (this == o) {
                        return true;
                }
                if (!(o instanceof SysIptables)) {
                        return false;
                }

                SysIptables that = (SysIptables) o;

                if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) {
                        return false;
                }
                if (getIsEnable() != null ? !getIsEnable().equals(that.getIsEnable()) : that.getIsEnable() != null) {
                        return false;
                }
                if (getLimitInNetworkModel() != null ? !getLimitInNetworkModel().equals(that.getLimitInNetworkModel()) : that.getLimitInNetworkModel() != null) {
                        return false;
                }
                if (getInNetworkIpJson() != null ? !getInNetworkIpJson().equals(that.getInNetworkIpJson()) : that.getInNetworkIpJson() != null) {
                        return false;
                }
                if (getLimitOutNetworkModel() != null ? !getLimitOutNetworkModel().equals(that.getLimitOutNetworkModel()) : that.getLimitOutNetworkModel() != null) {
                        return false;
                }
                if (getWorkNetworkIp() != null ? !getWorkNetworkIp().equals(that.getWorkNetworkIp()) : that.getWorkNetworkIp() != null) {
                        return false;
                }
                if (getLimitDomain() != null ? !getLimitDomain().equals(that.getLimitDomain()) : that.getLimitDomain() != null) {
                        return false;
                }
                if (getAllowLoginHours() != null ? !getAllowLoginHours().equals(that.getAllowLoginHours()) : that.getAllowLoginHours() != null) {
                        return false;
                }
                return getAllowLoginWeek() != null ? getAllowLoginWeek().equals(that.getAllowLoginWeek()) : that.getAllowLoginWeek() == null;
        }

        @Override
        public int hashCode() {
                int result = getId() != null ? getId().hashCode() : 0;
                result = 31 * result + (getIsEnable() != null ? getIsEnable().hashCode() : 0);
                result = 31 * result + (getLimitInNetworkModel() != null ? getLimitInNetworkModel().hashCode() : 0);
                result = 31 * result + (getInNetworkIpJson() != null ? getInNetworkIpJson().hashCode() : 0);
                result = 31 * result + (getLimitOutNetworkModel() != null ? getLimitOutNetworkModel().hashCode() : 0);
                result = 31 * result + (getWorkNetworkIp() != null ? getWorkNetworkIp().hashCode() : 0);
                result = 31 * result + (getLimitDomain() != null ? getLimitDomain().hashCode() : 0);
                result = 31 * result + (getAllowLoginHours() != null ? getAllowLoginHours().hashCode() : 0);
                result = 31 * result + (getAllowLoginWeek() != null ? getAllowLoginWeek().hashCode() : 0);
                return result;
        }
}