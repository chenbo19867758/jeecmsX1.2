/*
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.member.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.resource.domain.ResourcesSpaceData;

/**
 * 会员自定义属性实体
 * @author ljw
 * @version 1.0
 * @date 2019-07-10
 * 
 */
@Entity
@Table(name = "jc_member_attr")
public class MemberAttr implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer memberAttrId;
	/** 会员ID */
	private Integer memberId;
	/** 扩展字段名 */
	private String attrName;
	/** 扩展字段值 */
	private String attrValue;
	/** 数据类型(对应模型字段数据类型) */
	private String attrType;
	/** 资源ID(单资源) */
	private Integer resId;
	/** 省份编码 */
	private String provinceCode;
	/** 城市编码 */
	private String cityCode;
	/** 区县编码 */
	private String areaCode;
	
	/**会员对象**/
	private CoreUser member;
	/** 资源对象 */
	private ResourcesSpaceData resourcesSpaceData;
	
	/**
	 * 构造函数
	 */
	public MemberAttr() {
	}
	
	/**
	 * 构造函数
	 */
	public MemberAttr(String attrName, String attrType) {
		super();
		this.attrName = attrName;
		this.attrType = attrType;
	}

	@Id
	@Column(name = "member_attr_id", nullable = false, length = 11)
	@TableGenerator(name = "jc_member_attr", pkColumnValue = "jc_member_attr",
				initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_member_attr")
	public Integer getMemberAttrId() {
		return this.memberAttrId;
	}

	public void setMemberAttrId(Integer memberAttrId) {
		this.memberAttrId = memberAttrId;
	}

	@Column(name = "member_id", nullable = false, length = 11)
	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	@Column(name = "attr_name", nullable = false, length = 150)
	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	@Column(name = "attr_value", nullable = true, length = 715827882)
	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

	@Column(name = "attr_type", nullable = false, length = 50)
	public String getAttrType() {
		return attrType;
	}

	public void setAttrType(String attrType) {
		this.attrType = attrType;
	}

	@Column(name = "res_id", nullable = true, length = 11)
	public Integer getResId() {
		return resId;
	}

	public void setResId(Integer resId) {
		this.resId = resId;
	}

	@Column(name = "province_code", nullable = true, length = 50)
	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	@Column(name = "city_code", nullable = true, length = 50)
	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	@Column(name = "area_code", nullable = true, length = 50)
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", insertable = false, updatable = false)
	public CoreUser getMember() {
		return member;
	}

	public void setMember(CoreUser member) {
		this.member = member;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "res_id",referencedColumnName = "id",insertable = false,updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	public ResourcesSpaceData getResourcesSpaceData() {
		return resourcesSpaceData;
	}

	public void setResourcesSpaceData(ResourcesSpaceData resourcesSpaceData) {
		this.resourcesSpaceData = resourcesSpaceData;
	}

	/** 省份编码名称*/
	public static final String PROVINCE_CODE_NAME = "provinceCode";
	/** 城市编码名称*/
	public static final String CITY_CODE_NAME = "cityCode";
	/** 区县编码名称*/
	public static final String AREA_CODE_NAME = "areaCode";
	/** 详细地址名称*/
	public static final String ADDRESS_NAME = "address";
}