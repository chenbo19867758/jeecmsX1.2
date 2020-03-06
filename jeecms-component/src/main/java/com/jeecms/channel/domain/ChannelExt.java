package com.jeecms.channel.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.jeecms.content.constants.CmsModelConstant;
import com.jeecms.resource.domain.ResourcesSpaceData;

/**
 * The persistent class for the jc_channel_ext database table. 栏目扩展实体类
 * 
 * @author: tom
 * @date: 2019年5月5日 上午11:12:12
 */
@Entity
@Table(name = "jc_channel_ext")
@NamedQuery(name = "ChannelExt.findAll", query = "SELECT j FROM ChannelExt j")
public class ChannelExt extends com.jeecms.common.base.domain.AbstractIdDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 标识 */
	private Integer id;
	/** 评论设置(1允许游客评论 2登录后评论 3不允许评论) */
	private Short commentControl;
	/** 间隔时间 */
	private Integer intervalTime;
	/** 分页大小 */
	private Short pageSize;
	/** 栏目图片（引用图片空间资源id） */
	private Integer resourceId;
	/** SEO描述 */
	private String seoDescription;
	/** SEO关键字 */
	private String seoKeywork;
	/** SEO标题 */
	private String seoTitle;
	/** 定时发布 */
	private Boolean taskControl;
	/** 定时发布起始时间 */
	private Date taskTime;
	/** 定时类型（1小时 2天 3月） */
	private Short taskType;
	/** 内容 */
	private String txt;
	/** 浏览设置(1-都不需要 2-仅内容页需要登录 3-都需要登录 ) */
	private Short viewControl;
	/** 是否开启索引查询 */
	private Boolean isOpenIndex;
	/** 栏目页是否包含分页 */
	private Boolean isListChannel;

	/** 栏目实体类 */
	private Channel channel;
	/** 图片实体类对象 */
	private ResourcesSpaceData resourcesSpaceData;

	public ChannelExt() {
	}

	@Override
	@Id
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "comment_control", nullable = false)
	public Short getCommentControl() {
		return this.commentControl;
	}

	public void setCommentControl(Short commentControl) {
		this.commentControl = commentControl;
	}

	
	
	@Column(name = "interval_time")
	public Integer getIntervalTime() {
		return this.intervalTime;
	}

	public void setIntervalTime(Integer intervalTime) {
		this.intervalTime = intervalTime;
	}

	@Column(name = "page_size", nullable = false)
	public Short getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(Short pageSize) {
		this.pageSize = pageSize;
	}


	
	@Column(name = "resources_space_data_id")
	public Integer getResourceId() {
		return this.resourceId;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}

	@Column(name = "seo_description", length = 500)
	public String getSeoDescription() {
		return this.seoDescription;
	}

	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}

	@Column(name = "seo_keywork", length = 150)
	public String getSeoKeywork() {
		return this.seoKeywork;
	}

	public void setSeoKeywork(String seoKeywork) {
		this.seoKeywork = seoKeywork;
	}

	@Column(name = "seo_title", length = 150)
	public String getSeoTitle() {
		return this.seoTitle;
	}

	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	@Column(name = "task_control", nullable = false)
	public Boolean getTaskControl() {
		return this.taskControl;
	}

	public void setTaskControl(Boolean taskControl) {
		this.taskControl = taskControl;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "task_time")
	public Date getTaskTime() {
		return this.taskTime;
	}

	public void setTaskTime(Date taskTime) {
		this.taskTime = taskTime;
	}

	@Column(name = "task_type")
	public Short getTaskType() {
		return this.taskType;
	}

	public void setTaskType(Short taskType) {
		this.taskType = taskType;
	}

	@Column(name = "txt", length = 150)
	public String getTxt() {
		return this.txt;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}

	@Column(name = "view_control", nullable = false)
	public Short getViewControl() {
		return this.viewControl;
	}

	public void setViewControl(Short viewControl) {
		this.viewControl = viewControl;
	}

	@Column(name = "is_open_index", nullable = false)
	public Boolean getIsOpenIndex() {
		return isOpenIndex;
	}

	public void setIsOpenIndex(Boolean isOpenIndex) {
		this.isOpenIndex = isOpenIndex;
	}

	@Column(name = "is_list_channel", nullable = false)
	public Boolean getIsListChannel() {
		return isListChannel;
	}

	public void setIsListChannel(Boolean isListChannel) {
		this.isListChannel = isListChannel;
	}

	@MapsId
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channel_id", nullable = false)
	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "resources_space_data_id", referencedColumnName = "id", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	public ResourcesSpaceData getResourcesSpaceData() {
		return resourcesSpaceData;
	}

	public void setResourcesSpaceData(ResourcesSpaceData resourcesSpaceData) {
		this.resourcesSpaceData = resourcesSpaceData;
	}
	
	/**
	 * 获取真实，栏目图片
	 */
	@Transient
	public ResourcesSpaceData getRealResourcesSpaceData() {
		if (getChannel() != null && getChannel().getModel().existItem(CmsModelConstant.FIELD_CHANNEL_RESOURCE_ID)) {
			return resourcesSpaceData;
		}
		return null;
	}
	
	/**
	 * 获取真实评论设置
	 */
	@Transient
	public Short getRealCommentControl() {
		if (getChannel() != null) {
			if (getChannel().getModel().existItem(CmsModelConstant.FIELD_CHANNEL_COMMENT_CONTROL)) {
				return this.commentControl;
			}
			return Short.valueOf(getChannel().getSite().getConfig().getCommentSet()+"");
		}
		return null;
	}

	/**
	 * 获取真实栏目及内容的浏览权限
	 */
	@Transient
	public Short getRealViewControl() {
		if (getChannel() != null) {
			if (getChannel().getModel().existItem(CmsModelConstant.FIELD_CHANNEL_VIEW_CONTROL)) {
				return this.viewControl;
			}
			return getChannel().getSite().getConfig().getChannelVisitLimitType();
		}
		return null;
	}
	
	/**
	 * 获取真实，内容列表每页显示内容数
	 */
	@Transient
	public Short getRealPageSize() {
		if (channel != null) {
			if (getChannel().getModel().existItem(CmsModelConstant.FIELD_CHANNEL_PAGE_SIZE)) {
				return this.pageSize;
			}
			return Short.valueOf(channel.getSite().getConfig().getChannelDisplayContentNumber());
		}
		return null;
	}
	
	/**
	 * 获取真实SEO描述
	 */
	@Transient
	public String getRealSeoDescription() {
		if (getChannel() != null && getChannel().getModel().existItem(CmsModelConstant.FIELD_CHANNEL_SEO_DESCRIPTION)) {
			return this.seoDescription;
		}
		return null;
	}
	
	/**
	 * 获取真实SEO关键字
	 */
	@Transient
	public String getRealSeoKeywork() {
		if (getChannel() != null && getChannel().getModel().existItem(CmsModelConstant.FIELD_CHANNEL_SEO_KEYWORD)) {
			return this.seoKeywork;
		}
		return null;
	}
	
	/**
	 * 获取真实SEO标题
	 */
	@Transient
	public String getRealSeoTitle() {
		if (getChannel() != null && getChannel().getModel().existItem(CmsModelConstant.FIELD_CHANNEL_SEO_TITLE)) {
			return this.seoTitle;
		}
		return null;
	}
	
	/**
	 * 获取真实栏目页是否包含分页
	 */
	@Transient
	public Boolean getRealIsListChannel() {
		if (getChannel() != null && getChannel().getModel().existItem(CmsModelConstant.FIELD_SYS_CHANNEL_LIST)) {
			return isListChannel;
		}
		return true;
	}
}