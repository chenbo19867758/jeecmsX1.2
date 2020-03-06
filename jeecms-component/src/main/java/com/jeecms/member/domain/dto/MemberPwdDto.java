/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.member.domain.dto;

import javax.validation.constraints.NotBlank;

/**
 * 会员修改密码Dto
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/4/24
 */

public class MemberPwdDto {
	/**
	 * 旧密码
	 */
	private String oldPStr;
	/**
	 * 新密码
	 */
	private String newPStr;
	/**
	 * 再次输入密码
	 */
	private String againPStr;

	@NotBlank
	public String getOldPStr() {
		return oldPStr;
	}

	public void setOldPStr(String oldPStr) {
		this.oldPStr = oldPStr;
	}

	@NotBlank
	public String getNewPStr() {
		return newPStr;
	}

	public void setNewPStr(String newPStr) {
		this.newPStr = newPStr;
	}

	@NotBlank
	public String getAgainPStr() {
		return againPStr;
	}

	public void setAgainPStr(String againPStr) {
		this.againPStr = againPStr;
	}
}
