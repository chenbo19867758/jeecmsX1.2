package com.jeecms.common.wechat.api.pay;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.bean.request.pay.OrderqueryRequest;
import com.jeecms.common.wechat.bean.request.pay.RefundRequest;
import com.jeecms.common.wechat.bean.request.pay.RefundqueryRequest;
import com.jeecms.common.wechat.bean.request.pay.UnifiedorderRequest;
import com.jeecms.common.wechat.bean.response.pay.OrderqueryResponse;
import com.jeecms.common.wechat.bean.response.pay.RefundResponse;
import com.jeecms.common.wechat.bean.response.pay.RefundqueryResponse;
import com.jeecms.common.wechat.bean.response.pay.UnifiedorderResponse;

/***
 * 
 * @Description: 统一下单service接口
 * @author: chenming
 * @date:   2018年9月7日 上午8:59:16     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface WechatPayApiService {
	
	/**
	 * 微信支付，统一下单
	 * @Title: unifiedOrder  
	 * @param unifiedorderRequest
	 * @param validateToken
	 * @return
	 * @throws GlobalException      
	 * @return: UnifiedorderSucceedResponse
	 */
	UnifiedorderResponse unifiedOrder(UnifiedorderRequest unifiedorderRequest)throws GlobalException;
	
	/**
	 * 微信支付，申请退款
	 * @Title: refund  
	 * @param refundRequest
	 * @param mchId
	 * @return
	 * @throws Exception      
	 * @return: RefundResponse
	 */
	RefundResponse refund(RefundRequest refundRequest,String mchId)throws Exception;
	
	/**
	 * 微信支付，查询订单
	 * @Title: orderquery  
	 * @param request
	 * @return
	 * @throws GlobalException      
	 * @return: OrderqueryResponse
	 */
	OrderqueryResponse orderquery(OrderqueryRequest request) throws GlobalException;
	
	/**
	 * 微信支付，查询退款
	 * @Title: refundquery  
	 * @param request
	 * @return
	 * @throws Exception      
	 * @return: RefundqueryResponse
	 */
	RefundqueryResponse refundquery(RefundqueryRequest request) throws Exception;
	
}
