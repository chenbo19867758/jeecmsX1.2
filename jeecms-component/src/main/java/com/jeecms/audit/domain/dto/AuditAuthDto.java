/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.audit.domain.dto;

import javax.validation.constraints.NotBlank;

/**
 * 授权DTO
 * @author: ljw
 * @date: 2019年12月17日 下午4:00:12
 */
public class AuditAuthDto {

	/** 手机号 **/
	private String mobile;
	/** 验证码 **/
	private String code;

	public AuditAuthDto() {
	}

	@NotBlank
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@NotBlank
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
