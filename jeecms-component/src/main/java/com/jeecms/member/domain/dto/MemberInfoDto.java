/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.member.domain.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

/**
 * 会员基本信息Dto
 *
 * @author ljw
 * @version 1.0
 * @date 2019/7/18
 */

public class MemberInfoDto {

	/** 会员ID **/
	private Integer id;
	/** 真实姓名 **/
	private String realName;
	/**旧手机号**/
	private String telephone;
	/**旧手机验证码**/
	private String mobileCode;
	/** 新手机号 **/
	private String newTelephone;
	/** 新手机号 **/
	private String newTelephoneCode;
	

	@Length(min = 1, max = 150)
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	@NotNull
	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNewTelephone() {
		return newTelephone;
	}

	public void setNewTelephone(String newTelephone) {
		this.newTelephone = newTelephone;
	}

	public String getMobileCode() {
		return mobileCode;
	}

	public void setMobileCode(String mobileCode) {
		this.mobileCode = mobileCode;
	}

	public String getNewTelephoneCode() {
		return newTelephoneCode;
	}

	public void setNewTelephoneCode(String newTelephoneCode) {
		this.newTelephoneCode = newTelephoneCode;
	}
}
