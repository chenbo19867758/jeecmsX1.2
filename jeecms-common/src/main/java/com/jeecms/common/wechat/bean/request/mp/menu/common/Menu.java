package com.jeecms.common.wechat.bean.request.mp.menu.common;

import java.util.List;

/**
 * 
 * @Description: 整个菜单对象的封装
 * @author: chenming
 * @date:   2018年8月8日 下午2:28:13     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class Menu {
	
	/**
	 * 一级菜单数组，个数应为1~3个
	 */
	private List<ComplexButton> button;

	public List<ComplexButton> getButton() {
		return button;
	}

	public void setButton(List<ComplexButton> button) {
		this.button = button;
	}

	public Menu(List<ComplexButton> button) {
		super();
		this.button = button;
	}

	public Menu() {
		super();
	}
	
}
