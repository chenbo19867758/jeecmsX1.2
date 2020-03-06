package com.jeecms.common.base.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;
import com.jeecms.common.util.CurrUserContextUtils;

/**
 * 基础domain
 * 
 * @author: tom
 * @date: 2018年12月24日 下午3:11:10
 * @param <ID>
 *            id
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@MappedSuperclass
public abstract class AbstractDomain<ID extends Serializable> extends AbstractDelFlagDomain<ID> {

	private static final long serialVersionUID = -6529930769002715205L;
	private final String updateTimeName = "updateTime";
	private final String createTimeName = "createTime";
	private final String updateUserName = "updateUser";
	private final String createUserName = "createUser";
	/** 创建时间 */
	protected Date createTime;
	/** 创建用户名 */
	protected String createUser;
	/** 修改时间 */
	protected Date updateTime;
	/** 修改用户名 */
	protected String updateUser;

	@Transient
	public String getUpdateTimeName() {
		return updateTimeName;
	}

	@Transient
	public String getCreateTimeName() {
		return createTimeName;
	}

	@Transient
	public String getUpdateUserName() {
		return updateUserName;
	}

	@Transient
	public String getCreateUserName() {
		return createUserName;
	}

	/**
	 * 修改之前初始化修改时间和修改用户
	 * 
	 * @Title: preUpdate
	 * @param dbDomain
	 *            AbstractDomain domain
	 * @return: void
	 */
	@SuppressWarnings("rawtypes")
	@Transient
	public void preUpdate(AbstractDomain dbDomain) {
		this.updateTime = new Date();
		this.updateUser = CurrUserContextUtils.getCurrentUsername();
		setDbCreateInfo(dbDomain.getCreateTime(), dbDomain.getCreateUser(), dbDomain.getHasDeleted());
	}

	/**
	 * 创建之前初始化时间和用户信息
	 * 
	 * @Title: preCreate
	 * @return: void
	 */
	@Transient
	public void preCreate() {
		this.createTime = new Date();
		this.createUser = CurrUserContextUtils.getCurrentUsername();
		super.setHasDeleted(false);
	}

	@SuppressWarnings("rawtypes")
	@Transient
	public void preDelete(AbstractDomain dbDomain) {
		setDbCreateInfo(dbDomain.getCreateTime(), dbDomain.getCreateUser(), dbDomain.getHasDeleted());
		super.setHasDeleted(true);
	}

	/**
	 * 设置创建时间和创建用户
	 * 
	 * @Title: setDbCreateInfo
	 * @param dbCreateTime
	 *            创建时间
	 * @param dbCreateUser
	 *            创建用户
	 * @param hasDeleted
	 *            是否删除
	 * @return: void
	 */
	@Transient
	public void setDbCreateInfo(Date dbCreateTime, String dbCreateUser, Boolean hasDeleted) {
		this.createTime = dbCreateTime;
		this.createUser = dbCreateUser;
		super.setHasDeleted(hasDeleted);
	}

	@Temporal(TemporalType.TIMESTAMP)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")  
	@Column(name = "create_time", nullable = false)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "create_user", nullable = false, length = 150)
	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")  
	@Column(name = "update_time")
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "update_user", length = 150)
	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

}
