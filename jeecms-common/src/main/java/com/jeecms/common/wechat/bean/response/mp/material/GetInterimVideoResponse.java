package com.jeecms.common.wechat.bean.response.mp.material;

import com.jeecms.common.wechat.bean.response.mp.BaseResponse;

/**
 * 获取临时视频素材，response请求
 * @author: chenming
 * @date:   2019年6月4日 下午5:29:08
 */
public class GetInterimVideoResponse extends BaseResponse{
	/** 视频链接*/
	private String videoUrl;

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
}
