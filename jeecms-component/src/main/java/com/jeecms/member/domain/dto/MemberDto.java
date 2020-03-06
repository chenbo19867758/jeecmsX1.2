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
public class MemberDto {

	/** 会员ID **/
	private Integer id;
	/**来源站点ID**/
	private Integer siteId;
	/**************系统默认字段****************/
	/** 用户名 **/
	private String username;
	/** 电子邮箱 **/
	private String email;
	/** 所属会员组 **/
	private Integer userGroup;
	/** 会员等级 **/
	private Integer userLevel;
	/** 手机号 **/
	private String telephone;
	/** 密码 **/
	private String password;
	/** 积分 **/
	private Integer integral;
	/** 真实姓名 **/
	private String realname;
	/** 会员状态 **/
	private Boolean status;
	/**************自定义字段****************/
	/**自定义对象**/
	private JSONObject json;
	
	public MemberDto() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@NotNull(groups = One.class)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@NotNull(groups = { One.class, Two.class })
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(Integer userGroup) {
		this.userGroup = userGroup;
	}

	public Integer getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(Integer userLevel) {
		this.userLevel = userLevel;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Integer getIntegral() {
		return integral;
	}
	
	/**积分给默认值0**/
	public void setIntegral(Integer integral) {
		if (integral == null) {
			integral = 0;
		}
		this.integral = integral;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public JSONObject getJson() {
		return json;
	}

	public void setJson(JSONObject json) {
		this.json = json;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	@NotNull(groups = { One.class })
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@NotNull(groups = { One.class, Two.class })
	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public interface One {
	}

	public interface Two {
	}
}
