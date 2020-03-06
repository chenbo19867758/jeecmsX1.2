package com.jeecms.system.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.jeecms.common.base.domain.AbstractDomain;

/**
 * @author chenming
 * @version 1.0
 * @date 2019-01-24
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Entity
@Table(name = "jc_sys_message_push")
public class SystemMessagePush extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	/** 是否开启推送服务.true表示开启,false表示关闭 */
	private Boolean isEnable;
	/** AppKey */
	private String appKey;
	/** Master Secret用于服务器端 API 调用时与 AppKey 配合使用达到鉴权的目的 */
	private String masterSecret;

	public SystemMessagePush() {

	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_sys_push", pkColumnValue = "jc_sys_push", initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_push")
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "is_enable", nullable = false, length = 1)
	public Boolean getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Boolean isEnable) {
		this.isEnable = isEnable;
	}

	@Column(name = "app_key", nullable = true, length = 150)
	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	@Column(name = "master_secret", nullable = true, length = 150)
	public String getMasterSecret() {
		return masterSecret;
	}

	public void setMasterSecret(String masterSecret) {
		this.masterSecret = masterSecret;
	}

}