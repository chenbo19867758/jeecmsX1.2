package com.jeecms.channel.domain.vo;

import com.jeecms.resource.domain.ResourcesSpaceData;

/**
 * 展示的数据
 * 
 * @author: chenming
 * @date: 2019年4月18日 上午8:46:52
 */
public class ChannelVo {
	/** 栏目名称 */
	private String channelName;
	/** 访问路径 */
	private String channelPath;
	/** 显示在栏目循环列表 */
	private Boolean display;
	/** 栏目PC端模板 */
	private String tplPc;
	/** 栏目手机端模板 */
	private String tplMobile;
	/** 评论设置 */
	private Short commentControl;
	/** 浏览设置*/
	private Short viewControl;
	/** 工作流 */
	private Integer workflowId;
	/** 外部链接 */
	private String link;
	/** 外链是否新窗口打开 (0-否 1-是) */
	private Boolean linkTarget;
	/** SEO标题 */
	private String seoTitle;
	/** SEO关键字 */
	private String seoKeywork;
	/** SEO描述 */
	private String seoDescription;
	/** 是否允许投稿 */
	private Boolean contribute;
	/** 栏目图片ID */
	private Integer resourceId;
	/** 栏目对象 */
	private ResourcesSpaceData resourcesSpaceData;
	/** 栏目描述 */
	private String txt;

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getChannelPath() {
		return channelPath;
	}

	public void setChannelPath(String channelPath) {
		this.channelPath = channelPath;
	}

	public Boolean getDisplay() {
		return display;
	}

	public void setDisplay(Boolean display) {
		this.display = display;
	}

	public String getTplPc() {
		return tplPc;
	}

	public void setTplPc(String tplPc) {
		this.tplPc = tplPc;
	}

	public String getTplMobile() {
		return tplMobile;
	}

	public void setTplMobile(String tplMobile) {
		this.tplMobile = tplMobile;
	}

	public Short getCommentControl() {
		return commentControl;
	}

	public void setCommentControl(Short commentControl) {
		this.commentControl = commentControl;
	}

	public Integer getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Integer workflowId) {
		this.workflowId = workflowId;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getSeoTitle() {
		return seoTitle;
	}

	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	public String getSeoKeywork() {
		return seoKeywork;
	}

	public void setSeoKeywork(String seoKeywork) {
		this.seoKeywork = seoKeywork;
	}

	public String getSeoDescription() {
		return seoDescription;
	}

	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}

	public Boolean getContribute() {
		return contribute;
	}

	public void setContribute(Boolean contribute) {
		this.contribute = contribute;
	}

	public Integer getResourceId() {
		return resourceId;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}

	public ResourcesSpaceData getResourcesSpaceData() {
		return resourcesSpaceData;
	}

	public void setResourcesSpaceData(ResourcesSpaceData resourcesSpaceData) {
		this.resourcesSpaceData = resourcesSpaceData;
	}

	public String getTxt() {
		return txt;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}


	public Boolean getLinkTarget() {
		return linkTarget;
	}

	public void setLinkTarget(Boolean linkTarget) {
		this.linkTarget = linkTarget;
	}

	public Short getViewControl() {
		return viewControl;
	}

	public void setViewControl(Short viewControl) {
		this.viewControl = viewControl;
	}
	
	public String getUrl() {
		
		return this.getResourcesSpaceData().getUrl();
	}

}
