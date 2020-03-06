package com.jeecms.common.wechat.bean.request.applet;

/**
 * 
 * @Description: 修改小程序线上代码的可见状态---request
 * @author: chenming
 * @date:   2018年10月31日 下午8:23:13     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class ChangeVisitstatusRequest {
	/** 设置可访问状态，发布后默认可访问，close为不可见，open为可见*/
	private String action;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public ChangeVisitstatusRequest(String action) {
		super();
		this.action = action;
	}

	public ChangeVisitstatusRequest() {
		super();
	}
	
}
