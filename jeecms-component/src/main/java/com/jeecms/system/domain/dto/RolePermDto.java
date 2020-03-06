/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.jeecms.system.domain.dto.CmsDatePermDto.MiniDataUnit;


/**
 * 栏目权限分配 角色分配权限 dto
 * 
 * @author: tom
 * @date: 2019年4月29日 下午5:45:45
 */
public class RolePermDto implements Serializable {

        private static final long serialVersionUID = 5150755555480613702L;
        Integer roleId;
        Set<MiniDataUnit> ops;

        public Integer getRoleId() {
                return roleId;
        }

        public Set<MiniDataUnit> getOps() {
                return ops;
        }

        public void setRoleId(Integer roleId) {
                this.roleId = roleId;
        }

        public void setOps(Set<MiniDataUnit> ops) {
                this.ops = ops;
        }

}
