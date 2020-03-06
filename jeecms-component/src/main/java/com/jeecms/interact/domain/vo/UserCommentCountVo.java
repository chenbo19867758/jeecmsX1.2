package com.jeecms.interact.domain.vo;

/**
 * 评论列表数量vo
 * 
 * @author: chenming
 * @date: 2019年7月16日 下午9:48:38
 */
public class UserCommentCountVo {

	/** 全部评论数量 */
	private Integer allNum;
	/** 待审核评论数量 */
	private Integer pendingReviewNum;
	/** 审核成功评论数量 */
	private Integer successReviewNum;
	/** 审核失败评论数量 */
	private Integer errorReviewNum;
	/** 举报列表数量 */
	private Integer reportNum;
	/** 内容名称*/
	private String contentName;
	/** 栏目名称*/
	private String channelName;

	public UserCommentCountVo(Integer allNum, Integer pendingReviewNum, Integer successReviewNum,
			Integer errorReviewNum, Integer reportNum) {
		super();
		this.allNum = allNum;
		this.pendingReviewNum = pendingReviewNum;
		this.successReviewNum = successReviewNum;
		this.errorReviewNum = errorReviewNum;
		this.reportNum = reportNum;
	}

	public UserCommentCountVo() {
		super();
	}

	public Integer getAllNum() {
		return allNum;
	}

	public void setAllNum(Integer allNum) {
		this.allNum = allNum;
	}

	public Integer getPendingReviewNum() {
		return pendingReviewNum;
	}

	public void setPendingReviewNum(Integer pendingReviewNum) {
		this.pendingReviewNum = pendingReviewNum;
	}

	public Integer getSuccessReviewNum() {
		return successReviewNum;
	}

	public void setSuccessReviewNum(Integer successReviewNum) {
		this.successReviewNum = successReviewNum;
	}

	public Integer getErrorReviewNum() {
		return errorReviewNum;
	}

	public void setErrorReviewNum(Integer errorReviewNum) {
		this.errorReviewNum = errorReviewNum;
	}

	public Integer getReportNum() {
		return reportNum;
	}

	public void setReportNum(Integer reportNum) {
		this.reportNum = reportNum;
	}

	public String getContentName() {
		return contentName;
	}

	public void setContentName(String contentName) {
		this.contentName = contentName;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

}
