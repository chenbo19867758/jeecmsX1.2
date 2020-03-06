/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.vo;

/**
 * 首页今日统计和累计统计Vo
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/12 17:22
 */

public class IndexVo {

	/**
	 * 今日内容发布数
	 */
	private long todayContent;
	/**
	 * 累计内容发布数
	 */
	private long totalContent;
	/**
	 * 今日评论数
	 */
	private long todayComment;
	/**
	 * 累计评论数
	 */
	private long totalComment;
	/**
	 * 今日新增会员数
	 */
	private long todayUser;
	/**
	 * 累计新增会员数
	 */
	private long totalUser;
	/**
	 * 今日投稿数
	 */
	private long todaySubmission;
	/**
	 * 累计投稿数
	 */
	private long totalSubmission;

	public IndexVo() {
		super();
	}

	public IndexVo(long todayContent,
				   long totalContent,
				   long todayComment,
				   long totalComment,
				   long todayUser,
				   long totalUser,
				   long todaySubmission,
				   long totalSubmission) {
		this.todayContent = todayContent;
		this.totalContent = totalContent;
		this.todayComment = todayComment;
		this.totalComment = totalComment;
		this.todayUser = todayUser;
		this.totalUser = totalUser;
		this.todaySubmission = todaySubmission;
		this.totalSubmission = totalSubmission;
	}

	public long getTodayContent() {
		return todayContent;
	}

	public void setTodayContent(long todayContent) {
		this.todayContent = todayContent;
	}

	public long getTotalContent() {
		return totalContent;
	}

	public void setTotalContent(long totalContent) {
		this.totalContent = totalContent;
	}

	public long getTodayComment() {
		return todayComment;
	}

	public void setTodayComment(long todayComment) {
		this.todayComment = todayComment;
	}

	public long getTotalComment() {
		return totalComment;
	}

	public void setTotalComment(long totalComment) {
		this.totalComment = totalComment;
	}

	public long getTodayUser() {
		return todayUser;
	}

	public void setTodayUser(long todayUser) {
		this.todayUser = todayUser;
	}

	public long getTotalUser() {
		return totalUser;
	}

	public void setTotalUser(long totalUser) {
		this.totalUser = totalUser;
	}

	public long getTodaySubmission() {
		return todaySubmission;
	}

	public void setTodaySubmission(long todaySubmission) {
		this.todaySubmission = todaySubmission;
	}

	public long getTotalSubmission() {
		return totalSubmission;
	}

	public void setTotalSubmission(long totalSubmission) {
		this.totalSubmission = totalSubmission;
	}
}
