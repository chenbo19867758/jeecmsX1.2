package com.jeecms.common.wechat.bean.request.mp.menu.common;

/**
 * 
 * @Description: 所有的菜单的所有通用属性
 * @author: chenming
 * @date:   2018年8月8日 下午2:28:43     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class Button {
	
	/**
	 * 所有的一级菜单、二级菜单都共有一个相同的属性，那就是name
	 */
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
		
}
