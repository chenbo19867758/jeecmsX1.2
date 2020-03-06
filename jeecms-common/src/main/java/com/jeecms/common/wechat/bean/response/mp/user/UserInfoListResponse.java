package com.jeecms.common.wechat.bean.response.mp.user;

import java.util.List;

import com.jeecms.common.wechat.bean.response.mp.BaseResponse;
import com.jeecms.common.wechat.bean.response.mp.user.UserInfoResponse;

/**
 * {@link}https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=ACCESS_TOKEN
 * @Description:批量获取用户基本信息返回参数
 * @author: ljw
 * @date:   2018年7月30日 上午10:05:29     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class UserInfoListResponse extends BaseResponse{

	/**用户列表**/
	private List<UserInfoResponse> userInfoList;

	public List<UserInfoResponse> getUserInfoList() {
		return userInfoList;
	}

	public void setUserInfoList(List<UserInfoResponse> userInfoList) {
		this.userInfoList = userInfoList;
	}	
}
