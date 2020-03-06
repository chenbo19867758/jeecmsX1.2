/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.member.domain.dto;

import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSONObject;

/**
 * 后台会员新增Dto
 * 
 * @author: ljw
 * @date: 2019年7月11日 上午11:41:47
 */
public class MemberRegisterDto {

	/** 会员ID **/
	private Integer id;
	/**来源站点ID**/
	private Integer siteId;
	/**************系统默认字段****************/
	/**手机验证码**/
	private String mobileCode;
	/**邮箱验证码**/
	private String emailCode;
	/** 用户名 **/
	private String username;
	/** 电子邮箱 **/
	private String email;
	/** 手机号 **/
	private String telephone;
	/** 密码 **/
	private String password;
	/** 真实姓名 **/
	private String realname;
	/**************其他字段****************/
	/**注册类型1.无需验证码2.邮箱验证码3.手机验证码4.邮箱，手机验证码 5图形验证码**/
	private Integer type;
	/**图片验证码**/
	private String captcha;
	/**会话ID**/
	private String sessionId;
	
	/**************自定义字段****************/
	/**自定义对象**/
	private JSONObject json;
	
	public MemberRegisterDto() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@NotNull
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getMobileCode() {
		return mobileCode;
	}

	public void setMobileCode(String mobileCode) {
		this.mobileCode = mobileCode;
	}

	public String getEmailCode() {
		return emailCode;
	}

	public void setEmailCode(String emailCode) {
		this.emailCode = emailCode;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	public JSONObject getJson() {
		return json;
	}

	public void setJson(JSONObject json) {
		this.json = json;
	}

	@NotNull
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	/**注册类型 1.无需验证码**/
	public static final Integer TYPE_NONE = 1;
	/**注册类型 2.邮箱验证码**/
	public static final Integer TYPE_EMAIL = 2;
	/**注册类型 3.手机验证码**/
	public static final Integer TYPE_PHONE = 3;
	/**注册类型 4.邮箱，手机验证码**/
	public static final Integer TYPE_ALL = 4;
	
}
