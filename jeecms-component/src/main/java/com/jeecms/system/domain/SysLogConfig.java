/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain;

import com.jeecms.common.base.domain.AbstractDomain;

import javax.persistence.*;
import java.io.Serializable;


/**
 * 日志策略实体类
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-05-29 10:48:15
 */
@Entity
@Table(name = "jc_sys_log_config")
public class SysLogConfig extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 邮箱和手机号最大个数
	 */
	public static final int MAX_SIZE = 100;

	private Integer id;
	/**
	 * 预警阈值
	 */
	private Integer warnValue;
	/**
	 * 告警阈值
	 */
	private Integer dangerValue;
	/**
	 * 通知短信列表，多个使用 ;分隔
	 */
	private String noticeSmsList;
	/**
	 * 通知邮箱列表，多个使用 ;分隔
	 */
	private String noticeEmailList;
	/**
	 * 短信预警通知模板id
	 */
	private String warnSmsTmpId;
	/**
	 * 短信告警通知模板id
	 */
	private String dangerSmsTmpId;
	/**
	 * 表大小
	 */
	private String size;

	public SysLogConfig() {
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_sys_log_config", pkColumnValue = "jc_sys_log_config", initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_log_config")
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "warn_value", nullable = true, length = 11)
	public Integer getWarnValue() {
		return warnValue;
	}

	public void setWarnValue(Integer warnValue) {
		this.warnValue = warnValue;
	}

	@Column(name = "danger_value", nullable = true, length = 11)
	public Integer getDangerValue() {
		return dangerValue;
	}

	public void setDangerValue(Integer dangerValue) {
		this.dangerValue = dangerValue;
	}

	@Column(name = "notice_sms_list", nullable = true, length = 3000)
	public String getNoticeSmsList() {
		return noticeSmsList;
	}

	public void setNoticeSmsList(String noticeSmsList) {
		this.noticeSmsList = noticeSmsList;
	}

	@Column(name = "notice_email_list", nullable = true, length = 1500)
	public String getNoticeEmailList() {
		return noticeEmailList;
	}

	public void setNoticeEmailList(String noticeEmailList) {
		this.noticeEmailList = noticeEmailList;
	}

	@Column(name = "warn_sms_tmp_id", nullable = true, length = 50)
	public String getWarnSmsTmpId() {
		return warnSmsTmpId;
	}

	public void setWarnSmsTmpId(String warnSmsTmpId) {
		this.warnSmsTmpId = warnSmsTmpId;
	}

	@Column(name = "danger_sms_tmp_id", nullable = true, length = 50)
	public String getDangerSmsTmpId() {
		return dangerSmsTmpId;
	}

	public void setDangerSmsTmpId(String dangerSmsTmpId) {
		this.dangerSmsTmpId = dangerSmsTmpId;
	}

	@Transient
	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof SysLogConfig)) {
			return false;
		}

		SysLogConfig that = (SysLogConfig) o;

		if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) {
			return false;
		}
		if (getWarnValue() != null ? !getWarnValue().equals(that.getWarnValue()) : that.getWarnValue() != null) {
			return false;
		}
		if (getDangerValue() != null ? !getDangerValue().equals(that.getDangerValue()) : that.getDangerValue() != null) {
			return false;
		}
		if (getNoticeSmsList() != null ? !getNoticeSmsList().equals(that.getNoticeSmsList()) : that.getNoticeSmsList() != null) {
			return false;
		}
		if (getNoticeEmailList() != null ? !getNoticeEmailList().equals(that.getNoticeEmailList()) : that.getNoticeEmailList() != null) {
			return false;
		}
		if (getWarnSmsTmpId() != null ? !getWarnSmsTmpId().equals(that.getWarnSmsTmpId()) : that.getWarnSmsTmpId() != null) {
			return false;
		}
		if (getDangerSmsTmpId() != null ? !getDangerSmsTmpId().equals(that.getDangerSmsTmpId()) : that.getDangerSmsTmpId() != null) {
			return false;
		}
		return getSize() != null ? getSize().equals(that.getSize()) : that.getSize() == null;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + (getWarnValue() != null ? getWarnValue().hashCode() : 0);
		result = 31 * result + (getDangerValue() != null ? getDangerValue().hashCode() : 0);
		result = 31 * result + (getNoticeSmsList() != null ? getNoticeSmsList().hashCode() : 0);
		result = 31 * result + (getNoticeEmailList() != null ? getNoticeEmailList().hashCode() : 0);
		result = 31 * result + (getSize() != null ? getSize().hashCode() : 0);
		result = 31 * result + (getWarnSmsTmpId() != null ? getWarnSmsTmpId().hashCode() : 0);
		result = 31 * result + (getDangerSmsTmpId() != null ? getDangerSmsTmpId().hashCode() : 0);
		return result;
	}
}