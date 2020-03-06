/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain;

import com.jeecms.common.base.domain.AbstractDomain;

import javax.persistence.*;
import java.io.Serializable;


/**
 * 日志事件类型每日统计
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-06-19
 */
@Entity
@Table(name = "jc_statistics_log_click")
public class StatisticsLogClick extends AbstractDomain<Integer> implements Serializable {
        private static final long serialVersionUID = 1L;
        private Integer id;
        /**
         * 事件类型(1-系统事件   2-业务事件)
         */
        private Integer clickType;
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

        public StatisticsLogClick() {
        }

        @Id
        @Column(name = "id", nullable = false, length = 11)
        @TableGenerator(name = "jc_statistics_log_click", pkColumnValue = "jc_statistics_log_click", initialValue = 0, allocationSize = 10)
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_statistics_log_click")
        @Override
        public Integer getId() {
                return this.id;
        }

        @Override
        public void setId(Integer id) {
                this.id = id;
        }

        @Column(name = "click_type", nullable = false, length = 6)
        public Integer getClickType() {
                return clickType;
        }

        public void setClickType(Integer clickType) {
                this.clickType = clickType;
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
                if (!(o instanceof StatisticsLogClick)) {
                        return false;
                }

                StatisticsLogClick logClick = (StatisticsLogClick) o;

                if (getId() != null ? !getId().equals(logClick.getId()) : logClick.getId() != null) {
                        return false;
                }
                if (getClickType() != null ? !getClickType().equals(logClick.getClickType()) : logClick.getClickType() != null) {
                        return false;
                }
                if (getCounts() != null ? !getCounts().equals(logClick.getCounts()) : logClick.getCounts() != null) {
                        return false;
                }
                if (getStatisticsDay() != null ? !getStatisticsDay().equals(logClick.getStatisticsDay()) : logClick.getStatisticsDay() != null) {
                        return false;
                }
                if (getStatisticsMonth() != null ? !getStatisticsMonth().equals(logClick.getStatisticsMonth()) : logClick.getStatisticsMonth() != null) {
                        return false;
                }
                return getStatisticsYear() != null ? getStatisticsYear().equals(logClick.getStatisticsYear()) : logClick.getStatisticsYear() == null;
        }

        @Override
        public int hashCode() {
                int result = getId() != null ? getId().hashCode() : 0;
                result = 31 * result + (getClickType() != null ? getClickType().hashCode() : 0);
                result = 31 * result + (getCounts() != null ? getCounts().hashCode() : 0);
                result = 31 * result + (getStatisticsDay() != null ? getStatisticsDay().hashCode() : 0);
                result = 31 * result + (getStatisticsMonth() != null ? getStatisticsMonth().hashCode() : 0);
                result = 31 * result + (getStatisticsYear() != null ? getStatisticsYear().hashCode() : 0);
                return result;
        }
}