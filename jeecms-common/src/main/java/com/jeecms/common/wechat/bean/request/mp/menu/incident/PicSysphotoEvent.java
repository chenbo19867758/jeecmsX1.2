package com.jeecms.common.wechat.bean.request.mp.menu.incident;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * TODO
 * @author: tom
 * @date:   2019年3月8日 下午4:40:14
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class PicSysphotoEvent extends BaseEvent{
	
	/** 发送的信息 */
	@XStreamAlias("SendPicsInfo")
	private String sendPicsInfo;
	
	/** 发送的图片数量 */
	@XStreamAlias("Count")
	private Integer count;
	
	/** 图片列表 */
	@XStreamAlias("PicList")
	private String picList;
	
	/** 图片的MD5值，开发者若需要，可用于验证接受到图片 */
	@XStreamAlias("PicMd5Sum")
	private String picMd5Sum;

	public String getSendPicsInfo() {
		return sendPicsInfo;
	}

	public void setSendPicsInfo(String sendPicsInfo) {
		this.sendPicsInfo = sendPicsInfo;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getPicList() {
		return picList;
	}

	public void setPicList(String picList) {
		this.picList = picList;
	}

	public String getPicMd5Sum() {
		return picMd5Sum;
	}

	public void setPicMd5Sum(String picMd5Sum) {
		this.picMd5Sum = picMd5Sum;
	}
	
}
