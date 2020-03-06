/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Arrays;

/**
 * 年号批量录入Dto
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/5/23
 */

public class ContentMarkBatchDto {
        /**
         * 起始年号
         */
        private Integer beginYear;
        /**
         * 起始年号
         */
        private Integer endYear;
        /**
         * 排除年号数组
         */
        private Integer[] excludeYear;

        @NotNull
        @Length(min = 4, max = 4)
        public Integer getBeginYear() {
                return beginYear;
        }

        public void setBeginYear(Integer beginYear) {
                this.beginYear = beginYear;
        }

        @NotNull
        @Length(min = 4, max = 4)
        public Integer getEndYear() {
                return endYear;
        }

        public void setEndYear(Integer endYear) {
                this.endYear = endYear;
        }

        public Integer[] getExcludeYear() {
                return excludeYear;
        }

        public void setExcludeYear(Integer[] excludeYear) {
                this.excludeYear = excludeYear;
        }

        /**
         * 重写 hashCode
         */
        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((beginYear == null) ? 0 : beginYear.hashCode());
                result = prime * result + ((endYear == null) ? 0 : endYear.hashCode());
                result = prime * result + ((excludeYear == null) ? 0 : Arrays.hashCode(excludeYear));
                return result;
        }

        /**
         * 重写 equals
         */
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
                ContentMarkBatchDto other = (ContentMarkBatchDto) obj;
                if (beginYear == null) {
                        if (other.beginYear != null) {
                                return false;
                        }
                } else if (!beginYear.equals(other.beginYear)) {
                        return false;
                }
                if (endYear == null) {
                        if (other.endYear != null) {
                                return false;
                        }
                } else if (!endYear.equals(other.endYear)) {
                        return false;
                }
                if (!Arrays.equals(excludeYear, other.excludeYear)) {
                        return false;
                }
                return true;
        }
}
