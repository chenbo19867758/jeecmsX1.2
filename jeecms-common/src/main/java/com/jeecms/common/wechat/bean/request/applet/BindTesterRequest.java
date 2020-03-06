package com.jeecms.common.wechat.bean.request.applet;

/**
 * 
 * @Description: 绑定微信用户为小程序体验者---request
 * @author: chenming
 * @date:   2018年10月31日 下午2:26:38     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class BindTesterRequest{
	/** 微信号*/
	private String wechatid;

	public String getWechatid() {
		return wechatid;
	}

	public void setWechatid(String wechatid) {
		this.wechatid = wechatid;
	}

	public BindTesterRequest(String wechatid) {
		super();
		this.wechatid = wechatid;
	}

	public BindTesterRequest() {
		super();
	}
	
}
