package com.jeecms.common.wechat.bean.request.mp.mass;

/**
 * {@link}https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=ACCESS_TOKEN
 * http请求方式: POST（请使用https协议）
 * @Description:群发视频请求消息对象
 * @author: ljw
 * @date:   2018年7月30日 上午10:05:29     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class MpvideoMassRequest {

	/**用于设定消息的接收者**/
	private Filter filter;
	
	public class Filter {
		
		/**用于设定是否向全部用户发送，值为true或false，选择true该消息群发给所有用户，
		 * 选择false可根据tag_id发送给指定群组的用户**/
		private Boolean isToAll;
		/**群发到的标签的tag_id，参见用户管理中用户分组接口，若is_to_all值为true，可不填写tag_id**/
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
	/**视频对象**/
	private Mpvideo mpvideo;
	/**消息类型**/
	private String msgtype;
	
	public class Mpvideo {
		
		private String mediaId;

		private String title;
		
		private String description;
		
		public String getMediaId() {
			return mediaId;
		}

		public void setMediaId(String mediaId) {
			this.mediaId = mediaId;
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
				
	}

	public Mpvideo getMpvideo() {
		return mpvideo;
	}

	public void setMpvideo(Mpvideo mpvideo) {
		this.mpvideo = mpvideo;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

}
