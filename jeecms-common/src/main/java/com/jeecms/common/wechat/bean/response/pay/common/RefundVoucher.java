package com.jeecms.common.wechat.bean.response.pay.common;

/**
 * 
 * @Description: 优惠券信息--拥有一个下标值
 * @author: chenming
 * @date:   2018年11月12日 下午1:51:04     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class RefundVoucher {
	/**
	 * 代金券类型
	 * coupon_type_$n	----$n表示下标，从0开始。
	 */
	private String 	couponType;
	/** 退款代金券ID*/
	private String couponRefundId;
	/** 单个代金券退款金额*/
	private String couponRefundFee;
	
	public String getCouponType() {
		return couponType;
	}
	
	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}
	
	public String getCouponRefundId() {
		return couponRefundId;
	}
	
	public void setCouponRefundId(String couponRefundId) {
		this.couponRefundId = couponRefundId;
	}
	
	public String getCouponRefundFee() {
		return couponRefundFee;
	}
	
	public void setCouponRefundFee(String couponRefundFee) {
		this.couponRefundFee = couponRefundFee;
	}
}
