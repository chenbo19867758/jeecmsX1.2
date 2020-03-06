/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.resource.domain.dto;

import javax.validation.constraints.NotNull;

/**
 * 移动资源空间Dto
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/5/24 10:32:25
 */

public class ResourcesSpaceMoveDto {
        /**
         * 资源空间id
         */
        private Integer id;
        /**
         * 目标资源空间id
         */
        private Integer parentId;
        /**
         * 排序
         */
        private Integer sortNum;

        @NotNull
        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public Integer getParentId() {
                return parentId;
        }

        public void setParentId(Integer parentId) {
                this.parentId = parentId;
        }

        public Integer getSortNum() {
                return sortNum;
        }

        public void setSortNum(Integer sortNum) {
                this.sortNum = sortNum;
        }

        @Override
        public boolean equals(Object o) {
                if (this == o) {
                        return true;
                }
                if (!(o instanceof ResourcesSpaceMoveDto)) {
                        return false;
                }
                ResourcesSpaceMoveDto that = (ResourcesSpaceMoveDto) o;

                if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) {
                        return false;
                }
                if (getParentId() != null ? !getParentId().equals(that.getParentId()) : that.getParentId() != null) {
                        return false;
                }
                return getSortNum() != null ? getSortNum().equals(that.getSortNum()) : that.getSortNum() == null;
        }

        @Override
        public int hashCode() {
                int result = getId() != null ? getId().hashCode() : 0;
                result = 31 * result + (getParentId() != null ? getParentId().hashCode() : 0);
                result = 31 * result + (getSortNum() != null ? getSortNum().hashCode() : 0);
                return result;
        }
}
