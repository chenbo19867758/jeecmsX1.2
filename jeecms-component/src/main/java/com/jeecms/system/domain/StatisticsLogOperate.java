/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain;

import com.jeecms.common.base.domain.AbstractDomain;

import javax.persistence.*;
import java.io.Serializable;


/**
 * 日志操作类型每日统计
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-06-19
 */
@Entity
@Table(name = "jc_statistics_log_operate")
public class StatisticsLogOperate extends AbstractDomain<Integer> implements Serializable {
        private static final long serialVersionUID = 1L;
        private Integer id;
        /**
         * 操作类型
         */
        private String operateType;
        /**
         * 日志数量
         */
        private Integer counts;
        /**
         * 统计日期（格式yyyy-MM-dd）
         */
        private String statisticsDay;
        /**
         * 统计月份（格式yyyy-MM）
         */
        private String statisticsMonth;
        /**
         * 统计年份（格式yyyy）
         */
        private String statisticsYear;

        public StatisticsLogOperate() {
        }

        @Id
        @Column(name = "id", nullable = false, length = 11)
        @TableGenerator(name = "jc_statistics_log_operate", pkColumnValue = "jc_statistics_log_operate", initialValue = 0, allocationSize = 10)
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_statistics_log_operate")
        @Override
        public Integer getId() {
                return this.id;
        }

        @Override
        public void setId(Integer id) {
                this.id = id;
        }

        @Column(name = "operate_type", nullable = false, length = 150)
        public String getOperateType() {
                return operateType;
        }

        public void setOperateType(String operateType) {
                this.operateType = operateType;
        }

        @Column(name = "counts", nullable = false, length = 11)
        public Integer getCounts() {
                return counts;
        }

        public void setCounts(Integer counts) {
                this.counts = counts;
        }

        @Column(name = "statistics_day", nullable = false, length = 13)
        public String getStatisticsDay() {
                return statisticsDay;
        }

        public void setStatisticsDay(String statisticsDay) {
                this.statisticsDay = statisticsDay;
        }

        @Column(name = "statistics_month", nullable = false, length = 13)
        public String getStatisticsMonth() {
                return statisticsMonth;
        }

        public void setStatisticsMonth(String statisticsMonth) {
                this.statisticsMonth = statisticsMonth;
        }

        @Column(name = "statistics_year", nullable = false, length = 13)
        public String getStatisticsYear() {
                return statisticsYear;
        }

        public void setStatisticsYear(String statisticsYear) {
                this.statisticsYear = statisticsYear;
        }

        @Override
        public boolean equals(Object o) {
                if (this == o) {
                        return true;
                }
                if (!(o instanceof StatisticsLogOperate)) {
                        return false;
                }

                StatisticsLogOperate that = (StatisticsLogOperate) o;

                if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) {
                        return false;
                }
                if (getOperateType() != null ? !getOperateType().equals(that.getOperateType()) : that.getOperateType() != null) {
                        return false;
                }
                if (getCounts() != null ? !getCounts().equals(that.getCounts()) : that.getCounts() != null) {
                        return false;
                }
                if (getStatisticsDay() != null ? !getStatisticsDay().equals(that.getStatisticsDay()) : that.getStatisticsDay() != null) {
                        return false;
                }
                if (getStatisticsMonth() != null ? !getStatisticsMonth().equals(that.getStatisticsMonth()) : that.getStatisticsMonth() != null) {
                        return false;
                }
                return getStatisticsYear() != null ? getStatisticsYear().equals(that.getStatisticsYear()) : that.getStatisticsYear() == null;
        }

        @Override
        public int hashCode() {
                int result = getId() != null ? getId().hashCode() : 0;
                result = 31 * result + (getOperateType() != null ? getOperateType().hashCode() : 0);
                result = 31 * result + (getCounts() != null ? getCounts().hashCode() : 0);
                result = 31 * result + (getStatisticsDay() != null ? getStatisticsDay().hashCode() : 0);
                result = 31 * result + (getStatisticsMonth() != null ? getStatisticsMonth().hashCode() : 0);
                result = 31 * result + (getStatisticsYear() != null ? getStatisticsYear().hashCode() : 0);
                return result;
        }
}