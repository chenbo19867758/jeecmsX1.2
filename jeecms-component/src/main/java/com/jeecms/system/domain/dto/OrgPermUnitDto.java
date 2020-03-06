/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.dto;

import java.util.Set;

import com.jeecms.system.domain.dto.CmsDatePermDto.MiniDataUnit;

/**
 * 栏目权限分配 组织分配页面 单个组织以及下属角色 权限数据
 * 
 * @author: tom
 * @date: 2019年4月29日 下午6:17:22
 */
public class OrgPermUnitDto {
        Integer orgId;
        Integer roleId;
        /** 组织操作 */
        Set<MiniDataUnit> ops;

        public Integer getOrgId() {
                return orgId;
        }

        public void setOrgId(Integer orgId) {
                this.orgId = orgId;
        }
        

        /**
         * @return the roleId
         */
        public Integer getRoleId() {
                return roleId;
        }

        /**
         * @return the ops
         */
        public Set<MiniDataUnit> getOps() {
                return ops;
        }

        /**
         * @param roleId the roleId to set
         */
        public void setRoleId(Integer roleId) {
                this.roleId = roleId;
        }

        /**
         * @param ops the ops to set
         */
        public void setOps(Set<MiniDataUnit> ops) {
                this.ops = ops;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((ops == null) ? 0 : ops.hashCode());
                result = prime * result + ((orgId == null) ? 0 : orgId.hashCode());
                result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
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
                OrgPermUnitDto other = (OrgPermUnitDto) obj;
                if (ops == null) {
                        if (other.ops != null)
                                return false;
                } else if (!ops.equals(other.ops))
                        return false;
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
                return true;
        }
        
        
}
