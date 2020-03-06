/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.dto;

import java.io.Serializable;
import java.util.Set;

import com.jeecms.system.domain.dto.CmsDatePermDto.MiniDataUnit;

/**
 * 用户站点类、栏目类权限dto
 * 
 * @author: tom
 * @date: 2019年4月29日 下午5:55:01
 */
public class UserPermUnitDto implements Serializable {

        private static final long serialVersionUID = 2558248264751107510L;
        /** 用户Id */
        Integer userId;
        /** 操作选项 */
        Set<MiniDataUnit> ops;

        public Integer getUserId() {
                return userId;
        }

        public void setUserId(Integer userId) {
                this.userId = userId;
        }

        public Set<MiniDataUnit> getOps() {
                return ops;
        }

        public void setOps(Set<MiniDataUnit> ops) {
                this.ops = ops;
        }

        /**
         * 重写 hashCode
         */
        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((ops == null) ? 0 : ops.hashCode());
                result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
                UserPermUnitDto other = (UserPermUnitDto) obj;
                if (ops == null) {
                        if (other.ops != null) {
                                return false;
                        }
                } else if (!ops.equals(other.ops)) {
                        return false;
                }
                if (userId == null) {
                        if (other.userId != null) {
                                return false;
                        }
                } else if (!userId.equals(other.userId)) {
                        return false;
                }
                return true;
        }

}
