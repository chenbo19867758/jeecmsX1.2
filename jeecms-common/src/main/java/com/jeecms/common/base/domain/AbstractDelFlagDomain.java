package com.jeecms.common.base.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * 删除标识damain
 * 
 * @author: tom
 * @date: 2018年12月24日 下午3:10:40
 * @param <Id>
 *            id
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@MappedSuperclass
public abstract class AbstractDelFlagDomain<Id extends Serializable> extends AbstractIdDomain<Id> {

	private static final long serialVersionUID = -1678643174744804388L;
	/** 删除标识 */
	private Boolean hasDeleted;
	private static final String DELETE_FLAG_NAME = "hasDeleted";

	@Transient
	public String getHasDeletedName() {
		return DELETE_FLAG_NAME;
	}

	@Column(name = "deleted_flag", nullable = false)
	public Boolean getHasDeleted() {
		return this.hasDeleted;
	}

	public void setHasDeleted(Boolean hasDeleted) {
		this.hasDeleted = hasDeleted;
	}
}
