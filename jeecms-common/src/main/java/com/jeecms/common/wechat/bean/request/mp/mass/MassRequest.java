/**
 *  * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.common.wechat.bean.request.mp.mass;

/**
 * 群发图片请求消息对象
 * 
 * @link https:https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=ACCESS_TOKEN
 *       http请求方式: POST（请使用https协议）
 * @author: ljw
 * @date: 2018年7月30日 上午10:05:29
 */
public class MassRequest {

	/** 用于设定消息的接收者 **/
	private Filter filter;

	public class Filter {

		/**
		 * 用于设定是否向全部用户发送，值为true或false，选择true该消息群发给所有用户， 选择false可根据tag_id发送给指定群组的用户
		 **/
		private Boolean isToAll;
		/** 群发到的标签的tag_id，参见用户管理中用户分组接口，若is_to_all值为true，可不填写tag_id **/
		private Integer tagId;

		public Boolean getIsToAll() {
			return isToAll;
		}

		public void setIsToAll(Boolean isToAll) {
			this.isToAll = isToAll;
		}

		public Integer getTagId() {
			return tagId;
		}

		public void setTagId(Integer tagId) {
			this.tagId = tagId;
		}

	}

	/** 文本内容 **/
	private String content;
	/** 用于设定即将发送的图文消息 **/
	private String mpnews;
	/** 用于群发的消息的media_id **/
	private String mediaId;
	/**
	 * 群发的消息类型，图文消息为mpnews，文本消息为text，语音为voice，音乐为music，图片为image，视频为video，卡券为wxcard
	 **/
	private String msgtype;
	/** 消息的标题 **/
	private String title;
	/** 消息的描述 **/
	private String description;
	/** 视频缩略图的媒体ID **/
	private String thumbMediaId;
	/** 图文消息被判定为转载时，是否继续群发。 1为继续群发（转载），0为停止群发。 该参数默认为0。 **/
	private Integer sendIgnoreReprint;

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	public String getMpnews() {
		return mpnews;
	}

	public void setMpnews(String mpnews) {
		this.mpnews = mpnews;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getThumbMediaId() {
		return thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}

	public Integer getSendIgnoreReprint() {
		return sendIgnoreReprint;
	}

	public void setSendIgnoreReprint(Integer sendIgnoreReprint) {
		this.sendIgnoreReprint = sendIgnoreReprint;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
