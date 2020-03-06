package com.jeecms.backup.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Zhu Kaixiao
 * @version 1.0
 * @date 2019/8/2 14:03
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class BackupDto {
    @NotNull
    private Integer backupId;
    @NotBlank
    private String jdbcUrl;
    @NotBlank
    private String username;
    private String password;
    /**
     * 备份文件所在路径
     */
    private String bakFilePath;
    /**
     * 备份或还原完成后的回调地址
     */
    @NotBlank
    private String callbackUrl;
    /**是否来源于FTP或OSS**/
    private Boolean third;
    
	/**
	 * @return the backupId
	 */
	public Integer getBackupId() {
		return backupId;
	}
	/**
	 * @return the jdbcUrl
	 */
	public String getJdbcUrl() {
		return jdbcUrl;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @return the bakFilePath
	 */
	public String getBakFilePath() {
		return bakFilePath;
	}
	/**
	 * @return the callbackUrl
	 */
	public String getCallbackUrl() {
		return callbackUrl;
	}
	/**
	 * @param backupId the backupId to set
	 */
	public void setBackupId(Integer backupId) {
		this.backupId = backupId;
	}
	/**
	 * @param jdbcUrl the jdbcUrl to set
	 */
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @param bakFilePath the bakFilePath to set
	 */
	public void setBakFilePath(String bakFilePath) {
		this.bakFilePath = bakFilePath;
	}
	/**
	 * @param callbackUrl the callbackUrl to set
	 */
	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}
	
	public Boolean getThird() {
		return third;
	}
	public void setThird(Boolean third) {
		this.third = third;
	}
    
    
}
