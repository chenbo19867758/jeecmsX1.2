package com.jeecms.common.wechat.bean.response.mp.material;

import com.jeecms.common.wechat.bean.response.mp.BaseResponse;

/**
 * 
 * @Description: 获取视频消息素材：response
 * @author: chenming
 * @date:   2018年7月30日 下午6:04:08     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class GetVideoResponse extends BaseResponse{
	/** 视频标题 */
	private String title;
	/** 视频素材描述 */
	private String description;
	private String downUrl;
	
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
	public String getDownUrl() {
		return downUrl;
	}
	public void setDownUrl(String downUrl) {
		this.downUrl = downUrl;
	}
	
	
}
