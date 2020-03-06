package com.jeecms.common.wechat.bean.request.mp.material;

/**
 * 
 * @Description: 新增永久视频素材：request
 * @author: chenming
 * @date:   2018年7月30日 下午2:43:06     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class AddVideoMaterialRequest {
	/** 视频素材的标题*/
	private String title;
	/** 视频素材的描述*/
	private String introduction;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public AddVideoMaterialRequest(String title, String introduction) {
		super();
		this.title = title;
		this.introduction = introduction;
	}

	public AddVideoMaterialRequest() {
		super();
	}
	
}
