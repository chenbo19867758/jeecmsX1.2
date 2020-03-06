package com.jeecms.content.thread;

import com.jeecms.common.constants.UploadEnum.UploadServerType;
import com.jeecms.content.constants.ContentConstant;
import com.jeecms.system.domain.CmsSite;

public class RemoteFileDeleteThread implements Runnable {
        private String fileName;
        private CmsSite site;
        private Short distribteType;

        /**
         * 删除远端文件
         * 
         * @param site
         *                当前访问站点
         * @param distribteType
         *                类型 （1上传资源 2静态页面）
         * @param fileName
         *                文件路径
         */
        public RemoteFileDeleteThread(CmsSite site, Short distribteType, String fileName) {
                this.site = site;
                this.distribteType = distribteType;
                this.fileName = fileName;
        }

        @Override
        public void run() {
                if (ContentConstant.DISTRIBUTE_TYPE_UPLOAD.equals(distribteType)) {
                        if (UploadServerType.ftp.equals(site.getUploadServerType()) && site.getUploadFtp() != null) {
                                site.getUploadFtp().deleteFile(fileName);
                        } else if (UploadServerType.oss.equals(site.getUploadServerType())
                                        && site.getUploadOss() != null) {
                                site.getUploadOss().deleteFile(fileName);
                        }
                } else if (ContentConstant.DISTRIBUTE_TYPE_HTML.equals(distribteType)) {
                        if (UploadServerType.ftp.equals(site.getStaticServerType())
                                        && site.getStaticPageFtp() != null) {
                                site.getStaticPageFtp().deleteFile(fileName);
                        } else if (UploadServerType.oss.equals(site.getStaticServerType())
                                        && site.getStaticPageOss() != null) {
                                site.getStaticPageOss().deleteFile(fileName);
                        }
                }
        }

}
