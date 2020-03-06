/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.system.domain.SysLogConfig;

/**
 * 日志策略Service
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-05-29 10:53:36
 */
public interface SysLogConfigService extends IBaseService<SysLogConfig, Integer> {

        /**
         * 获取唯一的日志策略
         *
         * @return SysLogConfig
         */
        SysLogConfig getDefault();

        /**
         * 获取日志表大小
         *
         * @return 日志表大小
         */
        String getTableSize();
}
