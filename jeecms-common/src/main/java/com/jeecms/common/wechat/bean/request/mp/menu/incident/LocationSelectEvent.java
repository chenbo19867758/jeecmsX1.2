package com.jeecms.common.wechat.bean.request.mp.menu.incident;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * TODO
 * @author: tom
 * @date:   2019年3月8日 下午4:39:52
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class LocationSelectEvent extends BaseEvent{
	
	/** 发送的位置信息 */
	@XStreamAlias("SendLocationInfo")
	private String sendLocationInfo;
	
	/** X坐标信息 */
	@XStreamAlias("Location_X")
	private String locationX;
	
	/** Y坐标信息 */
	@XStreamAlias("Location_Y")
	private String locationY;
	
	/** 精度，可理解为精度或者比例尺、越精细的话 scale越高 */
	@XStreamAlias("Scale")
	private String scale;
	
	/** 地理位置的字符串信息 */
	@XStreamAlias("Label")
	private String label;
	
	/** 朋友圈POI的名字，可能为空 */
	@XStreamAlias("Poiname")
	private String poiname;

	public String getSendLocationInfo() {
		return sendLocationInfo;
	}

	public void setSendLocationInfo(String sendLocationInfo) {
		this.sendLocationInfo = sendLocationInfo;
	}

	public String getLocationX() {
		return locationX;
	}

	public void setLocationX(String locationX) {
		this.locationX = locationX;
	}

	public String getLocationY() {
		return locationY;
	}

	public void setLocationY(String locationY) {
		this.locationY = locationY;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getPoiname() {
		return poiname;
	}

	public void setPoiname(String poiname) {
		this.poiname = poiname;
	}
}
