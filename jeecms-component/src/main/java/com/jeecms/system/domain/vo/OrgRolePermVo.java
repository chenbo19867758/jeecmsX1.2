/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.vo;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.jeecms.auth.domain.CoreRole;
import com.jeecms.system.domain.CmsOrg;
import com.jeecms.system.domain.vo.CmsDataPermVo.MiniDataUnit;

/**
 * 角色栏目类、站点类权限Vo
 * 
 * @author: tom
 * @date: 2019年4月28日 下午4:55:41
 */
public class OrgRolePermVo implements Serializable {
        private static final long serialVersionUID = 4493808650948644156L;
        CoreRole role;
        CmsOrg org;
        List<MiniDataUnit> ops;
        
        /**
         * 构造角色 栏目类、站点类权限
         * @param role 角色
         * @param ops 操作集合
         */
        public OrgRolePermVo(CoreRole role, List<MiniDataUnit> ops) {
                super();
                this.role = role;
                this.ops = ops;
        }

        /**
         * 构造组织 栏目类、站点类权限
         * @param org 组织 
         * @param ops 操作集合
         */
        public OrgRolePermVo(CmsOrg org, List<MiniDataUnit> ops) {
                super();
                this.org = org;
                this.ops = ops;
        }
        
        
        public OrgRolePermVo() {
                super();
        }

        /**
         * 获取角色名称
         * @Title: getRoleName
         * @return: String
         */
        public String getRoleName() {
                if (getRole() != null) {
                        return getRole().getRoleName();
                }
                return null;
        }

        /**
         * 获取角色ID
         * @Title: getRoleId
         * @return: Integer
         */
        public Integer getRoleId() {
                if (getRole() != null) {
                        return getRole().getId();
                }
                return null;
        }

        /**
         * 获取组织名称
         * @Title: getOrgName
         * @return: String
         */
        public String getOrgName() {
                if (getOrg() != null) {
                        return getOrg().getName();
                }
                return null;
        }

        /**
         * 获取组织ID
         * @Title: getOrgId
         * @return: Integer
         */
        public Integer getOrgId() {
                if (getOrg() != null) {
                        return getOrg().getId();
                }
                return null;
        }

        @JSONField(serialize=false)
        public CoreRole getRole() {
                return role;
        }

        public List<MiniDataUnit> getOps() {
                return ops;
        }

        public void setRole(CoreRole role) {
                this.role = role;
        }

        public void setOps(List<MiniDataUnit> ops) {
                this.ops = ops;
        }

        /**
         * @return the org
         */
        @JSONField(serialize=false)
        public CmsOrg getOrg() {
                return org;
        }

        /**
         * @param org
         *                the org to set
         */
        public void setOrg(CmsOrg org) {
                this.org = org;
        }

}
