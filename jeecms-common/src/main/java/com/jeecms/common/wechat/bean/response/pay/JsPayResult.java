package com.jeecms.common.wechat.bean.response.pay;

import com.jeecms.common.wechat.bean.response.pay.common.JsPayParams;

/**
 * 
 * TODO
 * @author: tom
 * @date:   2019年3月8日 下午4:43:31
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class JsPayResult extends JsPayParams{
	/** 返回错误信息或提示扩展信息*/ 
	private String errMsg;
    /** 返回提示状态码*/
	private String resultCode;
	 
	public String getErrMsg() {
	     return errMsg;
	}
	 
	public void setErrMsg(String errMsg) {
	    this.errMsg = errMsg;
	}
	 
	public String getResultCode() {
		return resultCode;
	}
	
	public void setResultCode(String resultCode) {
	    this.resultCode = resultCode;
	}

	@Override
	public String toString() {
		return "JsPayResult [errMsg=" + errMsg + ", resultCode=" + resultCode + ", getErrMsg()=" + getErrMsg()
			+ ", getResultCode()=" + getResultCode() + ", getAppId()=" + getAppId() + ", getTimeStamp()="
			+ getTimeStamp() + ", getNonceStr()=" + getNonceStr() + ", getPackageStr()=" + getPackageStr()
			+ ", getSignType()=" + getSignType() + ", getPaySign()=" + getPaySign() + ", getClass()="
			+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	    
}
