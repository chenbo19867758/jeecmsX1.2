/**
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.common.util.MyDateUtils;

import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 信息实体类
 * 
 * @author ljw
 * @version 1.0
 * @date 2019-05-13
 *
 */
@Entity
@Table(name = "jc_sys_message")
public class SysMessage extends AbstractDomain<Integer> implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 接收对象类型全部 **/
	public static final int TARGETTYPE_ALL = 1;
	/** 接收对象类型全部管理员 **/
	public static final int TARGETTYPE_ALL_ADMIN = 2;
	/** 接收对象类型全部会员 **/
	public static final int TARGETTYPE_ALL_MEMBER = 3;
	/** 接收对象类型组织 **/
	public static final int TARGETTYPE_ORG = 4;
	/** 接收对象类型指定管理员 **/
	public static final int TARGETTYPE_APPOINT_ADMIN = 5;
	/** 接收对象类型会员等级 **/
	public static final int TARGETTYPE_MEMBER_LELVEL = 6;
	/** 接收对象类型会员组 **/
	public static final int TARGETTYPE_MEMBER_GROUP = 7;
	/** 接收对象类型指定会员 **/
	public static final int TARGETTYPE_APPOINT_MEMBER = 8;
	/** 正常状态 */
	public static final Integer MESSAGE_STATUS_NORMAL = 1;
	/** 删除状态 */
	public static final Integer MESSAGE_STATUS_DELETE = 2;

	private Integer id;
	/** 信息标题 */
	private String title;
	/** 内容 */
	private String content;
	/** 发件人名称 */
	private String  sendUserName;
	/** 接收对象类型 1-全部 2-全部管理员 3-全部会员 4-组织 5-指定管理员 6-会员等级 7-会员组 8-指定会员 */
	private Integer recTargetType;
	/** 组织id */
	private Integer orgId;
	/** 用户id **/
	private Integer userId;
	/** 会员组id */
	private Integer memeberGroupId;
	/** 会员等级id */
	private Integer memeberLevelId;
	/** 会员id */
	private Integer memeberId;
	/** 状态 1-正常 2-删除 */
	private Integer status;
	/** 是否已经查看，true为已看，false为未看 **/
	private Boolean read;
	/** 用户 **/
	private CoreUser coreUser;

	public SysMessage() {
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_sys_message", pkColumnValue = "jc_sys_message", 
			initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_message")
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@NotNull
	@Length(max = 150)
	@Column(name = "title", nullable = false, length = 150)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@NotNull
	@Length(max = 500)
	@Column(name = "content", nullable = false, length = 500)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@NotNull
	@Column(name = "rec_target_type", nullable = false, length = 6)
	public Integer getRecTargetType() {
		return recTargetType;
	}

	public void setRecTargetType(Integer recTargetType) {
		this.recTargetType = recTargetType;
	}

	@Column(name = "memeber_group_id", nullable = true, length = 11)
	public Integer getMemeberGroupId() {
		return memeberGroupId;
	}

	public void setMemeberGroupId(Integer memeberGroupId) {
		this.memeberGroupId = memeberGroupId;
	}

	@Column(name = "memeber_id", nullable = true, length = 11)
	public Integer getMemeberId() {
		return memeberId;
	}

	public void setMemeberId(Integer memeberId) {
		this.memeberId = memeberId;
	}

	@Column(name = "org_id", nullable = true, length = 11)
	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	@Column(name = "status", nullable = false, length = 6)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "user_id", length = 11)
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name = "member_level_id", length = 11)
	public Integer getMemeberLevelId() {
		return memeberLevelId;
	}

	public void setMemeberLevelId(Integer memeberLevelId) {
		this.memeberLevelId = memeberLevelId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	public CoreUser getCoreUser() {
		return coreUser;
	}

	public void setCoreUser(CoreUser coreUser) {
		this.coreUser = coreUser;
	}

	@Transient
	public Boolean getRead() {
		return read;
	}

	public void setRead(Boolean read) {
		this.read = read;
	}

	/**
	 * 获取收件人名称
	* @Title: getUsername 
	* @return
	 */
	@Transient
	public String getUsername() {
		String username = "";
		if (coreUser != null) {
			username = coreUser.getUsername();
		}
		return username;
	}

	@Transient
	public String getCreateTimes() {
		String time = MyDateUtils.formatDate(super.getCreateTime());
		return time;
	}

	@Column(name = "send_user_name", length = 11)
	public String getSendUserName() {
		return sendUserName;
	}

	public void setSendUserName(String sendUserName) {
		this.sendUserName = sendUserName;
	}
	
}