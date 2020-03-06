/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.common.weibo.api.user.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.util.client.HttpUtil;
import com.jeecms.common.weibo.Constants;
import com.jeecms.common.weibo.api.user.FansService;
import com.jeecms.common.weibo.bean.request.user.WeiboFansRequest;
import com.jeecms.common.weibo.bean.response.user.WeiboFansResponse;
import com.jeecms.common.weibo.bean.response.user.WeiboFansResponse.WeiboFansCommon;

/**   
 * 微博用户信息Service
 * @author: ljw
 * @date:   2019年6月17日 上午11:51:02     
 */
@Service
public class FansServiceImpl implements FansService {

	/** 根据用户ID获取用户信息 **/
	public static final String FANS_SHOW_URL = Constants.BASE_URL.concat("friendships/followers.json");
	
	public static final String ACCESS_TOKEN = "access_token";
	public static final String UID = "uid";
	public static final String CURSOR = "cursor";

	@Override
	public List<WeiboFansCommon> getWeiboFans(WeiboFansRequest fans) throws GlobalException {
		List<WeiboFansCommon> list = new ArrayList<WeiboFansResponse.WeiboFansCommon>(10);
		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, fans.getAccessToken());
		params.put(UID, fans.getUid().toString());
		Boolean flag = true;
		Integer cursor = 0;
		//如果还有粉丝数据，继续请求微博接口
		while (flag) {
			params.put(CURSOR, cursor.toString());
			WeiboFansResponse response = HttpUtil.getJsonBean(FANS_SHOW_URL, params, 
					WeiboFansResponse.class);
			if (!(response.getErrorCode() == null)) {
				break;
			}
			flag = !response.getUsers().isEmpty();
			if (response != null && flag) {
				list.addAll(response.getUsers());
			}
			cursor = response.getNextCursor();
		}
		return list;
	}

}
