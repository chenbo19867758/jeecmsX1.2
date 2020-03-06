package com.jeecms.backup.databasebackup;

import com.jeecms.backup.entity.BackupDto;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LocalSqlServerBackupTest {

    private BackupConfig backupConfig;
    private Backup backuper;

    @Before
    public void init() {
        BackupDto dataSourceProperties = new BackupDto();
        dataSourceProperties.setUsername("sa");
        dataSourceProperties.setPassword("mingming_chen");
        dataSourceProperties.setJdbcUrl("jdbc:sqlserver://192.168.0.2:1433;DatabaseName=zkx_backup_test");

        backupConfig = BackupConfig.of(dataSourceProperties);
        backupConfig.setDataSavePath("f:/1111.jcbak");

        backuper = DatabaseBackuperFactory.createBackuper(backupConfig);
    }

    @Test
    public void test() {
        doBackup();
        System.out.println("备份成功");
        doRecovery();
        System.out.println("还原成功");
    }

    private void doBackup() {
        String backup = backuper.backup();
        assertEquals(backup, backupConfig.getDataSavePath());
    }

    private void doRecovery() {
        boolean success = backuper.recovery();
        assertTrue(success);
    }
}