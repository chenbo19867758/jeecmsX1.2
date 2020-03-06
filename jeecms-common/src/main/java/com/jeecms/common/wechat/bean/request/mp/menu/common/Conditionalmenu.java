package com.jeecms.common.wechat.bean.request.mp.menu.common;

/**
 * 
 * TODO
 * @author: tom
 * @date:   2019年3月8日 下午4:38:48
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class Conditionalmenu extends Menu{

	
	/**
	 * 个性化菜单补充类
	 */
	private Matchrule matchrule;
	
	private String menuid;

	public Matchrule getMatchrule() {
		return matchrule;
	}

	public void setMatchrule(Matchrule matchrule) {
		this.matchrule = matchrule;
	}

	public String getMenuid() {
		return menuid;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}
	
	
}
