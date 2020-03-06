/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.vo;

import java.util.Date;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/9/2 16:39
 */

public class SysLogSelectVo {

	private Integer requestResult;

	private Integer eventType;

	private Integer operateType;

	private String username;

	private Date createTime;

	public SysLogSelectVo() {
		super();
	}

	public SysLogSelectVo(Integer requestResult, Integer eventType, Integer operateType, String username, Date createTime) {
		this.requestResult = requestResult;
		this.eventType = eventType;
		this.operateType = operateType;
		this.username = username;
		this.createTime = createTime;
	}

	public Integer getRequestResult() {
		return requestResult;
	}

	public void setRequestResult(Integer requestResult) {
		this.requestResult = requestResult;
	}

	public Integer getEventType() {
		return eventType;
	}

	public void setEventType(Integer eventType) {
		this.eventType = eventType;
	}

	public Integer getOperateType() {
		return operateType;
	}

	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
