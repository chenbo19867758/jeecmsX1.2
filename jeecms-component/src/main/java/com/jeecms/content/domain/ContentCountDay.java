/**
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.jeecms.common.base.domain.AbstractDomain;

/**
 * 内容日统计
 * 
 * @author ljw
 * @version 1.0
 * @date 2019-05-15
 */
@Entity
@Table(name = "jc_content_count_day")
public class ContentCountDay extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 内容ID */
	private Integer contentId;
	/** 日访问量 */
	private Integer viewsDay;
	/** 日评论量 */
	private Integer commentsDay;
	/** 日下载量 */
	private Integer downloadsDay;
	/** 日点赞数 */
	private Integer upsDay;
	/** 日点踩数 */
	private Integer downsDay;
	/** 日期 */
	private Date countDay;

	public ContentCountDay() {
	}

	@Id
	@Column(name = "content_id", nullable = false, length = 11)
	public Integer getContentId() {
		return contentId;
	}

	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}

	@Column(name = "views_day", nullable = false, length = 11)
	public Integer getViewsDay() {
		return viewsDay;
	}

	public void setViewsDay(Integer viewsDay) {
		this.viewsDay = viewsDay;
	}

	@Column(name = "comments_day", nullable = false, length = 11)
	public Integer getCommentsDay() {
		return commentsDay;
	}

	public void setCommentsDay(Integer commentsDay) {
		this.commentsDay = commentsDay;
	}

	@Column(name = "downloads_day", nullable = false, length = 11)
	public Integer getDownloadsDay() {
		return downloadsDay;
	}

	public void setDownloadsDay(Integer downloadsDay) {
		this.downloadsDay = downloadsDay;
	}

	@Column(name = "ups_day", nullable = false, length = 11)
	public Integer getUpsDay() {
		return upsDay;
	}

	public void setUpsDay(Integer upsDay) {
		this.upsDay = upsDay;
	}

	@Column(name = "downs_day", nullable = false, length = 11)
	public Integer getDownsDay() {
		return downsDay;
	}

	public void setDownsDay(Integer downsDay) {
		this.downsDay = downsDay;
	}

	@Column(name = "count_day", nullable = false, length = 10)
	public Date getCountDay() {
		return countDay;
	}

	public void setCountDay(Date countDay) {
		this.countDay = countDay;
	}

}