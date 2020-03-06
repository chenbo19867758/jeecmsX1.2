package com.jeecms.system.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

import com.jeecms.common.base.domain.AbstractDomain;


/**
 * 
 * @Description:字典类型
 * @author: ztx
 * @date:   2018年6月11日 上午10:30:44     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Entity
@Table(name="jc_sys_dict_type")
public class DictType extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 主键 */
	private Integer id;
	/** 字典名称 */
	private String dictName;
	/** 字典类型 */
	private String dictType;
	/** 是否系统内置，默认为是 */
	private Boolean isSystem;
	/** 备注 */
	private String remark;
	/** 排序 */
	private Integer sortNum;	
	
	/** 关联字典数据集合 */
	private List<DictData> datas;

	public DictType() {
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@TableGenerator(name = "jc_sys_dict_type", pkColumnValue = "jc_sys_dict_type", initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_dict_type")
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	
	@NotBlank
	@Length(max=150)
	@Column(name="dict_name", nullable=false, length=150)
	public String getDictName() {
		return this.dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	@NotBlank
	@Length(max=150)
	@Column(name="dict_type", nullable=false, length=150)
	public String getDictType() {
		return this.dictType;
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
	
	@Digits(integer=11, fraction = 0)
	@Column(name="sort_num")
	public Integer getSortNum() {
		return sortNum;
	}
	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	@OneToMany(mappedBy="coreDictType")
	@OrderBy(value="sortNum")
	@Where(clause=" deleted_flag=0 ")
	public List<DictData> getDatas() {
		return datas;
	}
	
	public void setDatas(List<DictData> datas) {
		this.datas = datas;
	}
	
	
	
}