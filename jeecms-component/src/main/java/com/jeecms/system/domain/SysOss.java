/*
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.system.domain;

import com.jeecms.common.base.domain.AbstractDomain;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * oss云存储配置
 * @author: wulongwei
 * @date: 2019年4月9日 下午1:39:30
 */
@Entity
@Table(name = "jc_sys_oss")
public class SysOss extends AbstractDomain<Integer>{

	
	private static final long serialVersionUID = 1L;
	/** 主键值 */
	private Integer id;
	/** APPID */
	private String appId;
	/** secret_id */
	private String secretId;
	/** key或者secret */
	private String appKey;
	/** bucket名 */
	private String bucketName;
	/** 地区码 */
	private String bucketArea;
	/** 存储类型(1腾讯云cos 2阿里云oss 3七牛云) */
	private Short ossType;
	/** end_point */
	private String endPoint;
	/** 访问域名 */
	private String accessDomain;
	/** 名称 */
	private String ossName;
    /**腾讯云*/
	public static final short TENCENT_CLOUD = 1;
	/**阿里云*/
	public static final short ALI_CLOUD = 2;
	/**七牛云*/
	public static final short SEVEN_CATTLE_CLOUD = 3;
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@TableGenerator(name = "jc_sys_oss", pkColumnValue = "jc_sys_oss", initialValue = 0, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_oss")
	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "app_id")
	@Length(max = 255)
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@Column(name = "secret_id")
	@NotNull
	@Length(min = 1, max = 255)
	public String getSecretId() {
		return secretId;
	}

	public void setSecretId(String secretId) {
		this.secretId = secretId;
	}

	@Column(name = "app_key")
	@NotNull
	@Length(min = 1, max = 255)
	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	@Column(name = "bucket_name")
	@Length(min = 1, max = 255)
	@NotNull
	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	@Column(name = "bucket_area")
	@Length(max = 255)
	public String getBucketArea() {
		return bucketArea;
	}

	public void setBucketArea(String bucketArea) {
		this.bucketArea = bucketArea;
	}

	@Column(name = "oss_type", length = 6)
	@NotNull
	public Short getOssType() {
		return ossType;
	}

	public void setOssType(Short ossType) {
		this.ossType = ossType;
	}

	@Column(name = "end_point")
	@Length(max = 255)
	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	@Column(name = "access_domain")
	@Length(max = 255)
	public String getAccessDomain() {
		return accessDomain;
	}

	public void setAccessDomain(String accessDomain) {
		this.accessDomain = accessDomain;
	}

	@Column(name = "oss_name")
	@NotNull
	@Length(min = 1, max = 255)
	public String getOssName() {
		return ossName;
	}

	public void setOssName(String ossName) {
		this.ossName = ossName;
	}

	public SysOss(Integer id, String appId, String secretId, String appKey, String bucketName, String bucketArea,
			Short ossType, String endPoint, String accessDomain, String ossName) {
		super();
		this.id = id;
		this.appId = appId;
		this.secretId = secretId;
		this.appKey = appKey;
		this.bucketName = bucketName;
		this.bucketArea = bucketArea;
		this.ossType = ossType;
		this.endPoint = endPoint;
		this.accessDomain = accessDomain;
		this.ossName = ossName;
	}

	/**
	 *  存储类型(1腾讯云cos 2阿里云oss 3七牛云)
	 * @return String
	 */
	@Transient
	public String getOssTypeString() {
		if (getOssType() != null) {
			if (TENCENT_CLOUD == getOssType()) {
				return "腾讯云cos";
			} else if (ALI_CLOUD == getOssType()) {
				return "阿里云oss";
			} else if(SEVEN_CATTLE_CLOUD == getOssType()) {
				return "七牛云";
			}
		}
		return null;
	}

	public SysOss() {
		super();
	}

}
