package com.jeecms.backup.controller;

import com.jeecms.backup.config.IpWhitelistInterceptor;
import com.jeecms.backup.entity.BackupDto;
import com.jeecms.backup.exception.BackupException;
import com.jeecms.backup.services.BackupServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Zhu Kaixiao
 * @version 1.0
 * @date 2019/8/2 13:59
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@RestController
public class BackupController {
	static Logger log = LoggerFactory.getLogger(BackupController.class);
	@Autowired
    private BackupServices backupServices;


    @PostMapping("/backup")
    public Map<String, Object> databaseBackup(@RequestBody @Valid BackupDto dto) {
        log.info("准备启动备份任务  [{}]", dto.toString());
        backupServices.databaseBackup(dto);
        Map<String, Object> ret = new HashMap<>(3);

        ret.put("status", 200);
        ret.put("message", "备份任务启动成功");
        return ret;
    }

    @PostMapping("/recovery")
    public Map<String, Object> databaseRecovery(@RequestBody @Valid BackupDto dto) throws BackupException {
        log.info("准备启动还原任务  [{}]", dto.toString());
        backupServices.databaseRecovery(dto);
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 200);
        ret.put("message", "还原任务启动成功");
        return ret;
    }
}
