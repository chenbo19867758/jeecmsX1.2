/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.auth.domain.dto;

/**
 * 密码Dto
 * 
 * @author: ljw
 * @date: 2019年4月26日 上午9:53:41
 */
public class PswDto {

	/** 原密码 **/
	private String oldpsw;
	/** 加密后的密码 **/
	private String psw;
	/** 用户名 **/
	private String username;

	PswDto() {
	}

	public String getPsw() {
		return psw;
	}

	public void setPsw(String psw) {
		this.psw = psw;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOldpsw() {
		return oldpsw;
	}

	public void setOldpsw(String oldpsw) {
		this.oldpsw = oldpsw;
	}

}
