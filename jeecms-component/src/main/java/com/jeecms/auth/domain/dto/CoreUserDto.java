/**
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.auth.domain.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

/**
 * 管理员DTO
 * 
 * @author: ljw
 * @date: 2019年4月10日 上午11:51:30
 */
public class CoreUserDto {

	/** 会员ID **/
	private Integer id;
	/** 用户名 **/
	private String username;
	/** 密码 **/
	private String oldpsw;
	/** 密码 **/
	private String psw;
	/** 真实姓名 **/
	private String realname;
	/** 座机号 */
	private String telephone;
	/** 用户手机号 **/
	private String usePhone;
	/** 邮箱 **/
	private String email;
	/** 是否禁用 **/
	private Boolean enabled;
	/** 所属组织 **/
	private Integer orgid;
	/** 所属角色 **/
	private List<Integer> roleid = new ArrayList<Integer>();
	/** 是否是管理员 **/
	private Boolean isAdmin;
	/** 会员组ID **/
	private Integer groupId;
	/** 积分 **/
	private Integer integral;
	/** 性别 1 男 2 女 3 保密 */
	private Integer gender;
	/** 备注 */
	private String remark;
	/** 来源站点 **/
	private Integer siteId;
	/** 人员密级 **/
	private Integer userSecretId;
	/** 会员头像资源ID **/
	private Integer headId;

	CoreUserDto() {
	}

	@NotNull(groups = One.class)
	@Length(min = 0, max = 150)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@NotNull(groups = { One.class, Three.class})
	@Length(min = 0, max = 150)
	public String getPsw() {
		return psw;
	}

	public void setPsw(String psw) {
		this.psw = psw;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getUsePhone() {
		return usePhone;
	}

	public void setUsePhone(String usePhone) {
		this.usePhone = usePhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@NotNull(groups = { One.class, Two.class })
	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Integer getOrgid() {
		return orgid;
	}

	public void setOrgid(Integer orgid) {
		this.orgid = orgid;
	}

	public List<Integer> getRoleid() {
		return roleid;
	}

	public void setRoleid(List<Integer> roleid) {
		this.roleid = roleid;
	}

	@NotNull(groups = Two.class)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public Integer getUserSecretId() {
		return userSecretId;
	}

	public void setUserSecretId(Integer userSecretId) {
		this.userSecretId = userSecretId;
	}

	public Integer getHeadId() {
		return headId;
	}

	public void setHeadId(Integer headId) {
		this.headId = headId;
	}

	@NotNull(groups = Three.class)
	public String getOldpsw() {
		return oldpsw;
	}

	public void setOldpsw(String oldpsw) {
		this.oldpsw = oldpsw;
	}

	public interface One {
	}

	public interface Two {
	}
	
	public interface Three {
	}
}
