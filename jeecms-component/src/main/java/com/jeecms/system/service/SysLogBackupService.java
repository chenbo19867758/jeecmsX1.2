/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.SysLog;
import com.jeecms.system.domain.SysLogBackup;

import java.util.List;

/**
 * 日志备份Service
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-05-31 14:12:54
 */
public interface SysLogBackupService extends IBaseService<SysLogBackup, Integer> {

        /**
         * 日志备份
         *
         * @param logBackup 日志备份实体类
         * @param site      站点id
         * @return SysLogBackup
         * @throws GlobalException 异常
         */
        SysLogBackup backup(SysLogBackup logBackup, CmsSite site) throws GlobalException;

        /**
         * 校验日志备份名称是否重复
         *
         * @param backupName 日志备份名称
         * @param id         日志备份id
         * @return true 不重复 false 重复
         */
        boolean checkByBackupName(String backupName, Integer id);

        /**
         * 还原日志
         * @param id 日志id
         * @param site 站点id
         * @return list
         * @throws GlobalException 异常
         */
        List<SysLog> restore(Integer id, CmsSite site) throws GlobalException;

}
