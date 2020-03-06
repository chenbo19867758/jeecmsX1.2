package com.jeecms.backup.databasebackup;

import com.jeecms.backup.entity.BackupDto;
import org.junit.Before;
import org.junit.Test;

public class AsyncBackupWrapperTest {

    private AsyncBackupWrapper asyncBackupWrapper;
    private BackupConfig backupConfig;
    private Backup backuper;
    private volatile boolean syncFlg;

    @Before
    public void init() {
        syncFlg = true;
        asyncBackupWrapper = new AsyncBackupWrapper();
        BackupDto dataSourceProperties = new BackupDto();
        dataSourceProperties.setUsername("zkx");
        dataSourceProperties.setPassword("123456");
        dataSourceProperties.setJdbcUrl("jdbc:oracle:thin:@localhost:1521:orcl");

        backupConfig = BackupConfig
                .of(dataSourceProperties);

        backuper = DatabaseBackuperFactory.createBackuper(backupConfig);
    }

    @Test
    public void backup() {

        asyncBackupWrapper.backup(backuper, (bakFilePath, errMsg) -> {
            System.out.println("异步备份完成, bakFilePath:" + bakFilePath + "   errMsg:" + errMsg);
            syncFlg = false;
        });

        while (syncFlg) {
            System.out.println("等待备份任务...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("异步备份测试结束");
    }

    @Test
    public void recovery() {
        backupConfig.setDataSavePath("F:\\code\\jeecms-parent\\jeecms-backup\\database-backup\\orcl_zkx_1565070879484.jcbak");
        asyncBackupWrapper.recovery(backuper, (success, errMsg) -> {
            System.out.println("异步还原完成, success:" + success + "   errMsg:" + errMsg);
            syncFlg = false;
        });

        while (syncFlg) {
            System.out.println("等待还原任务...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("异步还原测试结束");
    }
}