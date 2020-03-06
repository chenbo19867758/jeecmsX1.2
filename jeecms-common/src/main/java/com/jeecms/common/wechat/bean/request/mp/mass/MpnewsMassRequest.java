package com.jeecms.common.wechat.bean.request.mp.mass;

/**
 * {@link}https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=ACCESS_TOKEN
 * http请求方式: POST（请使用https协议）
 * @Description:群发图文请求消息对象
 * @author: ljw
 * @date:   2018年7月30日 上午10:05:29     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class MpnewsMassRequest {

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
	/**图文对象**/
	private Mpnews mpnews;
	/**消息类型**/
	private String msgtype;
	/**图文消息被判定为转载时，是否继续群发。 1为继续群发（转载），0为停止群发。 该参数默认为0。**/
	private Integer sendIgnoreReprint;
	/**避免重复推送做的参数调整**/
	private Long clientmsgid;
	
	public class Mpnews {
		
		private String mediaId;

		public String getMediaId() {
			return mediaId;
		}

		public void setMediaId(String mediaId) {
			this.mediaId = mediaId;
		}
		
	}

	public Mpnews getMpnews() {
		return mpnews;
	}

	public void setMpnews(Mpnews mpnews) {
		this.mpnews = mpnews;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public Integer getSendIgnoreReprint() {
		return sendIgnoreReprint;
	}

	public void setSendIgnoreReprint(Integer sendIgnoreReprint) {
		this.sendIgnoreReprint = sendIgnoreReprint;
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	public Long getClientmsgid() {
		return clientmsgid;
	}

	public void setClientmsgid(Long clientmsgid) {
		this.clientmsgid = clientmsgid;
	}
	
	
}
