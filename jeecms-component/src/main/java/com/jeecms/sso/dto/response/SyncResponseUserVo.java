/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.sso.dto.response;

import java.io.Serializable;

/**
 * 根据token得到用户的数据
 * 
 * @author: ljw
 * @date: 2019年10月26日 下午2:48:54
 */
public class SyncResponseUserVo extends SyncResponseBaseVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	/** 用户所属APPID **/
	private String appId;
	/** 用户名 **/
	private String username;
	/** 用户类别（1.管理员 2. 会员） */
	private Integer userType;

	public SyncResponseUserVo() {
	}

	/** 构造函数 **/
	public SyncResponseUserVo(String appId, String username, Integer userType) {
		this.appId = appId;
		this.username = username;
		this.userType = userType;
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

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
}
