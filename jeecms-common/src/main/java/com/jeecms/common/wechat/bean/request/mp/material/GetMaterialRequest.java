package com.jeecms.common.wechat.bean.request.mp.material;

/**
 * 
 * @Description:获取永久素材
 * @author: chenming
 * @date:   2018年7月30日 上午9:53:17     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class GetMaterialRequest {
	
	public GetMaterialRequest() {
		super();
	}

	/** 要获取的素材的media_id*/
	private String mediaId;

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public GetMaterialRequest(String mediaId) {
		super();
		this.mediaId = mediaId;
	}
	
}
