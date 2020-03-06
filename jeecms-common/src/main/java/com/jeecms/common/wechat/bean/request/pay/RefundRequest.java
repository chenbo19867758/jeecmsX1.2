package com.jeecms.common.wechat.bean.request.pay;

import com.jeecms.common.wechat.bean.request.pay.common.AbstractPayParams;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * @Description: 申请退款request请求
 * @author: chenming
 * @date:   2018年9月28日 下午2:02:33     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@XStreamAlias("xml")
public class RefundRequest extends AbstractPayParams{
	/** 子商户公众账号ID*/
	@XStreamAlias("subAppid")
	private String subAppid;
	/** 子商户号*/
	@XStreamAlias("subMchId")
	private String subMchId;
	/** 微信订单号*/
	@XStreamAlias("transactionId")
	private String transactionId;
	/** 商户订单号*/
	@XStreamAlias("outTradeNo")
	private String outTradeNo;
	/** 商户退款单号*/
	@XStreamAlias("outRefundNo")
	private String outRefundNo;
	/** 订单金额*/
	@XStreamAlias("totalFee")
	private Long totalFee;
	/** 申请退款金额*/
	@XStreamAlias("refundFee")
	private Long refundFee;
	/** 退款货币种类*/
	@XStreamAlias("refundFeeType")
	private String refundFeeType;
	/** 退款原因*/
	@XStreamAlias("refundDesc")
	private String refundDesc;
	/** 退款资金来源*/
	@XStreamAlias("refundAccount")
	private String refundAccount;
	/** 退款结果通知url*/
	@XStreamAlias("notifyUrl")
	private String notifyUrl;
	
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
	
	public Long getTotalFee() {
		return totalFee;
	}
	
	public void setTotalFee(Long totalFee) {
		this.totalFee = totalFee;
	}
	
	public Long getRefundFee() {
		return refundFee;
	}
	
	public void setRefundFee(Long refundFee) {
		this.refundFee = refundFee;
	}
	
	public String getRefundFeeType() {
		return refundFeeType;
	}
	
	public void setRefundFeeType(String refundFeeType) {
		this.refundFeeType = refundFeeType;
	}
	
	public String getRefundDesc() {
		return refundDesc;
	}
	
	public void setRefundDesc(String refundDesc) {
		this.refundDesc = refundDesc;
	}
	
	public String getRefundAccount() {
		return refundAccount;
	}
	
	public void setRefundAccount(String refundAccount) {
		this.refundAccount = refundAccount;
	}
	
	public String getNotifyUrl() {
		return notifyUrl;
	}
	
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	
	public RefundRequest() {
		super();
	}
}
