package com.jeecms.wechat.domain.dto;

import java.util.List;

import com.jeecms.common.wechat.bean.request.mp.material.DelMaterialRequest;

public class WechatDeleteDto {
	
	private List<DelMaterialRequest> requests;

	public List<DelMaterialRequest> getRequests() {
		return requests;
	}

	public void setRequests(List<DelMaterialRequest> requests) {
		this.requests = requests;
	}
}
