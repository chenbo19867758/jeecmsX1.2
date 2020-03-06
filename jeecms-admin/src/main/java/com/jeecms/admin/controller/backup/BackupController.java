package com.jeecms.admin.controller.backup;

import com.jeecms.backup.domain.DatabaseBackupRecord;
import com.jeecms.backup.service.DatabaseBackupRecordService;
import com.jeecms.backup.service.DatabaseBackupService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.util.SystemContextUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Collections;

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

    private static Logger log = LoggerFactory.getLogger(BackupController.class);

    /**
     * 执行数据库备份
     *
     * @return com.jeecms.common.response.ResponseInfo
     * @author Zhu Kaixiao
     * @date 2019/8/6 12:00
     **/
    @GetMapping("/backup")
    @ResponseBody
    public ResponseInfo databaseBackup(HttpServletRequest request, @RequestParam(required = false) String remark) throws GlobalException {
        log.info("数据库备份, 操作人:{}", SystemContextUtils.getCurrentUsername());
        Integer siteId = SystemContextUtils.getSite(request).getSiteId();
        ResponseInfo responseInfo = databaseBackupService.backup(siteId, remark);
        return responseInfo;
    }


    /**
     * 下载制定备份id的数据库备份文件
     * @param backupId 备份id
     * @author Zhu Kaixiao
     * @date 2019/8/6 12:01
     **/
    @RequestMapping("/download/{backupId}")
    public void downloadBackupFile(@PathVariable Integer backupId, HttpServletResponse response) throws Exception {
        log.info("数据库备份文件下载, 备份id:{}  操作人:{}", backupId, SystemContextUtils.getCurrentUsername());
        File file = databaseBackupService.download(backupId);
        downloadFile(file, response);
    }

    /**
     * 根据指定的备份id还原数据库
     * @param backupId 备份id
     * @return com.jeecms.common.response.ResponseInfo
     * @author Zhu Kaixiao
     * @date 2019/8/6 12:01
     **/
    @GetMapping("/recovery/{backupId}")
    @ResponseBody
    public ResponseInfo databaseRecovery(@PathVariable Integer backupId, HttpServletRequest request) throws Exception {
        log.info("数据库还原, 备份id:{}  操作人:{}", backupId, SystemContextUtils.getCurrentUsername());
        Integer siteId = SystemContextUtils.getSite(request).getSiteId();
        ResponseInfo recovery = databaseBackupService.recovery(backupId, siteId);
        return recovery;
    }

    @MoreSerializeField({
            @SerializeField(clazz = DatabaseBackupRecord.class, includes = {"id", "state", "remark", "errMsg", "createTime", "filename", "humanFileSize"}),
    })
    @GetMapping("/page")
    @ResponseBody
    public ResponseInfo page(@PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<DatabaseBackupRecord> page = databaseBackupRecordService.getPage(Collections.emptyMap(), pageable, true);
        return new ResponseInfo(page);
    }


    /**
     * 删除指定备份记录
     * 同时也会删除相关备份文件
     *
     * @param backupId 备份id
     * @return com.jeecms.common.response.ResponseInfo
     * @author Zhu Kaixiao
     * @date 2019/8/6 12:03
     **/
    @DeleteMapping("/{backupId}")
    @ResponseBody
    public ResponseInfo delete(@PathVariable Integer backupId) throws Exception {
        log.info("数据库备份记录删除, 备份id:{}  操作人:{}", backupId, SystemContextUtils.getCurrentUsername());
        databaseBackupService.deleteBackup(backupId);
        return new ResponseInfo();
    }


    private static boolean downloadFile(File file, HttpServletResponse response) {
        // 实现文件下载
        try (
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                OutputStream os = response.getOutputStream()
        ) {
            String filename = file.getName();

            // 配置文件下载
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));

            IOUtils.copy(bis, os);
            os.flush();

            log.debug("Provide download successfully! [{}]", file);
            return true;
        } catch (Exception e) {
            log.debug("Provide download failed! [{}]", file);
            return false;
        }
    }

    @Autowired
    private DatabaseBackupService databaseBackupService;
    @Autowired
    private DatabaseBackupRecordService databaseBackupRecordService;
}
