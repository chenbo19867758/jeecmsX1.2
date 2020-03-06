/**
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.domain;

import java.io.Serializable;
import com.jeecms.common.base.domain.AbstractDomain;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * 内容批注表
 * 
 * @author ljw
 * @version 1.0
 * @date 2019-05-15
 */
@Entity
@Table(name = "jc_content_annotation")
public class ContentAnnotation extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer annoId;
	/** 内容ID */
	private Integer contentId;
	/** 批注位置 */
	private Integer annoPosition;
	/** 批注内容 */
	private String annotation;

	public ContentAnnotation() {
	}

	@Id
	@Column(name = "anno_id", nullable = false, length = 11)
	@TableGenerator(name = "jc_content_annotation", pkColumnValue = "jc_content_annotation", 
		initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_content_annotation")
	public Integer getAnnoId() {
		return this.annoId;
	}

	public void setAnnoId(Integer annoId) {
		this.annoId = annoId;
	}

	@Column(name = "content_id", nullable = false, length = 11)
	public Integer getContentId() {
		return contentId;
	}

	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}

	@Column(name = "anno_position", nullable = false, length = 11)
	public Integer getAnnoPosition() {
		return annoPosition;
	}

	public void setAnnoPosition(Integer annoPosition) {
		this.annoPosition = annoPosition;
	}

	@Column(name = "annotation", nullable = false, length = 500)
	public String getAnnotation() {
		return annotation;
	}

	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

}