package com.jeecms.channel.domain.dto;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.channel.constants.ChannelConstant;
import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.domain.ChannelContentTpl;
import com.jeecms.content.constants.CmsModelConstant;
import com.jeecms.content.domain.CmsModelItem;

/**
 * 修改栏目主体Dto
 * 
 * @author: chenming
 * @date: 2019年4月25日 下午2:37:25
 */
public class ChannelDto {

	/** 栏目Id*/
	private Integer id;
	/** 上级栏目ID */
	private Integer channelParentId;
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
	/** 内容型模板 */
	private List<ChannelContentTpl> contentTpls;
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
	/** 是否允许投稿 */
	private Boolean contribute;
	/** 分页大小*/
	private Short pageSize;
	/** 栏目页是否包含分页*/
	private Boolean isListChannel;
	/** SEO标题 */
	private String seoTitle;
	/** SEO关键字 */
	private String seoKeywork;
	/** 分组类型*/
	private String groupType;
	               
	/** SEO描述 */
	private String seoDescription;
	/** 外部链接特殊处理*/
	private JSONObject link;
	/** 自定义模板 */
	private JSONObject json;
	

	public Integer getChannelParentId() {
		return channelParentId;
	}

	public void setChannelParentId(Integer channelParentId) {
		this.channelParentId = channelParentId;
	}

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

	public List<ChannelContentTpl> getContentTpls() {
		return contentTpls;
	}

	public void setContentTpls(List<ChannelContentTpl> contentTpls) {
		this.contentTpls = contentTpls;
	}

	@NotNull
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public JSONObject getJson() {
		return json;
	}

	public void setJson(JSONObject json) {
		this.json = json;
	}

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

	public JSONObject getLink() {
		return link;
	}

	public void setLink(JSONObject link) {
		this.link = link;
	}

	
	/**
	 * 初始化channelDto
	 * @Title: ChannelDto  
	 * @param items		没有修改的字段集合
	 * @param channel	需要修改的栏目对象
	 * @param dto		扩展dto
	 * @return: ChannelDto
	 */
	public static ChannelDto initChannelDto(List<CmsModelItem> items,Channel channel,ChannelDto dto) {
		if (items.size() > 0) {
			List<String> files = items.stream().map(CmsModelItem::getField).collect(Collectors.toList());
			for (String file : files) {
				if (CmsModelConstant.FIELD_SYS_CHANNEL_LIST.equals(file)) {
					dto.setIsListChannel(channel.getChannelExt().getIsListChannel());
				}
				if (CmsModelConstant.FIELD_CHANNEL_PARENT_ID.equals(file)) {
					dto.setChannelParentId(channel.getParentId());
				}
				if (CmsModelConstant.FIELD_CHANNEL_NAME.equals(file)) {
					dto.setChannelName(channel.getName());
				}
				if (CmsModelConstant.FIELD_CHANNEL_PATH.equals(file)) {
					dto.setChannelPath(channel.getPath());
				}
				if (CmsModelConstant.FIELD_CHANNEL_CONTENT_TPLS.equals(file)) {
					if (channel.getContentTpls() != null && channel.getContentTpls().isEmpty()) {
						dto.setContentTpls(channel.getContentTpls());
					} else {
						dto.setContentTpls(null);
					}
				}
				if (CmsModelConstant.FIELD_CHANNEL_LINK.equals(file)) {
					JSONObject linkJson = new JSONObject();
					linkJson.put(ChannelConstant.CHANNEL_LIKE_NAME, channel.getLink());
					linkJson.put(Channel.LINK_TARGET_NAME, channel.getLinkTarget());
					dto.setLink(linkJson);
				}
				if (CmsModelConstant.FIELD_CHANNEL_DISPLAY.equals(file)) {
					dto.setDisplay(channel.getDisplay());
				}
				if (CmsModelConstant.FIELD_CHANNEL_TPL_PC.equals(file)) {
					dto.setTplPc(channel.getTplPc());
				}
				if (CmsModelConstant.FIELD_CHANNEL_TPL_MOBILE.equals(file)) {
					dto.setTplMobile(channel.getTplMobile());
				}
				if (CmsModelConstant.FIELD_CHANNEL_VIEW_CONTROL.equals(file)) {
					dto.setViewControl(channel.getViewControl());
				}
				if (CmsModelConstant.FIELD_CHANNEL_COMMENT_CONTROL.equals(file)) {
					dto.setCommentControl(channel.getChannelExt().getCommentControl());
				}
				if (CmsModelConstant.FIELD_CHANNEL_WORKFLOWID.equals(file)) {
					dto.setWorkflowId(channel.getWorkflowId());
				}
				if (CmsModelConstant.FIELD_CHANNEL_SEO_TITLE.equals(file)) {
					dto.setSeoTitle(channel.getChannelExt().getSeoTitle());
				}
				if (CmsModelConstant.FIELD_CHANNEL_SEO_KEYWORD.equals(file)) {
					dto.setSeoKeywork(channel.getChannelExt().getSeoKeywork());
				}
				if (CmsModelConstant.FIELD_CHANNEL_SEO_DESCRIPTION.equals(file)) {
					dto.setSeoDescription(channel.getChannelExt().getSeoDescription());
				}
				if (CmsModelConstant.FIELD_CHANNEL_CONTRIBUTE.equals(file)) {
					dto.setContribute(channel.getContribute());
				}
				if (CmsModelConstant.FIELD_CHANNEL_PAGE_SIZE.equals(file)) {
					dto.setPageSize(channel.getPageSize());
				}
				if (CmsModelConstant.FIELD_CHANNEL_TXT.equals(file)) {
					dto.setTxt(channel.getChannelExt().getTxt());
				}
				if (CmsModelConstant.FIELD_CHANNEL_RESOURCE_ID.equals(file)) {
					dto.setResourceId(channel.getChannelExt().getResourceId());
				}
			}
		}
		if (dto.getDisplay() == null) {
			dto.setDisplay(channel.getDisplay());
		}
		if (dto.getContribute() == null) {
			dto.setContribute(channel.getContribute());
		}
		if (dto.getIsListChannel() == null) {
			dto.setIsListChannel(channel.getIsListChannel());
		}
		if (dto.getCommentControl() == null) {
			dto.setCommentControl(channel.getChannelExt().getCommentControl());
		}
		if (dto.getPageSize() == null) {
			dto.setPageSize(channel.getChannelExt().getPageSize());
		}
		if (dto.getViewControl() == null) {
			dto.setViewControl(channel.getChannelExt().getViewControl());
		}
		return dto;
	}

	@NotBlank
	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	
}
