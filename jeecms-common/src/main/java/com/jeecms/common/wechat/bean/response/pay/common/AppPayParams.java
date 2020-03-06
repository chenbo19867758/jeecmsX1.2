package com.jeecms.common.wechat.bean.response.pay.common;

/**
 * 
 * @Description: app调起微信支付，调起参数
 * @author: chenming
 * @date:   2019年1月21日 上午10:41:39     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class AppPayParams {
	/** 商户应用ID*/
	private String appId;
	/** 商户号*/
	private String partnerId;
	/** 支付交易会话ID*/
	private String prepayId;
	/** 固定值Sign=WXPay */
	private String packageStr="Sign=WXPay";  
	/** 随机字符串*/
	private String nonceStr; 
	/** 时间戳 格式1414561699*/
    private String timeStamp; 
    /** 签名*/
    private String paySign;
    /** 签名方式*/
    private String signType = "MD5";
    
	public String getAppId() {
		return appId;
	}
	
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	public String getPartnerId() {
		return partnerId;
	}
	
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	
	public String getPrepayId() {
		return prepayId;
	}
	
	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}
	
	public String getPackageStr() {
		return packageStr;
	}
	
	public void setPackageStr(String packageStr) {
		this.packageStr = packageStr;
	}
	
	public String getNonceStr() {
		return nonceStr;
	}
	
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	
	public String getTimeStamp() {
		return timeStamp;
	}
	
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public String getPaySign() {
		return paySign;
	}
	
	public void setPaySign(String paySign) {
		this.paySign = paySign;
	}
	
	public String getSignType() {
		return signType;
	}
	
	public void setSignType(String signType) {
		this.signType = signType;
	} 
    
}
