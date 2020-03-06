package com.jeecms.backup.service;

import com.jeecms.backup.domain.DatabaseBackupRecord;
import com.jeecms.common.base.service.IBaseService;

/**
 * @author Zhu Kaixiao
 * @version 1.0
 * @date 2019/8/3 10:37
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface DatabaseBackupRecordService extends IBaseService<DatabaseBackupRecord, Integer> {

    /**
     * 在程序启动时把所有状态为备份中和还原中的备份记录
     * 都改为备份失败和还原失败
     *
     * @return long 修改的数量
     * @author Zhu Kaixiao
     * @date 2019/8/6 14:58
     **/
    long fixLostState();

}
