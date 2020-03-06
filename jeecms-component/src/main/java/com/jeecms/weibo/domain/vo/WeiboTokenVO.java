/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.weibo.domain.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 微博Token对象
 * 
 * @author: ljw
 * @date: 2019年6月15日 下午2:39:12
 */
public class WeiboTokenVO {

	/**
	 * 用户授权的唯一票据，用于调用微博的开放接口， 同时也是第三方应用验证微博用户登录的唯一票据， 第三方应用应该用该票据和自己应用内的用户建立唯一影射关系，
	 * 来识别登录状态，不能使用本返回值里的UID字段来做登录识别。
	 **/
	@XStreamAlias("access_token")
	private String accessToken;
	/** access_token的生命周期（该参数即将废弃，开发者请使用expires_in）。 **/
	@XStreamAlias("remind_in")
	private String remindIn;
	/** access_token的生命周期，单位是秒数。 **/
	@XStreamAlias("expires_in")
	private Long expiresIn;
	/** 授权用户的UID **/
	private String uid;
	private String isRealName;

	public WeiboTokenVO() {
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRemindIn() {
		return remindIn;
	}

	public void setRemindIn(String remindIn) {
		this.remindIn = remindIn;
	}

	public Long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Long expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getIsRealName() {
		return isRealName;
	}

	public void setIsRealName(String isRealName) {
		this.isRealName = isRealName;
	}
	
	@Override
	public String toString() {
		return "WeiboTokenVO [" + "accessToken=" + accessToken + ", "
				+ "expireIn=" + expiresIn + ", uid=" + uid + ",uid="
				+ uid + "]";
	}
}
