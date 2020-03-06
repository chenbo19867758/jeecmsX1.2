package com.jeecms.common.wechat.bean.response.mp.material;

import com.jeecms.common.wechat.bean.response.mp.BaseResponse;

/**
 * 
 * @Description: 新增其它类型永久素材：response
 * @author: chenming
 * @date:   2018年7月30日 下午2:47:15     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class AddMeterialResponse extends BaseResponse{
	
	/**
	 * 新增的永久素材的media_id
	 */
	private String mediaId;
	/**
	 * 新增的图片素材的图片URL（仅新增图片素材时会返回该字段）
	 */
	private String url;

	
	public String getMediaId() {
		return mediaId;
	}
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
