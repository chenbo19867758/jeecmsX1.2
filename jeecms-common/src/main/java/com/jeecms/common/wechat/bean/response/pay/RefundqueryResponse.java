package com.jeecms.common.wechat.bean.response.pay;

import java.util.List;

import com.jeecms.common.wechat.bean.response.pay.common.AbstractSucceedResponse;
import com.jeecms.common.wechat.bean.response.pay.common.RefundqueryDto;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * @Description: 查询退款-response
 * @author: chenming
 * @date:   2018年10月12日 下午4:28:05     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@XStreamAlias("xml")
public class RefundqueryResponse extends AbstractSucceedResponse{
	/** 微信订单号*/
	private String transactionId;
	/** 商户订单号*/
	private String outTradeNo;
	/** 退款笔数*/
	private Integer refundCount;
	/** 订单总退款次数*/
	private Integer totalRefundCount;
	/** 退款金额*/
	private Long refundFee;
	/** 应结退款金额*/
	private Long settlementRefundFee;
	/** 应结退款金额*/
	private Long totalFee;
	/** 应结订单金额*/
	private Long settlementTotalFee;
	/** 标价币种*/
	private String feeType;
	/** 现金支付金额*/
	private Long cashFee;
	
	private List<RefundqueryDto> refundqueryDtos;
	
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
	
	public Integer getRefundCount() {
		return refundCount;
	}
	
	public void setRefundCount(Integer refundCount) {
		this.refundCount = refundCount;
	}
	
	public Integer getTotalRefundCount() {
		return totalRefundCount;
	}
	
	public void setTotalRefundCount(Integer totalRefundCount) {
		this.totalRefundCount = totalRefundCount;
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
	
	public String getFeeType() {
		return feeType;
	}
	
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	
	public Long getCashFee() {
		return cashFee;
	}
	
	public void setCashFee(Long cashFee) {
		this.cashFee = cashFee;
	}

	public List<RefundqueryDto> getRefundqueryDtos() {
		return refundqueryDtos;
	}

	public void setRefundqueryDtos(List<RefundqueryDto> refundqueryDtos) {
		this.refundqueryDtos = refundqueryDtos;
	}
}
