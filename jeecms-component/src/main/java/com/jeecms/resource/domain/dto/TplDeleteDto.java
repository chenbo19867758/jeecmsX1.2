package com.jeecms.resource.domain.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/***
 * 
 * @Description:模板资源删除Dto
 * @author: tom
 * @date: 2018年11月9日 下午2:14:28
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class TplDeleteDto implements Serializable {
	private static final long serialVersionUID = -6169862150788955142L;
	/** names */
	private String[] names;

	@NotNull
	@Size(min = 1, max = 10000)
	public String[] getNames() {
		return names;
	}

	public void setNames(String[] names) {
		this.names = names;
	}

}
