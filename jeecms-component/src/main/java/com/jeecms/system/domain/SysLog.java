/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.jeecms.common.base.domain.AbstractDomain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * 日志实体类
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-05-29 16:45:54
 */
@Entity
@Table(name = "jc_sys_log")
public class SysLog extends AbstractDomain<Integer> implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 日志分类-系统
	 */
	public static final Integer LOG_CATEGORY_SYSTEM = 1;
	/**
	 * 日志分类-业务
	 */
	public static final Integer LOG_CATEGORY_BUS = 2;
	/**
	 * 日志分类-审计
	 */
	public static final Integer LOG_CATEGORY_AUDIT = 3;
	/**
	 * 日志分类-安全
	 */
	public static final Integer LOG_CATEGORY_SAFE = 4;
	/**
	 * 日志分类-告警
	 */
	public static final Integer LOG_CATEGORY_WARN = 5;

	/**
	 * 日志类别-信息
	 */
	public static final Integer LOG_TYPE_INFO = 1;
	/**
	 * 日志类别-告警
	 */
	public static final Integer LOG_TYPE_WARN = 2;

	/**
	 * 日志事件类型-系统事件
	 */
	public static final Integer LOG_EVENT_TYPE_SYSTEM = 1;
	/**
	 * 日志事件类型-业务事件
	 */
	public static final Integer LOG_EVENT_TYPE_BUS = 2;

	/**
	 * 日志级别-高
	 */
	public static final Integer LOG_LEVEL_HEIGHT = 1;
	/**
	 * 日志级别-中
	 */
	public static final Integer LOG_LEVEL_MIDDLE = 2;
	/**
	 * 日志级别-低
	 */
	public static final Integer LOG_LEVEL_LOW = 3;


	private Integer id;
	/**
	 * 日志分类（1-系统日志）
	 */
	@Excel(name = "日志分类", replace = {"系统_1", "业务_2", "审计_3", "安全_4", "告警_5"},
			suffix = "日志", isImportField = "true_st")
	private Integer logCategory;
	/**
	 * 日志类别(1-信息   2-警告)
	 */
	@Excel(name = "日志类别", replace = {"信息_1", "警告_2"}, isImportField = "true_st")
	private Integer logType;
	/**
	 * 事件类型（1-系统事件  2-业务事件）
	 */
	@Excel(name = "事件类型", replace = {"系统事件_1", "业务事件_2"}, isImportField = "true_st")
	private Integer eventType;
	/**
	 * 日志级别（1-高   2-中  3-低）
	 */
	@Excel(name = "日志级别", replace = {"高_1", "中_2", "低_3"}, isImportField = "true_st")
	private Integer logLevel;
	/**
	 * 操作类型（1-查询  2-新增  3-修改   4-删除  5-导出  6-导入 7-上传 8-下载）
	 */
	@Excel(name = "操作类型", replace = {"查询_1", "新增_2", "修改_3", "删除_4", "导出_5", "导入_6", "上传_7", "下载_8"},
			isImportField = "true_st")
	private Integer operateType;
	/**
	 * 事件子类型
	 */
	@Excel(name = "事件子类型", width = 25, isImportField = "true_st")
	private String subEventType;
	/**
	 * 操作用户名
	 */
	@Excel(name = "用户名", isImportField = "true_st")
	private String username;
	/**
	 * 客户端请求IP地址
	 */
	@Excel(name = "客户端IP", width = 25, isImportField = "true_st")
	private String clientIp;
	/**
	 * 日志请求地址
	 */
	@Excel(name = "请求路径", width = 50, isImportField = "true_st")
	private String uri;
	/**
	 * 请求方式method,post,get等
	 */
	@Excel(name = "请求方式", isImportField = "true_st")
	private String method;
	/**
	 * 请求参数内容,json
	 */
	private String paramData;
	/**
	 * 请求接口唯一session标识'
	 */
	private String sessionId;
	/**
	 * 接口返回时间
	 */
	private String returmTime;
	/**
	 * 接口返回数据json
	 */
	private String returnData;
	/**
	 * 请求时httpStatusCode代码，如：200,400,404等
	 */
	private String httpStatusCode;
	/**
	 * 请求耗时（毫秒单位）
	 */
	@Excel(name = "响应时间（毫秒）", suffix = "ms", isImportField = "true_st")
	private Integer timeConsuming;
	/**
	 * 请求结果(1-成功   2-失败)
	 */
	@Excel(name = "请求结果", replace = {"成功_1", "失败_2"}, isImportField = "true_st")
	private Integer requestResult;
	/**
	 * 创建时间
	 */
	@Excel(name = "操作时间", width = 35, databaseFormat = "yyyy-MM-dd HH:mm:ss", isImportField = "true_st")
	private Date createTime;
	/**
	 * 浏览器
	 */
	private String browser;
	/**
	 * 操作系统
	 */
	private String os;
	/**
	 * 用户代理
	 */
	private String userAgent;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 响应时间（毫秒）
	 */
	private String responseTime;

	public SysLog() {
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_sys_log", pkColumnValue = "jc_sys_log", initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_log")
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "log_category", nullable = true, length = 11)
	public Integer getLogCategory() {
		return logCategory;
	}

	public void setLogCategory(Integer logCategory) {
		this.logCategory = logCategory;
	}

	@Column(name = "log_type", nullable = true, length = 6)
	public Integer getLogType() {
		return logType;
	}

	public void setLogType(Integer logType) {
		this.logType = logType;
	}

	@Column(name = "event_type", nullable = true, length = 6)
	public Integer getEventType() {
		return eventType;
	}

	public void setEventType(Integer eventType) {
		this.eventType = eventType;
	}

	@Column(name = "log_level", nullable = true, length = 6)
	public Integer getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(Integer logLevel) {
		this.logLevel = logLevel;
	}

	@Column(name = "operate_type", nullable = true, length = 6)
	public Integer getOperateType() {
		return operateType;
	}

	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}

	@Column(name = "sub_event_type", nullable = true, length = 255)
	public String getSubEventType() {
		return subEventType;
	}

	public void setSubEventType(String subEventType) {
		this.subEventType = subEventType;
	}

	@Column(name = "username", nullable = true, length = 150)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "client_ip", nullable = true, length = 50)
	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	@Column(name = "uri", nullable = true, length = 150)
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@Column(name = "method", nullable = true, length = 20)
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@Column(name = "param_data", nullable = true, length = 715827882)
	public String getParamData() {
		return paramData;
	}

	public void setParamData(String paramData) {
		this.paramData = paramData;
	}

	@Column(name = "session_id", nullable = true, length = 150)
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Column(name = "returm_time", nullable = true, length = 20)
	public String getReturmTime() {
		return returmTime;
	}

	public void setReturmTime(String returmTime) {
		this.returmTime = returmTime;
	}

	@Column(name = "return_data", nullable = true, length = 715827882)
	public String getReturnData() {
		return returnData;
	}

	public void setReturnData(String returnData) {
		this.returnData = returnData;
	}

	@Column(name = "http_status_code", nullable = true, length = 20)
	public String getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setHttpStatusCode(String httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	@Column(name = "time_consuming", nullable = true, length = 11)
	public Integer getTimeConsuming() {
		return timeConsuming;
	}

	public void setTimeConsuming(Integer timeConsuming) {
		this.timeConsuming = timeConsuming;
	}

	@Column(name = "request_result", nullable = true, length = 6)
	public Integer getRequestResult() {
		return requestResult;
	}

	public void setRequestResult(Integer requestResult) {
		this.requestResult = requestResult;
	}

	@Override
	public Date getCreateTime() {
		return createTime;
	}

	@Override
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "browser", nullable = true, length = 50)
	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	@Column(name = "os", nullable = true, length = 50)
	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	@Column(name = "user_agent", nullable = true, length = 500)
	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	@Column(name = "remark", nullable = true, length = 500)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Transient
	public String getResponseTime() {
		if (getTimeConsuming() != null) {
			double time = getTimeConsuming() * 1.0 / 1000;
			if (time > 1) {
				responseTime = time + "秒";
			} else {
				responseTime = getTimeConsuming() + "毫秒";
			}
		}
		return responseTime;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof SysLog)) {
			return false;
		}

		SysLog sysLog = (SysLog) o;

		if (getId() != null ? !getId().equals(sysLog.getId()) : sysLog.getId() != null) {
			return false;
		}
		if (getLogCategory() != null ? !getLogCategory().equals(sysLog.getLogCategory()) : sysLog.getLogCategory() != null) {
			return false;
		}
		if (getLogType() != null ? !getLogType().equals(sysLog.getLogType()) : sysLog.getLogType() != null) {
			return false;
		}
		if (getEventType() != null ? !getEventType().equals(sysLog.getEventType()) : sysLog.getEventType() != null) {
			return false;
		}
		if (getLogLevel() != null ? !getLogLevel().equals(sysLog.getLogLevel()) : sysLog.getLogLevel() != null) {
			return false;
		}
		if (getOperateType() != null ? !getOperateType().equals(sysLog.getOperateType()) : sysLog.getOperateType() != null) {
			return false;
		}
		if (getSubEventType() != null ? !getSubEventType().equals(sysLog.getSubEventType()) : sysLog.getSubEventType() != null) {
			return false;
		}
		if (getUsername() != null ? !getUsername().equals(sysLog.getUsername()) : sysLog.getUsername() != null) {
			return false;
		}
		if (getClientIp() != null ? !getClientIp().equals(sysLog.getClientIp()) : sysLog.getClientIp() != null) {
			return false;
		}
		if (getUri() != null ? !getUri().equals(sysLog.getUri()) : sysLog.getUri() != null) {
			return false;
		}
		if (getMethod() != null ? !getMethod().equals(sysLog.getMethod()) : sysLog.getMethod() != null) {
			return false;
		}
		if (getParamData() != null ? !getParamData().equals(sysLog.getParamData()) : sysLog.getParamData() != null) {
			return false;
		}
		if (getSessionId() != null ? !getSessionId().equals(sysLog.getSessionId()) : sysLog.getSessionId() != null) {
			return false;
		}
		if (getReturmTime() != null ? !getReturmTime().equals(sysLog.getReturmTime()) : sysLog.getReturmTime() != null) {
			return false;
		}
		if (getReturnData() != null ? !getReturnData().equals(sysLog.getReturnData()) : sysLog.getReturnData() != null) {
			return false;
		}
		if (getHttpStatusCode() != null ? !getHttpStatusCode().equals(sysLog.getHttpStatusCode()) : sysLog.getHttpStatusCode() != null) {
			return false;
		}
		if (getTimeConsuming() != null ? !getTimeConsuming().equals(sysLog.getTimeConsuming()) : sysLog.getTimeConsuming() != null) {
			return false;
		}
		if (getBrowser() != null ? !getBrowser().equals(sysLog.getBrowser()) : sysLog.getBrowser() != null) {
			return false;
		}
		if (getOs() != null ? !getOs().equals(sysLog.getOs()) : sysLog.getOs() != null) {
			return false;
		}
		if (getUserAgent() != null ? !getUserAgent().equals(sysLog.getUserAgent()) : sysLog.getUserAgent() != null) {
			return false;
		}
		if (getRemark() != null ? !getRemark().equals(sysLog.getRemark()) : sysLog.getRemark() != null) {
			return false;
		}
		if (getCreateTime() != null ? !getCreateTime().equals(sysLog.getCreateTime()) : sysLog.getCreateTime() != null) {
			return false;
		}
		if (getCreateUser() != null ? !getCreateUser().equals(sysLog.getCreateUser()) : sysLog.getCreateUser() != null) {
			return false;
		}
		if (getUpdateTime() != null ? !getUpdateTime().equals(sysLog.getUpdateTime()) : sysLog.getUpdateTime() != null) {
			return false;
		}
		if (getUpdateUser() != null ? !getUpdateUser().equals(sysLog.getUpdateUser()) : sysLog.getUpdateUser() != null) {
			return false;
		}
		if (getHasDeleted() != null ? !getHasDeleted().equals(sysLog.getHasDeleted()) : sysLog.getHasDeleted() != null) {
			return false;
		}
		return getRequestResult() != null ? getRequestResult().equals(sysLog.getRequestResult()) : sysLog.getRequestResult() == null;
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
		result = 31 * result + (getOs() != null ? getOs().hashCode() : 0);
		result = 31 * result + (getUserAgent() != null ? getUserAgent().hashCode() : 0);
		result = 31 * result + (getBrowser() != null ? getBrowser().hashCode() : 0);
		result = 31 * result + (getRemark() != null ? getRemark().hashCode() : 0);
		result = 31 * result + (getCreateTime() != null ? getCreateTime().hashCode() : 0);
		result = 31 * result + (getCreateUser() != null ? getCreateUser().hashCode() : 0);
		result = 31 * result + (getUpdateTime() != null ? getUpdateTime().hashCode() : 0);
		result = 31 * result + (getUpdateUser() != null ? getUpdateUser().hashCode() : 0);
		result = 31 * result + (getHasDeleted() != null ? getHasDeleted().hashCode() : 0);
		return result;
	}
}