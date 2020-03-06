/**
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *            仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。 
 */

package com.jeecms.member.domain.dto;

import java.io.Serializable;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.jeecms.system.domain.dto.ValidateCodeConstants;

/**
 * PC端的会员DTO
 * @author: ljw
 * @date: 2019年7月16日 下午7:12:26
 */
public class PcMemberDto implements Serializable {
	private static final long serialVersionUID = 6760797667571156579L;

	/** 会员id */
	private Integer id;
	/** 会员账户/邮箱 */
	private String key;
	/** 会员账户 */
	private String username;
	/** 密码 */
	private String pStr;
	/** 邮箱 */
	private String email;
	/** 电话 */
	private String phone;
	/** 验证码 */
	private String validateCode;
	/** 目标号码 */
	private String targetNumber;
	/**验证类型 1.邮箱找回密码 2.手机找回密码**/
	private Integer type;
	
	@Digits(integer = 11, fraction = 0, groups = { RegisterByEmail.class, RegisterByPhone.class })
	public Integer getId() {
		return id;
	}

	@NotBlank(groups = { RegisterByEmail.class, RegisterByPhone.class, BindingMember.class })
	@Length(max = 50, groups = { RegisterByEmail.class, RegisterByPhone.class, BindingMember.class })
	public String getUsername() {
		return username;
	}

	@NotBlank(groups = { RegisterByEmail.class, RegisterByPhone.class, RetrievePasswordByPhone.class,
			RetrievePasswordByEmail.class, BindingMember.class })
	@Length(max = 50, groups = { RegisterByEmail.class, RegisterByPhone.class, RetrievePasswordByPhone.class,
			RetrievePasswordByEmail.class, BindingMember.class })
	public String getpStr() {
		return pStr;
	}

	@NotBlank(groups = { RegisterByEmail.class })
	@Length(max = 60, groups = { RegisterByEmail.class })
	@Email(groups = { RegisterByEmail.class })
	public String getEmail() {
		return email;
	}

	@NotBlank(groups = { RegisterByPhone.class, RetrievePasswordByPhone.class })
	@Length(max = 12, groups = { RegisterByPhone.class, RetrievePasswordByPhone.class })
	public String getPhone() {
		return phone;
	}

	@NotBlank(groups = { RegisterByEmail.class, RegisterByPhone.class, RetrievePasswordByPhone.class,
			RetrievePasswordByEmail.class })
	@Length(max = ValidateCodeConstants.DEFAULT_VALIDATE_CODE_LENGTH, groups = { RegisterByEmail.class,
			RegisterByPhone.class, RetrievePasswordByPhone.class, RetrievePasswordByEmail.class })
	public String getValidateCode() {
		return validateCode;
	}

	public String getTargetNumber() {
		return targetNumber;
	}

	/**
	 * 手机号注册
	 */
	public interface RegisterByPhone {
	}

	/**
	 * 邮箱注册
	 */
	public interface RegisterByEmail {
	}

	/**
	 * 根据手机号找回密码
	 */
	public interface RetrievePasswordByPhone {
	}

	/**
	 * 根据邮箱找回密码
	 */
	public interface RetrievePasswordByEmail {
	}

	/**
	 * 绑定会员
	 */
	public interface BindingMember {
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setpStr(String pStr) {
		this.pStr = pStr;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setTargetNumber(String targetNumber) {
		this.targetNumber = targetNumber;
	}
	
	@Override
	public String toString() {
		return "PCMemberDTO.java";
	}

	@NotBlank(groups = { RetrievePasswordByEmail.class })
	@Length(max = 60, groups = { RetrievePasswordByEmail.class })
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	/**邮箱找回**/
	public static final Integer TYPE_EMAIL = 1;
	/**手机找回**/
	public static final Integer TYPE_TELEPHONE = 2;

}
