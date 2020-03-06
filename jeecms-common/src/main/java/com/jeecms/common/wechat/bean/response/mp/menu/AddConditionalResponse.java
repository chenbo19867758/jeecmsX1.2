package com.jeecms.common.wechat.bean.response.mp.menu;

import com.jeecms.common.wechat.bean.response.mp.BaseResponse;

/**
 * 
 * @Description: 创建个性化菜单：response
 * @author: chenming
 * @date:   2018年8月8日 下午7:12:16     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class AddConditionalResponse extends BaseResponse{
	
	/**
	 * menuid为菜单id
	 */
	private String menuid;

	public String getMenuid() {
		return menuid;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}
	
	
}
