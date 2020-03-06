/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.sso.dto.request;

import java.io.Serializable;

import com.jeecms.auth.domain.CoreUser;

/**
 * 删除用户
 * 
 * @author: ljw
 * @date: 2019年10月26日 下午4:37:34
 */
public class SyncDeleteUser implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 用户名 **/
	private String username;
	/** 用户类别（1.管理员 2. 会员） */
	private Integer userType;

	public SyncDeleteUser() {
	}

	/** 构造函数 **/
	public SyncDeleteUser(CoreUser user) {
		this.username = user.getUsername();
		if (user.getAdmin()) {
			this.userType = 1;
		} else {
			this.userType = 2;
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

}
