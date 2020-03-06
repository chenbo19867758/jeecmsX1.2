package com.jeecms.common.base.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * 含排序基类 domain
 * 
 * @author: tom
 * @date: 2018年12月24日 下午3:11:30
 * @param <ID> id
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@MappedSuperclass
public abstract class AbstractSortDomain<ID extends Serializable> extends AbstractDomain<ID> {

	private static final long serialVersionUID = -1678643174744804388L;
	/** 排序值 */
	private Integer sortNum;

	@Column(name = "sort_num", length = 11)
	public Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}
}
