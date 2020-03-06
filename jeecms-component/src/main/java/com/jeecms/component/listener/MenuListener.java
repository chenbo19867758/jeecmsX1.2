/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.component.listener;

import com.jeecms.auth.domain.CoreMenu;
import com.jeecms.common.exception.GlobalException;

/**
 * 菜单监听器(完成菜单权限自动赋予)
 * 
 * @author: tom
 * @date: 2019年4月19日 上午11:24:27
 */
public interface MenuListener {
        /**
         * 菜单保存后
         * 
         * @Title: afterMenuSave
         * @param menu 菜单对象
         * @throws GlobalException GlobalException         
         */
        void afterMenuSave(CoreMenu menu) throws GlobalException;
}
