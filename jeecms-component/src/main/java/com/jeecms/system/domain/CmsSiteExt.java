package com.jeecms.system.domain;

import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.common.resource.annotations.ResourceField;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.resource.domain.UploadFtp;
import com.jeecms.resource.domain.UploadOss;
import com.jeecms.system.domain.dto.CmsSiteExtDto;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * 站点扩展表
 * 
 * @author ljw
 * @version 1.0
 * @date 2019-04-11
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Entity
@Table(name = "jc_site_ext")
public class /**/CmsSiteExt extends AbstractDomain<Integer> implements Serializable {
        private static final long serialVersionUID = 1L;
        private Integer id;
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
        /** 站点对象 **/
        private CmsSite cmsSite;

        private UploadOss uploadOss;
        private UploadFtp uploadFtp;
        private UploadOss staticPageOss;
        private UploadFtp staticPageFtp;
        private ResourcesSpaceData watermarkResource;

        @ResourceField(targetIdField = "watermarkResourceId")
        private String watermarkRes;
        @ResourceField(targetIdField = "newContentResourceId")
        private String newContentRes;

        @Transient
        public String getWatermarkRes() {
                return watermarkRes;
        }

        @Transient
        public String getNewContentRes() {
                return newContentRes;
        }

        public CmsSiteExt() {
        }

        /**
         * 构造函数
         */
        public CmsSiteExt(CmsSiteExtDto dto, CmsSite cmsSite) {
                super();
                this.newContentResourceId = dto.getNewContentResourceId();
                this.watermarkResourceId = dto.getWatermarkResourceId();
                this.staticPageOssId = dto.getStaticPageOssId();
                this.staticPageFtpId = dto.getStaticPageFtpId();
                this.uploadOssId = dto.getUploadOssId();
                this.uploadFtpId = dto.getUploadFtpId();
                this.cmsSite = cmsSite;
        }

        /**
         * 构造函数
         */
        public void transfer(CmsSiteExtDto dto, CmsSiteExt ext) {
                ext.newContentResourceId = dto.getNewContentResourceId();
                ext.watermarkResourceId = dto.getWatermarkResourceId();
                ext.staticPageOssId = dto.getStaticPageOssId();
                ext.staticPageFtpId = dto.getStaticPageFtpId();
                ext.uploadOssId = dto.getUploadOssId();
                ext.uploadFtpId = dto.getUploadFtpId();
        }

        @Id
        @Override
        public Integer getId() {
                return this.id;
        }

        @Override
        public void setId(Integer id) {
                this.id = id;
        }

        @Column(name = "new_content_resource_id", nullable = true, length = 11)
        public Integer getNewContentResourceId() {
                return newContentResourceId;
        }

        public void setNewContentResourceId(Integer newContentResourceId) {
                this.newContentResourceId = newContentResourceId;
        }

        @Column(name = "watermar_resource_id", nullable = true, length = 11)
        public Integer getWatermarkResourceId() {
                return watermarkResourceId;
        }

        public void setWatermarkResourceId(Integer watermarkResourceId) {
                this.watermarkResourceId = watermarkResourceId;
        }

        @Column(name = "static_page_oss_id", nullable = true, length = 11)
        public Integer getStaticPageOssId() {
                return staticPageOssId;
        }

        public void setStaticPageOssId(Integer staticPageOssId) {
                this.staticPageOssId = staticPageOssId;
        }

        @Column(name = "static_page_ftp_id", nullable = true, length = 11)
        public Integer getStaticPageFtpId() {
                return staticPageFtpId;
        }

        public void setStaticPageFtpId(Integer staticPageFtpId) {
                this.staticPageFtpId = staticPageFtpId;
        }

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "watermar_resource_id", insertable = false, updatable = false)
        @NotFound(action = NotFoundAction.IGNORE)
        public ResourcesSpaceData getWatermarkResource() {
                return watermarkResource;
        }

        public void setWatermarkResource(ResourcesSpaceData watermarkResource) {
                this.watermarkResource = watermarkResource;
        }

        @Column(name = "upload_oss_id", nullable = true, length = 11)
        public Integer getUploadOssId() {
                return uploadOssId;
        }

        public void setUploadOssId(Integer uploadOssId) {
                this.uploadOssId = uploadOssId;
        }

        @Column(name = "upload_ftp_id", nullable = true, length = 11)
        public Integer getUploadFtpId() {
                return uploadFtpId;
        }

        public void setUploadFtpId(Integer uploadFtpId) {
                this.uploadFtpId = uploadFtpId;
        }

        @MapsId
        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "id", nullable = false)
        public CmsSite getCmsSite() {
                return cmsSite;
        }

        public void setCmsSite(CmsSite cmsSite) {
                this.cmsSite = cmsSite;
        }

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "upload_oss_id", insertable = false, updatable = false)
        public UploadOss getUploadOss() {
                return uploadOss;
        }

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "upload_ftp_id", insertable = false, updatable = false)
        public UploadFtp getUploadFtp() {
                return uploadFtp;
        }

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "static_page_oss_id", insertable = false, updatable = false)
        public UploadOss getStaticPageOss() {
                return staticPageOss;
        }

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "static_page_ftp_id", insertable = false, updatable = false)
        public UploadFtp getStaticPageFtp() {
                return staticPageFtp;
        }

        public void setUploadOss(UploadOss uploadOss) {
                this.uploadOss = uploadOss;
        }

        public void setUploadFtp(UploadFtp uploadFtp) {
                this.uploadFtp = uploadFtp;
        }

        public void setStaticPageOss(UploadOss staticPageOss) {
                this.staticPageOss = staticPageOss;
        }

        public void setStaticPageFtp(UploadFtp staticPageFtp) {
                this.staticPageFtp = staticPageFtp;
        }

}