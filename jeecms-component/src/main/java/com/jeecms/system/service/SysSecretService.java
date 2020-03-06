/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.system.domain.SysSecret;

import java.util.List;

/**
 * 内容/附件密级Service
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-25
 */
public interface SysSecretService extends IBaseService<SysSecret, Integer> {

        /**
         * 校验密集名称是否可用
         *
         * @param name 密级名称
         * @param id   密级id
         * @param secretType 密级类型
         * @return boolean true 可用 false 不可用
         */
        boolean checkByName(String name, Integer id, Integer secretType);
        
        List<SysSecret> findByType(Integer type);
}
