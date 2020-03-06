/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.collect.domain.vo;

import com.alibaba.fastjson.JSONArray;

/**
 * 采集内容列表
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/20 13:53
 */

public class CollectContentVo {

	private JSONArray title;
	private JSONArray list;

	public CollectContentVo() {
		super();
	}

	/**
	 * 构造器
	 *
	 * @param title 表头数据
	 * @param list  列表数据
	 */
	public CollectContentVo(JSONArray title, JSONArray list) {
		this.title = title;
		this.list = list;
	}

	public JSONArray getTitle() {
		return title;
	}

	public void setTitle(JSONArray title) {
		this.title = title;
	}

	public JSONArray getList() {
		return list;
	}

	public void setList(JSONArray list) {
		this.list = list;
	}
}
