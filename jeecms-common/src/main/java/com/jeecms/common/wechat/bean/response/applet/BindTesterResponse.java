package com.jeecms.common.wechat.bean.response.applet;

/**
 * 
 * @Description: 
 * @author: chenming
 * @date:   2018年10月31日 下午2:28:58     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class BindTesterResponse extends BaseAppletResponse{
	/** 人员对应的唯一字符串*/
	private String userstr;

	public String getUserstr() {
		return userstr;
	}

	public void setUserstr(String userstr) {
		this.userstr = userstr;
	}
	
}
