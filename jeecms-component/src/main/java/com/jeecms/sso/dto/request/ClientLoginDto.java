/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.sso.dto.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.jeecms.sso.dto.BaseDto;

/**
 * 客户端登录DTO
 * 
 * @author: ljw
 * @date: 2019年10月31日 下午2:49:48
 */
public class ClientLoginDto extends BaseDto implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 用户名 **/
	private String username;
	/** 密码加密串 **/
	private String password;
	/** 用户类别（1.管理员 2. 会员） */
	private Integer userType;
	
	public ClientLoginDto(){}
	
	/**构造函数**/
	public ClientLoginDto(String appId, String appSecret, String username,
			String password, Integer userType) {
		super.setAppId(appId);
		super.setAppSecret(appSecret);
		this.username = username;
		this.password = password;
		this.userType = userType;
	}

	@NotNull
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@NotNull
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getUserType() {
		return userType != null ? userType : 1;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

}
