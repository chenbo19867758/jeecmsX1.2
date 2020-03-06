package com.jeecms.common.wechat.bean.response.pay.common;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * @Description: 退款结果的加密信息
 * @author: chenming
 * @date:   2018年11月8日 下午1:48:23     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@XStreamAlias("root")
public class RefundInformDecode{
	/** 微信订单号*/
	private String transactionId;
	/** 商户订单号*/
	private String outTradeNo;
	/** 微信退款单号*/
	private String refundId;
	/** 商户退款单号*/
	private String outRefundNo;
	/** 订单金额*/
	private Long totalFee;
	/** 应结订单金额*/
	private Long settlementTotalFee;
	/** 申请退款金额*/
	private Long refundFee;
	/** 退款金额*/
	private Long settlementRefundFee;
	/** 退款状态*/
	private String refundStatus;
	/** 退款成功时间*/
	private String successTime;
	/** 退款入账账户*/
	private String refundRecvAccout;
	/** 退款资金来源*/
	private String refundAccount;
	/** 退款发起来源*/
	private String refundRequestSource;
	
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
	
	public String getRefundId() {
		return refundId;
	}
	
	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}
	
	public String getOutRefundNo() {
		return outRefundNo;
	}
	
	public void setOutRefundNo(String outRefundNo) {
		this.outRefundNo = outRefundNo;
	}
	
	public Long getTotalFee() {
		return totalFee;
	}
	
	public void setTotalFee(Long totalFee) {
		this.totalFee = totalFee;
	}
	
	public Long getSettlementTotalFee() {
		return settlementTotalFee;
	}
	
	public void setSettlementTotalFee(Long settlementTotalFee) {
		this.settlementTotalFee = settlementTotalFee;
	}
	
	public Long getRefundFee() {
		return refundFee;
	}
	
	public void setRefundFee(Long refundFee) {
		this.refundFee = refundFee;
	}
	
	public Long getSettlementRefundFee() {
		return settlementRefundFee;
	}
	
	public void setSettlementRefundFee(Long settlementRefundFee) {
		this.settlementRefundFee = settlementRefundFee;
	}
	
	public String getRefundStatus() {
		return refundStatus;
	}
	
	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}
	
	public String getSuccessTime() {
		return successTime;
	}
	
	public void setSuccessTime(String successTime) {
		this.successTime = successTime;
	}
	
	public String getRefundRecvAccout() {
		return refundRecvAccout;
	}
	
	public void setRefundRecvAccout(String refundRecvAccout) {
		this.refundRecvAccout = refundRecvAccout;
	}
	
	public String getRefundAccount() {
		return refundAccount;
	}
	
	public void setRefundAccount(String refundAccount) {
		this.refundAccount = refundAccount;
	}
	
	public String getRefundRequestSource() {
		return refundRequestSource;
	}
	
	public void setRefundRequestSource(String refundRequestSource) {
		this.refundRequestSource = refundRequestSource;
	}
}
