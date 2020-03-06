package com.jeecms.common.wechat.bean.request.applet.common;

/**
 * 
 * @Description: 小程序提交代码时，用于小程序区分的格外属性
 * @author: chenming
 * @date:   2018年11月12日 下午3:35:39     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class Ext {
	/** 小程序的appId*/
	private String appId;
	
	public String getAppId() {
		return appId;
	}
	
	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Ext(String appId, Integer storeId, Integer owerType) {
		super();
		this.appId = appId;
	}

	public Ext() {
		super();
	}
	
}
