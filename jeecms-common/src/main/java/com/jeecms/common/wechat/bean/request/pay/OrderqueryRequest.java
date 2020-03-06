package com.jeecms.common.wechat.bean.request.pay;

import com.jeecms.common.wechat.bean.request.pay.common.AbstractPayParams;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * @Description: 查询订单-request
 * @author: chenming
 * @date:   2018年9月12日 上午10:40:17     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@XStreamAlias("xml")
public class OrderqueryRequest extends AbstractPayParams{
	/** 微信分配的子商户公众账号ID*/
	@XStreamAlias("subAppid")
	private String subAppid;
	/** 微信支付分配的子商户号*/
	@XStreamAlias("subMchId")
	private String subMchId;
	/** 微信的订单号，优先使用*/
	@XStreamAlias("transactionId")
	private String transactionId;
	/** 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。*/
	@XStreamAlias("outTradeNo")
	private String outTradeNo;
	
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
	
	public OrderqueryRequest() {
		super();
	}
	
	public OrderqueryRequest(String subAppid, String subMchId, String transactionId, String outTradeNo) {
		super();
		this.subAppid = subAppid;
		this.subMchId = subMchId;
		this.transactionId = transactionId;
		this.outTradeNo = outTradeNo;
	}
}
