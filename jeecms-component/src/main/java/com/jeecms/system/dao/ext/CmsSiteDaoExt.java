/**
 * 
 */

package com.jeecms.system.dao.ext;

import com.jeecms.system.domain.CmsSite;

import java.util.Collection;
import java.util.List;

/**   
 * 站点扩展dao接口
 * @author: tom
 * @date:   2018年11月5日 下午1:55:53     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface CmsSiteDaoExt {
        /**
         * 依据域名查询站点集合
         * @Title: findByDomain
         * @param domain 域名
         * @return: List
         */
        List<CmsSite> findByDomain(String domain);
        /***
         * 根据ID查询
         * @param ids
         * @return
         */
        List<CmsSite> findByIds(Collection<Integer> ids);

}
