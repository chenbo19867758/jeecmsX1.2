/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.common.weibo.api.user.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.WeChatExceptionInfo;
import com.jeecms.common.wechat.util.client.HttpUtil;
import com.jeecms.common.weibo.Constants;
import com.jeecms.common.weibo.api.user.WeiboUserService;
import com.jeecms.common.weibo.bean.request.user.WeiboUserRequest;
import com.jeecms.common.weibo.bean.response.user.WeiboUserResponse;

/**   
 * 微博用户信息Service
 * @author: ljw
 * @date:   2019年6月17日 上午11:51:02     
 */
@Service
public class WeiboUserServiceImpl implements WeiboUserService {

	/** 根据用户ID获取用户信息 **/
	public static final String USER_SHOW_URL = Constants.BASE_URL.concat("users/show.json");
	
	public static final String ACCESS_TOKEN = "access_token";
	public static final String UID = "uid";

	@Override
	public WeiboUserResponse getWeiboUser(WeiboUserRequest user) throws GlobalException {
		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, user.getAccessToken());
		params.put(UID, user.getUid().toString());
		WeiboUserResponse userResponse = HttpUtil.getJsonBean(USER_SHOW_URL, params, 
				WeiboUserResponse.class);
		if (userResponse.getErrorCode() == null) {
			return userResponse;
		} else {
			throw new GlobalException(
					new WeChatExceptionInfo(userResponse.getErrorCode().toString(),
							userResponse.getError(),false));
		}
	}

}
