/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 敏感词新增Dto
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/4/29
 */

public class SensitiveWordDto {

        /**
         * 覆盖已有敏感词
         */
        public static final Integer DEAL_WITH_COVER = 1;

        /**
         * 跳过已有敏感词
         */
        public static final Integer DEAL_WITH_JUMP = 2;

        /**
         * 敏感词
         */
        private String sensitiveWord;

        /**
         * 替换词
         */
        private String replaceWord;
        /**
         * 处理方式
         */
        private Integer dealWithType = DEAL_WITH_COVER;

        @NotBlank
        public String getSensitiveWord() {
                return sensitiveWord;
        }

        public void setSensitiveWord(String sensitiveWord) {
                this.sensitiveWord = sensitiveWord;
        }

        public String getReplaceWord() {
                return replaceWord;
        }

        public void setReplaceWord(String replaceWord) {
                this.replaceWord = replaceWord;
        }

        @NotNull
        public Integer getDealWithType() {
                return dealWithType;
        }

        public void setDealWithType(Integer dealWithType) {
                this.dealWithType = dealWithType;
        }


        /**
         * 重写 hashCode
         *
         * @return int
         * @Title: hashCode
         */
        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((sensitiveWord == null) ? 0 : sensitiveWord.hashCode());
                result = prime * result + ((replaceWord == null) ? 0 : replaceWord.hashCode());
                result = prime * result + ((dealWithType == null) ? 0 : dealWithType.hashCode());
                return result;
        }

        /**
         * 重写  equals
         *
         * @param obj Object
         * @return boolean
         * @Title: equals
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
                SensitiveWordDto other = (SensitiveWordDto) obj;
                if (sensitiveWord == null) {
                        if (other.sensitiveWord != null) {
                                return false;
                        }
                } else if (!sensitiveWord.equals(other.sensitiveWord)) {
                        return false;
                }
                if (replaceWord == null) {
                        if (other.replaceWord != null) {
                                return false;
                        }
                } else if (!replaceWord.equals(other.replaceWord)) {
                        return false;
                }
                if (dealWithType == null) {
                        if (other.dealWithType != null) {
                                return false;
                        }
                } else if (!dealWithType.equals(other.dealWithType)) {
                        return false;
                }
                return true;
        }

}
