package com.jeecms.common.wechat.bean.response.pay;

import java.util.List;

import com.jeecms.common.wechat.bean.response.pay.common.AbstractSucceedResponse;
import com.jeecms.common.wechat.bean.response.pay.common.RefundVoucher;
import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * 
 * @Description: 退款--response请求
 * @author: chenming
 * @date:   2018年11月8日 上午9:48:28     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@XStreamAlias("xml")
public class RefundResponse extends AbstractSucceedResponse{
	private String refundChannel;
	/** 微信订单号*/
	private String transactionId;
	/** 商户订单号*/
	private String outTradeNo;
	/** 商户退款单号*/
	private String outRefundNo;
	/** 微信退款单号*/
	private String refundId;
	/** 申请退款金额*/
	private Integer refundFee;
	/** 退款金额*/
	private Integer settlementRefundFee;
	/** 订单金额*/
	private Integer totalFee;
	/** 应结订单金额*/
	private Integer settlementTotalFee;
	/** 货币种类*/
	private String  feeType;
	/** 现金支付金额*/
	private Integer cashFee;
	/** 现金退款金额*/
	private Integer cashRefundFee;
	/** 代金券退款总金额*/
	private Integer couponRefundFee;
	/** 退款代金券使用数量*/
	private Integer couponRefundCount;
	
	private List<RefundVoucher> rVouchers;
	
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
	public String getOutRefundNo() {
		return outRefundNo;
	}
	public void setOutRefundNo(String outRefundNo) {
		this.outRefundNo = outRefundNo;
	}
	public String getRefundId() {
		return refundId;
	}
	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}
	public Integer getRefundFee() {
		return refundFee;
	}
	public void setRefundFee(Integer refundFee) {
		this.refundFee = refundFee;
	}
	public Integer getSettlementRefundFee() {
		return settlementRefundFee;
	}
	public void setSettlementRefundFee(Integer settlementRefundFee) {
		this.settlementRefundFee = settlementRefundFee;
	}
	public Integer getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(Integer totalFee) {
		this.totalFee = totalFee;
	}
	public Integer getSettlementTotalFee() {
		return settlementTotalFee;
	}
	public void setSettlementTotalFee(Integer settlementTotalFee) {
		this.settlementTotalFee = settlementTotalFee;
	}
	public String getFeeType() {
		return feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	public Integer getCashFee() {
		return cashFee;
	}
	public void setCashFee(Integer cashFee) {
		this.cashFee = cashFee;
	}
	public Integer getCashRefundFee() {
		return cashRefundFee;
	}
	public void setCashRefundFee(Integer cashRefundFee) {
		this.cashRefundFee = cashRefundFee;
	}
	public Integer getCouponRefundFee() {
		return couponRefundFee;
	}
	public void setCouponRefundFee(Integer couponRefundFee) {
		this.couponRefundFee = couponRefundFee;
	}
	public Integer getCouponRefundCount() {
		return couponRefundCount;
	}
	public void setCouponRefundCount(Integer couponRefundCount) {
		this.couponRefundCount = couponRefundCount;
	}
	public String getRefundChannel() {
		return refundChannel;
	}
	public void setRefundChannel(String refundChannel) {
		this.refundChannel = refundChannel;
	}
	public List<RefundVoucher> getrVouchers() {
		return rVouchers;
	}
	public void setrVouchers(List<RefundVoucher> rVouchers) {
		this.rVouchers = rVouchers;
	}
	
}
