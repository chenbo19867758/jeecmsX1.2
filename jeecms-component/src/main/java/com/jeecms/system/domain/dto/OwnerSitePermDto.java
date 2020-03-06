/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.dto;

import java.io.Serializable;
import java.util.HashSet;

import javax.validation.constraints.NotNull;

/**
 * 站群类权限dto
 * 
 * @author: tom
 * @date: 2019年4月29日 下午5:55:01
 */
public class OwnerSitePermDto implements Serializable {

        private static final long serialVersionUID = 2558248264751107510L;
        HashSet<OwnerSitePermUnitDto> perms;
        Integer siteId;
        

        public HashSet<OwnerSitePermUnitDto> getPerms() {
                return perms;
        }

        @NotNull
        public Integer getSiteId() {
                return siteId;
        }

        public void setPerms(HashSet<OwnerSitePermUnitDto> perms) {
                this.perms = perms;
        }

        public void setSiteId(Integer siteId) {
                this.siteId = siteId;
        }

}
