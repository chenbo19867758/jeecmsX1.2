/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.common.weibo.api.user;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.weibo.bean.request.user.WeiboUserRequest;
import com.jeecms.common.weibo.bean.response.user.WeiboUserResponse;

/**   
 * 微博用户Service
 * @author: ljw
 * @date:   2019年6月17日 上午11:43:40     
 */
public interface WeiboUserService {

	/**
	 * 获取微博用户信息（向微博后台请求）
	* @Title: getUser 
	* @param user 用户请求
	* @return WeiboUserResponse
	* @throws GlobalException 异常
	 */
	WeiboUserResponse getWeiboUser(WeiboUserRequest user) throws GlobalException;
	
}
