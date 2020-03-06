/*
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.auth.domain;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 用户选择模型排序表
 * @author ljw
 * @version 1.0
 * @date 2019-12-13
 */
@Entity
@Table(name = "jc_user_model_sort")
public class UserModelSort implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	/** 模型ID */
	private Integer userId;
	/** 模型ID */
	private Integer modelId;
	/** 排序值 */
	private Integer sort;
	/** 统计时间 */
	private Date statisticsDay;
	
	public UserModelSort() {
	}
	
	/**
	 * 构造函数
	 * @param userId 用户ID
	 * @param modelId 模型ID
	 * @param sort 排序值
	 * @param statisticsDay 统计日期
	 */
	public UserModelSort(Integer userId, Integer modelId, Integer sort, Date statisticsDay) {
		this.userId = userId;
		this.modelId = modelId;
		this.sort = sort;
		this.statisticsDay = statisticsDay;
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_user_model_sort", pkColumnValue = "jc_user_model_sort", 
			initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_user_model_sort")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "model_id", nullable = false, length = 11)
	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}

	@Column(name = "sort", nullable = false, length = 11)
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	@Temporal(TemporalType.DATE)
	@JSONField(format = "yyyy-MM-dd")  
	@Column(name = "statistics_day", nullable = false)
	public Date getStatisticsDay() {
		return statisticsDay;
	}

	public void setStatisticsDay(Date statisticsDay) {
		this.statisticsDay = statisticsDay;
	}

	@Column(name = "user_id", nullable = false)
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}