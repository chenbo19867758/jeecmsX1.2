package com.jeecms.common.wechat.bean.response.pay.common;

/**
 * 
 * @Description: 微信退款查询，优惠券信息--拥有两个下标值
 * @author: chenming
 * @date:   2018年11月12日 下午1:41:27     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class RefundqueryVoucher {
	
	/** 代金券类型
	 * coupon_type_$n_$m		$n--第n张代金券  $m--类型
	 */
	private String couponType;
	/** 退款代金券ID*/
	private String couponRefundId;
	/** 单个代金券退款金额*/
	private Long couponRefundFee;
	
	public String getCouponType() {
		return couponType;
	}
	
	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	public Long getCouponRefundFee() {
		return couponRefundFee;
	}

	public void setCouponRefundFee(Long couponRefundFee) {
		this.couponRefundFee = couponRefundFee;
	}

	public String getCouponRefundId() {
		return couponRefundId;
	}

	public void setCouponRefundId(String couponRefundId) {
		this.couponRefundId = couponRefundId;
	}
}
