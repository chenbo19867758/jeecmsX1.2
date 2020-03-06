package com.jeecms.common.wechat.bean.response.pay.common;

/**
 * 
 * @Description: 微信内H5调起支付参数
 * @author: chenming
 * @date:   2018年9月11日 上午10:34:46     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class JsPayParams {
	/** 公众号id*/
	private String appId;
	/** 时间戳 格式1414561699*/
    private String timeStamp; 
    /** 随机字符串*/
    private String nonceStr; 
    /** package参数 订单详情扩展字符串 prepay_id=****/
    private String packageStr;  
    /** 签名方式*/
    private String signType = "MD5"; 
    /** 签名*/
    private String paySign;
 
    public String getAppId() {
        return appId;
    }
 
    public void setAppId(String appId) {
        this.appId = appId;
    }
 
    public String getTimeStamp() {
        return timeStamp;
    }
 
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
 
    public String getNonceStr() {
        return nonceStr;
    }
 
    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }
 
    public String getPackageStr() {
        return packageStr;
    }
 
    public void setPackageStr(String packageStr) {
        this.packageStr = packageStr;
    }
 
    public String getSignType() {
        return signType;
    }
 
    public void setSignType(String signType) {
        this.signType = signType;
    }
 
    public String getPaySign() {
        return paySign;
    }
 
    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }

}