/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.common.weibo.bean.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 基础返回响应数据
 * 
 * @author: ljw
 * @date: 2019年6月17日 上午10:01:44
 */
public class BaseResponse {

	@JSONField(serialize = false)
    public static final Integer SUCCESS_CODE = 10000;
	/** 错误信息 **/
	private String error;
	/** 返回的错误码 **/
	@XStreamAlias("error_code")
	private Integer errorCode;
	/** 请求地址 **/
	private String request;
	/** 请求地址 **/
	@XStreamAlias("error_uri")
	private String errorUri;
	/** 描述 **/
	@XStreamAlias("error_description")
	private String errorDescription;

	public BaseResponse() {}
	
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getErrorUri() {
		return errorUri;
	}

	public void setErrorUri(String errorUri) {
		this.errorUri = errorUri;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

}
