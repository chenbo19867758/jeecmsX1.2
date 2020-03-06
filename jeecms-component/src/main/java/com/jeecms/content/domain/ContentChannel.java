/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import com.jeecms.common.base.domain.AbstractIdDomain;
import com.jeecms.channel.domain.Channel;

/**
 * 内容栏目关联实体类
 * 
 * @author: chenming
 * @date: 2019年5月15日 下午4:17:19
 */
@Entity
@Table(name = "jc_content_channel")
public class ContentChannel extends AbstractIdDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer refId;
	/** 内容ID */
	private Integer contentId;
	/** 栏目ID */
	private Integer channelId;
	/** 创建方式（1:直接创建 2:投稿 3:站群推送 4:站群采集 5:复制 6:链接型引用 7:镜像型引用） */
	private Integer createType;
	/** 内容状态(1:草稿; 2-初稿 3:流转中; 4:待发布; 5:已发布; 6:退回; 7:下线 8-归档 ) */
	private Integer status;
	/** 是否加入回收站（0-否 1-是） **/
	private Boolean recycle;
	/** 是否引用数据 **/
	private Boolean isRef;
	
	private Content content;
	/** 栏目 **/
	private Channel channel;

	public ContentChannel() {

	}

	/**
	 * 内容新增、修改、引用是初始化
	 */
	public ContentChannel(Integer contentId, Integer channelId, Integer createType, Integer status,Boolean recycle,
			Content content,Boolean isRef) {
		super();
		this.contentId = contentId;
		this.channelId = channelId;
		this.createType = createType;
		this.status = status;
		this.recycle = recycle;
		this.content = content;
		this.isRef = isRef;
	}



	@Id
	@Column(name = "ref_id", nullable = false, length = 11)
	@TableGenerator(name = "jc_content_channel", pkColumnValue = "jc_content_channel", 
					initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_content_channel")
	public Integer getRefId() {
		return this.refId;
	}

	public void setRefId(Integer refId) {
		this.refId = refId;
		super.setId(refId);
	}

	@Column(name = "content_id", nullable = false, length = 11)
	public Integer getContentId() {
		return contentId;
	}

	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}

	@Column(name = "channel_id", nullable = false, length = 11)
	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	@Column(name = "create_type", nullable = true, length = 6)
	public Integer getCreateType() {
		return createType;
	}

	public void setCreateType(Integer createType) {
		this.createType = createType;
	}

	@Column(name = "status", nullable = false, length = 6)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "content_id", insertable = false, updatable = false)
	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channel_id", insertable = false, updatable = false)
	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	@Column(name = "is_recycle", nullable = false, length = 1)
	public Boolean getRecycle() {
		return recycle;
	}

	public void setRecycle(Boolean recycle) {
		this.recycle = recycle;
	}
	
	@Column(name = "is_ref", length = 1)
	public Boolean getIsRef() {
		return isRef;
	}

	public void setIsRef(Boolean isRef) {
		this.isRef = isRef;
	}

	/** 获取栏目名称 **/
	@Transient
	public String getChannelName() {
		return channel.getName();
	}
	
}
