package com.jeecms.backup.exception;

/**
 * @author Zhu Kaixiao
 * @version 1.0
 * @date 2019/8/3 14:42
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class BackupException extends RuntimeException {

    public BackupException() {
    }

    public BackupException(String msg) {
        super(msg);
    }
}
