/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.dto;

import javax.validation.constraints.NotBlank;
import java.util.Arrays;

/**
 * 人员密级Dto
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/4/25
 */

public class UserSecretDto {

        /**
         * 人员密级id
         */
        private Integer id;
        /**
         * 名称
         */
        private String name;
        /**
         * 备注
         */
        private Integer remark;
        /**
         * 排序
         */
        private Integer sortNum;

        /**
         * 内容密级id数组
         */
        private Integer[] contentSecretIds;

        /**
         * 附件密级数组
         */
        private Integer[] annexSecretIds;

        @Override
        public String toString() {
                return "UserSecretDto{"
                        + "id='" + id
                        + "name='" + name
                        + ", remark=" + remark
                        + ", sortNum=" + sortNum
                        + ", contentSecretIds="
                        + Arrays.toString(contentSecretIds)
                        + ", annexSecretIds="
                        + Arrays.toString(annexSecretIds)
                        + '}';
        }

        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        @NotBlank
        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public Integer getRemark() {
                return remark;
        }

        public void setRemark(Integer remark) {
                this.remark = remark;
        }

        public Integer getSortNum() {
                return sortNum;
        }

        public void setSortNum(Integer sortNum) {
                this.sortNum = sortNum;
        }

        public Integer[] getContentSecretIds() {
                return contentSecretIds;
        }

        public void setContentSecretIds(Integer[] contentSecretIds) {
                this.contentSecretIds = contentSecretIds;
        }

        public Integer[] getAnnexSecretIds() {
                return annexSecretIds;
        }

        public void setAnnexSecretIds(Integer[] annexSecretIds) {
                this.annexSecretIds = annexSecretIds;
        }

        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + Arrays.hashCode(annexSecretIds);
                result = prime * result + Arrays.hashCode(contentSecretIds);
                result = prime * result + ((id == null) ? 0 : id.hashCode());
                result = prime * result + ((name == null) ? 0 : name.hashCode());
                result = prime * result + ((remark == null) ? 0 : remark.hashCode());
                result = prime * result + ((sortNum == null) ? 0 : sortNum.hashCode());
                return result;
        }

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
                UserSecretDto other = (UserSecretDto) obj;
                if (!Arrays.equals(annexSecretIds, other.annexSecretIds)) {
                        return false;
                }
                if (!Arrays.equals(contentSecretIds, other.contentSecretIds)) {
                        return false;
                }
                if (id == null) {
                        if (other.id != null) {
                                return false;
                        }
                } else if (!id.equals(other.id)) {
                        return false;
                }
                if (name == null) {
                        if (other.name != null) {
                                return false;
                        }
                } else if (!name.equals(other.name)) {
                        return false;
                }
                if (remark == null) {
                        if (other.remark != null) {
                                return false;
                        }
                } else if (!remark.equals(other.remark)) {
                        return false;
                }
                if (sortNum == null) {
                        if (other.sortNum != null) {
                                return false;
                        }
                } else if (!sortNum.equals(other.sortNum)) {
                        return false;
                }
                return true;
        }
}
