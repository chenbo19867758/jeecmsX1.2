package com.jeecms.common.wechat.bean.response.mp.menu;

import com.jeecms.common.wechat.bean.request.mp.menu.common.GetConditionalmenu;
import com.jeecms.common.wechat.bean.request.mp.menu.common.GetMenu;
import com.jeecms.common.wechat.bean.response.mp.BaseResponse;

/**
 * 
 * TODO
 * @author: tom
 * @date:   2019年3月8日 下午4:42:49
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class GetConditionalResponse extends BaseResponse{
	
	private GetMenu menu;
	
	private GetConditionalmenu conditionalmenu;

	public GetMenu getMenu() {
		return menu;
	}

	public void setMenu(GetMenu menu) {
		this.menu = menu;
	}

	public GetConditionalmenu getConditionalmenu() {
		return conditionalmenu;
	}

	public void setConditionalmenu(GetConditionalmenu conditionalmenu) {
		this.conditionalmenu = conditionalmenu;
	}
	
}
