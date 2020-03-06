package com.jeecms.common.wechat.api.open;

import java.util.Map;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.open.GetOpenIdRequest;

/**
 * 
 * @Description:微信第三方平台发起微信网页授权(用于获取用户openId或是用户所有信息)
 * @author: ztx
 * @date: 2018年10月16日 下午2:00:33
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface WeChatLoginApiService {

	/**
	 * 获取到openId
	 * 
	 * @Title: gOpenIdRequest
	 * @param getOpenIdRequest
	 * @param validateToken
	 * @return
	 * @throws GlobalException
	 * @return: GetOpenIdRequest
	 */
	Map<String, Object> login(GetOpenIdRequest getOpenIdRequest, ValidateToken validateToken) throws GlobalException;

}
