package com.jeecms.common.wechat.bean.request.mp.menu.common;

import java.util.List;

/**
 * 
 * @Description: 父菜单项：包含有二级菜单项的一级菜单，这类菜单项包含有二个
 *               属性：name和sub_button，而sub_button以是一个子菜单项数组
 * @author: chenming
 * @date: 2018年8月8日 下午2:08:01
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class ComplexButton extends Button {
	// 二级菜单数组，个数应为1~5个
	private List<ComplexButton> subButton;
	
	private String type;
	// 菜单KEY值，用于消息接口推送(click等点击类型必须永久)
	private String key;
	// 网页链接(view、miniprogram类型必须)
	private String url;
	// 小程序的appid(仅认证公众号可配置，miniprogram类型必须)
	private String appid;
	// 小程序的页面路径(miniprogram类型必须)
	private String pagepath;
	// 调用新增永久素材接口返回的合法media_id(media_id类型和view_limited类型必须)
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

	public List<ComplexButton> getSubButton() {
		return subButton;
	}

	public void setSubButton(List<ComplexButton> subButton) {
		this.subButton = subButton;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

}
