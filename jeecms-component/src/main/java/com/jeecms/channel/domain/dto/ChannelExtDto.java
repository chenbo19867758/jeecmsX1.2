package com.jeecms.channel.domain.dto;

import java.util.List;
import java.util.Map;

import com.jeecms.channel.domain.ChannelAttr;

/**
 * 修改栏目扩展内容Dto
 * 
 * @author: chenming
 * @date: 2019年4月25日 下午2:36:53
 */
public class ChannelExtDto {
	/** 栏目Id*/
	private Integer id;
	/** 评论设置 */
	private Short commentControl;
	/** 浏览设置 */
	private Short viewControl;
	/** 工作流 */
	private Integer workflowId;
	/** 栏目图片ID */
	private Integer resourceId;
	/** 栏目描述 */
	private String txt;
	/** 外部链接 */
	private String link;
	/** 外链是否新窗口打开 (0-否 1-是) */
	private Boolean linkTarget;
	/** 是否允许投稿 */
	private Boolean contribute;
	/** 自定义模板*/
//	private List<ChannelAttr> attrs; 
	/** 分页大小*/
	private Short pageSize;
	/** 栏目页是否包含分页*/
	private Boolean isListChannel;
	/** 栏目内容*/
//	private Map<String,String> txtMap;

	public Short getCommentControl() {
		return commentControl;
	}

	public void setCommentControl(Short commentControl) {
		this.commentControl = commentControl;
	}

	public Short getViewControl() {
		return viewControl;
	}

	public void setViewControl(Short viewControl) {
		this.viewControl = viewControl;
	}

	public Integer getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Integer workflowId) {
		this.workflowId = workflowId;
	}

	public Integer getResourceId() {
		return resourceId;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}

	public String getTxt() {
		return txt;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Boolean getLinkTarget() {
		return linkTarget;
	}

	public void setLinkTarget(Boolean linkTarget) {
		this.linkTarget = linkTarget;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getContribute() {
		return contribute;
	}

	public void setContribute(Boolean contribute) {
		this.contribute = contribute;
	}

	public Short getPageSize() {
		return pageSize;
	}

	public void setPageSize(Short pageSize) {
		this.pageSize = pageSize;
	}

	public Boolean getIsListChannel() {
		return isListChannel;
	}

	public void setIsListChannel(Boolean isListChannel) {
		this.isListChannel = isListChannel;
	}

//	public List<ChannelAttr> getAttrs() {
//		return attrs;
//	}
//
//	public void setAttrs(List<ChannelAttr> attrs) {
//		this.attrs = attrs;
//	}
//
//	public Map<String,String> getTxtMap() {
//		return txtMap;
//	}
//
//	public void setTxtMap(Map<String,String> txtMap) {
//		this.txtMap = txtMap;
//	}


}
