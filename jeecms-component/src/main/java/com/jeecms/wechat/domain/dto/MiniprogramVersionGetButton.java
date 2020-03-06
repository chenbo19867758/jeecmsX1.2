package com.jeecms.wechat.domain.dto;

import java.util.Map;

/**
 * 小程序版本获取按钮
 * @author: chenming
 * @date:   2019年6月13日 下午7:59:11
 */
public class MiniprogramVersionGetButton {
	
	private Map<String, Boolean> button;

	public Map<String, Boolean> getButton() {
		return button;
	}

	public void setButton(Map<String, Boolean> button) {
		this.button = button;
	}

	public MiniprogramVersionGetButton(Map<String, Boolean> button) {
		super();
		this.button = button;
	}
	
	
}
