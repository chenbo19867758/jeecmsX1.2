package com.jeecms.common.wechat.bean.response.mp.material;

import com.jeecms.common.wechat.bean.response.mp.BaseResponse;

/**
 * 
 * @Description: 新增永久图文素材：response
 * @author: chenming
 * @date:   2018年7月30日 下午2:46:41     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class AddNewsResponse extends BaseResponse{
	
	/**
	 * 新增的图文消息素材的media_id
	 */
	private String mediaId;

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	
}
