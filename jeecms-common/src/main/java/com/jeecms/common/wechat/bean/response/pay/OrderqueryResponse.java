package com.jeecms.common.wechat.bean.response.pay;

import com.jeecms.common.wechat.bean.response.pay.common.AbstractSucceedResponse;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * @Description: 查询订单-response
 * @author: chenming
 * @date:   2018年10月12日 下午4:29:00     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@XStreamAlias("xml")
public class OrderqueryResponse extends AbstractSucceedResponse{
	/** 用户标识*/
	private String openid;
	/** 是否关注公众账号*/
	private String isSubscribe;
	/** 用户子标识*/
	private String subOpenid;
	/** 是否关注子公众账号*/
	private String subIsSubscribe;
	/** 交易类型*/
	private String tradeType;
	/** 交易状态
	 * SUCCESS—支付成功
	 * REFUND—转入退款
	 * NOTPAY—未支付
	 * CLOSED—已关闭
	 * REVOKED—已撤销(刷卡支付)
	 * USERPAYING--用户支付中
	 * PAYERROR--支付失败(其他原因，如银行返回失败)
	 */
	private String tradeState;
	/** 付款银行*/
	private String bankType;
	/** 商品详情*/
	private String detail;
	/** 标价金额*/
	private Long totalFee;
	/** 标价币种*/
	private String feeType;
	/** 应结订单金额*/
	private Long settlementTotalFee;
	/** 现金支付金额*/
	private Long cashFee;
	/** 现金支付货币类型*/
	private String cashFeeType;
	/** 代金券金额*/
	private Long couponFee;
	/** 代金券使用数量*/
	private Integer couponCount;
	/** 代金券ID*/
	private String coupon_id_$n;
	/** 代金券类型*/
	private String coupon_type_$n;
	/** 单个代金券金额*/
	private Long coupon_fee_$n;
	/** 微信支付订单号*/
	private String transactionId;
	/** 商户订单号*/
	private String outTradeNo;
	/** 商家数据包*/
	private String attach;
	/** 支付完成时间*/
	private String timeEnd;
	/**交易状态描述*/
	private String tradeStateDesc;
	
	
	public String getOpenid() {
		return openid;
	}
	
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
	public String getIsSubscribe() {
		return isSubscribe;
	}
	
	public void setIsSubscribe(String isSubscribe) {
		this.isSubscribe = isSubscribe;
	}
	
	public String getSubOpenid() {
		return subOpenid;
	}
	
	public void setSubOpenid(String subOpenid) {
		this.subOpenid = subOpenid;
	}
	
	public String getSubIsSubscribe() {
		return subIsSubscribe;
	}
	
	public void setSubIsSubscribe(String subIsSubscribe) {
		this.subIsSubscribe = subIsSubscribe;
	}
	
	public String getTradeType() {
		return tradeType;
	}
	
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	
	public String getTradeState() {
		return tradeState;
	}
	
	public void setTradeState(String tradeState) {
		this.tradeState = tradeState;
	}
	
	public String getBankType() {
		return bankType;
	}
	
	public void setBankType(String bankType) {
		this.bankType = bankType;
	}
	
	public String getDetail() {
		return detail;
	}
	
	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	public Long getTotalFee() {
		return totalFee;
	}
	
	public void setTotalFee(Long totalFee) {
		this.totalFee = totalFee;
	}
	
	public String getFeeType() {
		return feeType;
	}
	
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	
	public Long getSettlementTotalFee() {
		return settlementTotalFee;
	}
	
	public void setSettlementTotalFee(Long settlementTotalFee) {
		this.settlementTotalFee = settlementTotalFee;
	}
	
	public Long getCashFee() {
		return cashFee;
	}
	
	public void setCashFee(Long cashFee) {
		this.cashFee = cashFee;
	}
	
	public String getCashFeeType() {
		return cashFeeType;
	}
	
	public void setCashFeeType(String cashFeeType) {
		this.cashFeeType = cashFeeType;
	}
	
	public Long getCouponFee() {
		return couponFee;
	}
	
	public void setCouponFee(Long couponFee) {
		this.couponFee = couponFee;
	}
	
	public Integer getCouponCount() {
		return couponCount;
	}
	
	public void setCouponCount(Integer couponCount) {
		this.couponCount = couponCount;
	}
	
	public String getCoupon_id_$n() {
		return coupon_id_$n;
	}

	public void setCoupon_id_$n(String coupon_id_$n) {
		this.coupon_id_$n = coupon_id_$n;
	}

	public String getCoupon_type_$n() {
		return coupon_type_$n;
	}

	public void setCoupon_type_$n(String coupon_type_$n) {
		this.coupon_type_$n = coupon_type_$n;
	}

	public Long getCoupon_fee_$n() {
		return coupon_fee_$n;
	}

	public void setCoupon_fee_$n(Long coupon_fee_$n) {
		this.coupon_fee_$n = coupon_fee_$n;
	}

	public String getTransactionId() {
		return transactionId;
	}
	
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
	public String getOutTradeNo() {
		return outTradeNo;
	}
	
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	
	public String getAttach() {
		return attach;
	}
	
	public void setAttach(String attach) {
		this.attach = attach;
	}
	
	public String getTimeEnd() {
		return timeEnd;
	}
	
	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}
	
	public String getTradeStateDesc() {
		return tradeStateDesc;
	}
	
	public void setTradeStateDesc(String tradeStateDesc) {
		this.tradeStateDesc = tradeStateDesc;
	}
}
