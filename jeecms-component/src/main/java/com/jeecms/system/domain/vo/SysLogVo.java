/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.jeecms.system.domain.SysLog;

import java.util.Date;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/8/15 14:21
 */

public class SysLogVo {
	@Excel(name = "id")
	private Integer id;
	/**
	 * 日志分类（1-系统日志）
	 */
	@Excel(name = "logCategory")
	private Integer logCategory;
	/**
	 * 日志类别(1-信息   2-警告)
	 */
	@Excel(name = "logType")
	private Integer logType;
	/**
	 * 事件类型（1-系统事件  2-业务事件）
	 */
	@Excel(name = "eventType")
	private Integer eventType;
	/**
	 * 日志级别（1-高   2-中  3-低）
	 */
	@Excel(name = "logLevel")
	private Integer logLevel;
	/**
	 * 操作类型（1-查询  2-新增  3-修改   4-删除  5-导出  6-导入 7-上传 8-下载）
	 */
	@Excel(name = "operateType")
	private Integer operateType;
	/**
	 * 事件子类型
	 */
	@Excel(name = "subEventType")
	private String subEventType;
	/**
	 * 操作用户名
	 */
	@Excel(name = "username")
	private String username;
	/**
	 * 客户端请求IP地址
	 */
	@Excel(name = "clientIp")
	private String clientIp;
	/**
	 * 日志请求地址
	 */
	@Excel(name = "uri")
	private String uri;
	/**
	 * 请求方式method,post,get等
	 */
	@Excel(name = "method")
	private String method;
	/**
	 * 请求参数内容,json
	 */
	@Excel(name = "paramData")
	private String paramData;
	/**
	 * 请求接口唯一session标识'
	 */
	@Excel(name = "sessionId")
	private String sessionId;
	/**
	 * 接口返回时间
	 */
	@Excel(name = "returmTime")
	private String returmTime;
	/**
	 * 接口返回数据json
	 */
	@Excel(name = "returnData")
	private String returnData;
	/**
	 * 请求时httpStatusCode代码，如：200,400,404等
	 */
	@Excel(name = "httpStatusCode")
	private String httpStatusCode;
	/**
	 * 请求耗时（毫秒单位）
	 */
	@Excel(name = "timeConsuming")
	private Integer timeConsuming;
	/**
	 * 请求结果(1-成功   2-失败)
	 */
	@Excel(name = "requestResult")
	private Integer requestResult;
	/**
	 * 浏览器
	 */
	@Excel(name = "browser")
	private String browser;
	/**
	 * 操作系统
	 */
	@Excel(name = "os")
	private String os;
	/**
	 * 用户代理
	 */
	@Excel(name = "userAgent")
	private String userAgent;
	/**
	 * 备注
	 */
	@Excel(name = "remark")
	private String remark;
	/**
	 * 创建时间
	 */
	@Excel(name = "createTime")
	private Date createTime;
	/**
	 * 创建人
	 */
	@Excel(name = "createUser")
	private String createUser;
	/**
	 * 修改时间
	 */
	@Excel(name = "updateTime")
	private Date updateTime;
	/**
	 * 修改人
	 */
	@Excel(name = "createTime")
	private String updateUser;
	/**
	 * 删除标识
	 */
	@Excel(name = "hasDeleted")
	private Boolean hasDeleted;

	public SysLogVo() {
		super();
	}

	public SysLogVo(SysLog log) {
		this.id = log.getId();
		this.logCategory = log.getLogCategory();
		this.logType = log.getLogType();
		this.eventType = log.getEventType();
		this.logLevel = log.getLogLevel();
		this.operateType = log.getOperateType();
		this.subEventType = log.getSubEventType();
		this.username = log.getUsername();
		this.clientIp = log.getClientIp();
		this.uri = log.getUri();
		this.method = log.getMethod();
		this.paramData = log.getParamData();
		this.sessionId = log.getSessionId();
		this.returmTime = log.getReturmTime();
		this.returnData = log.getReturnData();
		this.httpStatusCode = log.getHttpStatusCode();
		this.timeConsuming = log.getTimeConsuming();
		this.requestResult = log.getRequestResult();
		this.browser = log.getBrowser();
		this.os = log.getOs();
		this.userAgent = log.getUserAgent();
		this.remark = log.getRemark();
		this.createTime = log.getCreateTime();
		this.createUser = log.getCreateUser();
		this.updateTime = log.getUpdateTime();
		this.updateUser = log.getUpdateUser();
		this.hasDeleted = log.getHasDeleted();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLogCategory() {
		return logCategory;
	}

	public void setLogCategory(Integer logCategory) {
		this.logCategory = logCategory;
	}

	public Integer getLogType() {
		return logType;
	}

	public void setLogType(Integer logType) {
		this.logType = logType;
	}

	public Integer getEventType() {
		return eventType;
	}

	public void setEventType(Integer eventType) {
		this.eventType = eventType;
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

	public String getSubEventType() {
		return subEventType;
	}

	public void setSubEventType(String subEventType) {
		this.subEventType = subEventType;
	}

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

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getParamData() {
		if (paramData != null && paramData.length() > 32767) {
			paramData = paramData.substring(0, 32766);
		}
		return paramData;
	}

	public void setParamData(String paramData) {
		this.paramData = paramData;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getReturmTime() {
		return returmTime;
	}

	public void setReturmTime(String returmTime) {
		this.returmTime = returmTime;
	}

	public String getReturnData() {
		if (returnData != null && returnData.length() > 32767) {
			returnData = returnData.substring(0, 32766);
		}
		return returnData;
	}

	public void setReturnData(String returnData) {
		this.returnData = returnData;
	}

	public String getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setHttpStatusCode(String httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	public Integer getTimeConsuming() {
		return timeConsuming;
	}

	public void setTimeConsuming(Integer timeConsuming) {
		this.timeConsuming = timeConsuming;
	}

	public Integer getRequestResult() {
		return requestResult;
	}

	public void setRequestResult(Integer requestResult) {
		this.requestResult = requestResult;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Boolean getHasDeleted() {
		return hasDeleted;
	}

	public void setHasDeleted(Boolean hasDeleted) {
		this.hasDeleted = hasDeleted;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof SysLogVo)) {
			return false;
		}

		SysLogVo sysLogVo = (SysLogVo) o;

		if (getId() != null ? !getId().equals(sysLogVo.getId()) :
				sysLogVo.getId() != null) {
			return false;
		}
		if (getLogCategory() != null ? !getLogCategory().equals(sysLogVo.getLogCategory()) :
				sysLogVo.getLogCategory() != null) {
			return false;
		}
		if (getLogType() != null ? !getLogType().equals(sysLogVo.getLogType()) :
				sysLogVo.getLogType() != null) {
			return false;
		}
		if (getEventType() != null ? !getEventType().equals(sysLogVo.getEventType()) :
				sysLogVo.getEventType() != null) {
			return false;
		}
		if (getLogLevel() != null ? !getLogLevel().equals(sysLogVo.getLogLevel()) :
				sysLogVo.getLogLevel() != null) {
			return false;
		}
		if (getOperateType() != null ? !getOperateType().equals(sysLogVo.getOperateType()) :
				sysLogVo.getOperateType() != null) {
			return false;
		}
		if (getSubEventType() != null ? !getSubEventType().equals(sysLogVo.getSubEventType()) :
				sysLogVo.getSubEventType() != null) {
			return false;
		}
		if (getUsername() != null ? !getUsername().equals(sysLogVo.getUsername()) :
				sysLogVo.getUsername() != null) {
			return false;
		}
		if (getClientIp() != null ? !getClientIp().equals(sysLogVo.getClientIp()) :
				sysLogVo.getClientIp() != null) {
			return false;
		}
		if (getUri() != null ? !getUri().equals(sysLogVo.getUri()) :
				sysLogVo.getUri() != null) {
			return false;
		}
		if (getMethod() != null ? !getMethod().equals(sysLogVo.getMethod()) :
				sysLogVo.getMethod() != null) {
			return false;
		}
		if (getParamData() != null ? !getParamData().equals(sysLogVo.getParamData()) :
				sysLogVo.getParamData() != null) {
			return false;
		}
		if (getSessionId() != null ? !getSessionId().equals(sysLogVo.getSessionId()) :
				sysLogVo.getSessionId() != null) {
			return false;
		}
		if (getReturmTime() != null ? !getReturmTime().equals(sysLogVo.getReturmTime()) :
				sysLogVo.getReturmTime() != null) {
			return false;
		}
		if (getReturnData() != null ? !getReturnData().equals(sysLogVo.getReturnData()) :
				sysLogVo.getReturnData() != null) {
			return false;
		}
		if (getHttpStatusCode() != null ? !getHttpStatusCode().equals(sysLogVo.getHttpStatusCode()) :
				sysLogVo.getHttpStatusCode() != null) {
			return false;
		}
		if (getTimeConsuming() != null ? !getTimeConsuming().equals(sysLogVo.getTimeConsuming()) :
				sysLogVo.getTimeConsuming() != null) {
			return false;
		}
		if (getRequestResult() != null ? !getRequestResult().equals(sysLogVo.getRequestResult()) :
				sysLogVo.getRequestResult() != null) {
			return false;
		}
		if (getBrowser() != null ? !getBrowser().equals(sysLogVo.getBrowser()) :
				sysLogVo.getBrowser() != null) {
			return false;
		}
		if (getOs() != null ? !getOs().equals(sysLogVo.getOs()) :
				sysLogVo.getOs() != null) {
			return false;
		}
		if (getUserAgent() != null ? !getUserAgent().equals(sysLogVo.getUserAgent()) :
				sysLogVo.getUserAgent() != null) {
			return false;
		}
		if (getRemark() != null ? !getRemark().equals(sysLogVo.getRemark()) :
				sysLogVo.getRemark() != null) {
			return false;
		}
		if (getCreateTime() != null ? !getCreateTime().equals(sysLogVo.getCreateTime()) :
				sysLogVo.getCreateTime() != null) {
			return false;
		}
		if (getCreateUser() != null ? !getCreateUser().equals(sysLogVo.getCreateUser()) :
				sysLogVo.getCreateUser() != null) {
			return false;
		}
		if (getUpdateTime() != null ? !getUpdateTime().equals(sysLogVo.getUpdateTime()) :
				sysLogVo.getUpdateTime() != null) {
			return false;
		}
		if (getUpdateUser() != null ? !getUpdateUser().equals(sysLogVo.getUpdateUser()) :
				sysLogVo.getUpdateUser() != null) {
			return false;
		}
		return getHasDeleted() != null ? getHasDeleted().equals(sysLogVo.getHasDeleted()) : sysLogVo.getHasDeleted() == null;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + (getLogCategory() != null ? getLogCategory().hashCode() : 0);
		result = 31 * result + (getLogType() != null ? getLogType().hashCode() : 0);
		result = 31 * result + (getEventType() != null ? getEventType().hashCode() : 0);
		result = 31 * result + (getLogLevel() != null ? getLogLevel().hashCode() : 0);
		result = 31 * result + (getOperateType() != null ? getOperateType().hashCode() : 0);
		result = 31 * result + (getSubEventType() != null ? getSubEventType().hashCode() : 0);
		result = 31 * result + (getUsername() != null ? getUsername().hashCode() : 0);
		result = 31 * result + (getClientIp() != null ? getClientIp().hashCode() : 0);
		result = 31 * result + (getUri() != null ? getUri().hashCode() : 0);
		result = 31 * result + (getMethod() != null ? getMethod().hashCode() : 0);
		result = 31 * result + (getParamData() != null ? getParamData().hashCode() : 0);
		result = 31 * result + (getSessionId() != null ? getSessionId().hashCode() : 0);
		result = 31 * result + (getReturmTime() != null ? getReturmTime().hashCode() : 0);
		result = 31 * result + (getReturnData() != null ? getReturnData().hashCode() : 0);
		result = 31 * result + (getHttpStatusCode() != null ? getHttpStatusCode().hashCode() : 0);
		result = 31 * result + (getTimeConsuming() != null ? getTimeConsuming().hashCode() : 0);
		result = 31 * result + (getRequestResult() != null ? getRequestResult().hashCode() : 0);
		result = 31 * result + (getBrowser() != null ? getBrowser().hashCode() : 0);
		result = 31 * result + (getOs() != null ? getOs().hashCode() : 0);
		result = 31 * result + (getUserAgent() != null ? getUserAgent().hashCode() : 0);
		result = 31 * result + (getRemark() != null ? getRemark().hashCode() : 0);
		result = 31 * result + (getCreateTime() != null ? getCreateTime().hashCode() : 0);
		result = 31 * result + (getCreateUser() != null ? getCreateUser().hashCode() : 0);
		result = 31 * result + (getUpdateTime() != null ? getUpdateTime().hashCode() : 0);
		result = 31 * result + (getUpdateUser() != null ? getUpdateUser().hashCode() : 0);
		result = 31 * result + (getHasDeleted() != null ? getHasDeleted().hashCode() : 0);
		return result;
	}
}
