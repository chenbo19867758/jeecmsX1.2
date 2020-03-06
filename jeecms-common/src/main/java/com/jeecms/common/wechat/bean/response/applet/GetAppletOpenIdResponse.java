package com.jeecms.common.wechat.bean.response.applet;

/**
 * 
 * @Description: 微信小程序获取openId--response返回
 * @author: chenming
 * @date:   2018年11月12日 下午4:15:00     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class GetAppletOpenIdResponse extends BaseAppletResponse{
	/** 用户唯一标识*/
	private String openid;
	/** 会话秘钥*/
	private String sessionKey;
	/** 用户在开放平台的唯一标识符*/
	private String unionid;

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
}
