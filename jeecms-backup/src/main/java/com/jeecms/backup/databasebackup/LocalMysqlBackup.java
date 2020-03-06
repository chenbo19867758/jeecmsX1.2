package com.jeecms.backup.databasebackup;

import com.jeecms.backup.config.Constant;
import com.jeecms.backup.exception.BackupException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

import static com.jeecms.backup.utils.CmdExecutor.CmdResult;
import static com.jeecms.backup.utils.CmdExecutor.executeCommand;

/**
 * 本地MySql数据库备份还原
 *
 * @author Zhu Kaixiao
 * @version 1.0
 * @date 2019/7/30 13:50
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class LocalMysqlBackup extends AbstractDatabaseBackup {
	static Logger log = LoggerFactory.getLogger(LocalMysqlBackup.class);
    /**
     * 备份
     * 参考备份命令:
     * mysqldump -h127.0.0.1 -P1433 -uroot -p123456 --databases Test >D:/12580.sql
     *
     * 导出表的数据及结构
     * mysqldump  databaseName -u username -ppassword --tables table_name1 table_name2 table_name3>D:\db_script.sql
     * @author Zhu Kaixiao
     * @date 2019/7/31 9:11
     **/
    @Override
    public String doBackup() {
        // 1. 执行备份命令
        List<String> cmdList = new LinkedList<>();
        cmdList.add("mysqldump");
        cmdList.add("-h" + backupConfig.getHost());
        cmdList.add("-P" + backupConfig.getPort());
        cmdList.add("-u" + backupConfig.getUsername());
        if (StringUtils.isNotBlank(backupConfig.getPassword())) {
            cmdList.add("-p" + backupConfig.getPassword());
        }

        // 参数中加了--databases或--all-databases, 那么生成的sql文件中就会有create database语句
        if (StringUtils.isNotBlank(backupConfig.getDatabaseName())) {
            cmdList.add("--databases");
            cmdList.add(backupConfig.getDatabaseName());
            // 忽略指定表
            for (String ignoreTable : Constant.IGNORE_TABLES) {
                cmdList.add(String.format("--ignore-table=%s.%s", backupConfig.getDatabaseName(), ignoreTable));
            }
        } else {
            cmdList.add("--all-databases");
        }

        cmdList.add(">\"" + backupConfig.getDataSavePath() + "\"");
        CmdResult cmdResult = executeCommand(cmdList);

        // 校验命令执行结果
        if (cmdResult == null) {
            log.error("备份命令执行失败");
            return null;
        }
        if (cmdResult.getCode() != 0) {
            if (cmdResult.getError().contains("error: 1045")) {
                throw new BackupException("用户名或密码错误");
            } else if (cmdResult.getError().contains("error: 1049")) {
                throw new BackupException("数据库[" + backupConfig.getDatabaseName() + "]不存在");
            } else {
                throw new BackupException(cmdResult.getError());
            }
        }
        return backupConfig.getDataSavePath();
    }


    /**
     * 参考恢复命令:
     * mysql -h127.0.0.1 -P3306 -uroot -p123456 jeecms <D:/12580.sql
     *
     * @author Zhu Kaixiao
     * @date 2019/7/31 9:06
     **/
    @Override
    public boolean doRecovery() {
        List<String> cmdList = new LinkedList<>();
        cmdList.add("mysql");
        cmdList.add("-h" + backupConfig.getHost());
        cmdList.add("-P" + backupConfig.getPort());
        cmdList.add("-u" + backupConfig.getUsername());
        if (StringUtils.isNotBlank(backupConfig.getPassword())) {
            cmdList.add("-p" + backupConfig.getPassword());
        }

        cmdList.add("--default-character-set=utf8");

        if (StringUtils.isNotBlank(backupConfig.getDatabaseName())) {
            cmdList.add(backupConfig.getDatabaseName());
        }

        cmdList.add("<\"" + backupConfig.getDataSavePath() + "\"");
        CmdResult cmdResult = executeCommand(cmdList);


        if (cmdResult == null) {
            log.error("还原命令执行失败");
            return false;
        }
        if (cmdResult.getCode() != 0) {
            if (cmdResult.getError().contains("ERROR 1045")) {
                throw new BackupException("用户名或密码错误");
            } else if (cmdResult.getError().contains("ERROR 1049")) {
                throw new BackupException("数据库[" + backupConfig.getDatabaseName() + "]不存在");
            } else {
                throw new BackupException(cmdResult.getError());
            }
        }
        return true;
    }

}
