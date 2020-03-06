package com.jeecms.common.wechat.bean.request.applet;

/**
 * 
 * @Description: 解除绑定小程序的体验者---request
 * @author: chenming
 * @date:   2018年10月31日 下午3:03:04     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class UnbindTesterRequest {
	/** 微信号*/
	private String wechatid;
	/** 人员对应的唯一字符串*/
	private String userstr;
	
	public String getWechatid() {
		return wechatid;
	}
	
	public void setWechatid(String wechatid) {
		this.wechatid = wechatid;
	}
	
	public String getUserstr() {
		return userstr;
	}
	
	public void setUserstr(String userstr) {
		this.userstr = userstr;
	}
	
	public UnbindTesterRequest() {
		super();
	}
	
	public UnbindTesterRequest(String wechatid) {
		super();
		this.wechatid = wechatid;
	}
	
}
