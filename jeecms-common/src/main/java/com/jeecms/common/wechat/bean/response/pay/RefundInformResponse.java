package com.jeecms.common.wechat.bean.response.pay;

import com.jeecms.common.wechat.bean.response.pay.common.AbstractSucceedResponse;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * @Description: 退款结果回调查询
 * @author: chenming
 * @date:   2018年11月8日 上午9:50:06     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@XStreamAlias("xml")
public class RefundInformResponse extends AbstractSucceedResponse{
	
	/** 加密信息*/
	private String 	reqInfo;

	public String getReqInfo() {
		return reqInfo;
	}

	public void setReqInfo(String reqInfo) {
		this.reqInfo = reqInfo;
	}

}
