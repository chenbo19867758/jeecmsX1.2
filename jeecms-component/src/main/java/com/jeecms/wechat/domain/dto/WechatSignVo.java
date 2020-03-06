package com.jeecms.wechat.domain.dto;

/**
 * 微信分享dto
 * @author: chenming
 * @date:   2019年10月31日 下午2:02:42
 */
public class WechatSignVo {
	/** 公众号appId */
	private String appId;
	/** 签名时间戳 */
	private String timestamp;
	/** 签名随机串 */
	private String nonceStr;
	/** 签名 */
	private String signature;

	public WechatSignVo(String appId, String timestamp, String nonceStr, String signature) {
		super();
		this.appId = appId;
		this.timestamp = timestamp;
		this.nonceStr = nonceStr;
		this.signature = signature;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

}
