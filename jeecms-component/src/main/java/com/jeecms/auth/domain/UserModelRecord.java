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
 * 用户选择内容模型记录表
 * @author ljw
 * @version 1.0
 * @date 2019-12-13
 */
@Entity
@Table(name = "jc_user_model_record")
public class UserModelRecord implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	/** 用户ID */
	private Integer userId;
	/** 用户选择内容模型ID */
	private Integer modelId;
	/** 创建时间 */
	private Date createTime;
	
	public UserModelRecord() {
	}
	
	/**
	 * 构造函数
	 */
	public UserModelRecord(Integer userId, Integer modelId) {
		this.userId = userId;
		this.modelId = modelId;
		this.createTime = new Date();
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_user_model_record", pkColumnValue = "jc_user_model_record", 
		initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_user_model_record")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "user_id", nullable = false, length = 11)
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name = "model_id", nullable = false, length = 11)
	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")  
	@Column(name = "create_time", nullable = false)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}