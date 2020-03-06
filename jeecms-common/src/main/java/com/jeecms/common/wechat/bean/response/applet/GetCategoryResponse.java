package com.jeecms.common.wechat.bean.response.applet;

import java.util.List;

import com.jeecms.common.wechat.bean.response.applet.common.Category;

/**
 * 
 * @Description: 获取授权小程序帐号的可选类目--response返回
 * @author: chenming
 * @date:   2018年10月30日 下午4:50:02     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class GetCategoryResponse extends BaseAppletResponse{
	/**
	 * 可填选的类目列表
	 */
	private List<Category> categoryList;

	public List<Category> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}
}
