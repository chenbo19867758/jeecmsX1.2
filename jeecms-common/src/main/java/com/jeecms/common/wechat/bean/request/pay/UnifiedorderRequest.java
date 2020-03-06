package com.jeecms.common.wechat.bean.request.pay;

import com.jeecms.common.wechat.bean.request.pay.common.AbstractPayParams;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * @Description: 统一下单request
 * @author: chenming
 * @date:   2018年9月12日 上午10:40:52     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@XStreamAlias("xml")
public class UnifiedorderRequest extends AbstractPayParams{
	
	/** 签名类型*/
	@XStreamAlias("signType")
	private String signType;
	/** 设备号 可以为终端设备号(门店号或收银设备ID)，PC网页或公众号内支付可以传"WEB"*/
	private String deviceInfo;
	/** 商品描述*/
	private String body;
	/** 商品详情*/
	private String detail;
	/** 附加数据*/
	private String attach;
	/** 商户订单号*/
	@XStreamAlias("outTradeNo")
	private String outTradeNo;
	/** 货币类型，默认为人民币CNY*/
	@XStreamAlias("feeType")
	private String feeType;
	/** 总金额，传入int类型的数据*/
	@XStreamAlias("totalFee")
	private Long totalFee;
	/** 终端ip*/
	@XStreamAlias("spbillCreateIp")
	private String spbillCreateIp;
	/** 交易起始时间-订单生成时间*/
	@XStreamAlias("timeStart")
	private String timeStart;
	/** 交易结束时间-订单失效时间*/
	@XStreamAlias("timeExpire")
	private String timeExpire;
	/** 订单优惠标记*/
	@XStreamAlias("goodsTag")
	private String goodsTag;
	/** 通知url*/
	@XStreamAlias("notifyUrl")
	private String notifyUrl;
	/** 交易类型 JSAPI，NATIVE，APP*/
	@XStreamAlias("tradeType")
	private String tradeType;
	/** 商品id trade_type=NATIVE时（即扫码支付），此参数必传*/
	@XStreamAlias("productId")
	private String productId;
	/** 指定支付方式 no_credit--可限制用户不能使用信用卡支付*/
	@XStreamAlias("limitPay")
	private String limitPay;
	/** 用户标识(trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识)*/
	private String openid;
	/** 该字段用于统一下单时上报场景信息，目前支持上报实际门店信息 格式{"store_id":// "SZT10000", "store_name":"腾讯大厦腾大餐厅"}*/
	@XStreamAlias("sceneInfo")
	private String sceneInfo;
	
	public String getDeviceInfo() {
		return deviceInfo;
	}
	
	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
	
	public String getBody() {
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public String getDetail() {
		return detail;
	}
	
	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	public String getAttach() {
		return attach;
	}
	
	public void setAttach(String attach) {
		this.attach = attach;
	}
	
	public String getOutTradeNo() {
		return outTradeNo;
	}
	
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	
	public String getFeeType() {
		return feeType;
	}
	
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	
	public Long getTotalFee() {
		return totalFee;
	}
	
	public void setTotalFee(Long totalFee) {
		this.totalFee = totalFee;
	}
	
	public String getSpbillCreateIp() {
		return spbillCreateIp;
	}
	
	public void setSpbillCreateIp(String spbillCreateIp) {
		this.spbillCreateIp = spbillCreateIp;
	}
	
	public String getTimeStart() {
		return timeStart;
	}
	
	public void setTimeStart(String timeStart) {
		this.timeStart = timeStart;
	}
	
	public String getTimeExpire() {
		return timeExpire;
	}
	
	public void setTimeExpire(String timeExpire) {
		this.timeExpire = timeExpire;
	}
	
	public String getGoodsTag() {
		return goodsTag;
	}
	
	public void setGoodsTag(String goodsTag) {
		this.goodsTag = goodsTag;
	}
	
	public String getNotifyUrl() {
		return notifyUrl;
	}
	
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	
	public String getTradeType() {
		return tradeType;
	}
	
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	
	public String getProductId() {
		return productId;
	}
	
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public String getLimitPay() {
		return limitPay;
	}
	
	public void setLimitPay(String limitPay) {
		this.limitPay = limitPay;
	}
	
	public String getOpenid() {
		return openid;
	}
	
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
	public String getSceneInfo() {
		return sceneInfo;
	}
	
	public void setSceneInfo(String sceneInfo) {
		this.sceneInfo = sceneInfo;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	@Override
	public String toString() {
		return "UnifiedorderRequest [sign_type=" + signType + ", device_info=" + deviceInfo + ", body=" + body
				+ ", detail=" + detail + ", attach=" + attach + ", out_trade_no=" + outTradeNo + ", fee_type="
				+ feeType + ", total_fee=" + totalFee + ", spbill_create_ip=" + spbillCreateIp + ", time_start="
				+ timeStart + ", time_expire=" + timeExpire + ", goods_tag=" + goodsTag + ", notify_url="
				+ notifyUrl + ", trade_type=" + tradeType + ", product_id=" + productId + ", limit_pay=" + limitPay
				+ ", openid=" + openid + ", scene_info=" + sceneInfo + "]";
	}
	
}
