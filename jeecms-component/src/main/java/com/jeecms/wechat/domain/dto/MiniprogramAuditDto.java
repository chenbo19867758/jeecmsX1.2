package com.jeecms.wechat.domain.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;

/**
 * 小程序提交审核dto
 * @author: chenming
 * @date:   2019年6月12日 下午7:54:57
 */
public class MiniprogramAuditDto {
	/** 提交审核dto */
	private List<AuditVersionDto> versionDto;
	/** 小程序appId */
	private String appId;

	public List<AuditVersionDto> getVersionDto() {
		return versionDto;
	}

	public void setVersionDto(List<AuditVersionDto> versionDto) {
		this.versionDto = versionDto;
	}

	@NotBlank
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

}
