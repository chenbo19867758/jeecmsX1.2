/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.vo;

import java.io.Serializable;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.system.domain.vo.CmsDataPermVo.SiteMap;

/**
 * 用户站群权限Vo
 * 
 * @author: tom
 * @date: 2019年4月28日 下午5:30:55
 */
public class UserOwnerSitePermVo implements Serializable {

        private static final long serialVersionUID = 2558248264751107510L;
        /** 用户对象 */
        CoreUser user;
        SiteMap siteOwner;

        public CoreUser getUser() {
                return user;
        }

        public void setUser(CoreUser user) {
                this.user = user;
        }

        public SiteMap getSiteOwner() {
                return siteOwner;
        }

        public void setSiteOwner(SiteMap siteOwner) {
                this.siteOwner = siteOwner;
        }

        

}
