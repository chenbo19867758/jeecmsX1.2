/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Arrays;

/**
 * 防火墙配置Dto
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/6/13 15:25
 */

public class SysIptablesDto implements Serializable {

        /**
         * 是否开启(0-否   1-是)
         */
        private Boolean isEnable;
        /**
         * 限制内网ip模式（1-不限制   2-设置白名单  3-设置黑名单）
         */
        private Integer limitInNetworkModel;
        /**
         * 内网模式限制号段集
         */
        private InNetworkIpJson[] inNetworkIpJsons;
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

        public static class InNetworkIpJson {
                /**
                 * 内网模式限制号段集:开始号段
                 */
                private String startIp;
                /**
                 * 内网模式限制号段集:结束号段
                 */
                private String endIp;

                @Length(max = 20)
                public String getStartIp() {
                        return startIp;
                }

                public void setStartIp(String startIp) {
                        this.startIp = startIp;
                }

                @Length(max = 20)
                public String getEndIp() {
                        return endIp;
                }

                public void setEndIp(String endIp) {
                        this.endIp = endIp;
                }

                @Override
                public String toString() {
                        return "InNetworkIpJson{" + "startIp='" + startIp + '\'' + ", endIp='" + endIp + '\'' + '}';
                }
        }

        @NotNull
        public Boolean getEnable() {
                return isEnable;
        }

        public void setEnable(Boolean enable) {
                isEnable = enable;
        }

        @Digits(integer = 6, fraction = 0)
        public Integer getLimitInNetworkModel() {
                return limitInNetworkModel;
        }

        public void setLimitInNetworkModel(Integer limitInNetworkModel) {
                this.limitInNetworkModel = limitInNetworkModel;
        }

        public InNetworkIpJson[] getInNetworkIpJsons() {
                return inNetworkIpJsons;
        }

        public void setInNetworkIpJsons(InNetworkIpJson[] inNetworkIpJsons) {
                this.inNetworkIpJsons = inNetworkIpJsons;
        }

        public Integer getLimitOutNetworkModel() {
                return limitOutNetworkModel;
        }

        public void setLimitOutNetworkModel(Integer limitOutNetworkModel) {
                this.limitOutNetworkModel = limitOutNetworkModel;
        }

        public String getWorkNetworkIp() {
                return workNetworkIp;
        }

        public void setWorkNetworkIp(String workNetworkIp) {
                this.workNetworkIp = workNetworkIp;
        }

        public String getLimitDomain() {
                return limitDomain;
        }

        public void setLimitDomain(String limitDomain) {
                this.limitDomain = limitDomain;
        }

        @Length(max = 120)
        public String getAllowLoginHours() {
                return allowLoginHours;
        }

        public void setAllowLoginHours(String allowLoginHours) {
                this.allowLoginHours = allowLoginHours;
        }

        @Length(max = 50)
        public String getAllowLoginWeek() {
                return allowLoginWeek;
        }

        public void setAllowLoginWeek(String allowLoginWeek) {
                this.allowLoginWeek = allowLoginWeek;
        }

        @Override
        public String toString() {
                return "SysIptablesDto{"
                        + "isEnable=" + isEnable
                        + ", limitInNetworkModel=" + limitInNetworkModel
                        + ", inNetworkIpJsons=" + Arrays.toString(inNetworkIpJsons)
                        + ", limitOutNetworkModel=" + limitOutNetworkModel
                        + ", workNetworkIp='" + workNetworkIp + '\''
                        + ", limitDomain='" + limitDomain + '\''
                        + ", allowLoginHours='" + allowLoginHours + '\''
                        + ", allowLoginWeek='" + allowLoginWeek + '\''
                        + '}';
        }
}
