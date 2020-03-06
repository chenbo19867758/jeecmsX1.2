package com.jeecms.common.wechat.bean.response.pay.common;

import com.jeecms.common.wechat.bean.response.pay.AbstractPayResponse;

/**
 * 
 * @Description: 微信内H5调起支付参数(扩展类，其所有的参数已在AbstractPayResponse中)
 * @author: chenming
 * @date:   2018年9月12日 上午11:07:17     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class AbstractSucceedResponse extends AbstractPayResponse{
	/** 公众号id*/
	private String appid; 
	/** 商户号*/
	private String mchId; 
	/** 微信分配的子商户公众账号ID*/
	private String subAppid;
	/** 微信支付分配的子商户号*/
	private String subMchId;
	/** 调用接口提交的终端设备号*/
	private String deviceInfo;
	/** 微信返回的随机字符串*/
	private String nonceStr;
	/** 微信返回的签名，详见签名算法*/
	private String sign;
	/** SUCCESS/FAIL*/
	private String resultCode;
	/** 详细参见第6节错误列表*/
	private String errCodeDes;
	/** 错误返回的信息描述*/
	private String errCode;
	
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getMchId() {
		return mchId;
	}
	public void setMchId(String mchId) {
		this.mchId = mchId;
	}
	public String getSubAppid() {
		return subAppid;
	}
	public void setSubAppid(String subAppid) {
		this.subAppid = subAppid;
	}
	public String getSubMchId() {
		return subMchId;
	}
	public void setSubMchId(String subMchId) {
		this.subMchId = subMchId;
	}
	public String getDeviceInfo() {
		return deviceInfo;
	}
	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
	public String getNonceStr() {
		return nonceStr;
	}
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getErrCodeDes() {
		return errCodeDes;
	}
	public void setErrCodeDes(String errCodeDes) {
		this.errCodeDes = errCodeDes;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	
	
}
