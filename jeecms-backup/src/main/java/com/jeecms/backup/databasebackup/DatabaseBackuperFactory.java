package com.jeecms.backup.databasebackup;

import javax.validation.constraints.NotNull;

/**
 * @author Zhu Kaixiao
 * @version 1.0
 * @date 2019/7/30 14:45
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class DatabaseBackuperFactory {

    public static Backup createBackuper(@NotNull BackupConfig backupConfig) {
        AbstractDatabaseBackup backuper;
        switch (backupConfig.getDatabaseType()) {
            case BackupConfig.DB_TYPE_MYSQL:
                backuper = new LocalMysqlBackup();
                break;
            case BackupConfig.DB_TYPE_SQL_SERVER:
                backuper = new LocalSqlServerBackup();
                break;
            case BackupConfig.DB_TYPE_ORACLE:
                backuper = new LocalOracleBackup();
                break;
            default:
                backuper = new NonBackup();
                break;
        }
        backuper.setBackupConfig(backupConfig);
        return backuper;
    }
}
