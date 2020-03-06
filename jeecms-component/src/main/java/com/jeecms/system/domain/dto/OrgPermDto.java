/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

/**
 * 栏目权限分配 组织分配页面权限dto
 * 
 * @author: tom
 * @date: 2019年4月29日 下午5:43:57
 */
public class OrgPermDto implements Serializable {

        private static final long serialVersionUID = 7858333753867330303L;
        /** 数据类型 2栏目 3内容 1站点 */
        Short dataType;
        /**栏目Id或者站点ID*/
        Integer dataId;
        HashSet<OrgPermUnitDto> perms;

        @NotNull
        public Short getDataType() {
                return dataType;
        }

        public HashSet<OrgPermUnitDto> getPerms() {
                return perms;
        }

        public void setDataType(Short dataType) {
                this.dataType = dataType;
        }

        public void setPerms(HashSet<OrgPermUnitDto> perms) {
                this.perms = perms;
        }

        @NotNull
        public Integer getDataId() {
                return dataId;
        }

        public void setDataId(Integer dataId) {
                this.dataId = dataId;
        }

}
