package com.jeecms.wechat.domain.dto;

import javax.validation.constraints.NotNull;

import com.jeecms.common.wechat.bean.request.mp.material.AddNewsRequest;

/**
 * 新增图文素材dto
 * 
 * @author: chenming
 * @date: 2019年6月5日 下午5:18:18
 */
public class AddNewsRequestDto {
	/** 新增图文素材对象 */
	private AddNewsRequest addNewsRequest;
	/** appId */
	private String appId;

	public AddNewsRequest getAddNewsRequest() {
		return addNewsRequest;
	}

	public void setAddNewsRequest(AddNewsRequest addNewsRequest) {
		this.addNewsRequest = addNewsRequest;
	}

	@NotNull
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

}
