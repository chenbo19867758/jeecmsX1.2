/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.vo;

import java.io.Serializable;

import com.jeecms.auth.domain.CoreRole;
import com.jeecms.system.domain.vo.CmsDataPermVo.SiteMap;

/**
 * 角色站群类权限Vo
 * 
 * @author: tom
 * @date: 2019年4月28日 下午4:55:41
 */
public class RoleOwnerSitePermVo implements Serializable {
        private static final long serialVersionUID = 4493808650948644156L;
        CoreRole role;
        SiteMap siteOwner;

        public CoreRole getRole() {
                return role;
        }

        public void setRole(CoreRole role) {
                this.role = role;
        }

        public SiteMap getSiteOwner() {
                return siteOwner;
        }

        public void setSiteOwner(SiteMap siteOwner) {
                this.siteOwner = siteOwner;
        }

        

}
