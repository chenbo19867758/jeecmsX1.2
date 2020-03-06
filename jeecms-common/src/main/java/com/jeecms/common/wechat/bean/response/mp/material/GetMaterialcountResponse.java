package com.jeecms.common.wechat.bean.response.mp.material;

import com.jeecms.common.wechat.bean.response.mp.BaseResponse;

/**
 * 
 * @Description: 获取素材总数：response
 * @author: chenming
 * @date:   2018年7月30日 下午3:00:06     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class GetMaterialcountResponse extends BaseResponse{
	/** 语音总数量 */
	private Integer voiceCount;
	/** 视频总数量 */
	private Integer videoCount;
	/** 图片总数量 */
	private Integer imageCount;
	/** 图文总数量 */
	private Integer newsCount;
	
	public Integer getVoiceCount() {
		return voiceCount;
	}
	public void setVoiceCount(Integer voiceCount) {
		this.voiceCount = voiceCount;
	}
	public Integer getVideoCount() {
		return videoCount;
	}
	public void setVideoCount(Integer videoCount) {
		this.videoCount = videoCount;
	}
	public Integer getImageCount() {
		return imageCount;
	}
	public void setImageCount(Integer imageCount) {
		this.imageCount = imageCount;
	}
	public Integer getNewsCount() {
		return newsCount;
	}
	public void setNewsCount(Integer newsCount) {
		this.newsCount = newsCount;
	}
	
	
	
}
