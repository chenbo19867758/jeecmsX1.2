/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.member.domain;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.vo.ContentFrontVo;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-24
 */
@Entity
@Table(name = "jc_user_collection")
public class UserCollection extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	/**
	 * 文章id
	 */
	private Integer contentId;
	/**
	 * 文章
	 */
	private Content content;
	/**
	 * 会员id
	 */
	private Integer userId;
	/**
	 * 会员
	 */
	private CoreUser user;

	private ContentFrontVo contentMobileVo;

	public UserCollection() {
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "js_user_collection", pkColumnValue = "js_user_collection", initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "js_user_collection")
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@NotNull
	@Column(name = "content_id", nullable = false, length = 11)
	public Integer getContentId() {
		return contentId;
	}

	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "content_id", insertable = false, updatable = false)
	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	@Column(name = "user_id", nullable = false, length = 11)
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	public CoreUser getUser() {
		return user;
	}

	public void setUser(CoreUser user) {
		this.user = user;
	}

	@Transient
	public ContentFrontVo getContentMobileVo() {
		return contentMobileVo;
	}

	public void setContentMobileVo(ContentFrontVo contentMobileVo) {
		this.contentMobileVo = contentMobileVo;
	}

	/**
	 * 发布时间简短中文格式 刚刚，1分钟前...
	 *
	 * @Title: getReleaseTimeString
	 * @return: String
	 */
	@Transient
	public String getCollectionTime() {
		if (getContent() != null) {
			return getContent().getReleaseTimeString();
		}
		return "";
	}

	@Transient
	public String getCreateDate() {
		return MyDateUtils.formatDate(getCreateTime());
	}

	/**
	 * 内容访问地址
	 *
	 * @return String
	 */
	@Transient
	public String getUrl() {
		if (getContent() != null) {
			return getContent().getUrl();
		} else {
			return null;
		}
	}

	/**
	 * 获取内容图片
	 *
	 * @return String
	 */
	@Transient
	public String getIconUrl() {
		if (getContent() != null) {
			return getContent().getIconUrl();
		} else {
			return null;
		}
	}

	/**
	 * 获取内容标题
	 *
	 * @return String
	 */
	@Transient
	public String getTitle() {
		if (getContent() != null) {
			return getContent().getTitle();
		} else {
			return null;
		}
	}

	/**
	 * 获取内容来源
	 *
	 * @return String
	 */
	@Transient
	public String getSource() {
		if (getContent() != null && getContent().getSource() != null) {
			return getContent().getSource().getSourceName();
		} else {
			return null;
		}
	}

}