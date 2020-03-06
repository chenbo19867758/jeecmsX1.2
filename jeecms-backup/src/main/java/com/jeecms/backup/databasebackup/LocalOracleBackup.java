package com.jeecms.backup.databasebackup;

import com.jeecms.backup.config.Constant;
import com.jeecms.backup.exception.BackupException;
import com.jeecms.backup.utils.OSUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static com.jeecms.backup.utils.CmdExecutor.CmdResult;
import static com.jeecms.backup.utils.CmdExecutor.executeCommand;

/**
 * 本地Oracle数据库备份还原
 *
 * @author Zhu Kaixiao
 * @version 1.0
 * @date 2019/7/31 11:39
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class LocalOracleBackup extends AbstractDatabaseBackup {
	static Logger log = LoggerFactory.getLogger(LocalOracleBackup.class);
    private static final String DRIVER_CLASS_NAME = "oracle.jdbc.driver.OracleDriver";

    private String dumpfile;

    /**
     * oracle数据库通过数据泵备份
     * 参考命令:
     * 导出用户及其对象
     * expdp zkx/123456@127.0.0.1:1251/orcl schemas=zkx dumpfile=expdp.dmp
     * 导出指定表
     * expdp zkx/123456@127.0.0.1:1251/orcl tables=emp,dept dumpfile=expdp.dmp directory=dump_dir logfile=expdp.log;
     *
     * @return java.lang.String
     * @author Zhu Kaixiao
     * @date 2019/8/5 9:47
     **/
    @Override
    protected String doBackup() {
        // 1. 获取备份文件保存目录
        String dumpDir = getDumpDir();
        if (dumpDir == null) {
            log.error("数据库[{}.{}]备份失败!\t无法获取[DATA_PUMP_DIR]",
                    backupConfig.getDatabaseName(), backupConfig.getUsername());
            return null;
        }

        if (!doBackup("/")) {
            return null;
        }

        File bakFile = new File(dumpDir, dumpfile);
        if (bakFile.exists()) {
            try {
                FileUtils.copyFile(bakFile, new File(backupConfig.getDataSavePath()));
                return backupConfig.getDataSavePath();
            } catch (IOException ignore) {
                log.error("复制文件[{}]到[{}]失败", bakFile, backupConfig.getDataSavePath());
            }
        } else {
            log.error("备份命令执行成功, 但文件[{}]不存在", bakFile);
        }
        return null;
    }

    private boolean doBackup(String separator) {
        // 2. 执行备份命令
        List<String> cmdList = new LinkedList<>();
        cmdList.add("expdp");

        cmdList.add(String.format("%s/%s@%s:%d%s%s", backupConfig.getUsername(), backupConfig.getPassword(),
                backupConfig.getHost(), backupConfig.getPort(), separator, backupConfig.getDatabaseName()));
        cmdList.add("schemas=" + backupConfig.getUsername());
        cmdList.add(String.format("dumpfile=\"%s\"", dumpfile));
        CmdResult cmdResult = executeCommand(cmdList);

        // 3. 校验结果
        if (cmdResult == null) {
            log.error("备份命令执行失败");
            return false;
        }
        if (cmdResult.getCode() != 0) {
            if (cmdResult.getError().contains("ORA-12545")) {
                if ("/".equals(separator)) {
                    return doBackup(":");
                }
                return false;
            } else if (cmdResult.getError().contains("ORA-12541")) {
                throw new BackupException("无监听程序");
            } else if (cmdResult.getError().contains("ORA-12170")) {
                throw new BackupException("连接超时");
            } else if (cmdResult.getError().contains("ORA-27038")) {
                throw new BackupException("备份文件[" + dumpfile + "]已存在");
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * oracle通过数据泵还原数据库
     * 参考命令:
     * impdp zkx/123456@127.0.0.1:1251/orcl schemas=zkx dumpfile=expdp.dmp
     * @return boolean
     * @author Zhu Kaixiao
     * @date 2019/8/5 9:51
     **/
    @Override
    protected boolean doRecovery() {
        // 1. 获取备份文件保存目录
        String dumpDir = getDumpDir();
        if (dumpDir == null) {
            log.error("数据库[{}.{}]还原失败!\t无法获取[DATA_PUMP_DIR]",
                    backupConfig.getDatabaseName(), backupConfig.getUsername());
            return false;
        }
        try {
            // 先把文件拷贝到oracle指定的文件夹下
            File srcFile = new File(backupConfig.getDataSavePath());
            File destFile = new File(dumpDir, dumpfile);
            if (!Objects.equals(srcFile, destFile)) {
                FileUtils.copyFile(srcFile, destFile);
            }
        } catch (IOException e) {
            log.error("数据库[{}.{}]还原失败!\t复制备份文件到DATA_PUMP_DIR失败",
                    backupConfig.getDatabaseName(), backupConfig.getUsername());
            return false;
        }

        return doRecovery("/");
    }


    private boolean doRecovery(String separator) {
        List<String> cmdList = new LinkedList<>();
        cmdList.add("impdp");
        cmdList.add(String.format("%s/%s@%s:%d%s%s", backupConfig.getUsername(), backupConfig.getPassword(),
                backupConfig.getHost(), backupConfig.getPort(), separator, backupConfig.getDatabaseName()));
        cmdList.add("FULL=y");
        cmdList.add("table_exists_action=replace");
        // 忽略指定表
        // 参考: https://blog.csdn.net/liqfyiyi/article/details/7248911
        // exclude=table:\"like \'%LOG%\'\",table:\"like \'%REPORT%\'\"
        StringBuilder sb = new StringBuilder("exclude=TABLE:\\\"in ");
        if (OSUtil.currentPlatform() == OSUtil.PLATFORM_WINDOWS) {
            sb.append("(");
        }
        for (String ignoreTable : Constant.IGNORE_TABLES) {
            sb.append("\\'").append(ignoreTable).append("\\',");
        }
        sb.delete(sb.length() - 1, sb.length());
        if (OSUtil.currentPlatform() == OSUtil.PLATFORM_WINDOWS) {
            sb.append(")");
        }
        sb.append("\\\"");
        cmdList.add(sb.toString());
        cmdList.add(String.format("dumpfile=\"%s\"", dumpfile));
        CmdResult cmdResult = executeCommand(cmdList);

        if (cmdResult == null) {
            log.error("还原命令执行失败");
            return false;
        }
        // "已经完成, 但是有 xx 个错误" 时code为5, 但不确定其他情况下会不会有code=5
        if (cmdResult.getCode() == 0 || cmdResult.getCode() == 5) {
            return true;
        } else {
            log.error("数据库[{}.{}]还原失败!\t执行还原命令失败!\t{}",
                    backupConfig.getDatabaseName(), backupConfig.getUsername(), cmdResult.getError());
            if (cmdResult.getError().contains("ORA-12545")) {
                if ("/".equals(separator)) {
                    return doRecovery(":");
                }
            }
            return false;
        }
    }

    @Override
    protected void before() {
        super.before();
        Objects.requireNonNull(backupConfig.getDatabaseName());
        dumpfile = FilenameUtils.getName(backupConfig.getDataSavePath());
    }


    /**
     * 获取oracle中设置的dump文件保存目录
     *
     * @return java.lang.String
     * @author Zhu Kaixiao
     * @date 2019/8/5 9:44
     **/
    private String getDumpDir() {
        String dumpDir = null;
        try (Connection connection = getConnection(DRIVER_CLASS_NAME,
                backupConfig.getJdbcUrl(), backupConfig.getUsername(), backupConfig.getPassword())) {
            String sql = "select * from dba_directories";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String directoryName = resultSet.getString("directory_name");
                if ("DATA_PUMP_DIR".equalsIgnoreCase(directoryName)) {
                    dumpDir = resultSet.getString("directory_path");
                    break;
                }
            }
        } catch (Exception e) {
            log.error("查询[DATA_PUMP_DIR]失败: {}", e.getMessage());
            if (e.getMessage().contains("ORA-01017")) {
                throw new BackupException("用户名或密码错误");
            }
        }
        return dumpDir;
    }


    /* 从备份命令执行结果中提取备份文件保存路径
       但是现在改为先从数据库中查询出保存目录, 然后在拼接路径  所以这个方法用不上了
    private static final Pattern WINDOWS_PATH_PATTERN = Pattern.compile("[a-z]:\\\\.+?\\.((jcbak)|(sql))", Pattern.CASE_INSENSITIVE);
    private static final Pattern LINUX_PATH_PATTERN = Pattern.compile("/.+?\\.((jcbak)|(sql))", Pattern.CASE_INSENSITIVE);

    private String fetchBakFilePath(CmdResult cmdResult) {
        Pattern pathPattern = OSUtil.currentPlatform() == OSUtil.PLATFORM_WINDOWS
                ? WINDOWS_PATH_PATTERN
                : LINUX_PATH_PATTERN;
        // 1. 从命令行执行结果中提取文件路径
        if (cmdResult.getCode() == 0) {
            String str = cmdResult.getOut() + cmdResult.getError();
            Matcher matcher = pathPattern.matcher(str);
            int mStart = 0;
            while (matcher.find(mStart)) {
                String match = matcher.group();
                try {
                    File file = new File(match);
                    return file.getCanonicalPath();
                } catch (Exception e) {
                    mStart = matcher.start() + 1;
                }
            }
        }

        // 2. 拷贝文件到指定目录
        return null;
    }
    */
}
