/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain;

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
import com.jeecms.common.base.domain.AbstractDomain;

/**
 * 来源管理实体类
 * 
 * @author: wulongwei
 * @version 1.0
 * @date: 2019年5月6日 上午11:18:25
 */
@Entity
@Table(name = "jc_content_source")
public class ContentSource extends AbstractDomain<Integer> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	/** 来源名称 */
	private String sourceName;
	/** 来源链接 */
	private String sourceLink;
	/** 是否新窗口打开 */
	private Boolean isOpenTarget;
	/** 是否默认 */
	private Boolean isDefault;

	/**
	 * 初始化来源
	 */
	public ContentSource(String sourceName, String sourceLink, Boolean isOpenTarget, Boolean isDefault) {
		super();
		this.sourceName = sourceName;
		this.sourceLink = sourceLink;
		this.isOpenTarget = isOpenTarget;
		this.isDefault = isDefault;
	}

	public ContentSource() {
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_content_source", pkColumnValue = "jc_content_source", initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_content_source")
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@NotBlank
	@Column(name = "source_name", nullable = false, length = 150)
	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	@Column(name = "source_link", nullable = true, length = 255)
	public String getSourceLink() {
		return sourceLink;
	}

	public void setSourceLink(String sourceLink) {
		this.sourceLink = sourceLink;
	}

	@NotNull
	@Column(name = "is_open_target", nullable = false, length = 1)
	public Boolean getIsOpenTarget() {
		return isOpenTarget;
	}

	public void setIsOpenTarget(Boolean isOpenTarget) {
		this.isOpenTarget = isOpenTarget;
	}

	@NotNull
	@Column(name = "is_default", nullable = false, length = 1)
	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

}