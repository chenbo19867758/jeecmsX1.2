package com.jeecms.common.wechat.bean.response.pay;

/**
 * 
 * @Description: 微信支付无论其正确与否都返回该实体类中的参数
 * @author: chenming
 * @date:   2018年9月12日 上午11:08:28     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class AbstractPayResponse {
	/** SUCCESS/FAIL 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断*/
	private String returnCode;
	/** 返回信息，如非空，为错误原因，签名失败，参数格式校验错误*/
	private String returnMsg;
	
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnMsg() {
		return returnMsg;
	}
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	
	
}
