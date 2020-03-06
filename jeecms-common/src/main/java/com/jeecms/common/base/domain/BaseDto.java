package com.jeecms.common.base.domain;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author: tom
 * @date:   2019年3月8日 下午4:27:33   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class BaseDto<T extends Serializable> implements Serializable {

	private static final long serialVersionUID = -2210986438052638262L;
	T object;

	@NotNull
	@Valid
	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

}
