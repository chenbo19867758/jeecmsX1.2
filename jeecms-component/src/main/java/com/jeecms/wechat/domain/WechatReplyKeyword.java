package com.jeecms.wechat.domain;

import java.io.Serializable;

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
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import com.jeecms.common.base.domain.AbstractDomain;

/**
 * 关键字
 * 
 * @author ASUS
 * @version 1.0
 * @date 2018-08-08
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Entity
@Table(name = "jc_wechat_reply_keyword")
public class WechatReplyKeyword extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String IS_REPLY_NAME = "wechatIsReply";
	
	public static final String REPLY_KEY = "wechatReply";
	
	private Integer id;
	/** 所属公众号appId */
	private String appId;
	/** 回复内容ID */
	private Integer replyContentId;
	/** 关键词(精确匹配) */
	private String wordkeyEq;
	/** 关键词(模糊匹配) */
	private String wordkeyLike;

	private String[] keyEq;
	private String[] keyLike;
	/** 回复内容对象*/
	private WechatReplyContent wechatReplyContent;

	public WechatReplyKeyword() {
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_wechat_reply_keyword", pkColumnValue = "jc_wechat_reply_keyword", 
					initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_wechat_reply_keyword")
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "app_id", nullable = false, length = 50)
	@NotBlank
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@Column(name = "reply_content_id", nullable = false, length = 11)
	public Integer getReplyContentId() {
		return replyContentId;
	}

	public void setReplyContentId(Integer replyContentId) {
		this.replyContentId = replyContentId;
	}

	@Column(name = "wordkey_eq", nullable = true, length = 50)
	public String getWordkeyEq() {
		return wordkeyEq;
	}

	public void setWordkeyEq(String wordkeyEq) {
		this.wordkeyEq = wordkeyEq;
	}

	@Column(name = "wordkey_like", nullable = true, length = 50)
	public String getWordkeyLike() {
		return wordkeyLike;
	}

	public void setWordkeyLike(String wordkeyLike) {
		this.wordkeyLike = wordkeyLike;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reply_content_id", insertable = false, updatable = false)
	public WechatReplyContent getWechatReplyContent() {
		return wechatReplyContent;
	}

	public void setWechatReplyContent(WechatReplyContent wechatReplyContent) {
		this.wechatReplyContent = wechatReplyContent;
	}

	@Transient
	public String[] getKeyEq() {
		return keyEq;
	}

	public void setKeyEq(String[] keyEq) {
		this.keyEq = keyEq;
	}

	@Transient
	public String[] getKeyLike() {
		return keyLike;
	}

	public void setKeyLike(String[] keyLike) {
		this.keyLike = keyLike;
	}

	@Transient
	public String getMsgType() {
		if (wechatReplyContent != null) {
			return wechatReplyContent.getMsgType();
		}
		return null;
	}
	
}