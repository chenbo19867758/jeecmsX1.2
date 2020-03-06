package com.jeecms.content.domain.dto;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 内容->"相关内容"dto接口
 * 
 * @author: chenming
 * @date: 2019年6月21日 下午4:51:09
 */
public class ContentRelationDto {
	/** 内容集合 */
	private List<Integer> contentIds;
	/** 相关内容*/
	private List<Integer> contentRelationIds;
	/** 内容id */
	private Integer contentId;
	
	private Integer contentRelationId;
	/** 排序true之前，false之后 **/
	private Boolean location;

	@Size(min = 1,groups = {ContentRelationSave.class})
	public List<Integer> getContentIds() {
		return contentIds;
	}

	public void setContentIds(List<Integer> contentIds) {
		this.contentIds = contentIds;
	}

	@NotNull(groups = {ContentRelationSave.class})
	public Integer getContentId() {
		return contentId;
	}

	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}

	@NotNull(groups = {ContentRelationSort.class})
	public Boolean getLocation() {
		return location;
	}

	public void setLocation(Boolean location) {
		this.location = location;
	}

	@Size(min = 1,groups = {ContentRelationSort.class})
	public List<Integer> getContentRelationIds() {
		return contentRelationIds;
	}

	public void setContentRelationIds(List<Integer> contentRelationIds) {
		this.contentRelationIds = contentRelationIds;
	}

	@NotNull(groups = {ContentRelationSort.class})
	public Integer getContentRelationId() {
		return contentRelationId;
	}

	public void setContentRelationId(Integer contentRelationId) {
		this.contentRelationId = contentRelationId;
	}

	public interface ContentRelationSave{
		
	}
	
	public interface ContentRelationSort{
		
	}
	
}
