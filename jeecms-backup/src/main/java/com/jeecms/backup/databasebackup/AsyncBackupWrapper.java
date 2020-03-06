package com.jeecms.backup.databasebackup;

import com.jeecms.backup.exception.BackupException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 备份任务异步执行包装器
 *
 * @author Zhu Kaixiao
 * @version 1.0
 * @date 2019/8/5 16:17
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class AsyncBackupWrapper {
	static Logger log = LoggerFactory.getLogger(AsyncBackupWrapper.class);
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

    /**
     * 空闲
     */
    public static final int IDLE = 0;
    /**
     * 备份中
     */
    public static final int IN_BACKUP = 1;
    /**
     * 还原中
     */
    public static final int IN_RECOVERY = 2;

    private volatile int state;

    public void backup(final Backup backup, final Callback<String, String> callback) {
        if (state == IDLE) {
            EXECUTOR.submit(() -> {
                try {
                    state = IN_BACKUP;
                    String errMsg = null;
                    String bakFilePath = null;
                    try {
                        bakFilePath = backup.backup();
                    } catch (Exception e) {
                        errMsg = e.getMessage();
                    }
                    callback.call(bakFilePath, errMsg);
                } finally {
                    state = IDLE;
                }
            });
        } else {
            throw new BackupException("已有" + (state == IN_BACKUP ? "备份" : "还原") + "任务正在执行");
        }
    }

    public void recovery(final Backup backup, final Callback<Boolean, String> callback) {
        if (state == IDLE) {
            EXECUTOR.submit(() -> {
                try {
                    state = IN_RECOVERY;
                    String errMsg = null;
                    boolean success = false;
                    try {
                        success = backup.recovery();
                    } catch (Exception e) {
                        errMsg = e.getMessage();
                    }
                    callback.call(success, errMsg);
                } finally {
                    state = IDLE;
                }
            });
        } else {
            throw new BackupException("已有" + (state == IN_BACKUP ? "备份" : "还原") + "任务正在执行");
        }
    }

    public interface Callback<P, ERR> {
        void call(P param, ERR errorMsg);
    }
}

