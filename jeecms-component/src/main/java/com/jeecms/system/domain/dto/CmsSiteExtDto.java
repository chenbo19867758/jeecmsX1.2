package com.jeecms.system.domain.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jeecms.system.domain.SiteModelTpl;

/**
 * 站点配置Dto
 * 
 * @author: ljw
 * @date: 2019年4月12日 下午4:37:00
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class CmsSiteExtDto {

        /** 站点ID **/
        private Integer id;
        /** 评论工作流id */
        private Integer commentFlowId;
        /** 新内容图标id */
        private Integer newContentResourceId;
        /** 水印图片id */
        private Integer watermarkResourceId;
        /** 静态页OSS */
        private Integer staticPageOssId;
        /** 静态页FTP */
        private Integer staticPageFtpId;
        /** 上传资源OSS */
        private Integer uploadOssId;
        /** 上传资源FTP */
        private Integer uploadFtpId;
        /** 站点配置键值对 **/
        private Map<String, String> cfg = new HashMap<String, String>(16);
        /** 站点配置模板 **/
        private List<SiteModelTpl> modelTpls;

        CmsSiteExtDto() {
        }

        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public Integer getCommentFlowId() {
                return commentFlowId;
        }

        public void setCommentFlowId(Integer commentFlowId) {
                this.commentFlowId = commentFlowId;
        }

        public Integer getNewContentResourceId() {
                return newContentResourceId;
        }

        public void setNewContentResourceId(Integer newContentResourceId) {
                this.newContentResourceId = newContentResourceId;
        }

        public Integer getWatermarkResourceId() {
                return watermarkResourceId;
        }

        public void setWatermarkResourceId(Integer watermarkResourceId) {
                this.watermarkResourceId = watermarkResourceId;
        }

        public Integer getStaticPageOssId() {
                return staticPageOssId;
        }

        public void setStaticPageOssId(Integer staticPageOssId) {
                this.staticPageOssId = staticPageOssId;
        }

        public Integer getStaticPageFtpId() {
                return staticPageFtpId;
        }

        public void setStaticPageFtpId(Integer staticPageFtpId) {
                this.staticPageFtpId = staticPageFtpId;
        }

        public Integer getUploadOssId() {
                return uploadOssId;
        }

        public void setUploadOssId(Integer uploadOssId) {
                this.uploadOssId = uploadOssId;
        }

        public Integer getUploadFtpId() {
                return uploadFtpId;
        }

        public void setUploadFtpId(Integer uploadFtpId) {
                this.uploadFtpId = uploadFtpId;
        }

        public Map<String, String> getCfg() {
                return cfg;
        }

        public void setCfg(Map<String, String> cfg) {
                this.cfg = cfg;
        }

        public List<SiteModelTpl> getModelTpls() {
                return modelTpls;
        }

        public void setModelTpls(List<SiteModelTpl> modelTpls) {
                this.modelTpls = modelTpls;
        }
}
