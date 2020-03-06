package com.jeecms.common.base.domain;

/**   
 * @author: tom
 * @date:   2018年4月5日 上午11:18:55     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
import java.io.Serializable;

public abstract class AbstractIdDomain<ID extends Serializable> implements Serializable {
	private static final long serialVersionUID = -1720385080227360706L;
	private ID id;

	public ID getId() {
		return this.id;
	}

	public void setId(ID id) {
		this.id = id;
	}
}
