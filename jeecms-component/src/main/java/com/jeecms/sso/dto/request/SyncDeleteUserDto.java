/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.sso.dto.request;

import java.io.Serializable;
import java.util.List;

import com.jeecms.sso.dto.BaseDto;

/**
 * 删除数据Dto
 * 
 * @author: ljw
 * @date: 2019年10月26日 下午4:24:58
 */
public class SyncDeleteUserDto extends BaseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 用户列表 **/
	private List<SyncDeleteUser> users;

	public List<SyncDeleteUser> getUsers() {
		return users;
	}

	public void setUsers(List<SyncDeleteUser> users) {
		this.users = users;
	}

}
