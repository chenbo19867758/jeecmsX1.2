/**
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.common.wechat.bean.request.mp.user;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量获取用户基本信息请求接口
 * @link https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=ACCESS_TOKEN
 *       http请求方式: POST（请使用https协议） 
 * @author: ljw
 * @date: 2018年7月30日 上午10:05:29
 */
public class UserInfoRequest {

	/** openid集合 **/
	private List<Data> userList;

	public UserInfoRequest() {

	}
	
	/**
	 * 构造函数
	 * @param userList openID
	 */
	public UserInfoRequest(List<String> userList) {
		List<Data> list = new ArrayList<>();
		for (String string : userList) {
			Data data = new Data();
			data.setOpenid(string);
			list.add(data);
		}
		this.userList = list;
	}
	
	public class Data {

		/** 微信号的openid **/
		private String openid;

		public String getOpenid() {
			return openid;
		}

		public void setOpenid(String openid) {
			this.openid = openid;
		}
	}

	public List<Data> getUserList() {
		return userList;
	}

	public void setUserList(List<Data> userList) {
		this.userList = userList;
	}

}
