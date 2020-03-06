package com.jeecms.common.wechat.bean.request.mp.menu;

import java.util.List;

import com.jeecms.common.wechat.bean.request.mp.menu.common.ComplexButton;
import com.jeecms.common.wechat.bean.request.mp.menu.common.Matchrule;
import com.jeecms.common.wechat.bean.request.mp.menu.common.Menu;

/**
 * 
 * @Description: 创建个性化菜单：request
 * @author: chenming
 * @date:   2018年8月8日 下午7:11:18     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class AddConditionalRequest extends Menu{
	
	/**
	 * 个性化菜单补充类
	 */
	private Matchrule matchrule;

	public AddConditionalRequest(List<ComplexButton> button, Matchrule matchrule) {
		super(button);
		this.matchrule = matchrule;
	}

	public AddConditionalRequest() {
		super();
	}

	public Matchrule getMatchrule() {
		return matchrule;
	}

	public void setMatchrule(Matchrule matchrule) {
		this.matchrule = matchrule;
	}
	
}
