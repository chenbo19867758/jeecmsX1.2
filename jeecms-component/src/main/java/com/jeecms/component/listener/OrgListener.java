/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.component.listener;

import com.jeecms.common.exception.GlobalException;

/**
 * 组织监听器
 * 
 * @author: tom
 * @date: 2019年4月8日 上午9:23:08
 */
public interface OrgListener {

        /**
         * 删除组织
         * 
         * @Title: afterOrgDelete
         * @param ids
         *                组织ID
         * @throws GlobalException
         *                 GlobalException
         */
        void afterOrgDelete(Integer[] ids) throws GlobalException;
}
