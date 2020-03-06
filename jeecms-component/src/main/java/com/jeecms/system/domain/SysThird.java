/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain;

import com.jeecms.common.base.domain.AbstractDomain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * 第三方登陆配置表
 * 
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-18
 */
@Entity
@Table(name = "jc_sys_third")
public class SysThird extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	/** 第三方登陆标识(QQ WECHAT WEIBO ) */
	private String code;
	/** 应用id */
	private String appId;
	/** 应用密钥 */
	private String appKey;
	/** 是否启用(0-否 1-是) */
	private Boolean isEnable;
	/** 备注 */
	private String reMark;

	public SysThird() {
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_sys_third", pkColumnValue = "jc_sys_third", initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_third")
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "code", nullable = false, length = 50)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "app_id", nullable = false, length = 50)
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@Column(name = "app_key", nullable = false, length = 150)
	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	@Column(name = "is_enable", nullable = false, length = 1)
	public Boolean getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Boolean isEnable) {
		this.isEnable = isEnable;
	}

	@Column(name = "re_mark", nullable = true, length = 500)
	public String getReMark() {
		return reMark;
	}

	public void setReMark(String reMark) {
		this.reMark = reMark;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appId == null) ? 0 : appId.hashCode());
		result = prime * result + ((appKey == null) ? 0 : appKey.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isEnable == null) ? 0 : isEnable.hashCode());
		result = prime * result + ((reMark == null) ? 0 : reMark.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SysThird other = (SysThird) obj;
		if (appId == null) {
			if (other.appId != null) {
				return false;
			}
		} else if (!appId.equals(other.appId)) {
			return false;
		}
		if (appKey == null) {
			if (other.appKey != null) {
				return false;
			}
		} else if (!appKey.equals(other.appKey)) {
			return false;
		}
		if (code == null) {
			if (other.code != null) {
				return false;
			}
		} else if (!code.equals(other.code)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (isEnable == null) {
			if (other.isEnable != null) {
				return false;
			}
		} else if (!isEnable.equals(other.isEnable)) {
			return false;
		}
		if (reMark == null) {
			if (other.reMark != null) {
				return false;
			}
		} else if (!reMark.equals(other.reMark)) {
			return false;
		}
		return true;
	}
	
	public static final String WECHAT = "WECHAT"; 
	public static final String QQ = "QQ"; 
	public static final String WEIBO = "WEIBO"; 
	
}