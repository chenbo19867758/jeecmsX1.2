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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.jeecms.common.base.domain.AbstractDomain;

/**
 * 内容->相关内容
 * 
 * @author: chenming
 * @date: 2019年6月21日 下午4:01:28
 */
@Entity
@Table(name = "jc_content_relation")
public class ContentRelation extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	/** 关联内容id */
	private Integer relationContentId;
	/** 内容ID */
	private Integer contentId;
	/** 排序值 */
	private Integer sortNum;
	/** 排序值权重(排序值相同情况下，权重越大，排序越前) */
	private Integer sortWeight;

	/** 目标内容 */
	private Content content;
	/** 该目标内容关联的内容 */
	private Content relationContent;

	public ContentRelation() {

	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_content_relation", pkColumnValue = "jc_content_relation", 
		initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_content_relation")
	@Override
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "relation_content_id", nullable = false, length = 11)
	public Integer getRelationContentId() {
		return relationContentId;
	}

	public void setRelationContentId(Integer relationContentId) {
		this.relationContentId = relationContentId;
	}

	@Column(name = "content_id", nullable = false, length = 11)
	public Integer getContentId() {
		return contentId;
	}

	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}

	@Column(name = "sort_num", nullable = false, length = 11)
	public Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	@Column(name = "sort_weight", nullable = false, length = 11)
	public Integer getSortWeight() {
		return sortWeight;
	}

	public void setSortWeight(Integer sortWeight) {
		this.sortWeight = sortWeight;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "content_id", insertable = false, updatable = false)
	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "relation_content_id", insertable = false, updatable = false)
	public Content getRelationContent() {
		return relationContent;
	}

	public void setRelationContent(Content relationContent) {
		this.relationContent = relationContent;
	}

}