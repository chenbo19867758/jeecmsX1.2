/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.util.Date;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/8/13 18:23
 */

public class SysLogExportVo {

	@Excel(name = "告警模块", width = 25, isImportField = "true_st")
	private String subEventType;
	@Excel(name = "告警类型", replace = {"信息_1", "警告_2"}, isImportField = "true_st")
	private Integer logType;
	@Excel(name = "告警时间", width = 35, databaseFormat = "yyyy-MM-dd HH:mm:ss", isImportField = "true_st")
	private Date createTIme;
	@Excel(name = "告警级别", replace = {"高_1", "中_2", "低_3"}, isImportField = "true_st")
	private Integer logLevel;

	public SysLogExportVo() {
		super();
	}

	public SysLogExportVo(String subEventType, Integer logType, Date createTIme, Integer logLevel) {
		this.subEventType = subEventType;
		this.logType = logType;
		this.createTIme = createTIme;
		this.logLevel = logLevel;
	}

	public String getSubEventType() {
		return subEventType;
	}

	public void setSubEventType(String subEventType) {
		this.subEventType = subEventType;
	}

	public Integer getLogType() {
		return logType;
	}

	public void setLogType(Integer logType) {
		this.logType = logType;
	}

	public Date getCreateTIme() {
		return createTIme;
	}

	public void setCreateTIme(Date createTIme) {
		this.createTIme = createTIme;
	}

	public Integer getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(Integer logLevel) {
		this.logLevel = logLevel;
	}
}
