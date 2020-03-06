package com.jeecms.system.domain.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;

import com.jeecms.system.domain.Area;

/**
 * 
 * @Description: Area设置的扩展类
 * @author: chenming
 * @date:   2018年8月29日 下午1:43:54     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class AreaDto {
	
	/** 主键值 */
	private Integer id; 
	/** 区域编号 */
	private String areaCode;
	/** 区域名称 */
	private String areaName;
	/** 父级节点 */
	private Integer parentId;
	/**	区域类型（字典code，字典类型area_type）*/
	private String areaDictCode;
	/** 备注 */
	private String remark;
	/** 排序  */
	private Integer sortNum;
	/** 创建时间 */
	private Date createTime;
	/** 创建用户 */
	private String createUser;
	/** 修改时间 */
	private Date updateTime;
	/** 修改用户 */
	private String updateUser;
	/** 逻辑删除标识。仅且仅有0和1两个值，1表示已经被逻辑删除，0表示正常可用 */
	private Boolean hasDeleted;
	/** 是否展示下级菜单布局，true是，false否 */
	private Boolean lowerArea;
//	/** 父元素的id数组 */
//	private Integer[] parentIds;
	/**	CoreDictData的name，字典名称*/
	private String areaTypeName;
	
	public AreaDto(Area area) {
		this.id=area.getId();
		this.areaCode=area.getAreaCode();
		this.areaName=area.getAreaName();
		this.parentId=area.getParentId();
		this.remark=area.getRemark();
		this.sortNum=area.getSortNum();
		this.createTime=area.getCreateTime();
		this.createUser=area.getCreateUser();
		this.updateTime=area.getUpdateTime();
		this.updateUser=area.getUpdateUser();
		this.hasDeleted=area.getHasDeleted();
		this.areaDictCode = area.getAreaCode();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@NotNull
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	
	
	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Boolean getHasDeleted() {
		return hasDeleted;
	}

	public void setHasDeleted(Boolean hasDeleted) {
		this.hasDeleted = hasDeleted;
	}


	public String getAreaTypeName() {
		return areaTypeName;
	}

	public void setAreaTypeName(String areaTypeName) {
		this.areaTypeName = areaTypeName;
	}

	public Boolean getLowerArea() {
		return lowerArea;
	}

	public void setLowerArea(Boolean lowerArea) {
		this.lowerArea = lowerArea;
	}

	public String getAreaDictCode() {
		return areaDictCode;
	}

	public void setAreaDictCode(String areaDictCode) {
		this.areaDictCode = areaDictCode;
	}
	
	
	
}
