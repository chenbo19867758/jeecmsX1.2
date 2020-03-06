/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.resource.domain.vo;

import com.jeecms.resource.domain.ResourcesSpace;

import java.util.List;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/8/17 10:38
 */

public class ResourcesSpaceVo {

	private String name;
	private Integer userId;
	private List<ResourcesSpace> children;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public List<ResourcesSpace> getChildren() {
		return children;
	}

	public void setChildren(List<ResourcesSpace> children) {
		this.children = children;
	}

	public ResourcesSpaceVo() {
		super();
	}

	public ResourcesSpaceVo(String name, Integer userId, List<ResourcesSpace> children) {
		this.name = name;
		this.userId = userId;
		this.children = children;
	}
}
