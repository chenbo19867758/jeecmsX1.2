/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.component.listener;

import com.jeecms.common.exception.GlobalException;

/**   
 * 用户角色监听器
 * @author: tom
 * @date:   2019年4月8日 上午11:48:36     
 */
public interface RoleListener {
        
        /**
         * 角色删除的操作
         * @Title: afterRoleDelete
         * @param ids 角色id
         * @throws GlobalException GlobalException
         */
        void afterRoleDelete(Integer[] ids) throws GlobalException;
}
