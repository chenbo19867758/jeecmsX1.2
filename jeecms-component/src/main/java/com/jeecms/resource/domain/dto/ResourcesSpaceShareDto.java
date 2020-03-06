/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.resource.domain.dto;

import java.util.Arrays;

/**
 * 资源库分享Dto
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/5/24 11:54:25
 */

public class ResourcesSpaceShareDto {

	private Integer id;

	private Integer[] ids;
	/**
	 * 组织id数组
	 */
	private Integer[] orgIds;
	/**
	 * 角色id数组
	 */
	private Integer[] roleIds;
	/**
	 * 用户id数组
	 */
	private Integer[] userIds;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer[] getIds() {
		return ids;
	}

	public void setIds(Integer[] id) {
		this.ids = id;
	}

	public Integer[] getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(Integer[] orgIds) {
		this.orgIds = orgIds;
	}

	public Integer[] getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(Integer[] roleIds) {
		this.roleIds = roleIds;
	}

	public Integer[] getUserIds() {
		return userIds;
	}

	public void setUserIds(Integer[] userIds) {
		this.userIds = userIds;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ResourcesSpaceShareDto)) {
			return false;
		}

		ResourcesSpaceShareDto dto = (ResourcesSpaceShareDto) o;

		if (getId() != null ? !getId().equals(dto.getId()) :
				dto.getId() != null) {
			return false;
		}
		// Probably incorrect - comparing Object[] arrays with Arrays.equals
		if (!Arrays.equals(getIds(), dto.getIds())) {
			return false;
		}
		// Probably incorrect - comparing Object[] arrays with Arrays.equals
		if (!Arrays.equals(getOrgIds(), dto.getOrgIds())) {
			return false;
		}
		// Probably incorrect - comparing Object[] arrays with Arrays.equals
		if (!Arrays.equals(getRoleIds(), dto.getRoleIds())) {
			return false;
		}
		// Probably incorrect - comparing Object[] arrays with Arrays.equals
		return Arrays.equals(getUserIds(), dto.getUserIds());
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + Arrays.hashCode(getIds());
		result = 31 * result + Arrays.hashCode(getOrgIds());
		result = 31 * result + Arrays.hashCode(getRoleIds());
		result = 31 * result + Arrays.hashCode(getUserIds());
		return result;
	}
}
