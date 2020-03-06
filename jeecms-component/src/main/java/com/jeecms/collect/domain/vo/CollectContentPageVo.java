/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.collect.domain.vo;

import com.alibaba.fastjson.JSONArray;

/**
 * 采集内容分页Vo
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/20 16:31
 */

public class CollectContentPageVo {
	private JSONArray title;
	private JSONArray content;
	private Boolean first;
	private Boolean last;
	private Integer size;
	private Long totalElements;
	private Integer totalPages;
	private Integer taskId;

	public CollectContentPageVo() {
		super();
	}

	/**
	 * 构造器
	 *
	 * @param title         列数据
	 * @param content       列表数据
	 * @param first         是否第一页
	 * @param last          是否最好一页
	 * @param size          每页条数
	 * @param totalElements 总条数
	 * @param totalPages    总页数
	 * @param taskId        任务id
	 * @param modelType     模板类型（1中国政府网2百度搜索词3微博4公众号）
	 * @param templateId    模型id
	 */
	public CollectContentPageVo(JSONArray title,
								JSONArray content,
								Boolean first,
								Boolean last,
								Integer size,
								Long totalElements,
								Integer totalPages,
								Integer taskId) {
		this.title = title;
		this.content = content;
		this.first = first;
		this.last = last;
		this.size = size;
		this.totalElements = totalElements;
		this.totalPages = totalPages;
		this.taskId = taskId;
	}

	public JSONArray getTitle() {
		return title;
	}

	public void setTitle(JSONArray title) {
		this.title = title;
	}

	public JSONArray getContent() {
		return content;
	}

	public void setContent(JSONArray content) {
		this.content = content;
	}

	public Boolean getFirst() {
		return first;
	}

	public void setFirst(Boolean first) {
		this.first = first;
	}

	public Boolean getLast() {
		return last;
	}

	public void setLast(Boolean last) {
		this.last = last;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(Long totalElements) {
		this.totalElements = totalElements;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
}
