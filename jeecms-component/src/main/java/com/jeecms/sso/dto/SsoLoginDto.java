/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.sso.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * 单点登录设置Dto
 * 
 * @author: ljw
 * @date: 2019年10月26日 上午9:42:16
 */
public class SsoLoginDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**是否开启,1开启，0关闭**/
	private Boolean openSso;
	/**SSO应用APPID**/
	private String appId;
	/**SSO应用APPID**/
	private String appSecret;
	
	public SsoLoginDto() {}

	@NotNull(groups = { One.class, Two.class })
	public Boolean getOpenSso() {
		return openSso;
	}

	public void setOpenSso(Boolean openSso) {
		this.openSso = openSso;
	}

	@NotNull(groups = {One.class})
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@NotNull(groups = {One.class})
	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	/**验证**/
	public interface One {}
	
	/**验证**/
	public interface Two {}
}
