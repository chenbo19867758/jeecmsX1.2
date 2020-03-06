/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.dto;

import javax.validation.constraints.NotBlank;

/**
 * 发文字号添加Dto
 * @author xiaohui
 * @version 1.0
 * @date 2019/5/21
 */

public class ContentMarkDto {

        private String markName;

        @NotBlank
        public String getMarkName() {
                return markName;
        }

        public void setMarkName(String markName) {
                this.markName = markName;
        }

        /**
         * 重写 hashCode
         */
        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((markName == null) ? 0 : markName.hashCode());
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
                ContentMarkDto other = (ContentMarkDto) obj;
                if (markName == null) {
                        if (other.markName != null) {
                                return false;
                        }
                } else if (!markName.equals(other.markName)) {
                        return false;
                }

                return true;
        }
}
