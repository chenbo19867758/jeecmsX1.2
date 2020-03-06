/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.interact.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.jeecms.common.base.domain.AbstractDomain;

/**
 * 用户互动记录
 * 
 * @author: chenming
 * @date: 2019年5月5日 下午5:56:13
 */
@Entity
@Table(name = "jc_user_interaction_record")
public class UserInteractionRecord extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 全局唯一标识符 */
	private Integer id;
	/** 资源记录，根据type字段类型，分别指向不同资源id */
	private Integer resourceId;
	/** ip */
	private String ip;
	/** 用户id */
	private Integer userId;
	/** 用户名 */
	private String userName;
	/** 操作类型(1-点赞 2-点踩) */
	private Integer operateType;
	/** 业务类型（1-评论模块 2-内容模块） */
	private Integer type;
	
	

	public UserInteractionRecord() {
		
	}

	@Id
	@TableGenerator(name = "jc_user_interaction_record", pkColumnValue = "jc_user_interaction_record", 
		initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_user_interaction_record")
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "resource_id", nullable = false, length = 6)
	public Integer getResourceId() {
		return resourceId;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}

	@Column(name = "ip", nullable = true, length = 50)
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "user_id", nullable = true, length = 11)
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name = "user_name", nullable = true, length = 50)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "operate_type", nullable = false, length = 6)
	public Integer getOperateType() {
		return operateType;
	}

	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}

	@Column(name = "type", nullable = false, length = 11)
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}