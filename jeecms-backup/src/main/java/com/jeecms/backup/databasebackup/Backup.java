package com.jeecms.backup.databasebackup;

/**
 * @author Zhu Kaixiao
 * @version 1.0
 * @date 2019/7/30 14:38
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface Backup {

    /**
     * 备份
     *
     * @return String 生成的备份文件路径
     * @author Zhu Kaixiao
     * @date 2019/7/30 14:41
     **/
    String backup();

    /**
     * 还原
     *
     * @return boolean 是否还原成功
     * @author Zhu Kaixiao
     * @date 2019/7/30 14:41
     **/
    boolean recovery();
}
