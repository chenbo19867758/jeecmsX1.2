package com.jeecms.system.domain.dto;

import java.io.Serializable;
import java.util.List;

import com.jeecms.system.domain.Area;

/**
 * @author: tom
 * @date:   2019年3月5日 下午4:48:37
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class GlobalConfigDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 名称 */
	private String name;
	/** 标题 */
	private String title;
	/** 描述 */
	private String description;
	/** 关键词 */
	private String keywords;
	/** 客服电话 */
	private String customerServiceMobile;
	/** 客服邮箱 */
	private String customerServiceEmail;
	/** 地址 */
	private String address;
	/** 显示地区(区) */
	private String areaDistrict;
	/** 显示地区(市) */
	private String areaCity;
	/** 搜索关键词数组 */
	private String[] searchKeyWords;
	/** 搜索热词 */
	private String hotWords;
	
	/** logo */
	private String logo;
	/** 注册登录页log */
	private String loginAndRegLogo;
	/** 用户注册协议 */
	private String userRegisterAgreement;
	
	/** 地区数据 */
	private List<Area> area;
	/** 版权信息 */
	private String copyright;

	/** 微信公众号图片 */
	private String wechatQRcodeUrl;
	/** 微信小程序图片 */
	private String miniprogramQRcodeUrl;

	/** qq登录是否开启 */
	private boolean qqOpen;
	/** 微信登录是否开启 */
	private boolean wechatOpen;
	/** 微博登录是否开启 */
	private boolean weiboOpen;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getCustomerServiceMobile() {
		return customerServiceMobile;
	}

	public void setCustomerServiceMobile(String customerServiceMobile) {
		this.customerServiceMobile = customerServiceMobile;
	}

	public String getCustomerServiceEmail() {
		return customerServiceEmail;
	}

	public void setCustomerServiceEmail(String customerServiceEmail) {
		this.customerServiceEmail = customerServiceEmail;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String[] getSearchKeyWords() {
		return searchKeyWords;
	}

	public void setSearchKeyWords(String[] searchKeyWords) {
		this.searchKeyWords = searchKeyWords;
	}

	public String getHotWords() {
		return hotWords;
	}

	public void setHotWords(String hotWords) {
		this.hotWords = hotWords;
	}
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getLoginAndRegLogo() {
		return loginAndRegLogo;
	}

	public void setLoginAndRegLogo(String loginAndRegLogo) {
		this.loginAndRegLogo = loginAndRegLogo;
	}

	public String getUserRegisterAgreement() {
		return userRegisterAgreement;
	}

	public void setUserRegisterAgreement(String userRegisterAgreement) {
		this.userRegisterAgreement = userRegisterAgreement;
	}


	public List<Area> getArea() {
		return area;
	}

	public void setArea(List<Area> area) {
		this.area = area;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public String getWechatQRcodeUrl() {
		return wechatQRcodeUrl;
	}

	public void setWechatQRcodeUrl(String wechatQRcodeUrl) {
		this.wechatQRcodeUrl = wechatQRcodeUrl;
	}

	public String getMiniprogramQRcodeUrl() {
		return miniprogramQRcodeUrl;
	}

	public void setMiniprogramQRcodeUrl(String miniprogramQRcodeUrl) {
		this.miniprogramQRcodeUrl = miniprogramQRcodeUrl;
	}

	public String getAreaDistrict() {
		return areaDistrict;
	}

	public void setAreaDistrict(String areaDistrict) {
		this.areaDistrict = areaDistrict;
	}

	public String getAreaCity() {
		return areaCity;
	}

	public void setAreaCity(String areaCity) {
		this.areaCity = areaCity;
	}

	public boolean isQqOpen() {
		return qqOpen;
	}

	public void setQqOpen(boolean qqOpen) {
		this.qqOpen = qqOpen;
	}

	public boolean isWechatOpen() {
		return wechatOpen;
	}

	public void setWechatOpen(boolean wechatOpen) {
		this.wechatOpen = wechatOpen;
	}

	public boolean isWeiboOpen() {
		return weiboOpen;
	}

	public void setWeiboOpen(boolean weiboOpen) {
		this.weiboOpen = weiboOpen;
	}

	@Override
	public String toString() {
		return "GlobalConfigDTO.java";
	}

}