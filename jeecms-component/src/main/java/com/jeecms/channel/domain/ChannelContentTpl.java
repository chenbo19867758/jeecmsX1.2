package com.jeecms.channel.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.jeecms.content.domain.CmsModel;

/**
 * 栏目内容模板表
 * 
 * @author: tom
 * @date: 2019年3月19日 下午6:23:19
 */
@Entity
@Table(name = "jc_channel_content_tpl")
@NamedQuery(name = "ChannelContentTpl.findAll", query = "SELECT c FROM ChannelContentTpl c")
public class ChannelContentTpl extends com.jeecms.common.base.domain.AbstractIdDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 主键值*/
	private Integer id;
	/** 栏目ID */
	private Integer channelId;
	/** 模型ID */
	private Integer modelId;
	/** 排序值 */
	private Integer sortNum;
	/** 手机内容模板 */
	private String tplMobile;
	/** PC内容模板 */
	private String tplPc;
	/** 是否勾选 */
	private Boolean select;

	/** 栏目 */
	private Channel channel;
	/** 模型对象 */
	private CmsModel model;

	public ChannelContentTpl() {
	}

	@Id
	@TableGenerator(name = "jc_channel_content_tpl", pkColumnValue = "jc_channel_content_tpl", 
					initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_channel_content_tpl")
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "channel_id", nullable = false)
	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	@Column(name = "model_id", nullable = false)
	public Integer getModelId() {
		return this.modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}

	@Column(name = "sort_num", nullable = false)
	public Integer getSortNum() {
		return this.sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	@Column(name = "tpl_mobile", length = 150)
	public String getTplMobile() {
		return this.tplMobile;
	}

	public void setTplMobile(String tplMobile) {
		this.tplMobile = tplMobile;
	}

	@Column(name = "tpl_pc", length = 150)
	public String getTplPc() {
		return this.tplPc;
	}

	public void setTplPc(String tplPc) {
		this.tplPc = tplPc;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channel_id", insertable = false, updatable = false)
	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "model_id", insertable = false, updatable = false)
	public CmsModel getModel() {
		return model;
	}

	public void setModel(CmsModel model) {
		this.model = model;
	}

	@Column(name = "is_select", length = 1)
	public Boolean getSelect() {
		return select;
	}

	public void setSelect(Boolean select) {
		this.select = select;
	}

}