/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.dto;

import java.util.Date;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/8/19 16:26
 */

public class SearchLogDto {

	private String username;
	private String clientIp;
	private String subEventType;
	private Integer logLevel;
	private Integer operateType;
	private Integer requestResult;
	private Integer logCategory;
	private Date beginDate;
	private Date endDate;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getSubEventType() {
		return subEventType;
	}

	public void setSubEventType(String subEventType) {
		this.subEventType = subEventType;
	}

	public Integer getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(Integer logLevel) {
		this.logLevel = logLevel;
	}

	public Integer getOperateType() {
		return operateType;
	}

	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}

	public Integer getRequestResult() {
		return requestResult;
	}

	public void setRequestResult(Integer requestResult) {
		this.requestResult = requestResult;
	}

	public Integer getLogCategory() {
		return logCategory;
	}

	public void setLogCategory(Integer logCategory) {
		this.logCategory = logCategory;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
