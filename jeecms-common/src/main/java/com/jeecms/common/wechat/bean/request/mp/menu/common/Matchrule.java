package com.jeecms.common.wechat.bean.request.mp.menu.common;

/**
 * 
 * @Description: 个性化菜单的补充接口
 * @author: chenming
 * @date:   2018年8月8日 下午7:00:03     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class Matchrule {
	// 用户标签的Id
	/** 用户标签的Id */
	private String tagId;
	/** 性别男(1)，女(1)，不填则不做匹配 */
	private String sex;
	/** 国家信息，是用户在微信中设置的地区 */
	private String country;
	/** 省份信息，是用户在微信中设置的地区 */
	private String province;
	/** 城市信息，是用户在微信中设置的地区 */
	private String city;
	/** 客户端版本，当前只具体到系统型号: IOS(1)，Android(2)，Others(3)不填则不做匹配 */
	private String clientPlatformType;
	/** 语音信息，是用户在微信中设置的语言 */
	private String language;
	
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
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	
	public String getTagId() {
		return tagId;
	}
	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	public String getClientPlatformType() {
		return clientPlatformType;
	}
	public void setClientPlatformType(String clientPlatformType) {
		this.clientPlatformType = clientPlatformType;
	}
	
	public Matchrule(String tagId, String sex, String country, String province, String city,
			String clientPlatformType, String language) {
		super();
		this.tagId = tagId;
		this.sex = sex;
		this.country = country;
		this.province = province;
		this.city = city;
		this.clientPlatformType = clientPlatformType;
		this.language = language;
	}
	public Matchrule() {
		super();
	}
	
	
}
