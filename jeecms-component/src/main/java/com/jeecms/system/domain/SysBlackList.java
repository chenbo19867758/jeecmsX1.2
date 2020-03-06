/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.interact.domain.UserComment;

/**
 * 黑名单实体类
 * 
 * @author: chenming
 * @date: 2019年5月5日 下午6:00:23
 */
@Entity
@Table(name = "jc_sys_black_list")
@Where(clause = "deleted_flag = 0")
public class SysBlackList extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 状态为评论 */
	public static final Integer USER_COMMENT_TYPE = 1;

	/** 全局唯一标识符 */
	private Integer id;
	/** 站点Id */
	private Integer siteId;
	/** ip */
	private String ip;
	/** 用户id */
	private Integer userId;
	/** 用户名 */
	private String userName;
	/** 业务类型（1-评论模块） */
	private Integer type;

	/** 用户来源站点信息 */
	private String userSiteName;
	/** 评论集合 */
	private List<UserComment> userComments;

	/** 会员对象 */
	private CoreUser user;
	/** 关联站点信息 */
	private CmsSite site;

	public SysBlackList() {

	}

	@Id
	@TableGenerator(name = "jc_sys_black_list", pkColumnValue = "jc_sys_black_list", 
					initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_black_list")
	@Column(name = "id", unique = true, nullable = false)
	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "site_id", nullable = false, insertable = false, updatable = false)
	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
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

	@Column(name = "type", nullable = false, length = 11)
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	public CoreUser getUser() {
		return user;
	}

	public void setUser(CoreUser user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "site_id")
	public CmsSite getSite() {
		return site;
	}

	public void setSite(CmsSite site) {
		this.site = site;
	}

	@Transient
	public String getUserSiteName() {
		return userSiteName;
	}

	public void setUserSiteName(String userSiteName) {
		this.userSiteName = userSiteName;
	}

	@Transient
	public List<UserComment> getUserComments() {
		return userComments;
	}

	public void setUserComments(List<UserComment> userComments) {
		this.userComments = userComments;
	}

}