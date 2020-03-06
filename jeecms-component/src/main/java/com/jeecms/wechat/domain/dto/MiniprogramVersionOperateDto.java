package com.jeecms.wechat.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * 小程序版本操作dto
 * @author: chenming
 * @date:   2019年6月12日 下午7:33:10
 */
public class MiniprogramVersionOperateDto {
	/** 小程序appId*/
	private String appId;
	
	private Integer id;

	@NotBlank(groups = {NotIncludeId.class})
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@NotNull(groups = {IncludeId.class})
	@Null(groups = {NotIncludeId.class})
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * 包括id
	 */
	public interface IncludeId{
		
	}
	
	/**
	 * 不包括id
	 */
	public interface NotIncludeId{
		
	}
}
