package com.jeecms.common.wechat.bean.response.pay.common;

import java.util.List;

/**
 * 
 * @Description: 微信退款查询dto--只可能存在一个下标值
 * @author: chenming
 * @date:   2018年11月12日 下午1:40:36     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class RefundqueryDto {
	/** 退款状态
	 * refund_status_$n		----$n表示下标，从0开始。
	 */
	private String refundStatus;
	/** 退款资金来源*/
	private String refundAccount;
	/** 退款入账账户*/
	private String refundRecvAccout;
	/** 退款成功时间*/
	private String refundSuccessTime;
	/** 总代金券退款金额*/
	private String couponRefundFee;
	/** 退款代金券使用数量*/
	private String couponRefundCount;
	/** 申请退款金额*/
	private String refundFee;
	/** 退款金额*/
	private String settlementRefundFee;
	/** 商户退款单号*/
	private String outRefundNo;
	/** 微信退款单号*/
	private String refundId;
	/** 退款渠道*/
	private String refundChannel;
	
	/** 代金券集合*/
	private List<RefundqueryVoucher> refundVouchers;
	
	public String getRefundStatus() {
		return refundStatus;
	}
	
	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}
	
	public String getRefundAccount() {
		return refundAccount;
	}
	
	public void setRefundAccount(String refundAccount) {
		this.refundAccount = refundAccount;
	}
	
	public String getRefundRecvAccout() {
		return refundRecvAccout;
	}
	
	public void setRefundRecvAccout(String refundRecvAccout) {
		this.refundRecvAccout = refundRecvAccout;
	}
	
	public String getRefundSuccessTime() {
		return refundSuccessTime;
	}
	
	public void setRefundSuccessTime(String refundSuccessTime) {
		this.refundSuccessTime = refundSuccessTime;
	}
	
	public String getCouponRefundFee() {
		return couponRefundFee;
	}
	
	public void setCouponRefundFee(String couponRefundFee) {
		this.couponRefundFee = couponRefundFee;
	}
	
	public String getCouponRefundCount() {
		return couponRefundCount;
	}
	
	public void setCouponRefundCount(String couponRefundCount) {
		this.couponRefundCount = couponRefundCount;
	}
	
	public String getRefundFee() {
		return refundFee;
	}
	
	public void setRefundFee(String refundFee) {
		this.refundFee = refundFee;
	}
	
	public String getSettlementRefundFee() {
		return settlementRefundFee;
	}
	
	public void setSettlementRefundFee(String settlementRefundFee) {
		this.settlementRefundFee = settlementRefundFee;
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
	
	public String getRefundChannel() {
		return refundChannel;
	}
	
	public void setRefundChannel(String refundChannel) {
		this.refundChannel = refundChannel;
	}

	public List<RefundqueryVoucher> getRefundVouchers() {
		return refundVouchers;
	}

	public void setRefundVouchers(List<RefundqueryVoucher> refundVouchers) {
		this.refundVouchers = refundVouchers;
	}
}
