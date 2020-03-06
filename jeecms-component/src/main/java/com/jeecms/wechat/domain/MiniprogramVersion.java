package com.jeecms.wechat.domain;

import java.util.Date;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.alibaba.fastjson.annotation.JSONField;
import com.jeecms.common.base.domain.AbstractDomain;

/**
 * 
 * @Description: 小程序代码版本管理实体类
 * @author: chenming
 * @date: 2018年11月1日 上午9:07:03
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Entity
@Table(name = "jc_miniprogram_version")
public class MiniprogramVersion extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 审核版本*/
	public static final Integer AUDIT_VERSION = 2;
	/** 线上版本 */
	public static final Integer RELEASE_VERSION = 3;

	public static final String VERSION_CLOSE = "close";

	public static final String VERSION_OPEN = "open";

	private Integer id;
	/** 版本类型 1-开发版本 2-审核版本 3-线上版本 */
	private Integer versionType;
	/** appId */
	private String appId;
	/** 模板ID */
	private Integer templateId;
	/** 代码版本号 */
	private String codeVersion;
	/** 代码描述 */
	private String codeDesc;
	/** 状态：1-提交/审核中 2-成功 3-失败 */
	private Integer status;
	/** 提交审核后返回的auditid */
	private Integer auditid;
	/** 上线版本是否可见：close为不可见，open为可见 */
	private String action;
	/** 失败原因 */
	private String failReason;
	/** 审核时间 */
	private Date auditTime;

	public MiniprogramVersion() {

	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_miniprogram_version", pkColumnValue = "jc_miniprogram_version", 
					initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_miniprogram_version")
	@Override
	@NotNull(groups = { UpdateReleaseStatus.class })
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "version_type", nullable = false, length = 6)
	public Integer getVersionType() {
		return versionType;
	}

	public void setVersionType(Integer versionType) {
		this.versionType = versionType;
	}

	@Column(name = "app_id", nullable = false, length = 50)
	@Length(max = 50)
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@Column(name = "template_id", nullable = true, length = 11)
	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	@Column(name = "code_version", nullable = false, length = 50)
	@Length(max = 50)
	public String getCodeVersion() {
		return codeVersion;
	}

	public void setCodeVersion(String codeVersion) {
		this.codeVersion = codeVersion;
	}

	@Column(name = "code_desc", nullable = false, length = 150)
	@Length(max = 150)
	public String getCodeDesc() {
		return codeDesc;
	}

	public void setCodeDesc(String codeDesc) {
		this.codeDesc = codeDesc;
	}

	@Column(name = "status", nullable = true, length = 6)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "auditid", nullable = true, length = 11)
	public Integer getAuditid() {
		return auditid;
	}

	public void setAuditid(Integer auditid) {
		this.auditid = auditid;
	}

	@Column(name = "action", nullable = true, length = 20)
	@Length(max = 20)
	@NotBlank(groups = { UpdateReleaseStatus.class })
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Column(name = "fail_reason", nullable = true, length = 150)
	@Length(max = 150)
	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	@Column(name = "audit_time", nullable = true, length = 19)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public interface UpdateReleaseStatus {

	}
}