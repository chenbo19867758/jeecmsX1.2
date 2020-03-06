/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

/**
 * 用户站点类、栏目类权限dto
 * 
 * @author: tom
 * @date: 2019年4月29日 下午5:55:01
 */
public class UserPermDto implements Serializable {

        private static final long serialVersionUID = 2558248264751107510L;
        /** 数据类型 2栏目 3内容  1站点类*/
        Short dataType;
        /** 操作选项 */
        HashSet<UserPermUnitDto> perms;
        /**操作的栏目或者站点ID*/
        Integer dataId;
        

        @NotNull
        public Integer getDataId() {
                return dataId;
        }

        public void setDataId(Integer dataId) {
                this.dataId = dataId;
        }

        @NotNull
        public Short getDataType() {
                return dataType;
        }

        public HashSet<UserPermUnitDto> getPerms() {
                return perms;
        }

        public void setDataType(Short dataType) {
                this.dataType = dataType;
        }

        public void setPerms(HashSet<UserPermUnitDto> perms) {
                this.perms = perms;
        }

}
