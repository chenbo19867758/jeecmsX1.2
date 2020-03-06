/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.sso.dto;

import java.io.Serializable;
import java.util.List;

import com.jeecms.sso.dto.request.SyncRequestUser;

/**
 * 请求Dto
 * 
 * @author: ljw
 * @date: 2019年10月29日 上午10:07:30
 */
public class SyncRequestRegisterDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 应用APPID */
	private String appId;
	/** 用户列表 **/
	private List<SyncRequestUser> users;
	/**扩展参数**/
	private String param;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public List<SyncRequestUser> getUsers() {
		return users;
	}

	public void setUsers(List<SyncRequestUser> users) {
		this.users = users;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

}
