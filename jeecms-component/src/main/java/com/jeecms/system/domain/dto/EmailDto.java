package com.jeecms.system.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 * 邮箱配置Dto
 * 
 * @author: chenming
 * @date: 2019年4月12日 下午6:00:37
 */
public class EmailDto {

	/** SMTP 服务器 */
	private String smtpService;
	/** SMTP 端口 */
	private String smtpPort;
	/** 邮箱账号 */
	private String emailName;
	/** 邮箱密码 */
	private String emailPassword;
	/** 是否使用sll协议 0-否 1-是 */
	private Boolean isSsl;
	/** 测试邮件账号 */
	private String toAddress;

	@NotBlank
	@Length(max = 255)
	public String getSmtpService() {
		return smtpService;
	}

	public void setSmtpService(String smtpService) {
		this.smtpService = smtpService;
	}

	@NotBlank
	@Length(max = 50)
	public String getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}

	@NotBlank
	@Length(max = 150)
	public String getEmailName() {
		return emailName;
	}

	public void setEmailName(String emailName) {
		this.emailName = emailName;
	}

	@NotBlank
	@Length(max = 50)
	public String getEmailPassword() {
		return emailPassword;
	}

	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}

	@NotNull
	public Boolean getIsSsl() {
		return isSsl;
	}

	public void setIsSsl(Boolean isSsl) {
		this.isSsl = isSsl;
	}

	@NotBlank
	@Length(max = 150)
	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

}
