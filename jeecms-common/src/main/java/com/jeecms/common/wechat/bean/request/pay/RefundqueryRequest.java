package com.jeecms.common.wechat.bean.request.pay;

import com.jeecms.common.wechat.bean.request.pay.common.AbstractPayParams;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * @Description: 查询退款-request
 * @author: chenming
 * @date:   2018年10月12日 下午2:58:42     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@XStreamAlias("xml")
public class RefundqueryRequest extends AbstractPayParams{
	/** 子商户公众账号ID*/
	private String subAppid;
	/** 子商户号*/
	private String subMchId;
	/** 微信订单号*/
	private String transactionId;
	/** 商户订单号*/
	private String outTradeNo;
	/** 商户退款单号*/
	private String outRefundNo;
	/** 微信退款单号*/
	private String refundId;
	/** 偏移量*/
	private Integer offset;
	
	public RefundqueryRequest(String subAppid, String subMchId, String transactionId, String outTradeNo,
			String outRefundNo, String refundId, Integer offset) {
		super();
		this.subAppid = subAppid;
		this.subMchId = subMchId;
		this.transactionId = transactionId;
		this.outTradeNo = outTradeNo;
		this.outRefundNo = outRefundNo;
		this.refundId = refundId;
		this.offset = offset;
	}
	
	public RefundqueryRequest() {
		super();
	}
	
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
	
	public String getRefundId() {
		return refundId;
	}
	
	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}
	
	public Integer getOffset() {
		return offset;
	}
	
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
}
