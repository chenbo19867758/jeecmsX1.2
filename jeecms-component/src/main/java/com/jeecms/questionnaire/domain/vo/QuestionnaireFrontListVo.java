/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.domain.vo;

import java.util.Date;

/**
 * 问卷前台列表显示vo
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/11/6 14:39
 */
public class QuestionnaireFrontListVo {

	private Integer id;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 详情
	 */
	private String details;
	/**
	 * 参与人次
	 */
	private Integer number;
	/**
	 * 状态
	 */
	private Integer status;
	/**
	 * 最后时间
	 */
	private Date deadline;
	/**
	 * 封面图地址
	 */
	private String coverPicUrl;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public String getCoverPicUrl() {
		return coverPicUrl;
	}

	public void setCoverPicUrl(String coverPicUrl) {
		this.coverPicUrl = coverPicUrl;
	}
}
