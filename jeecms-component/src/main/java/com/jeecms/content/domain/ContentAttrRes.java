/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.domain;

import com.jeecms.common.base.domain.AbstractIdDomain;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.system.domain.SysSecret;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 内容自定义属性-多资源表
 *
 * @author ljw
 * @version 1.0
 * @date 2019-05-15
 */

@Entity
@Table(name = "jc_content_attr_res")
public class ContentAttrRes extends AbstractIdDomain<Integer> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /** 内容自定义属性ID */
    private Integer contentAttrId;
    /** 资源Id */
    private Integer resId;
    /** 附件密级 */
    private Integer secretId;
    /** 描述 */
    private String description;
    /** 内容自定义属性对象*/
    private ContentAttr contentAttr;
    /** 资源对象*/
    private ResourcesSpaceData resourcesSpaceData;
    /** 附件密级*/
    private SysSecret secret;

    @Id
    @Column(name = "id", nullable = false, length = 11)
    @TableGenerator(name = "jc_content_attr_res", pkColumnValue = "jc_content_attr_res", initialValue = 0, allocationSize = 10)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_content_attr_res")
    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取资源地址
     *
     * @Title: getResUrl
     * @return: String
     */
    @Transient
    public String getResUrl() {
        if (getResourcesSpaceData() != null) {
            return getResourcesSpaceData().getUrl();
        }
        return null;
    }

    /**
     * 获取资源名称
     *
     * @Title: getResAlias
     * @return: String
     */
    @Transient
    public String getResAlias() {
        if (getResourcesSpaceData() != null) {
            return getResourcesSpaceData().getAlias();
        }
        return null;
    }

    /**
     * 获取资源时长(时间秒)
     *
     * @Title: getResDuration
     * @return: String
     */
    @Transient
    public String getResDuration() {
        if (getResourcesSpaceData() != null) {
            return getResourcesSpaceData().getDuration();
        }
        return null;
    }

    /**
     * 获取资源对象类型（1图片 2视频 3音频 4附件)
     *
     * @return 资源对象类型
     */
    @Transient
    public Short getResType() {
        return getResourcesSpaceData() != null ? getResourcesSpaceData().getResourceType() : null;
    }

    /**
     * 获取资源对象大小
     *
     * @return 资源对象大小
     */
    @Transient
    public String getResSize() {
        return getResourcesSpaceData() != null ? getResourcesSpaceData().getSizeUnit() : "1KB";
    }

    /**
     * 获取资源对象大小
     *
     * @return 资源对象大小
     */
    @Transient
    public Integer getFileSize() {
        return getResourcesSpaceData() != null ? getResourcesSpaceData().getSize() : 1;
    }

    /**
     * 获取密级名称
     *
     * @Title: getSecretName
     * @return: String
     */
    @Transient
    public String getSecretName() {
        if (getSecret() != null) {
            return getSecret().getName();
        }
        return null;
    }

    public ContentAttrRes() {

    }

    public ContentAttrRes(Integer resId, Integer secretId, String description) {
        super();
        this.resId = resId;
        this.secretId = secretId;
        this.description = description;
    }

    @Column(name = "content_attr_id", nullable = false, length = 11)
    public Integer getContentAttrId() {
        return this.contentAttrId;
    }

    public void setContentAttrId(Integer contentAttrId) {
        this.contentAttrId = contentAttrId;
    }

    @Column(name = "res_id", nullable = false, length = 11)
    public Integer getResId() {
        return this.resId;
    }

    public void setResId(Integer resId) {
        this.resId = resId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_attr_id", referencedColumnName = "content_attr_id", insertable = false, updatable = false)
    public ContentAttr getContentAttr() {
        return contentAttr;
    }

    public void setContentAttr(ContentAttr contentAttr) {
        this.contentAttr = contentAttr;
    }

    @Column(name = "secret_id", nullable = false, length = 11)
    public Integer getSecretId() {
        return secretId;
    }

    public void setSecretId(Integer secretId) {
        this.secretId = secretId;
    }

    @Column(name = "description", nullable = false, length = 500)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "res_id", referencedColumnName = "id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public ResourcesSpaceData getResourcesSpaceData() {
        return resourcesSpaceData;
    }

    public void setResourcesSpaceData(ResourcesSpaceData resourcesSpaceData) {
        this.resourcesSpaceData = resourcesSpaceData;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "secret_id", referencedColumnName = "secret_id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public SysSecret getSecret() {
        return secret;
    }

    public void setSecret(SysSecret secret) {
        this.secret = secret;
    }
}