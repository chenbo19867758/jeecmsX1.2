/*
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.weibo.domain;

import java.io.Serializable;
import com.jeecms.common.base.domain.AbstractDomain;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;

/**
 * 微博应用实体类
 * @author ljw
 * @version 1.0
 * @date 2019-06-15
 */
@Entity
@Table(name = "jc_weibo_app_config")
public class WeiboAppConfig extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	/** 所属站点 */
	private Integer siteId;
	/** appid */
	private String appId;
	/** secret */
	private String appSecret;
	/** 描述 */
	private String description;

	public WeiboAppConfig() {
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_weibo_app_config", pkColumnValue = "jc_weibo_app_config", 
				initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_weibo_app_config")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "site_id", nullable = false, length = 11)
	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	@NotNull
	@Column(name = "app_id", nullable = false, length = 50)
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@NotNull
	@Column(name = "app_secret", nullable = false, length = 150)
	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	@Column(name = "description", nullable = true, length = 500)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}