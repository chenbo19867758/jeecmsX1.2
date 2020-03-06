/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.collect.domain;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.domain.AbstractDomain;

import javax.persistence.*;
import java.io.Serializable;


/**
 * 采集内容库
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-07-08
 */
@Entity
@Table(name = "jc_collect_content")
public class CollectContent extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	/**
	 * 任务表
	 */
	private Integer taskId;
	/**
	 * json数据 采集的字段及对应数据{"title":"标题","content","内容"}
	 */
	private String contentValue;

	/**
	 * 这条内容的标题
	 */
	private String title;

	/**
	 * 这条内容的来源链接
	 */
	private String url;

	private JSONObject json;

	public CollectContent() {
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_collect_content", pkColumnValue = "jc_collect_content", 
					initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_collect_content")
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "task_id", nullable = false, length = 11)
	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	@Column(name = "content_value", nullable = true, length = 65535)
	public String getContentValue() {
		return contentValue;
	}

	public void setContentValue(String contentValue) {
		this.contentValue = contentValue;
	}


	@Column(name = "title", nullable = true, length = 255)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "url", nullable = true, length = 255)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Transient
	public JSONObject getJson() {
		if (getContentValue() != null) {
			json = JSONObject.parseObject(getContentValue());
			json.put("id", getId());
		}
		return json;
	}
}