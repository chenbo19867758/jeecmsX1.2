package com.jeecms.content.thread;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeecms.common.constants.UploadEnum.UploadServerType;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.constants.ContentConstant;
import com.jeecms.system.domain.CmsSite;

/**
 * 线程上传文件或者静态化html页面
 * 
 * @author: tom
 * @date: 2019年6月10日 下午3:43:46
 */
public class DistributionFileThread implements Runnable {

        private static final Logger log = LoggerFactory.getLogger(DistributionFileThread.class);
        private String path;
        private InputStream in;
        private CmsSite site;
        private Short distribteType;

        /**
         * 发布文件
         * 
         * @param site
         *                当前站点
         * @param path
         *                文件路径
         * @param distribteType
         *                类型 （1资源上传 2静态页面上传）
         * @param in
         *                输入流
         */
        public DistributionFileThread(CmsSite site, Short distribteType, String path, InputStream in) {
                this.site = site;
                this.distribteType = distribteType;
                this.path = path;
                this.in = in;
        }

        @Override
        public void run() {
                if (ContentConstant.DISTRIBUTE_TYPE_UPLOAD.equals(distribteType)) {
                        if (UploadServerType.ftp.equals(site.getUploadServerType()) && site.getUploadFtp() != null) {
                                try {
                                        site.getUploadFtp().storeByExt(path, in);
                                } catch (IOException e) {
                                        log.error(e.getMessage());
                                }
                        } else if (UploadServerType.oss.equals(site.getUploadServerType())
                                        && site.getUploadOss() != null) {
                                try {
                                        site.getUploadOss().storeByFilename(path, in);
                                } catch (IOException e) {
                                        log.error(e.getMessage());
                                } catch (GlobalException e) {
                                        log.error(e.getMessage());
                                }
                        }
                } else if (ContentConstant.DISTRIBUTE_TYPE_HTML.equals(distribteType)) {
                        if (UploadServerType.ftp.equals(site.getStaticServerType())
                                        && site.getStaticPageFtp() != null) {
                                try {
                                        site.getStaticPageFtp().storeByExt(path, in);
                                } catch (IOException e) {
                                        log.error(e.getMessage());
                                }
                        } else if (UploadServerType.oss.equals(site.getStaticServerType())
                                        && site.getStaticPageOss() != null) {
                                try {
                                        site.getStaticPageOss().storeByFilename(path, in);
                                } catch (IOException e) {
                                        e.printStackTrace();
                                        log.error(e.getMessage());
                                } catch (GlobalException e) {
                                        e.printStackTrace();
                                        log.error(e.getMessage());
                                } finally {
                                        if (in != null) {
                                                try {
                                                        in.close();
                                                } catch (IOException e) {
                                                        log.error(e.getMessage());
                                                }
                                        }   
                                }
                        }
                }
        }

}
