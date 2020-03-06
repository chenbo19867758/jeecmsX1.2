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
 * 内容月统计
 * 
 * @author ljw
 * @version 1.0
 * @date 2019-05-15
 */
@Entity
@Table(name = "jc_content_count_month")
public class ContentCountMonth extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 内容ID */
	private Integer contentId;
	/** 月访问量 */
	private Integer viewsMonth;
	/** 月评论量 */
	private Integer commentsMonth;
	/** 月下载量 */
	private Integer downloadsMonth;
	/** 月点赞数 */
	private Integer upsMonth;
	/** 月点踩数 */
	private Integer downsMonth;
	/** 时间 */
	private Date countDay;

	public ContentCountMonth() {
	}

	@Id
	@Column(name = "content_id", nullable = false, length = 11)
	public Integer getContentId() {
		return contentId;
	}

	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}

	@Column(name = "views_month", nullable = false, length = 11)
	public Integer getViewsMonth() {
		return viewsMonth;
	}

	public void setViewsMonth(Integer viewsMonth) {
		this.viewsMonth = viewsMonth;
	}

	@Column(name = "comments_month", nullable = false, length = 11)
	public Integer getCommentsMonth() {
		return commentsMonth;
	}

	public void setCommentsMonth(Integer commentsMonth) {
		this.commentsMonth = commentsMonth;
	}

	@Column(name = "downloads_month", nullable = false, length = 11)
	public Integer getDownloadsMonth() {
		return downloadsMonth;
	}

	public void setDownloadsMonth(Integer downloadsMonth) {
		this.downloadsMonth = downloadsMonth;
	}

	@Column(name = "ups_month", nullable = false, length = 11)
	public Integer getUpsMonth() {
		return upsMonth;
	}

	public void setUpsMonth(Integer upsMonth) {
		this.upsMonth = upsMonth;
	}

	@Column(name = "downs_month", nullable = false, length = 11)
	public Integer getDownsMonth() {
		return downsMonth;
	}

	public void setDownsMonth(Integer downsMonth) {
		this.downsMonth = downsMonth;
	}

	@Column(name = "count_day", nullable = false, length = 10)
	public Date getCountDay() {
		return countDay;
	}

	public void setCountDay(Date countDay) {
		this.countDay = countDay;
	}

}