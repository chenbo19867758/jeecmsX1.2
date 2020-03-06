package com.jeecms.member.domain.vo;

import java.io.Serializable;

/**
 * @Description:会员信息(提供前台模板获取使用，过滤掉关键信息，如密码、支付密码)
 * @author: qqwang
 * @date: 2018年6月6日 下午7:13:50
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class MemberVo implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 会员id */
	private Integer id;
	/** 姓名 */
	private String name;
	/** 会员账户 */
	private String username;
	/** 昵称 */
	private String nickname;
	/** 邮箱 */
	private String email;
	/** 电话 */
	private String phone;
	/** 性别 1 男 2 女 3 保密 */
	private Integer gender;
	/** 生日 */
	private String birthday;
	/** 是否启用 false-否 true-是 */
	private Boolean enabled;
	/** 会员头像url */
	private String headImgUrl;
	/** 头像图片资源id */
	private Integer headImgId;
	/** 成长值 */
	private Integer exp;
	/** 会员等级名称 */
	private String levelName;
	/** 会员等级图片 */
	private String levelImageUrl;
	/** 是否管理员 */
	private Boolean admin;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Integer getHeadImgId() {
		return headImgId;
	}

	public void setHeadImgId(Integer headImgId) {
		this.headImgId = headImgId;
	}

	public Integer getExp() {
		return exp;
	}

	public void setExp(Integer exp) {
		this.exp = exp;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getLevelImageUrl() {
		return levelImageUrl;
	}

	public void setLevelImageUrl(String levelImageUrl) {
		this.levelImageUrl = levelImageUrl;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}
}
