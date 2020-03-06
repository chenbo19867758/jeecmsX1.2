/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.member.domain.dto;

import javax.validation.constraints.NotBlank;

/**
 * 会员修改邮箱Dto
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/4/24
 */

public class MemberEmailDto {

	/**
	 * 验证码
	 */
	private String code;

	/**
	 * 新邮箱
	 */
	private String newEmail;
	
	/**
	 * 新邮箱验证码
	 */
	private String newEmailCode;

	@NotBlank
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getNewEmail() {
		return newEmail;
	}

	public void setNewEmail(String newEmail) {
		this.newEmail = newEmail;
	}

	public String getNewEmailCode() {
		return newEmailCode;
	}

	public void setNewEmailCode(String newEmailCode) {
		this.newEmailCode = newEmailCode;
	}
}
