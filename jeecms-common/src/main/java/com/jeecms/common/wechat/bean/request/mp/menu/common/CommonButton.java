package com.jeecms.common.wechat.bean.request.mp.menu.common;

/**
 * 
 * @Description: 子菜单项：没有子菜单的菜单项，有可能是二级菜单项，也有可能是不含二级菜单的一级菜单
 * @author: chenming
 * @date:   2018年8月8日 下午2:04:59     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class CommonButton extends Button{
	
	/** 菜单的响应动作类型 */
	private String type;
	/** 菜单KEY值，用于消息接口推送(click等点击类型必须永久) */
	private String key;
	/** 网页链接(view、miniprogram类型必须) */
	private String url;
	/** 小程序的appid(仅认证公众号可配置，miniprogram类型必须)  */
	private String appid;
	/** 小程序的页面路径(miniprogram类型必须) */
	private String pagepath;
	/** 调用新增永久素材接口返回的合法media_id(media_id类型和view_limited类型必须) */
	private String mediaId;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getPagepath() {
		return pagepath;
	}

	public void setPagepath(String pagepath) {
		this.pagepath = pagepath;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	
}
