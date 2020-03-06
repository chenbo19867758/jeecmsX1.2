/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.vo;

/**
 * 系统信息VO
 * 
 * @author: ljw
 * @date: 2019年7月12日 下午1:45:32
 */
public class SystemInfoVo {

	/** 软件名称 **/
	private String productName;
	/** 版本号 **/
	private String version;
	/** 用户单位 **/
	private String siteName;
	/** 版权所有 **/
	private String copyright;
	/** 公司官网 **/
	private String systemUrl;
	/** 授权码 **/
	private String authCode;
	/** 授权许可 **/
	private String authorization;
	/** 授权状态1.已授权 2.未授权 3.未完成授权 4.授权过期 **/
	private Integer authState;
	/** 授权到期时间 **/
	private String authExpiredDate;
	/** 授权到期时间 **/
	private String afterSaleExpiredDate;
	/** 操作系统 **/
	private String system;
	/** JDK **/
	private String jdk;
	/** 数据库类型 **/
	private String dbType;
	/** 数据库URL **/
	private String dbUrl;
	/** 数据库PORT **/
	private Integer dbPort;
	/** 数据库名称 **/
	private String dbName;

	public SystemInfoVo() {
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public String getAuthorization() {
		return authorization;
	}

	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}

	public Integer getAuthState() {
		return authState;
	}

	public void setAuthState(Integer authState) {
		this.authState = authState;
	}

	public String getAuthExpiredDate() {
		return authExpiredDate;
	}

	public void setAuthExpiredDate(String authExpiredDate) {
		this.authExpiredDate = authExpiredDate;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getJdk() {
		return jdk;
	}

	public void setJdk(String jdk) {
		this.jdk = jdk;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public Integer getDbPort() {
		return dbPort;
	}

	public void setDbPort(Integer dbPort) {
		this.dbPort = dbPort;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getAfterSaleExpiredDate() {
		return afterSaleExpiredDate;
	}

	public void setAfterSaleExpiredDate(String afterSaleExpiredDate) {
		this.afterSaleExpiredDate = afterSaleExpiredDate;
	}

	public String getSystemUrl() {
		return systemUrl;
	}

	public void setSystemUrl(String systemUrl) {
		this.systemUrl = systemUrl;
	}

}
