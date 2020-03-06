package com.jeecms.backup.databasebackup;

import com.jeecms.backup.exception.BackupException;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 本地Sql Server数据库备份还原
 *
 * @author Zhu Kaixiao
 * @version 1.0
 * @date 2019/7/31 9:20
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class LocalSqlServerBackup extends AbstractDatabaseBackup {
	static Logger log = LoggerFactory.getLogger(LocalSqlServerBackup.class);
    private static final String DRIVER_CLASS_NAME = "com.microsoft.sqlserver.jdbc.SQLServerDriver";


    /**
     * 通过T-SQL备份
     * BACKUP DATABASE zkx_backup_test TO DISK='D:\2.bak'
     * 备份生成的bak文件在db服务器上, 而不是在当前应用服务器上
     * T-SQL备份详细资料: https://docs.microsoft.com/zh-cn/sql/t-sql/statements/backup-transact-sql?view=sql-server-2017
     *
     * @author Zhu Kaixiao
     * @date 2019/7/31 9:35
     **/
    @Override
    protected String doBackup() {
        try (Connection connection = getConnection(DRIVER_CLASS_NAME, jdbcUrl(),
                backupConfig.getUsername(), backupConfig.getPassword())) {
            String tSql = String.format("BACKUP DATABASE %s TO DISK='%s'",
                    backupConfig.getDatabaseName(), backupConfig.getDataSavePath());
            log.debug("准备执行备份T-SQL [{}]", tSql);
            Statement statement = connection.createStatement();
            statement.execute(tSql);
            log.info("数据库[{}]备份成功, 备份文件路径:[{}]", backupConfig.getDatabaseName(), backupConfig.getDataSavePath());
            return backupConfig.getDataSavePath();
        } catch (Exception e) {
            log.error("数据库[{}]备份失败!\t{}", backupConfig.getDatabaseName(), e.getMessage());
            throw dressException(e);
        }
    }

    /**
     * 通过T-SQL还原, 调用该方法将会指定数据库上的所有用户链接,并回滚事务
     * 因为在还原数据库时,需要获得对数据库的独占访问权
     * <p>
     * 目前只支持还原全量备份
     * RESTORE DATABASE参考资料: https://docs.microsoft.com/zh-cn/sql/t-sql/statements/restore-statements-transact-sql?view=sql-server-2017
     *
     * @author Zhu Kaixiao
     * @date 2019/7/31 11:18
     **/
    @Override
    protected boolean doRecovery() {
        try (Connection connection = getConnection(DRIVER_CLASS_NAME, jdbcUrl(),
                backupConfig.getUsername(), backupConfig.getPassword())) {
            try {
//          ALTER DATABASE zkx_backup_test
//          SET OFFLINE WITH ROLLBACK IMMEDIATE
//          用于断开所有用户链接，并回滚所有事务;  因为在还原数据库时,需要获得对数据库的独占访问权
                String tSql1 = "ALTER DATABASE zkx_backup_test SET OFFLINE WITH ROLLBACK IMMEDIATE";
                log.debug("尝试获取数据库独占访问权 [{}]", tSql1);
                Statement statement = connection.createStatement();
                statement.execute(tSql1);
            } catch (Exception e) {
                // 如果是数据库不存在, 忽略异常
                if (!e.getMessage().contains("数据库不存在")) {
                    throw dressException(e);
                }
            }

            String tSql2 = String.format("RESTORE DATABASE %s FROM DISK='%s'",
                    backupConfig.getDatabaseName(), backupConfig.getDataSavePath());
            log.debug("准备执行还原T-SQL [{}]", tSql2);
            Statement statement = connection.createStatement();
            statement.execute(tSql2);
            log.info("数据库[{}]还原成功, 还原文件路径:[{}]", backupConfig.getDatabaseName(), backupConfig.getDataSavePath());
            return true;
        } catch (Exception e) {
            log.error("数据库[{}]还原失败!\t{}", backupConfig.getDatabaseName(), e.getMessage());
            throw dressException(e);
        }
    }

    @Override
    protected void before() {
        super.before();
        Objects.requireNonNull(backupConfig.getDatabaseName());
    }


    private BackupException dressException(Exception e) {
        Throwable cause = e.getCause();
        cause = cause == null ? e : cause;
        String msg;
        if (cause.getMessage().toLowerCase().contains("connection timed out")) {
            msg = "数据库连接超时";
        } else {
            msg = cause.getMessage().split("ClientConnectionId:")[0];
        }
        return new BackupException(msg);
    }

    private String jdbcUrl() {
        return String.format("jdbc:sqlserver://%s:%d;database=master;", backupConfig.getHost(), backupConfig.getPort());
    }
}
