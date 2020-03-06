package com.jeecms.common.base.domain;

import com.alibaba.fastjson.annotation.JSONField;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * 删除及批量删除dto
 * 
 * @Description:
 * @author: gl
 * @date: 2018年3月27日 下午3:14:51
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class DeleteDto {

	/** id */
	@JSONField(serialize = true)
	private Integer[] ids;

	@NotNull
	@Size(min = 1, max = 1000000000)
	public Integer[] getIds() {
		return ids;
	}

	public void setIds(Integer[] ids) {
		this.ids = ids;
	}

}
