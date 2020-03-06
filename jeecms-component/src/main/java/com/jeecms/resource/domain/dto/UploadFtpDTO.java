package com.jeecms.resource.domain.dto;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

/**
 * @author: tom
 * @date:   2019年3月5日 下午4:48:37
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class UploadFtpDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String encoding;
	private String ftpName;
	private String ftpPath;
	private String ip;
	private String password;
	private Integer port;
	private Integer timeout;
	private String url;
	private String username;

	public UploadFtpDTO() {
	}


	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getEncoding() {
		return this.encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}


	@NotNull
	public String getFtpName() {
		return this.ftpName;
	}

	public void setFtpName(String ftpName) {
		this.ftpName = ftpName;
	}


	@Length(max=255)
	public String getFtpPath() {
		return this.ftpPath;
	}

	public void setFtpPath(String ftpPath) {
		this.ftpPath = ftpPath;
	}


	@Length(max=50)
	@NotNull
	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}


	@Length(max=100)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@NotNull
	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}


	public int getTimeout() {
		return this.timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}


	@Length(max=255)
	@NotNull
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Length(max=100)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@Override
	public String toString() {
		return "UploadFtpDTO.java";
	}

}