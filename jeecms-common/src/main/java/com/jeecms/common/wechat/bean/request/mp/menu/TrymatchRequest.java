package com.jeecms.common.wechat.bean.request.mp.menu;

/**
 * 
 * @Description: 测试个性化菜单匹配：request
 * @author: chenming
 * @date:   2018年8月8日 下午7:40:12     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class TrymatchRequest {
	
	/**
	 * user_id可以是粉丝的OpenID，也可以是粉丝的微信号
	 */
	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
