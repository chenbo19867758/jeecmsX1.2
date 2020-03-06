/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.common.weibo.api.user;

import java.util.List;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.weibo.bean.request.user.WeiboFansRequest;
import com.jeecms.common.weibo.bean.response.user.WeiboFansResponse.WeiboFansCommon;

/**   
 * 微博粉丝
 * @author: ljw
 * @date:   2019年6月18日 上午10:29:39     
 */
public interface FansService {

	/**
	 * 获取微博用户粉丝信息（向微博后台请求）
	* @Title: getWeiboFans 
	* @param fans 用户请求
	* @return WeiboFansResponse
	* @throws GlobalException 异常
	 */
	List<WeiboFansCommon> getWeiboFans(WeiboFansRequest fans) throws GlobalException;
}
