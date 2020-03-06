package com.jeecms.common.wechat.bean.response.applet;

/**
 * 
 * @Description: 获取某个指定版本的审核状态-response
 * @author: chenming
 * @date:   2019年3月12日 下午4:19:49     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class GetAudistatusResponse extends BaseAppletResponse{
	/** 审核状态，其中0为审核成功，1为审核失败，2为审核中，3已撤回*/
	private Integer status;
	/** 当status=1，审核被拒绝时，返回的拒绝原因*/
	private String reason;
	/** 当status=1，审核被拒绝时，会返回审核失败的小程序截图示例。 xxx丨yyy丨zzz是media_id可通过获取永久素材接口 拉取截图内容）*/
	private String screenshot;
	
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public String getScreenshot() {
		return screenshot;
	}
	
	public void setScreenshot(String screenshot) {
		this.screenshot = screenshot;
	}
	
}
