package com.jeecms.common.base.domain;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 数组类型对象
 * 
 * @author: tom
 * @date: 2018年7月17日 上午10:18:00
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class ArrayDto<T extends Serializable> {

	private T[] arrays;

	@NotNull
	@Valid
	public T[] getArrays() {
		return arrays;
	}

	public void setArrays(T[] arrays) {
		this.arrays = arrays;
	}

}
