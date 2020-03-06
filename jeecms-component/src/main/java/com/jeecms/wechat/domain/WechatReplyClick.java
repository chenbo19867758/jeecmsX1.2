package com.jeecms.wechat.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.jeecms.common.base.domain.AbstractDomain;

/**
 * 事件触发
 * 
 * @author ASUS
 * @version 1.0
 * @date 2018-08-08
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Entity
@Table(name = "jc_wechat_reply_click")
public class WechatReplyClick extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 主键 */
	private Integer id;
	/** 所属公众号appId */
	private String appId;
	/** 回复类型(1-关注后自动回复 2-默认回复) */
	private Integer replyType;
	/** 回复消息的内部 */
	private Integer replyContentId;

	/** 回复内容对象*/
	private WechatReplyContent wechatReplyContent;

	public WechatReplyClick() {
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_wechat_reply_click", pkColumnValue = "jc_wechat_reply_click", 
					initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_wechat_reply_click")
	@NotNull(groups = {UpdateClick.class})
	@Null(groups = {SaveClick.class})
	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "app_id", nullable = false, length = 50)
	@NotBlank(groups = {SaveClick.class})
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@Column(name = "reply_content_id", nullable = true, length = 50)
	public Integer getReplyContentId() {
		return replyContentId;
	}

	public void setReplyContentId(Integer replyContentId) {
		this.replyContentId = replyContentId;
	}

	@Column(name = "reply_type", nullable = false, length = 6)
	@NotNull(groups = {SaveClick.class})
	@Min(value = 1,groups = {SaveClick.class})
	@Max(value = 2,groups = {SaveClick.class})
	public Integer getReplyType() {
		return replyType;
	}

	public void setReplyType(Integer replyType) {
		this.replyType = replyType;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reply_content_id", insertable = false, updatable = false)
	public WechatReplyContent getWechatReplyContent() {
		return wechatReplyContent;
	}

	public void setWechatReplyContent(WechatReplyContent wechatReplyContent) {
		this.wechatReplyContent = wechatReplyContent;
	}
	
	/**
	 * 新增事件触发
	 */
	public interface SaveClick{
		
	}
	
	/**
	 * 修改事件触发
	 */
	public interface UpdateClick{
		
	}

}