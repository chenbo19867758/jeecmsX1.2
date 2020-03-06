package com.jeecms.common.wechat.bean.request.pay.common;

/**
 * 
 * @Description: 微信H5支付的场景值对象
 * @author: chenming
 * @date:   2018年11月12日 下午4:07:15     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class Info {
	/** 场景类型*/
	private String type;
	/** WAP网站URL地址*/
	private String wapUrl;
	/** WAP网站名*/
	private String wapName;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getWapUrl() {
		return wapUrl;
	}
	public void setWapUrl(String wapUrl) {
		this.wapUrl = wapUrl;
	}
	public String getWapName() {
		return wapName;
	}
	public void setWapName(String wapName) {
		this.wapName = wapName;
	}
	public Info() {
		super();
	}
	public Info(String type, String wapUrl, String wapName) {
		super();
		this.type = type;
		this.wapUrl = wapUrl;
		this.wapName = wapName;
	}
	
}
