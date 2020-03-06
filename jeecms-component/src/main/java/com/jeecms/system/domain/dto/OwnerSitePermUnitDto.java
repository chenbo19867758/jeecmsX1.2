/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * 站群类权限最小单元dto
 * 
 * @author: tom
 * @date: 2019年4月29日 下午5:55:01
 */
public class OwnerSitePermUnitDto implements Serializable {

        private static final long serialVersionUID = 2558248264751107510L;
        /** 是否选中 */
        Boolean selected;
        /** 组织ID */
        Integer orgId;
        /** 角色Id*/
        Integer roleId;
        /** 用户ID */
        Integer userId;

        @NotNull
        public Boolean getSelected() {
                return selected;
        }

        public void setSelected(Boolean selected) {
                this.selected = selected;
        }
        
        

        /**
         * @return the orgId
         */
        public Integer getOrgId() {
                return orgId;
        }

        /**
         * @return the roleId
         */
        public Integer getRoleId() {
                return roleId;
        }

        /**
         * @return the userId
         */
        public Integer getUserId() {
                return userId;
        }

        /**
         * @param orgId the orgId to set
         */
        public void setOrgId(Integer orgId) {
                this.orgId = orgId;
        }

        /**
         * @param roleId the roleId to set
         */
        public void setRoleId(Integer roleId) {
                this.roleId = roleId;
        }

        /**
         * @param userId the userId to set
         */
        public void setUserId(Integer userId) {
                this.userId = userId;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((orgId == null) ? 0 : orgId.hashCode());
                result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
                result = prime * result + ((selected == null) ? 0 : selected.hashCode());
                result = prime * result + ((userId == null) ? 0 : userId.hashCode());
                return result;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
                if (this == obj)
                        return true;
                if (obj == null)
                        return false;
                if (getClass() != obj.getClass())
                        return false;
                OwnerSitePermUnitDto other = (OwnerSitePermUnitDto) obj;
                if (orgId == null) {
                        if (other.orgId != null)
                                return false;
                } else if (!orgId.equals(other.orgId))
                        return false;
                if (roleId == null) {
                        if (other.roleId != null)
                                return false;
                } else if (!roleId.equals(other.roleId))
                        return false;
                if (selected == null) {
                        if (other.selected != null)
                                return false;
                } else if (!selected.equals(other.selected))
                        return false;
                if (userId == null) {
                        if (other.userId != null)
                                return false;
                } else if (!userId.equals(other.userId))
                        return false;
                return true;
        }

        

}
