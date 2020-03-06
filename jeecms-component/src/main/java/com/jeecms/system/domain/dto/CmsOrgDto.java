/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.dto;

import javax.validation.constraints.NotNull;

import com.jeecms.system.domain.CmsOrg;

/**
 * 修改组织DTO
 * 
 * @author: ljw
 * @date: 2019年7月10日 上午10:05:34
 */
public class CmsOrgDto {

	private Integer id;
	/** 父ID **/
	private Integer parentId;
	/** 部门代码 **/
	private String code;
	/** 是否虚拟组织 **/
	private Boolean isVirtual;
	/** 组织名称 **/
	private String name;
	/** 组织描述 **/
	private String orgRemark;
	/** 组织编号 **/
	private String orgNum;
	/** 组织负责人 **/
	private String orgLeader;
	/** 电话 **/
	private String phone;
	/** 传真 **/
	private String orgFax;

	public CmsOrgDto() {}
	
	/**
	 * 函数
	 */
	public CmsOrg dto(CmsOrg org, CmsOrgDto dto) {
		org.setName(name);
		org.setCode(code);
		org.setOrgFax(orgFax);
		org.setOrgLeader(orgLeader);
		org.setOrgNum(orgNum);
		org.setOrgRemark(orgRemark);
		org.setIsVirtual(isVirtual);
		org.setPhone(phone);
		org.setParentId(parentId);
		return org;
	}
	
	@NotNull
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Boolean getIsVirtual() {
		return isVirtual;
	}

	public void setIsVirtual(Boolean isVirtual) {
		this.isVirtual = isVirtual;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrgRemark() {
		return orgRemark;
	}

	public void setOrgRemark(String orgRemark) {
		this.orgRemark = orgRemark;
	}

	public String getOrgNum() {
		return orgNum;
	}

	public void setOrgNum(String orgNum) {
		this.orgNum = orgNum;
	}

	public String getOrgLeader() {
		return orgLeader;
	}

	public void setOrgLeader(String orgLeader) {
		this.orgLeader = orgLeader;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getOrgFax() {
		return orgFax;
	}

	public void setOrgFax(String orgFax) {
		this.orgFax = orgFax;
	}

}
