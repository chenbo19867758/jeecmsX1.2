package com.jeecms.common.wechat.api.mp;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.bean.request.mp.oauth2.GetOpenIdRequest;
import com.jeecms.common.wechat.bean.response.mp.oauth2.GetOpenIdResponse;

/**
 * 
 * @Description: 微信公众号网页授权获取openId与微信对接service接口
 * @author: chenming
 * @date:   2018年10月16日 下午2:05:01     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface WechatOauthApiService{
	
	/**
	 * 获取openId
	 * @Title: getOpenId  
	 * @param request
	 * @return
	 * @throws GlobalException      
	 * @return: GetOpenIdResponse
	 */
	GetOpenIdResponse getOpenId(GetOpenIdRequest request) throws GlobalException;
}
