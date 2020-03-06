package com.jeecms.common.wechat.bean.request.mp.menu.common;

/**
 * 
 * TODO
 * @author: tom
 * @date:   2019年3月8日 下午4:39:11
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class GetMatchrule {
	
	private Integer groupId;
	
	private Integer sex;
	
	private String country;
	
	private String province;
	
	private String city;
	
	private Integer clientPlatformType;

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getClientPlatformType() {
		return clientPlatformType;
	}

	public void setClientPlatformType(Integer clientPlatformType) {
		this.clientPlatformType = clientPlatformType;
	}
	
	
	
}
