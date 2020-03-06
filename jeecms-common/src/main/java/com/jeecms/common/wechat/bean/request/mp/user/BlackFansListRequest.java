/**
 *  * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.common.wechat.bean.request.mp.user;

/**
 * https://api.weixin.qq.com/cgi-bin/tags/members/getblacklist?access_token=ACCESS_TOKEN
 * http请求方式: POST（请使用https协议）
 * 
 * @Description 公众号可通过该接口来获取帐号的黑名单列表， 黑名单列表由一串
 *              OpenID（加密后的微信号，每个用户对每个公众号的OpenID是唯一的）组成。 该接口每次调用最多可拉取 10000
 *              个OpenID，当列表数较多时，可以通过多次拉取的方式来满足需求。
 * @author: ljw
 * @date: 2018年8月31日 上午10:05:29
 */
public class BlackFansListRequest {

	/** 调用接口凭证 **/
	private String accessToken;

	/** 第一个拉取的OPENID，不填默认从头开始拉取 **/
	private String beginOpenid;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getBeginOpenid() {
		return beginOpenid;
	}

	public void setBeginOpenid(String beginOpenid) {
		this.beginOpenid = beginOpenid;
	}
}
