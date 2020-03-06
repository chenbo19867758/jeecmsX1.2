package com.jeecms.resource.domain;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.UploadExceptionInfo;
import com.jeecms.common.exception.error.SysOtherErrorCodeEnum;
import com.jeecms.common.upload.AliOssCloudClient;
import com.jeecms.common.upload.QcoscloudClient;
import com.jeecms.common.upload.QiniuOssCloudClient;
import com.jeecms.common.upload.UploadUtils;

/**
 * 云存储domain
 * 
 * @author: tom
 * @date: 2019年3月5日 下午4:48:37
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Entity
@Table(name = "jc_sys_oss")
public class UploadOss extends AbstractDomain<Integer> implements Serializable {
        private static final Logger log = LoggerFactory.getLogger(UploadOss.class);

        public static final Short OSS_TYPE_Q_CLOUD = 1;

        public static final Short OSS_TYPE_QINIU_CLOUD = 3;

        public static final Short OSS_TYPE_ALI_CLOUD = 2;

        public static final int OSS_UPLOAD_SUCCESS = 1;

        public static final int OSS_UPLOAD_FAIL = 0;

        public enum OSSTYPE {
                /**
                 * 腾讯云cos
                 */
                QCLOUD,
                /**
                 * 七牛云oss
                 */
                QINIUCLOUD,
                /**
                 * 阿里云oss
                 */
                ALICLOUD,
                /**
                 * 百度云
                 */
                BAIDUCLOUD
        }

        ;

        /**
         * 上传文件
         * 
         * @Title: storeByExt
         * @param path
         *                上传路径
         * @param ext
         *                格式
         * @param in
         *                输入流
         * @throws IOException
         *                 IOException
         * @throws GlobalException
         *                 GlobalException
         * @return String
         */
        @Transient
        public String storeByExt(String path, String ext, InputStream in) throws IOException, GlobalException {
                String remoteFileName = UploadUtils.generateFilename(path, ext);
                String fileOssUrl = store(remoteFileName, in);
                return fileOssUrl;
        }

        @Transient
        public String storeByFilename(String filename, InputStream in) throws IOException, GlobalException {
                String fileOssUrl = store(filename, in);
                return fileOssUrl;
        }

        @Transient
        private String store(String remote, InputStream in) throws GlobalException {
                String result = "";
                String fileUrl = "";
                if (OSS_TYPE_Q_CLOUD.equals(getOssType())) {
                        /**
                         * 腾讯云
                         */
                        result = QcoscloudClient.uploadFileByInputStream(getBucketArea(), getBucketName(), getAppId(),
                                        getSecretId(), getAppKey(), remote, in);
                        String startKey = "{";
                        if (StringUtils.isNotBlank(result) && result.startsWith(startKey)) {
                                JSONObject json =new JSONObject();
                                try {
                                        json = new JSONObject(result);
                                        Object code = json.get("code");
                                        if (code != null && code.equals(0)) {
                                                fileUrl = json.getJSONObject("data").getString("source_url");
                                        }
                                } catch (JSONException e) {
                                        log.error("upload error->" + e.getMessage());
                                        log.error("upload json->" + json);
                                        throw new GlobalException(new UploadExceptionInfo(
                                                        SysOtherErrorCodeEnum.UPLOAD_OSS_ERROR.getDefaultMessage(),
                                                        SysOtherErrorCodeEnum.UPLOAD_OSS_ERROR.getCode()));
                                }
                        }
                } else if (OSS_TYPE_ALI_CLOUD.equals(getOssType())) {
                        /**
                         * 阿里云
                         */
                        boolean succ = AliOssCloudClient.uploadFileByInputStream(getBucketName(), getEndPoint(),
                                        getAppKey(), getSecretId(), remote, in);
                        if (succ) {
                                fileUrl =  this.getAccessDomain() + remote;
                        } else {
                                log.error("upload error");
                                throw new GlobalException(new UploadExceptionInfo(
                                                SysOtherErrorCodeEnum.UPLOAD_OSS_ERROR.getDefaultMessage(),
                                                SysOtherErrorCodeEnum.UPLOAD_OSS_ERROR.getCode()));
                        }
                } else if (OSS_TYPE_QINIU_CLOUD.equals(getOssType())) {
                        /**
                         * 七牛云
                         */
                        boolean succ = QiniuOssCloudClient.uploadFileByInputStream(
                                        getBucketArea(),getBucketName(),
                                        getAppKey(), getSecretId(), remote, in);
                        if (succ) {
                                // 七牛云存存储accessDomain得设置带http://
                                fileUrl = this.getAccessDomain() + remote;
                        } else {
                                throw new GlobalException(new UploadExceptionInfo(
                                                SysOtherErrorCodeEnum.UPLOAD_OSS_ERROR.getDefaultMessage(),
                                                SysOtherErrorCodeEnum.UPLOAD_OSS_ERROR.getCode()));
                        }
                }
                return fileUrl;
        }

        /**
         * 删除文件
         * @Title: deleteFile
         * @param remote 远程文件路径
         * @return: boolean
         */
        @Transient
        public boolean deleteFile(String remote) {
                String result = "";
                if (OSS_TYPE_Q_CLOUD.equals(getOssType())) {
                        result = QcoscloudClient.deleteFile(getBucketArea(), getBucketName(), getAppId(), getSecretId(),
                                        getAppKey(), remote);
                        String startKey = "{";
                        if (StringUtils.isNotBlank(result) && result.startsWith(startKey)) {
                                JSONObject json;
                                json = new JSONObject(result);
                                Object code = json.get("code");
                                if (code != null && code.equals(0)) {
                                        return true;
                                }
                                return false;
                        }
                } else if (OSS_TYPE_ALI_CLOUD.equals(getOssType())) {
                        return AliOssCloudClient.deleteFile(getBucketName(), getEndPoint(), getAppKey(),
                                        getSecretId(), remote);
                } else if (OSS_TYPE_QINIU_CLOUD.equals(getOssType())) {
                        return  QiniuOssCloudClient.deleteFile(getBucketName(),
                                        getAppKey(), getSecretId(), remote);
                }
                return false;
        }

        private static final long serialVersionUID = 1L;
        private Integer id;
        private String accessDomain;
        private String appId;
        private String appKey;
        private String bucketArea;
        private String bucketName;
        private String endPoint;
        private String ossName;
        private Short ossType;
        private String secretId;

        public UploadOss() {
        }

        @Id
        @Column(name = "id", unique = true, nullable = false)
        @TableGenerator(name = "tg_gou_oss", pkColumnValue = "gou_oss", initialValue = 0, allocationSize = 10)
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_gou_oss")
        @Override
        public Integer getId() {
                return this.id;
        }

        @Override
        public void setId(Integer id) {
                this.id = id;
        }

        @Column(name = "access_domain", nullable = false, length = 255)
        public String getAccessDomain() {
                return this.accessDomain;
        }

        public void setAccessDomain(String accessDomain) {
                this.accessDomain = accessDomain;
        }

        @Column(name = "app_id", length = 255)
        public String getAppId() {
                return this.appId;
        }

        public void setAppId(String appId) {
                this.appId = appId;
        }

        @Column(name = "app_key", nullable = false, length = 255)
        public String getAppKey() {
                return this.appKey;
        }

        public void setAppKey(String appKey) {
                this.appKey = appKey;
        }

        @Column(name = "bucket_area", length = 255)
        public String getBucketArea() {
                return this.bucketArea;
        }

        public void setBucketArea(String bucketArea) {
                this.bucketArea = bucketArea;
        }

        @Column(name = "bucket_name", length = 255)
        public String getBucketName() {
                return this.bucketName;
        }

        public void setBucketName(String bucketName) {
                this.bucketName = bucketName;
        }

        @Column(name = "end_point", length = 255)
        public String getEndPoint() {
                return this.endPoint;
        }

        public void setEndPoint(String endPoint) {
                this.endPoint = endPoint;
        }

        @Column(name = "oss_name", nullable = false, length = 255)
        public String getOssName() {
                return this.ossName;
        }

        public void setOssName(String ossName) {
                this.ossName = ossName;
        }

        @Column(name = "oss_type", nullable = false)
        public Short getOssType() {
                return this.ossType;
        }

        public void setOssType(Short ossType) {
                this.ossType = ossType;
        }

        @Column(name = "secret_id", nullable = false, length = 255)
        public String getSecretId() {
                return this.secretId;
        }

        public void setSecretId(String secretId) {
                this.secretId = secretId;
        }

}