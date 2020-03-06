/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.system.domain.SysLogBackup;


/**
 * 日志备份Dao
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-05-31 14:11:24
 */
public interface SysLogBackupDao extends IBaseDao<SysLogBackup, Integer> {

        /**
         * 通过名称查找日志备份
         *
         * @param backupName 日志备份名称
         * @return SysLogBackup
         */
        SysLogBackup findByBackupName(String backupName);
}
