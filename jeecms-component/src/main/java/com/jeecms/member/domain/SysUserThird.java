/*
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.member.domain;

import java.io.Serializable;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.base.domain.AbstractDomain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * 用户第三方账户绑定
 * 
 * @author ljw
 * @version 1.0
 * @date 2019-07-19
 */
@Entity
@Table(name = "jc_sys_user_third")
public class SysUserThird extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	/** 第三方平台appid */
	private String appId;
	/** 第三方平台返回唯一id */
	private String thirdId;
	/** 微信openid，主要为后期支付使用 */
	private String openId;
	/** 三方平台用户名 */
	private String thirdUsername;
	/** 会员ID */
	private Integer memberId;
	/** 会员用户名 */
	private String username;
	/** 第三方登陆标识(QQ WECHAT WEIBO) */
	private String thirdTypeCode;

	/** 会员信息 */
	private CoreUser member;
	
	public SysUserThird() {
	}
	
	/**
	 * 构造函数
	 **/
	public SysUserThird(String thirdId, String thirdUsername, String username, String thirdTypeCode, 
			Integer memberId,
			String openId, String appId) {
		super();
		this.thirdId = thirdId;
		this.thirdUsername = thirdUsername;
		this.username = username;
		this.thirdTypeCode = thirdTypeCode;
		this.memberId = memberId;
		this.openId = openId;
		this.appId = appId;
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_sys_user_third", pkColumnValue = "jc_sys_user_third", 
			initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_user_third")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "app_id", nullable = true, length = 50)
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@Column(name = "third_id", nullable = false, length = 255)
	public String getThirdId() {
		return thirdId;
	}

	public void setThirdId(String thirdId) {
		this.thirdId = thirdId;
	}

	@Column(name = "third_username", nullable = true, length = 150)
	public String getThirdUsername() {
		return thirdUsername;
	}

	public void setThirdUsername(String thirdUsername) {
		this.thirdUsername = thirdUsername;
	}

	@Column(name = "member_id", nullable = false, length = 11)
	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	@Column(name = "username", nullable = false, length = 150)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "third_type_code", nullable = false, length = 50)
	public String getThirdTypeCode() {
		return thirdTypeCode;
	}

	public void setThirdTypeCode(String thirdTypeCode) {
		this.thirdTypeCode = thirdTypeCode;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", referencedColumnName = "id", insertable = false, updatable = false)
	public CoreUser getMember() {
		return member;
	}

	public void setMember(CoreUser member) {
		this.member = member;
	}

	@Column(name = "open_id", nullable = false, length = 50)
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
}