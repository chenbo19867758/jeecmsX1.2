package com.jeecms.system.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.jeecms.common.base.domain.AbstractTreeDomain;

/**
 * 区域设置实体类
 * @author: chenming
 * @date:   2019年4月13日 下午2:35:20
 */
@Entity
@Table(name = "jc_sys_area")
public class Area extends AbstractTreeDomain<Area, Integer> {
	private static final long serialVersionUID = 1L;

	/** 当前定位区域键 */
	public static final String CURRENT_ADDRESS_ATTRNAME = "currentAddress";
	/** 缓存区域信息键 */
	public static final String AREA_LIST_ATTRNAME = "areaList";
	/** 缓存区域toString的信息键*/
	public static final String AREA_LIST_TOSTRING_NAME = "areaCacheAreaList";
	
	public static final String AREA_CACHE_KEY = "AREA";
	
	public static final String AREA_TYPE_PROVINCE = "1";
	
	public static final String AREA_TYPE_CITY = "2";
	
	public static final String AREA_TYPE_REGION = "3";
	

	/** 主键值 */
	private Integer id;
	/** 区域编号 */
	private String areaCode;
	/** 区域名称 */
	private String areaName;
	/** 区域类型（字典code，字典类型area_type） */
	private String areaDictCode;
	/** 备注 */
	private String remark;

	public Area() {

	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@TableGenerator(name = "jc_sys_area", pkColumnValue = "jc_sys_area", initialValue = 0, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_area")
	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "area_code")
	@NotBlank
	@Length(min = 1, max = 150)
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	@Column(name = "area_name")
	@NotBlank
	@Length(min = 1, max = 150)
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	@Column(name = "area_dict_code")
	@NotBlank
	@Length(max = 50)
	public String getAreaDictCode() {
		return areaDictCode;
	}

	public void setAreaDictCode(String areaDictCode) {
		this.areaDictCode = areaDictCode;
	}

	@Column(name = "remark")
	@Length(max = 150)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 判断是否为根节点，true表示是，false表示否
	 */
	@Transient
	public Boolean getIsChild() {
		// 判断是否为节点
		if (super.getChilds().size() == 0) {
			return true;
		}
		return false;
	}
}
