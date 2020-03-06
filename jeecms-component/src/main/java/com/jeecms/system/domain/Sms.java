package com.jeecms.system.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.jeecms.common.base.domain.AbstractDomain;

/**
 * 短信配置实现类
 * @author: chenming
 * @date:   2019年4月12日 下午6:24:14
 */
@Entity
@Table(name = "jc_sys_sms")
@NamedQuery(name = "Sms.findAll", query = "SELECT s FROM Sms s")
public class Sms extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 主键值 */
	private Integer id;
	/** 是否全局配置 true开启，false关闭 */
	private Boolean isGloable;
	/** AccessKey值 */
	private String accessKey;
	/** AccessKeySecret值 */
	private String accesskeySecret;
	/** 是否开启短信服务 true开启，false关闭 */
	private Boolean isEnable;
	/** 服务商 1 阿里云 2 腾讯云 3 百度云 */
	private Short serviceProvider;
	/** 短信签名 */
	private String smsSign;

	public Sms() {

	}

	/**
	 * 发送短信
	 */
	public Sms(String accessKey, String accesskeySecret, Short serviceProvider, String smsSign) {
		super();
		this.accessKey = accessKey;
		this.accesskeySecret = accesskeySecret;
		this.serviceProvider = serviceProvider;
		this.smsSign = smsSign;
	}



	@TableGenerator(name = "jc_sys_sms", pkColumnValue = "jc_sys_sms", initialValue = 0, allocationSize = 10)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_sms")
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "is_gloable", nullable = false)
	public Boolean getIsGloable() {
		return isGloable;
	}

	public void setIsGloable(Boolean isGloable) {
		this.isGloable = isGloable;
	}

	@Column(name = "access_key", nullable = true, length = 255)
	@Length(max = 255)
	public String getAccessKey() {
		return this.accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	@Column(name = "accesskey_secret", nullable = true, length = 1500)
	@Length(max = 1500)
	public String getAccesskeySecret() {
		return this.accesskeySecret;
	}

	public void setAccesskeySecret(String accesskeySecret) {
		this.accesskeySecret = accesskeySecret;
	}

	@Column(name = "is_enable", nullable = false)
	@NotNull()
	public Boolean getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Boolean isEnable) {
		this.isEnable = isEnable;
	}

	@Column(name = "service_provider", nullable = true)
	public Short getServiceProvider() {
		return this.serviceProvider;
	}

	public void setServiceProvider(Short serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	@Column(name = "sms_sign", nullable = true, length = 1500)
	@Length(max = 1500)
	public String getSmsSign() {
		return this.smsSign;
	}

	public void setSmsSign(String smsSign) {
		this.smsSign = smsSign;
	}

}