package com.jeecms.common.wechat.bean.response.pay;

import org.apache.commons.lang3.StringUtils;

import com.jeecms.common.constants.ContentSecurityConstants;
import com.jeecms.common.util.DesUtil;
import com.jeecms.common.wechat.bean.response.pay.common.AbstractSucceedResponse;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * @Description: 统一下单接口返回
 * @author: chenming
 * @date: 2018年9月12日 上午11:02:12
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@XStreamAlias("xml")
public class UnifiedorderResponse extends AbstractSucceedResponse {

	/** 调用接口提交的交易类型，取值如下：JSAPI，NATIVE，APP，,H5支付固定传MWEB */
	private String tradeType;
	/** 微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时,针对H5支付此参数无特殊用途 */
	private String prepayId;
	/** trade_type为NATIVE是有返回，可将该参数值生成二维码展示出来进行扫码支付 */
	private String codeUrl;
	/**
	 * mweb_url为拉起微信支付收银台的中间页面，可通过访问该url来拉起微信客户端，
	 * 完成支付,mweb_url的有效期为5分钟。当trade_type为MWEB时有返回
	 */
	private String mwebUrl;

	public UnifiedorderResponse() {
		super();
	}

	public UnifiedorderResponse(String tradeType, String prepayId, String codeUrl, String mwebUrl) {
		super();
		this.tradeType = tradeType;
		this.prepayId = prepayId;
		this.codeUrl = codeUrl;
		this.mwebUrl = mwebUrl;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getPrepayId() {
		return prepayId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}
	
	/**
	 * 扫码支付返回用于生成二维码的支付串泄露，进行加密，前端调用生成二维码支付的链接，已进行解密处理
	 * @return
	 */
	public String getCodeUrl() {
		return StringUtils.isNotBlank(codeUrl) ? DesUtil.encrypt(codeUrl, 
				ContentSecurityConstants.DES_KEY,ContentSecurityConstants.DES_IV) :"";
	}

	public void setCodeUrl(String codeUrl) {
		this.codeUrl = codeUrl;
	}

	public String getMwebUrl() {
		return mwebUrl;
	}

	public void setMwebUrl(String mwebUrl) {
		this.mwebUrl = mwebUrl;
	}
}
