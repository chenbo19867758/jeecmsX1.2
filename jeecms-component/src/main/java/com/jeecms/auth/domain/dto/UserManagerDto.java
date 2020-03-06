/**
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。 
 */

package com.jeecms.auth.domain.dto;

import java.util.List;

/**   
 * 用户管理Dto
 * @author: ljw
 * @date:   2019年4月11日 上午9:52:13  
 */
public class UserManagerDto {

	/**角色ID，组织ID，用户组ID**/
	private Integer id;
	/**类型1.角色2.组织3.用户组**/
	private Integer type;
	/**用户ID**/
	private List<Integer> userId;
	
	UserManagerDto(){}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Integer> getUserId() {
		return userId;
	}

	public void setUserId(List<Integer> userId) {
		this.userId = userId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public static final Integer ROLE_TYPE = 1;
	public static final Integer ORG_TYPE = 2;
	public static final Integer GROUP_TYPE = 3;
}
