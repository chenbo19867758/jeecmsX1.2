package com.jeecms.backup.databasebackup;

import com.jeecms.backup.entity.BackupDto;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LocalMysqlBackupTest {

    private BackupConfig backupConfig;
    private Backup backuper;

    @Before
    public void init() {
        BackupDto dataSourceProperties = new BackupDto();
        dataSourceProperties.setUsername("root");
        dataSourceProperties.setPassword("123456");
        dataSourceProperties.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/test");

        backupConfig = BackupConfig
                .of(dataSourceProperties);

        backuper = DatabaseBackuperFactory.createBackuper(backupConfig);
    }

    private void doBackup() {
        String backup = backuper.backup();
        assertEquals(backup, backupConfig.getDataSavePath());
    }

    private void doRecovery() {
        boolean success = backuper.recovery();
        assertTrue(success);
    }

    @Test
    public void test() {
        doBackup();
        System.out.println("备份成功");
        doRecovery();
        System.out.println("还原成功");
    }
}