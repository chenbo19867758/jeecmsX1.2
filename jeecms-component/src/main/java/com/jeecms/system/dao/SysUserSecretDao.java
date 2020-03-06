/*   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.system.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.system.domain.SysUserSecret;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;


/**
 * 人员密级Dao
 *
* @author xiaohui
* @version 1.0
* @date 2019-04-25
*/
public interface SysUserSecretDao extends IBaseDao<SysUserSecret, Integer> {

        /**
         * 通过密级名称查找密级
         *
         * @param name 密级名称
         * @return SysSecret
         */
        @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
        SysUserSecret findByName(String name);
}
