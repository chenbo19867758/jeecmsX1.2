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
 * 内容周统计
 * 
 * @author ljw
 * @version 1.0
 * @date 2019-05-15
 */
@Entity
@Table(name = "jc_content_count_week")
public class ContentCountWeek extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 内容ID */
	private Integer contentId;
	/** 周访问量 */
	private Integer viewsWeek;
	/** 周评论量 */
	private Integer commentsWeek;
	/** 周下载量 */
	private Integer downloadsWeek;
	/** 周点赞数 */
	private Integer upsWeek;
	/** 周点踩数 */
	private Integer downsWeek;
	/** 日期 */
	private Date countDay;

	public ContentCountWeek() {
	}

	@Id
	@Column(name = "content_id", nullable = false, length = 11)
	public Integer getContentId() {
		return contentId;
	}

	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}

	@Column(name = "views_week", nullable = false, length = 11)
	public Integer getViewsWeek() {
		return viewsWeek;
	}

	public void setViewsWeek(Integer viewsWeek) {
		this.viewsWeek = viewsWeek;
	}

	@Column(name = "comments_week", nullable = false, length = 11)
	public Integer getCommentsWeek() {
		return commentsWeek;
	}

	public void setCommentsWeek(Integer commentsWeek) {
		this.commentsWeek = commentsWeek;
	}

	@Column(name = "downloads_week", nullable = false, length = 11)
	public Integer getDownloadsWeek() {
		return downloadsWeek;
	}

	public void setDownloadsWeek(Integer downloadsWeek) {
		this.downloadsWeek = downloadsWeek;
	}

	@Column(name = "ups_week", nullable = false, length = 11)
	public Integer getUpsWeek() {
		return upsWeek;
	}

	public void setUpsWeek(Integer upsWeek) {
		this.upsWeek = upsWeek;
	}

	@Column(name = "downs_week", nullable = false, length = 11)
	public Integer getDownsWeek() {
		return downsWeek;
	}

	public void setDownsWeek(Integer downsWeek) {
		this.downsWeek = downsWeek;
	}

	@Column(name = "count_day", nullable = false, length = 10)
	public Date getCountDay() {
		return countDay;
	}

	public void setCountDay(Date countDay) {
		this.countDay = countDay;
	}

}