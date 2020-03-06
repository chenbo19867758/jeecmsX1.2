/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.weibo.domain.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 设置管理员Dto
 * 
 * @author: ljw
 * @date: 2019年6月20日 下午3:53:43
 */
public class SetAdminDto {

	/** 实体IDs集合 **/
	private List<Integer> ids = new ArrayList<Integer>(10);
	/** 微博账户ID **/
	private Integer weiboUserId;

	public SetAdminDto() {
	}

	@NotEmpty
	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

	@NotNull
	public Integer getWeiboUserId() {
		return weiboUserId;
	}

	public void setWeiboUserId(Integer weiboUserId) {
		this.weiboUserId = weiboUserId;
	}

}
