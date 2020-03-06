package com.jeecms.common.wechat.bean.response.applet;

import com.jeecms.common.wechat.bean.response.mp.BaseResponse;

/**
 * 
 * @Description: 将第三方提交的代码包提交审核--response
 * @author: chenming
 * @date:   2018年10月31日 下午6:57:14     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class SubmitAuditResponse extends BaseResponse{
	
	/** 审核编号*/
	private Integer auditid;

	public Integer getAuditid() {
		return auditid;
	}

	public void setAuditid(Integer auditid) {
		this.auditid = auditid;
	}
}
