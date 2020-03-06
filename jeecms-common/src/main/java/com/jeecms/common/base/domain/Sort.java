package com.jeecms.common.base.domain;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

/**
 * 排序dto
 * 
 * @Description:
 * @author: gl
 * @date: 2018年3月27日 下午3:14:51
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class Sort {

	/** id */
	private Integer id;
	/** 排序 */
	private Integer sortNum;

	@NotNull
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@NotNull
	@Digits(integer = 11, fraction = 0)
	public Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

}