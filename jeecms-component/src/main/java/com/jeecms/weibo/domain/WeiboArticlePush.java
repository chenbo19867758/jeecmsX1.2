/*
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.weibo.domain;

import java.io.Serializable;
import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.content.domain.Content;

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
 * 微博推送记录
 * @author ljw
 * @version 1.0
 * @date 2019-06-18
 */
@Entity
@Table(name = "jc_weibo_article_push")
public class WeiboArticlePush extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	/** 所属站点 */
	private Integer siteId;
	/** 原文id */
	private Integer contentId;
	/** 用户uid */
	private String uid;
	/** 文章标题 */
	private String title;
	/** 原文链接地址 */
	private String articleSourceUrl;
	/** 微博推送改地址 */
	private String articleWeiboUrl;
	/** 推送结果（1-成功 2-失败） */
	private Integer pushResult;

	/**关联内容**/
	private Content content;
	/**关联微博信息**/
	private WeiboInfo weiboInfo;
	
	public WeiboArticlePush() {}

	@Override
	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_weibo_article_push", pkColumnValue = "jc_weibo_article_push", 
			initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_weibo_article_push")
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "site_id", nullable = false, length = 11)
	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	@Column(name = "content_id", nullable = false, length = 11)
	public Integer getContentId() {
		return contentId;
	}

	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}

	@Column(name = "u_uid", nullable = false, length = 50)
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	@Column(name = "title", nullable = true, length = 255)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "article_source_url", nullable = true, length = 255)
	public String getArticleSourceUrl() {
		return articleSourceUrl;
	}

	public void setArticleSourceUrl(String articleSourceUrl) {
		this.articleSourceUrl = articleSourceUrl;
	}

	@Column(name = "article_weibo_url", nullable = true, length = 255)
	public String getArticleWeiboUrl() {
		return articleWeiboUrl;
	}

	public void setArticleWeiboUrl(String articleWeiboUrl) {
		this.articleWeiboUrl = articleWeiboUrl;
	}

	@Column(name = "push_result", nullable = true, length = 6)
	public Integer getPushResult() {
		return pushResult;
	}

	public void setPushResult(Integer pushResult) {
		this.pushResult = pushResult;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "content_id", insertable = false, updatable = false)
	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "u_uid", referencedColumnName = "u_uid", insertable = false, updatable = false)
	public WeiboInfo getWeiboInfo() {
		return weiboInfo;
	}

	public void setWeiboInfo(WeiboInfo weiboInfo) {
		this.weiboInfo = weiboInfo;
	}
	
}