package com.jeecms.common.wechat.bean.request.pay.common;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * @Description: 微信支付的诸多方法共有的参数-request
 * @author: chenming
 * @date:   2018年9月12日 上午10:41:37     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class AbstractPayParams {
	
	/** 公众号id*/
	private String appid;
	/** 商户号*/
	@XStreamAlias("mch_id")
	private String mchId;
	/** 随机字符串*/
	@XStreamAlias("nonce_str")
	private String nonceStr;
	/** 签名*/
	private String sign;
	
	
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
	
}
