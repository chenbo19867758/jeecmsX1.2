package com.jeecms.backup.databasebackup;

import com.jeecms.backup.entity.BackupDto;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LocalOracleBackupTest {

    private BackupConfig backupConfig;
    private Backup backuper;

    @Before
    public void init() {
        BackupDto dataSourceProperties = new BackupDto();
        dataSourceProperties.setUsername("zkx");
        dataSourceProperties.setPassword("123456");
        dataSourceProperties.setJdbcUrl("jdbc:oracle:thin:@localhost:1521:orcl");

        backupConfig = BackupConfig
                .of(dataSourceProperties);

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
        backupConfig.setDataSavePath("F:\\APP\\ASUS\\ADMIN\\ORCL\\DPDUMP\\ORCL_ZKX_1564972504256.JCBAK");
        boolean success = backuper.recovery();
        assertTrue(success);
    }
}