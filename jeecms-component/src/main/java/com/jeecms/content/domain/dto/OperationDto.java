/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.domain.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;

/**
 * 内容操作Dto
 * 
 * @author: ljw
 * @date: 2019年5月21日 上午10:55:50
 */
public class OperationDto {

	/** 实体IDs集合 **/
	private List<Integer> ids = new ArrayList<Integer>(10);
	/** 置顶开始时间 **/
	private Date startTime;
	/** 置顶结束时间 **/
	private Date endTime;
	/** 内容类型ID **/
	private Integer contentTypeId;
	/** 是否新增内容类型，true 新增 false 取消 **/
	private Boolean add;
	/** 栏目ID **/
	private Integer channelId;
	/** 栏目集合ID **/
	private List<Integer> channelIds = new ArrayList<Integer>(10);
	/** 选择排序内容ID **/
	private Integer contentId;
	/** 排序true之前，false之后 **/
	private Boolean location;
	/** 创建方式 1.链接型引用 2.镜像型引用 **/
	private Integer createType;

	public OperationDto() {
	}

	@NotEmpty
	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getContentTypeId() {
		return contentTypeId;
	}

	public void setContentTypeId(Integer contentTypeId) {
		this.contentTypeId = contentTypeId;
	}

	public Boolean getAdd() {
		return add;
	}

	public void setAdd(Boolean add) {
		this.add = add;
	}

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public Integer getContentId() {
		return contentId;
	}

	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}

	public Boolean getLocation() {
		return location;
	}

	public void setLocation(Boolean location) {
		this.location = location;
	}

	public List<Integer> getChannelIds() {

		return channelIds;
	}

	public void setChannelIds(List<Integer> channelIds) {
		this.channelIds = channelIds;
	}

	public Integer getCreateType() {
		return createType;
	}

	public void setCreateType(Integer createType) {
		this.createType = createType;
	}
}
