package com.jeecms.member.domain.dto;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

/**
 * 
 * @Description: 会员详情修改实体类
 * @author: chenming
 * @date:   2018年11月21日 下午4:17:30     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class MemberDetails {
	/** 昵称 */
	private String nickname;
	/** 性别 */
	private Integer gender;
	/** 生日 */
	private Date birthday;
	/** 头像图片资源id */
	private Integer resourceDataId;
	
	@Length(max = 150)
	public String getNickname() {
		return nickname;
	}
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public Integer getGender() {
		return gender;
	}
	
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	
	public Date getBirthday() {
		return birthday;
	}
	
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	public Integer getResourceDataId() {
		return resourceDataId;
	}
	
	public void setResourceDataId(Integer resourceDataId) {
		this.resourceDataId = resourceDataId;
	}
	
}
