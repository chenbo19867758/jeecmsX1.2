package com.jeecms.common.wechat.bean.response.pay;

import com.jeecms.common.wechat.bean.response.pay.common.AbstractSucceedResponse;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * @Description: 支付结果通知
 * @author: chenming
 * @date: 2018年9月12日 上午11:05:23
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@XStreamAlias("xml")
public class PayResultInformResponse extends AbstractSucceedResponse {
	/** 用户在商户appid下的唯一标识 */
	private String openid;
	/** 用户是否关注公众账号，Y-关注，N-未关注，仅在公众账号类型支付有效 */
	private String isSubscribe;
	/** 用户在子商户appid下的唯一标识 */
	private String subOpenid;
	/** 用户是否关注子公众账号，Y-关注，N-未关注，仅在公众账号类型支付有效 */
	private String subIsSubscribe;
	/** JSAPI、NATIVE、APP */
	private String tradeType;
	/** 银行类型，采用字符串类型的银行标识，银行类型见附表 */
	private String bankType;
	/** 订单总金额，单位为分 */
	private Integer totalFee;
	/** 货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型 */
	private String feeType;
	/** 现金支付金额订单现金支付金额，详见支付金额 */
	private Integer cashFee;
	/** 货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型 */
	private String cashFeeType;
	/** 应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额。 */
	private Integer settlementTotalFee;
	/** 代金券或立减优惠金额<=订单总金额，订单总金额-代金券或立减优惠金额=现金支付金额，详见支付金额 */
	private Integer couponFee;
	/** 代金券或立减优惠使用数量 */
	private Integer couponCount;
	/**
	 * CASH--充值代金券
	 * ，NO_CASH---非充值代金券，并且订单使用了免充值券后有返回（取值：CASH、NO_CASH）。$n为下标,从0开始编号，举例：
	 * coupon_type_$0
	 */
	private Integer coupon_type_$n;
	/** 代金券或立减优惠ID, $n为下标，从1开始编号 */
	private String coupon_id_$n;
	/** 单个代金券或立减优惠支付金额, $n为下标，从1开始编号 */
	private Integer coupon_fee_$n;
	/** 微信支付订单号 */
	private String transactionId;
	/** 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。 */
	private String outTradeNo;
	/** 商家数据包，原样返回 */
	private String attach;
	/**
	 * 支付完成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
	 */
	private String timeEnd;

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

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	public Integer getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Integer totalFee) {
		this.totalFee = totalFee;
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

	public String getCashFeeType() {
		return cashFeeType;
	}

	public void setCashFeeType(String cashFeeType) {
		this.cashFeeType = cashFeeType;
	}

	public Integer getSettlementTotalFee() {
		return settlementTotalFee;
	}

	public void setSettlementTotalFee(Integer settlementTotalFee) {
		this.settlementTotalFee = settlementTotalFee;
	}

	public Integer getCouponFee() {
		return couponFee;
	}

	public void setCouponFee(Integer couponFee) {
		this.couponFee = couponFee;
	}

	public Integer getCouponCount() {
		return couponCount;
	}

	public void setCouponCount(Integer couponCount) {
		this.couponCount = couponCount;
	}

	public Integer getCoupon_type_$n() {
		return coupon_type_$n;
	}

	public void setCoupon_type_$n(Integer coupon_type_$n) {
		this.coupon_type_$n = coupon_type_$n;
	}

	public String getCoupon_id_$n() {
		return coupon_id_$n;
	}

	public void setCoupon_id_$n(String coupon_id_$n) {
		this.coupon_id_$n = coupon_id_$n;
	}

	public Integer getCoupon_fee_$n() {
		return coupon_fee_$n;
	}

	public void setCoupon_fee_$n(Integer coupon_fee_$n) {
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

	@Override
	public String toString() {
		return "PayResultInformResponse [openid=" + openid + ", is_subscribe=" + isSubscribe + ", sub_openid="
				+ subOpenid + ", sub_is_subscribe=" + subIsSubscribe + ", trade_type=" + tradeType + ", bank_type="
				+ bankType + ", total_fee=" + totalFee + ", fee_type=" + feeType + ", cash_fee=" + cashFee
				+ ", cash_fee_type=" + cashFeeType + ", settlement_total_fee=" + settlementTotalFee
				+ ", coupon_fee=" + couponFee + ", coupon_count=" + couponCount + ", coupon_type_$n=" + coupon_type_$n
				+ ", coupon_id_$n=" + coupon_id_$n + ", coupon_fee_$n=" + coupon_fee_$n + ", transaction_id="
				+ transactionId + ", out_trade_no=" + outTradeNo + ", attach=" + attach + ", time_end=" + timeEnd
				+ "]";
	}

}
