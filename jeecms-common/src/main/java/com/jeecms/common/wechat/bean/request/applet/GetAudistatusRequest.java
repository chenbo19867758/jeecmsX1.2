package com.jeecms.common.wechat.bean.request.applet;

/**
 * 
 * @Description: 获取某个指定版本的审核状态-request
 * @author: chenming
 * @date:   2019年3月12日 下午4:19:49     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class GetAudistatusRequest {
	/** 提交审核时获得的审核id*/
	private Integer auditid;

	public Integer getAuditid() {
		return auditid;
	}

	public void setAuditid(Integer auditid) {
		this.auditid = auditid;
	}

	public GetAudistatusRequest(Integer auditid) {
		super();
		this.auditid = auditid;
	}
	
	
}
