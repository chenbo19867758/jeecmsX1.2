package com.jeecms.common.wechat.bean.request.mp.menu;

/**
 * 
 * @Description: 删除个性化菜单
 * @author: chenming
 * @date:   2018年8月8日 下午7:41:43     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class DelConditionalRequest {
	
	/**
	 * menuid为菜单id，可以通过自定义菜单查询接口获取
	 */
	private String menuid;

	public String getMenuid() {
		return menuid;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}

	public DelConditionalRequest(String menuid) {
		super();
		this.menuid = menuid;
	}

	public DelConditionalRequest() {
		super();
	}
	
	
}
