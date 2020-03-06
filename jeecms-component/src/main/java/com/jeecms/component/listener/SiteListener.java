/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.component.listener;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.domain.CmsSite;

/**
 * 站点监听器
 * 
 * @author: tom
 * @date: 2019年4月8日 上午9:16:35
 */
public interface SiteListener {
        
        /**
         * 站点物理删除操作
         * 
         * @Title: beforeSitePhysicDelete
         * @param ids 站点id
         * @throws GlobalException GlobalException
         */
        void beforeSitePhysicDelete(Integer[] ids) throws GlobalException;
        
        /**
         * 站点保存监听
         * @Title: afterSiteSave
         * @param site 站点
         * @throws GlobalException GlobalException
         * @return: void
         */
        void afterSiteSave(CmsSite site) throws GlobalException;
}
