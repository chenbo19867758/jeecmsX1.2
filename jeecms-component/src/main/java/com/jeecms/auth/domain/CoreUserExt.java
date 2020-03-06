/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.auth.domain;

import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.resource.domain.ResourcesSpaceData;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 用户信息扩展类
 *
 * @author ljw
 * @version 1.0
 * @date 2019-04-10
 */
@Entity
@Table(name = "jc_sys_user_ext")
public class CoreUserExt extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	/** 会员头像图片资源id */
	private Integer userImgId;
	/** 姓名 */
	private String realname;
	/** 性别 1 男 2 女 3 保密 */
	private Integer gender;
	/** 备注 */
	private String remark;
	/** 生日 */
	private Date birthday;
	/** 锁定时间 */
	private Date lockedTime;
	/** 锁定ip */
	private String lockedIp;
	/** 座机号 */
	private String linephone;
	/** 用户 **/
	private CoreUser user;
	/** 头像资源 **/
	private ResourcesSpaceData resourcesSpaceData;
	private String shareUserId;
	private String shareRoleId;
	private String shareOrgId;

	public CoreUserExt() {
	}

	@Override
	@Id
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "user_img_id", nullable = true, length = 11)
	public Integer getUserImgId() {
		return userImgId;
	}

	public void setUserImgId(Integer userImgId) {
		this.userImgId = userImgId;
	}

	@Column(name = "realname", nullable = true, length = 150)
	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	@Column(name = "gender", nullable = false, length = 6)
	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	@Column(name = "remark", nullable = true, length = 150)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "birthday", nullable = true, length = 19)
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Column(name = "locked_time", nullable = true, length = 19)
	public Date getLockedTime() {
		return lockedTime;
	}

	public void setLockedTime(Date lockedTime) {
		this.lockedTime = lockedTime;
	}

	@Column(name = "locked_ip", nullable = true, length = 50)
	public String getLockedIp() {
		return lockedIp;
	}

	public void setLockedIp(String lockedIp) {
		this.lockedIp = lockedIp;
	}

	@Column(name = "telephone", nullable = true, length = 20)
	public String getLinephone() {
		return linephone;
	}

	public void setLinephone(String linephone) {
		this.linephone = linephone;
	}

	@Column(name = "share_user_id", nullable = true, length = 500)
	public String getShareUserId() {
		return shareUserId;
	}

	public void setShareUserId(String shareUserId) {
		this.shareUserId = shareUserId;
	}

	@Column(name = "share_role_id", nullable = true, length = 500)
	public String getShareRoleId() {
		return shareRoleId;
	}

	public void setShareRoleId(String shareRoleId) {
		this.shareRoleId = shareRoleId;
	}

	@Column(name = "share_org_id", nullable = true, length = 500)
	public String getShareOrgId() {
		return shareOrgId;
	}

	public void setShareOrgId(String shareOrgId) {
		this.shareOrgId = shareOrgId;
	}

	@MapsId
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id", nullable = false)
	public CoreUser getUser() {
		return user;
	}

	public void setUser(CoreUser user) {
		this.user = user;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_img_id", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	public ResourcesSpaceData getResourcesSpaceData() {
		return resourcesSpaceData;
	}

	public void setResourcesSpaceData(ResourcesSpaceData resourcesSpaceData) {
		this.resourcesSpaceData = resourcesSpaceData;
	}
	
	@Transient
	public String getUserImgUrl() {
		return user.getHeadImage();
	}

	public static Integer GENDER_BOY = 1;
	public static Integer GENDER_GIRL = 2;
	public static Integer GENDER_SECRET = 3;
}