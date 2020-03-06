package com.jeecms.wechat.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.common.util.MyDateUtils;

/**
 * 小程序模版草稿箱
 * 
 * @Description:
 * @author wulongwei
 * @date 2018年11月1日 上午9:56:09
 */
@Entity
@Table(name = "jc_miniprogram_code")
public class MiniprogramCode extends AbstractDomain<Integer> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	/** 模版版本号 */
	private String codeVersion;

	/** 模版描述 */
	private String codeDesc;

	/** 草稿id */
	private Integer draftId;

	/** 模板id */
	private Integer templateId;

	/** 提交时间 */
	private Long submitTime;

	/** 1-草稿箱 2-模板 */
	private Short codeType;

	/** 模板是否为最新 0-否 1-是 */
	private Boolean isNew;

	/** 1-草稿箱 2-模板 */
	public static final Short DRAFT = 1;
	public static final Short TEMPLATE = 2;

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@TableGenerator(name = "jc_miniprogram_code", pkColumnValue = "jc_miniprogram_code", 
					initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_miniprogram_code")
	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@NotNull
	@Column(name = "code_version", length = 50, nullable = false)
	@Length(max = 50)
	public String getCodeVersion() {
		return codeVersion;
	}

	public void setCodeVersion(String codeVersion) {
		this.codeVersion = codeVersion;
	}

	@NotNull
	@Column(name = "code_desc", length = 150, nullable = false)
	@Length(max = 150)
	public String getCodeDesc() {
		return codeDesc;
	}

	public void setCodeDesc(String codeDesc) {
		this.codeDesc = codeDesc;
	}

	@Column(name = "draft_id")
	public Integer getDraftId() {
		return draftId;
	}

	public void setDraftId(Integer draftId) {
		this.draftId = draftId;
	}

	@Column(name = "template_id")
	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	@Column(name = "submit_time")
	public Long getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Long submitTime) {
		this.submitTime = submitTime;
	}

	@NotNull
	@Column(name = "code_type", nullable = false)
	public Short getCodeType() {
		return codeType;
	}

	public void setCodeType(Short codeType) {
		this.codeType = codeType;
	}

	@NotNull
	@Column(name = "is_new", nullable = false)
	public Boolean getIsNew() {
		return isNew;
	}

	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}

	/** 提交时间(前台无法转换Long格式的时间戳，所以此处转换成str给前台)*/
	@Transient
	public String getSubmitTimeStr() {
		String time = "";
		if (getSubmitTime() != null) {
			time = MyDateUtils.formatDate(new Date(getSubmitTime()));
		}
		return time;
	}
	
	@Override
	public String toString() {
		return "MiniprogramCode [id=" + id + ", codeVersion=" + codeVersion + ", codeDesc=" + codeDesc 
				+ ", draftId=" + draftId + ", templateId=" + templateId + ", submitTime=" + submitTime 
				+ ", codeType=" + codeType + ", isNew=" + isNew + "]";
	}

	/**
	 * code值
	 */
	public MiniprogramCode(Integer id, String codeVersion, String codeDesc, Integer draftId, Integer templateId,
			Long submitTime, Short codeType, Boolean isNew) {
		super();
		this.id = id;
		this.codeVersion = codeVersion;
		this.codeDesc = codeDesc;
		this.draftId = draftId;
		this.templateId = templateId;
		this.submitTime = submitTime;
		this.codeType = codeType;
		this.isNew = isNew;
	}

	public MiniprogramCode() {
		super();
	}

}
