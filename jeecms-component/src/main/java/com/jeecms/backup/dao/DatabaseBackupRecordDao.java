package com.jeecms.backup.dao;

import com.jeecms.backup.dao.ext.DatabaseBackupRecordDaoExt;
import com.jeecms.backup.domain.DatabaseBackupRecord;
import com.jeecms.common.base.dao.IBaseDao;

/**
 * @author Zhu Kaixiao
 * @version 1.0
 * @date 2019/8/3 10:22
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface DatabaseBackupRecordDao extends IBaseDao<DatabaseBackupRecord, Integer>, DatabaseBackupRecordDaoExt {
}
