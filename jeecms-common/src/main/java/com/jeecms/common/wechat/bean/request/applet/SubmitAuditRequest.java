package com.jeecms.common.wechat.bean.request.applet;

import java.util.List;

import com.jeecms.common.wechat.bean.request.applet.common.AuditList;

/**
 * 
 * @Description: 将第三方提交的代码包提交审核--request
 * @author: chenming
 * @date:   2018年10月31日 下午6:53:24     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class SubmitAuditRequest {
	
	/** 提交审核项的一个列表（至少填写1项，至多填写5项）*/
	private List<AuditList> itemList;

	public List<AuditList> getItemList() {
		return itemList;
	}

	public void setItemList(List<AuditList> itemList) {
		this.itemList = itemList;
	}

	public SubmitAuditRequest(List<AuditList> itemList) {
		super();
		this.itemList = itemList;
	}

	public SubmitAuditRequest() {
		super();
	}
	
}
