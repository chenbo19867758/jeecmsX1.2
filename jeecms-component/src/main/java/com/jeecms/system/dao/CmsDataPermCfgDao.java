/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.system.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.system.domain.CmsDataPermCfg;

/**   
 * 权限配置dao
 * @author: tom
 * @date:   2019年4月22日 上午11:18:29     
 */
public interface CmsDataPermCfgDao extends IBaseDao<CmsDataPermCfg, Integer> {
        
        @Modifying
        @Query(value = "delete from CmsDataPermCfg permCfg where permCfg.siteId=?1")
        void deleteBySiteId(Integer siteId);
}
