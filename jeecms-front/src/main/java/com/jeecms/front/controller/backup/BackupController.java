package com.jeecms.front.controller.backup;

import com.jeecms.backup.domain.dto.CompleteDto;
import com.jeecms.backup.service.DatabaseBackupService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.NotFundExceptionInfo;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.util.JdbcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;
import java.util.Objects;

/**
 * @author Zhu Kaixiao
 * @version 1.0
 * @date 2019/8/2 16:23
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Controller
@RequestMapping("/databaseBackup")
public class BackupController {
    private Logger log = LoggerFactory.getLogger(BackupController.class);

    /**
     * 备份完成回调接口
     * <p>
     * db备份程序在备份成功或备份失败时将会调用该接口
     *
     * @param dto 备份完成信息
     * @return com.jeecms.common.response.ResponseInfo
     * @author Zhu Kaixiao
     * @date 2019/8/6 12:02
     **/
    @PostMapping("/backupComplete")
    @ResponseBody
    public ResponseInfo onBackupComplete(@RequestBody @Valid CompleteDto dto) throws Exception {
        validIp();
        databaseBackupService.onBackupComplete(dto);
        return new ResponseInfo();
    }


    /**
     * 还原完成回调接口
     * <p>
     * db备份程序在还原成功或还原失败时将会调用该接口
     *
     * @param dto 还原完成信息
     * @return com.jeecms.common.response.ResponseInfo
     * @author Zhu Kaixiao
     * @date 2019/8/6 12:02
     **/
    @PostMapping("/recoveryComplete")
    @ResponseBody
    public ResponseInfo onRecoveryComplete(@RequestBody @Valid CompleteDto dto) throws GlobalException {
        validIp();
        databaseBackupService.onRecoveryComplete(dto);
        return new ResponseInfo();
    }

    /**
     * 验证ip
     *
     * @author Zhu Kaixiao
     * @date 2019/8/7 17:57
     **/
    private void validIp() throws GlobalException {
        String remoteAddr = RequestUtils.getRemoteAddr(request);
        Map<String, Object> dsMap = JdbcUtil.resolveJdbcUrl(dataSourceProperties.getUrl());
        boolean equals = Objects.equals(dsMap.get("host"), remoteAddr);
        if (!equals) {
            log.info("拦截非法访问ip:{}", request);
            throw new GlobalException(new NotFundExceptionInfo());
        }
    }


    @Autowired
    private HttpServletRequest request;
    @Autowired
    private DataSourceProperties dataSourceProperties;
    @Autowired
    private DatabaseBackupService databaseBackupService;
}
