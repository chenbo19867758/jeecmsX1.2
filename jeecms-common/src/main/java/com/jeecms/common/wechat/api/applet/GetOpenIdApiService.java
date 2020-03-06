package com.jeecms.common.wechat.api.applet;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.bean.request.applet.GetAppletOpenIdRequest;
import com.jeecms.common.wechat.bean.response.applet.GetAppletOpenIdResponse;

/**
 * 
 * @Description: 微信小程序登录获取openId
 * @author: chenming
 * @date:   2018年11月12日 下午4:01:22     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface GetOpenIdApiService {
	
	/**
	 * 微信小程序获取openId
	 * @Title: getOpenId  
	 * @param request
	 * @return
	 * @throws GlobalException      
	 * @return: GetAppletOpenIdResponse
	 */
	GetAppletOpenIdResponse getOpenId(GetAppletOpenIdRequest request)throws GlobalException;
}
