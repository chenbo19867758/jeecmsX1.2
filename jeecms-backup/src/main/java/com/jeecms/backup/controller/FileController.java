package com.jeecms.backup.controller;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * @author Zhu Kaixiao
 * @version 1.0
 * @date 2019/8/2 15:25
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Controller
public class FileController {
	static Logger log = LoggerFactory.getLogger(FileController.class);
    @RequestMapping("/download")
    public void downloadFile(@RequestParam String filepath, HttpServletResponse response) {
        File file = new File(filepath);
        if (!file.exists()) {
            return;
        }

        String filename = FilenameUtils.getName(filepath);

        // 实现文件下载
        try (
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                OutputStream os = response.getOutputStream()
        ) {
            // 配置文件下载
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));

            IOUtils.copy(bis, os);
            os.flush();

            log.debug("Provide download successfully! [{}]", filepath);
        } catch (Exception e) {
            log.debug("Provide download failed! [{}]", filepath);
        }
    }

    @RequestMapping("/deleteFile")
    @ResponseBody
    public void deleteFile(@RequestParam String filepath) {
        log.info("删除文件[{}]", filepath);
        new File(filepath).delete();
    }
}
