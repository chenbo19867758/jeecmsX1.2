package com.jeecms.system.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.hibernate.validator.constraints.Length;
import com.alibaba.fastjson.annotation.JSONField;
import com.jeecms.common.base.domain.AbstractDomain;

/**
 * email实体类
 * @author: chenming
 * @date:   2019年4月12日 下午6:20:39
 */
@Entity
@Table(name = "jc_sys_email")
@NamedQuery(name = "Email.findAll", query = "SELECT e FROM Email e")
public class Email extends AbstractDomain<Integer> {
	private static final long serialVersionUID = 1L;
	/** 主键值 */
	private Integer id;
	 /** 是否开启短信服务  0-否  1-是 */
    private  Boolean isEnable ;
	/** 是否使用全局配置 0-否 1-是 */
	private Boolean isGloable;
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

	@JSONField(serialize = false)
	private HtmlEmail htmlEmail;

	public Email() {

	}

	/**
	 * email创建实体类
	 */
	public Email(String smtpService, String smtpPort, String emailName, String emailPassword, Boolean isSsl) {
		super();
		this.smtpService = smtpService;
		this.smtpPort = smtpPort;
		this.emailName = emailName;
		this.emailPassword = emailPassword;
		this.isSsl = isSsl;
	}

	@Id
	@TableGenerator(name = "jc_sys_email", pkColumnValue = "jc_sys_email", initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_email")
	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "is_enable", nullable = false, length = 1)
	@NotNull
	public Boolean getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Boolean isEnable) {
		this.isEnable = isEnable;
	}
	
	@Column(name = "is_gloable", nullable = false)
	public Boolean getIsGloable() {
		return isGloable;
	}

	public void setIsGloable(Boolean isGloable) {
		this.isGloable = isGloable;
	}

	@Column(name = "smtp_service", nullable = true, length = 255)
	@Length(max = 255)
	public String getSmtpService() {
		return smtpService;
	}

	public void setSmtpService(String smtpService) {
		this.smtpService = smtpService;
	}

	@Column(name = "smtp_port", nullable = true, length = 50)
	@Length(max = 50)
	public String getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}

	@Column(name = "email_name", nullable = true, length = 150)
	@Length(max = 150)
	public String getEmailName() {
		return emailName;
	}

	public void setEmailName(String emailName) {
		this.emailName = emailName;
	}

	@Column(name = "email_password", nullable = true, length = 50)
	@Length(max = 50)
	public String getEmailPassword() {
		return emailPassword;
	}

	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}

	/**
	 * 获取HtmlEmail
	 */
	@Transient
	public HtmlEmail getHtmlEmail() {
		htmlEmail = new HtmlEmail();
		try {
			htmlEmail.setFrom(this.emailName, this.emailName.substring(0, this.emailName.indexOf("@")));
		} catch (EmailException e) {
			e.printStackTrace();
		}
		htmlEmail.setAuthentication(this.emailName, this.emailPassword);
		htmlEmail.setHostName(this.smtpService);
		// ssl协议设置端口号
		htmlEmail.setSslSmtpPort(this.smtpPort);
		htmlEmail.setCharset("utf-8");
		return htmlEmail;
	}

	public void setHtmlEmail(HtmlEmail htmlEmail) {
		this.htmlEmail = htmlEmail;
	}

	@Column(name = "is_ssl", nullable = true)
	public Boolean getIsSsl() {
		return isSsl;
	}

	public void setIsSsl(Boolean isSsl) {
		this.isSsl = isSsl;
	}



}
