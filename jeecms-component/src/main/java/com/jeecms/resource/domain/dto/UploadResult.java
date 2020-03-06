/**
 * 
 */
package com.jeecms.resource.domain.dto;

/**
 * @Description:上传结果
 * @author: tom
 * @date: 2018年4月14日 下午4:53:55
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class UploadResult {
	private String fileUrl;
	private String origName;
	private Long fileSize;
	private String dimensions;
	private Integer resourceId;
	private Short resourceType;
	/** 视频或音频的时长(单位: 秒) */
	private Integer duration;

	public UploadResult(String fileUrl, String origName, Long fileSize, String dimensions, Short resourceType) {
		super();
		this.fileUrl = fileUrl;
		this.origName = origName;
		this.fileSize = fileSize;
		this.dimensions = dimensions;
		this.resourceType = resourceType;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getOrigName() {
		return origName;
	}

	public void setOrigName(String origName) {
		this.origName = origName;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getDimensions() {
		return dimensions;
	}

	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;
	}

	public Integer getResourceId() {
		return resourceId;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}

	public Short getResourceType() {
		return resourceType;
	}

	public void setResourceType(Short resourceType) {
		this.resourceType = resourceType;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	
	
	

}
