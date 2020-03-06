/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.vo;

import java.io.Serializable;
import java.util.List;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.system.domain.vo.CmsDataPermVo.MiniDataUnit;

/**
 * 用户站点类、栏目类权限Vo
 * 
 * @author: tom
 * @date: 2019年4月28日 下午5:30:55
 */
public class UserPermVo implements Serializable {

        private static final long serialVersionUID = 2558248264751107510L;
        /** 用户对象 */
        CoreUser user;
        /** 操作选项 */
        List<MiniDataUnit> ops;
        
        public CoreUser getUser() {
                return user;
        }

        public List<MiniDataUnit> getOps() {
                return ops;
        }

        public void setUser(CoreUser user) {
                this.user = user;
        }

        public void setOps(List<MiniDataUnit> ops) {
                this.ops = ops;
        }

}
