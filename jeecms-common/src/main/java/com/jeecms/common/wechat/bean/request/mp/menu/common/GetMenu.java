package com.jeecms.common.wechat.bean.request.mp.menu.common;

/**
 * 
 * TODO
 * @author: tom
 * @date:   2019年3月8日 下午4:39:21
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class GetMenu {
	
	/**
	 * 一级菜单数组，个数应为1~3个
	 */
	private Button[] button;
	private String menuid;
	
	public Button[] getButton() {
		return button;
	}
	public void setButton(Button[] button) {
		this.button = button;
	}
	public String getMenuid() {
		return menuid;
	}
	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}
	
	
}
