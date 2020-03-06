/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.sso.dto.response;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 基础响应
 * 
 * @author: ljw
 * @date: 2019年10月26日 下午3:03:22
 */
public class SyncResponseBaseVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 响应代码。 */
	private int code;
	/** 响应提示信息。 */
	private String message;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@JSONField(serialize = false)
    public static final Integer SUCCESS_CODE = 200;
}
