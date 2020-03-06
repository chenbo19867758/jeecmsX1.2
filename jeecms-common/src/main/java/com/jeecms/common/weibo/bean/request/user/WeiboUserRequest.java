/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.common.weibo.bean.request.user;

import com.jeecms.common.weibo.bean.request.BaseRequest;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 根据用户ID获取用户信息
 * @url https://api.weibo.com/2/users/show.json
 * @author: ljw
 * @date: 2019年6月17日 上午11:28:24
 */
public class WeiboUserRequest extends BaseRequest {

	// 参数uid与screen_name二者必选其一，且只能选其一；
	/** 需要查询的用户ID。 **/
	private Long uid;
	/** 需要查询的用户昵称。 **/
	@XStreamAlias("screen_name")
	private String screenName;

	public WeiboUserRequest() {
		super();
	}

	public WeiboUserRequest(String accessToken, Long uid) {
		super(accessToken);
		this.uid = uid;
	}
	
	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

}
