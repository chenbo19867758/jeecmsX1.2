/*
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.wechat.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.jeecms.common.base.domain.AbstractDomain;

/**
 * 已群发成功的文章
 * @author ljw
 * @version 1.0
 * @date 2019-06-04
 */
@Entity
@Table(name = "jc_wechat_send_article")
public class WechatSendArticle extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	/** 所属公众号appId */
	private String appId;
	/** 群发id */
	private String msgDataId;
	/** 多图文时，用来指定第几篇图文，从0开始，不带默认返回该msg_data_id的第一篇图文 */
	private Integer msgDataIndex;
	/** 当前已读文章留言最大的评论id */
	private Integer maxUserCommentId;
	/**文章标题**/
	private String title;
	/**是否开启留言**/
	private Integer open;
	
	public WechatSendArticle() {
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_wechat_send_article", pkColumnValue = "jc_wechat_send_article", 
					initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_wechat_send_article")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "app_id", nullable = false, length = 50)
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
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

	@Column(name = "max_user_comment_id", nullable = false)
	public Integer getMaxUserCommentId() {
		return maxUserCommentId;
	}

	public void setMaxUserCommentId(Integer maxUserCommentId) {
		this.maxUserCommentId = maxUserCommentId;
	}

	@Column(name = "article_title", length = 255)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "open")
	public Integer getOpen() {
		return open;
	}

	public void setOpen(Integer open) {
		this.open = open;
	}
}