/**
 *  * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.common.wechat.bean.request.mp.message;

/**
 * 公众号可通过该接口发送信息；
 * http请求方式: POST（请使用https协议）
 * @link https://api.weixin.qq.com/cgi-bin/message/mass/preview?access_token=ACCESS_TOKEN
 * @author: ljw
 * @date: 2018年7月30日 上午10:05:29
 */
public class MessageRequest {

	/**公众号APPID**/
	private String appId;
	/** 接收消息用户对应该公众号的openid，该字段也可以改为towxname，以实现对微信号的预览 **/
	private String touser;
	/**
	 * 群发的消息类型，图文消息为mpnews，文本消息为text，语音为voice，音乐为music，图片为image，视频为video，卡券为wxcard
	 **/
	private String msgtype;
	/** 用于群发的消息的media_id **/
	private String mediaId;
	/** 发送文本消息时文本的内容 **/
	private String content;

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

}
