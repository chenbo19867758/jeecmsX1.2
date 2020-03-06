package com.jeecms.common.wechat.bean.response.applet;

import java.util.List;

import com.jeecms.common.wechat.bean.response.mp.BaseResponse;

/**
 * 
 * @Description: 获取小程序的第三方提交代码的页面配置--response返回
 * @author: chenming
 * @date:   2018年11月12日 下午4:38:59     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class GetPageResponse extends BaseResponse{
	/** page_list 页面配置列表*/
	private List<String> pageList;

	public List<String> getPageList() {
		return pageList;
	}

	public void setPageList(List<String> pageList) {
		this.pageList = pageList;
	}
}
