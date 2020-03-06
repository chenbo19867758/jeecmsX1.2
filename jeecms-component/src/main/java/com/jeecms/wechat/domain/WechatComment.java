/*
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.wechat.domain;

import java.util.Date;
import java.io.Serializable;
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
 * 微信留言实体类
 * @author ljw
 * @version 1.0
 * @date 2019-05-31
 */
@Entity
@Table(name = "jc_wechat_comment")
public class WechatComment extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	/**公众号APPID**/
	private String appId;
	/** 群发id */
	private String msgDataId;
	/** 多图文时，用来指定第几篇图文，从0开始，不带默认返回该msg_data_id的第一篇图文 */
	private Integer msgDataIndex;
	/** 微信返回评论id */
	private String userCommentId;
	/** 用户openid */
	private String openid;
	/** 素材json */
	private String content;
	/** 是否精选评论，0为即非精选，1为true，即精选 */
	private Boolean commentType;
	/** 评论时间 */
	private Date commentTime;
	/** 作者回复内容 */
	private String replyContent;
	/** 作者回复时间 */
	private Date replyTime;
	/**关联粉丝**/
	private WechatFans wechatFans;

	public WechatComment() {}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_wechat_comment", pkColumnValue = "jc_wechat_comment", 
			initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_wechat_comment")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "msg_data_id", nullable = false, length = 150)
	public String getMsgDataId() {
		return msgDataId;
	}

	public void setMsgDataId(String msgDataId) {
		this.msgDataId = msgDataId;
	}

	@Column(name = "msg_data_index", nullable = false, length = 11)
	public Integer getMsgDataIndex() {
		return msgDataIndex;
	}

	public void setMsgDataIndex(Integer msgDataIndex) {
		this.msgDataIndex = msgDataIndex;
	}

	@Column(name = "user_comment_id", nullable = false, length = 50)
	public String getUserCommentId() {
		return userCommentId;
	}

	public void setUserCommentId(String userCommentId) {
		this.userCommentId = userCommentId;
	}

	@Column(name = "openid", nullable = true, length = 50)
	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	@Column(name = "content", nullable = false, length = 715827882)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "comment_type", nullable = false, length = 1)
	public Boolean getCommentType() {
		return commentType;
	}

	public void setCommentType(Boolean commentType) {
		this.commentType = commentType;
	}

	@Column(name = "comment_time", nullable = false, length = 19)
	public Date getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(Date commentTime) {
		this.commentTime = commentTime;
	}

	@Column(name = "reply_content", nullable = true, length = 715827882)
	public String getReplyContent() {
		return replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	@Column(name = "reply_time", nullable = false, length = 19)
	public Date getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}

	@Column(name = "app_id", nullable = false, length = 50)
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "openid", referencedColumnName = "openid", insertable = false, updatable = false)
	public WechatFans getWechatFans() {
		return wechatFans;
	}

	public void setWechatFans(WechatFans wechatFans) {
		this.wechatFans = wechatFans;
	}

}