package com.jeecms.common.wechat.bean.request.mp.news;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * TODO
 * @author: tom
 * @date:   2019年3月8日 下午4:41:09
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@XStreamAlias("xml")
public class ReqMessage extends BaseMessage {
	/** 内容 */
	@XStreamAlias("Content")
	private String content;
	@XStreamAlias("MsgId")
	private String msgId;
	/** 事件类型，subscribe(订阅)、unsubscribe(取消订阅) */
	@XStreamAlias("Event")
	private String event;
	/** 事件KEY值，qrscene_为前缀，后面为二维码的参数值 */
	@XStreamAlias("EventKey")
	private String eventKey;
	/** 地理位置纬度 */
	@XStreamAlias("Latitude")
	private String latitude;
	/** 地理位置经度 */
	@XStreamAlias("Longitude")
	private String longitude;
	/** 地理位置纬度 */
	@XStreamAlias("Precision")
	private String precision;
	/** 二维码的ticket，可用来换取二维码图片 */
	@XStreamAlias("Ticket")
	private String ticket;
	/** 获取到的菜单MenuId */
	@XStreamAlias("MenuId")
	private String menuId;
	@XStreamAlias("SuccTime")
	private Long succTime;
	@XStreamAlias("Reason")
	private String reason;
	@XStreamAlias("FailTime")
	private Long failTime;
	
	@XStreamAlias("PicUrl")
	private String picUrl;
	@XStreamAlias("MsgType")
	private String msgType;
	@XStreamAlias("Format")
	private String format;
	@XStreamAlias("Recognition")
	private String recognition;
	@XStreamAlias("ThumbMediaId")
	private String thumbMediaId;
	@XStreamAlias("Location_X")
	private Double location_X;
	@XStreamAlias("location_Y")
	private Double Location_Y;
	@XStreamAlias("Scale")
	private Long scale;
	@XStreamAlias("Label")
	private String label;
	@XStreamAlias("Title")
	private String title;
	@XStreamAlias("Description")
	private String description;
	@XStreamAlias("Url")
	private String url;
	@XStreamAlias("MediaId")
	private String mediaId;
	
	public Long getSuccTime() {
		return succTime;
	}

	public void setSuccTime(Long succTime) {
		this.succTime = succTime;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Long getFailTime() {
		return failTime;
	}

	public void setFailTime(Long failTime) {
		this.failTime = failTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getEventKey() {
		return eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getPrecision() {
		return precision;
	}

	public void setPrecision(String precision) {
		this.precision = precision;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	
	
	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getRecognition() {
		return recognition;
	}

	public void setRecognition(String recognition) {
		this.recognition = recognition;
	}

	public String getThumbMediaId() {
		return thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}

	public Double getLocation_X() {
		return location_X;
	}

	public void setLocation_X(Double location_X) {
		this.location_X = location_X;
	}

	public Double getLocation_Y() {
		return Location_Y;
	}

	public void setLocation_Y(Double location_Y) {
		Location_Y = location_Y;
	}

	public Long getScale() {
		return scale;
	}

	public void setScale(Long scale) {
		this.scale = scale;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((event == null) ? 0 : event.hashCode());
		result = prime * result + ((eventKey == null) ? 0 : eventKey.hashCode());
		result = prime * result + ((latitude == null) ? 0 : latitude.hashCode());
		result = prime * result + ((longitude == null) ? 0 : longitude.hashCode());
		result = prime * result + ((menuId == null) ? 0 : menuId.hashCode());
		result = prime * result + ((msgId == null) ? 0 : msgId.hashCode());
		result = prime * result + ((precision == null) ? 0 : precision.hashCode());
		result = prime * result + ((ticket == null) ? 0 : ticket.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ReqMessage other = (ReqMessage) obj;
		if (content == null) {
			if (other.content != null) {
				return false;
			}
		} else if (!content.equals(other.content)) {
			return false;
		}
		if (event == null) {
			if (other.event != null) {
				return false;
			}
		} else if (!event.equals(other.event)) {
			return false;
		}
		if (eventKey == null) {
			if (other.eventKey != null) {
				return false;
			}
		} else if (!eventKey.equals(other.eventKey)) {
			return false;
		}
		if (latitude == null) {
			if (other.latitude != null) {
				return false;
			}
		} else if (!latitude.equals(other.latitude)) {
			return false;
		}
		if (longitude == null) {
			if (other.longitude != null) {
				return false;
			}
		} else if (!longitude.equals(other.longitude)) {
			return false;
		}
		if (menuId == null) {
			if (other.menuId != null) {
				return false;
			}
		} else if (!menuId.equals(other.menuId)) {
			return false;
		}
		if (msgId == null) {
			if (other.msgId != null) {
				return false;
			}
		} else if (!msgId.equals(other.msgId)) {
			return false;
		}
		if (precision == null) {
			if (other.precision != null) {
				return false;
			}

		} else if (!precision.equals(other.precision)) {
			return false;
		}
		if (ticket == null) {
			if (other.ticket != null) {
				return false;
			}
		} else if (!ticket.equals(other.ticket)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ReqMessage [Content=" + content + ", MsgId=" + msgId + ", Event=" + event + ", EventKey=" + eventKey
				+ ", Latitude=" + latitude + ", Longitude=" + longitude + ", Precision=" + precision + ", Ticket="
				+ ticket + ", MenuId=" + menuId + "]";
	}

}
