/**
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.vo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.jeecms.common.util.MyDateUtils;

/**
 * 返回给前台的VO
 * @author: ljw
 * @date: 2019年1月23日 上午10:32:39
 */
@Entity
public class MessageVo {
	/** 消息id */
	private Integer messageId;
	/** 标题 **/
	private String title;
	/** 内容 */
	private String content;
	/** 状态（0为未读，1为已读） */
	private Integer status;
	/** 管理员ID **/
	private Integer userId;
	/** 会员ID **/
	private Integer memberId;
	/** 管理员消息ID **/
	private Integer coreUserMessageId;
	/** 会员消息ID **/
	private Integer memberMessageId;
	/** 创建时间 **/
	private Date createTime;
	/** 发件人名称 */
	private String sendUserName;

	public MessageVo() {}

	@Id
	public Integer getMessageId() {
		return messageId;
	}

	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	/**是否已读**/
	public Integer getStatus() {
		if (status == null) {
			this.status = 0;
		}
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Transient
	public String getCreateTimes() {
		String time = MyDateUtils.formatDate(getCreateTime());
		return time;
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public Integer getMemberMessageId() {
		return memberMessageId;
	}

	public void setMemberMessageId(Integer memberMessageId) {
		this.memberMessageId = memberMessageId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getCoreUserMessageId() {
		return coreUserMessageId;
	}

	public void setCoreUserMessageId(Integer coreUserMessageId) {
		this.coreUserMessageId = coreUserMessageId;
	}

	public String getSendUserName() {
		return sendUserName;
	}

	public void setSendUserName(String sendUserName) {
		this.sendUserName = sendUserName;
	}

}
