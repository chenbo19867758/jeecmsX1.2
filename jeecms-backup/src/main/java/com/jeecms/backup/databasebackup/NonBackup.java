package com.jeecms.backup.databasebackup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 啥都不干的
 *
 * @author Zhu Kaixiao
 * @version 1.0
 * @date 2019/7/30 14:53
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class NonBackup extends AbstractDatabaseBackup {
    private static Logger log = LoggerFactory.getLogger(LocalSqlServerBackup.class);

    @Override
    public String doBackup() {
        // do nothing
        return null;
    }

    @Override
    public boolean doRecovery() {
        // do nothing
        return false;
    }

    @Override
    protected void before() {
        log.error("不支持的数据库版本");
        throw new IllegalArgumentException("不支持的数据库版本");
    }
}
