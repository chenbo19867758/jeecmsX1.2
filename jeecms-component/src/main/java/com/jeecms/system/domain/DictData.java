package com.jeecms.system.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.jeecms.common.base.domain.AbstractSortDomain;

/**
 * 
 * @Description:字典数据
 * @author: ztx
 * @date:   2018年6月11日 上午10:30:19     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Entity
@Table(name="jc_sys_dict_data")
public class DictData extends AbstractSortDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 主键 */
	private Integer id;
	/** 字典标签 */
	private String dictLabel;
	/** 字典类型 */
	private String dictType;
	/** 是否系统内置 */
	private Boolean isSystem;
	/** 备注 */
	private String remark;
    /** 字典编号 */
	private String dictCode;
	
	
	/** 字典类型Id */
	private Integer dictTypeId;
	
	/** 字典类型 */
	private DictType coreDictType;
	
	
		
	public DictData() {
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@TableGenerator(name = "jc_sys_dict_data", pkColumnValue = "jc_sys_dict_data", initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_dict_data")
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@NotBlank
	@Length(max=50)
	@Column(name="dict_label", nullable=false, length=150)
	public String getDictLabel() {
		return this.dictLabel;
	}

	public void setDictLabel(String dictLabel) {
		this.dictLabel = dictLabel;
	}

	@NotBlank
	@Length(max=150)
	@Column(name="dict_type", length=150)
	public String getDictType() {
		return dictType;
	}
	public void setDictType(String dictType) {
		this.dictType = dictType;
	}

	@NotNull
	@Column(name="is_system", nullable=false)
	public Boolean getIsSystem() {
		return this.isSystem;
	}

	public void setIsSystem(Boolean isSystem) {
		this.isSystem = isSystem;
	}

	@Length(max=255)
	@Column(name="remark",length=500)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * bi-directional many-to-one association to CoreDictType
	 * TODO
	 * @Title: getCoreDictType  
	 * @return   DictType
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="dict_type_id",insertable=false,updatable=false)
	public DictType getCoreDictType() {
		return this.coreDictType;
	}

	public void setCoreDictType(DictType coreDictType) {
		this.coreDictType = coreDictType;
	}
	
	@Length(max=50)
	@Column(name="dict_code")
	public String getDictCode() {
		return dictCode;
	}
	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}
	
	@NotNull
	@Digits(integer=11, fraction = 0)
	@Column(name="dict_type_id",nullable=false)
	public Integer getDictTypeId() {
		return dictTypeId;
	}
	public void setDictTypeId(Integer dictTypeId) {
		this.dictTypeId = dictTypeId;
	}
	
	
}