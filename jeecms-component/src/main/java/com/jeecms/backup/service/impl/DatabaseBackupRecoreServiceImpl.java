package com.jeecms.backup.service.impl;

import com.jeecms.backup.dao.DatabaseBackupRecordDao;
import com.jeecms.backup.domain.DatabaseBackupRecord;
import com.jeecms.backup.service.DatabaseBackupRecordService;
import com.jeecms.common.base.service.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Zhu Kaixiao
 * @version 1.0
 * @date 2019/8/3 10:37
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DatabaseBackupRecoreServiceImpl extends BaseServiceImpl<DatabaseBackupRecord, DatabaseBackupRecordDao, Integer>
        implements DatabaseBackupRecordService {


    @Override
    public long fixLostState() {
        return dao.fixLostState();
    }
}
