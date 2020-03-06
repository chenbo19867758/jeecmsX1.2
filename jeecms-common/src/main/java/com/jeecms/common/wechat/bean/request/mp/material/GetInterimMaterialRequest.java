package com.jeecms.common.wechat.bean.request.mp.material;

/**
 * 获取临时素材request请求
 * 
 * @author: chenming
 * @date: 2019年6月4日 下午5:22:30
 */
public class GetInterimMaterialRequest {

	/** 要获取的素材的media_id */
	private String mediaId;

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public GetInterimMaterialRequest() {
		super();
	}

	public GetInterimMaterialRequest(String mediaId) {
		super();
		this.mediaId = mediaId;
	}
}