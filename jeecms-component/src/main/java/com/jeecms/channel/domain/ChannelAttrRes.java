/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.channel.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.jeecms.common.base.domain.AbstractIdDomain;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.system.domain.SysSecret;

/**
 * 栏目内容属性-多资源表
 * 
 * @author: chenming
 * @date: 2019年6月28日 下午3:29:13
 */
@Entity
@Table(name = "jc_channel_attr_res")
public class ChannelAttrRes extends AbstractIdDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	/** 栏目自定义属性ID*/
	private Integer channelAttrId;
	/** 资源id*/
	private Integer resId;
	/** 附件密级 */
	private Integer secretId;
	/** 描述 */
	private String description;

	/** 栏目自定义属性对象*/
	private ChannelAttr channelAttr;
	/** 资源对象*/
	private ResourcesSpaceData resourcesSpaceData;
	/** 附件密级*/
	private SysSecret secret;
	
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
        
        
	public ChannelAttrRes() {
	}

	public ChannelAttrRes(Integer resId, Integer secretId, String description) {
		super();
		this.resId = resId;
		this.secretId = secretId;
		this.description = description;
	}

	@Id
    @Column(name = "id", nullable = false, length = 11)
    @TableGenerator(name = "jc_channel_attr_res", pkColumnValue = "jc_channel_attr_res", initialValue = 0, allocationSize = 10)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_channel_attr_res")
    @Override
    public Integer getId () {
        return this.id;
    }

    public void setId (Integer id) {
        this.id = id;
    }

    @Column(name = "channel_attr_id", nullable = false, length = 11)
	public Integer getChannelAttrId() {
		return this.channelAttrId;
	}

	public void setChannelAttrId(Integer channelAttrId) {
		this.channelAttrId = channelAttrId;
	}

	@Column(name = "res_id", nullable = false, length = 11)
	@NotNull
	public Integer getResId() {
		return this.resId;
	}

	public void setResId(Integer resId) {
		this.resId = resId;
	}

	@Column(name = "secret_id", nullable = true, length = 11)
	public Integer getSecretId() {
		return secretId;
	}

	public void setSecretId(Integer secretId) {
		this.secretId = secretId;
	}

	@Column(name = "description", nullable = true, length = 500)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channel_attr_id",referencedColumnName = "channel_attr_id",insertable = false,updatable = false)
	public ChannelAttr getChannelAttr() {
		return channelAttr;
	}

	public void setChannelAttr(ChannelAttr channelAttr) {
		this.channelAttr = channelAttr;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "res_id",referencedColumnName = "id",insertable = false,updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	public ResourcesSpaceData getResourcesSpaceData() {
		return resourcesSpaceData;
	}

	public void setResourcesSpaceData(ResourcesSpaceData resourcesSpaceData) {
		this.resourcesSpaceData = resourcesSpaceData;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "secret_id",referencedColumnName = "secret_id",insertable = false,updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	public SysSecret getSecret() {
		return secret;
	}

	public void setSecret(SysSecret secret) {
		this.secret = secret;
	}

	
}