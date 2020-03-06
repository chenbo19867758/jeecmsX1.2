/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.sso.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.jeecms.common.constants.ContentSecurityConstants;
import com.jeecms.common.util.DesUtil;

/**
 * 请求基础Dto
 * 
 * @author: ljw
 * @date: 2019年10月26日 下午2:40:36
 */
public class BaseDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** 应用APPID */
	private String appId;
	/** 应用APP秘钥 */
	private String appSecret;
	
	public BaseDto() {}
	
	/**构造函数**/
	public BaseDto(String appId, String appSecret) {
		this.appId = appId;
		this.appSecret = DesUtil.encrypt(appSecret, ContentSecurityConstants.DES_KEY,
                ContentSecurityConstants.DES_IV);
	}
	
	@NotNull
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@NotNull
	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

}
