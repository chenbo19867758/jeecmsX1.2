/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.domain;

import com.jeecms.common.base.domain.AbstractIdDomain;
import com.jeecms.content.constants.CmsModelConstant;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.system.domain.ContentMark;
import com.jeecms.system.domain.ContentSource;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 内容扩展表
 * 
 * @author: chenming
 * @date: 2019年5月15日 下午4:22:09
 */
@Entity
@Table(name = "jc_content_ext")
public class ContentExt extends AbstractIdDomain<Integer> implements  Serializable {
	private static final long serialVersionUID = 1L;

	/** 是否新窗口打开链接名称 */
	public static final String IS_NEW_TARGET_NAME = "isNewTarget";
	/** 发文字号-机关代字 **/
	public static final String SUE_ORG_NAME = "issueOrg";
	/** 发文字号-年份 **/
	public static final String SUE_YEAR_NAME = "issueYear";
	/** 发文字号-顺序号 **/
	public static final String SUE_NUM_NAME = "issueNum";

	public static final String SOURCE_NAME = "sourceName";

	public static final String SOURCE_LINK = "sourceLink";

	private Integer id;
	/** 摘要 */
	private String description;
	/** 作者 */
	private String author;
	/** 关键词 */
	private String keyWord;
	/** 内容来源id */
	private Integer contentSourceId;
	/** 外部链接 */
	private String outLink;
	/** 新窗口打开外部链接（0-否 1-是） */
	private Boolean isNewTarget;
	/** 手机模板 */
	private String tplMobile;
	/** 指定模版 */
	private String tplPc;
	/** 微信资源id */
	private String wxMediaId;
	/** 微博资源id */
	private String wbMediaId;
	/** 图片资源id */
	private Integer picResId;
	/** 发文字号-机关代字 **/
	private Integer issueOrg;
	/** 发文字号-年份 **/
	private Integer issueYear;
	/** 发文字号-顺序号 **/
	private String issueNum;
	/** 流程实例ID */
	private String flowProcessId;
	/** 流转发起人 */
	private Integer flowStartUserId;
	/** 流程ID */
	private Integer workflowId;
	/** 当前工作流节点ID */
	private Integer currNodeId;
	/** 本月访问量 */
	private Integer viewsMonth;
	/** 本月评论量 */
	private Integer commentsMonth;
	/** 本月下载量 */
	private Integer downloadsMonth;
	/** 本月点赞数 */
	private Integer upsMonth;
	/** 本月点踩数 */
	private Integer downsMonth;
	/** 本周访问量 */
	private Integer viewsWeek;
	/** 本周评论量 */
	private Integer commentsWeek;
	/** 本周下载量 */
	private Integer downloadsWeek;
	/** 本周点赞数 */
	private Integer upsWeek;
	/** 本周点踩数 */
	private Integer downsWeek;
	/** 本日访问量 */
	private Integer viewsDay;
	/** 本日评论量 */
	private Integer commentsDay;
	/** 本日下载量 */
	private Integer downloadsDay;
	/** 本日点赞数 */
	private Integer upsDay;
	/** 本日点踩数 */
	private Integer downsDay;
	/** 内容PDF地址 **/
	private String pdfUrl;

	private Integer docResourceId;

	/** 原始资源对象 */
	private ResourcesSpaceData docResource;
	/** 内容对象 */
	private Content content;
	/** 内容来源 **/
	private ContentSource contentSource;
	/** 图片资源对象 */
	private ResourcesSpaceData reData;
	/** 发文-机关代号 */
	private ContentMark sueOrg;
	/** 发文-年号 */
	private ContentMark sueYear;

	public ContentExt() {
	}

	@Id
	@Column(name = "content_id", nullable = false, length = 11)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	@Column(name = "description", nullable = true, length = 500)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "author", nullable = true, length = 150)
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Column(name = "key_word", nullable = true, length = 255)
	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	@Column(name = "content_source_id", nullable = true, length = 11)
	public Integer getContentSourceId() {
		return contentSourceId;
	}

	public void setContentSourceId(Integer contentSourceId) {
		this.contentSourceId = contentSourceId;
	}

	@Column(name = "out_link", nullable = true, length = 255)
	public String getOutLink() {
		return outLink;
	}

	public void setOutLink(String outLink) {
		this.outLink = outLink;
	}

	@Column(name = "is_new_target", nullable = false, length = 1)
	public Boolean getIsNewTarget() {
		return isNewTarget;
	}

	public void setIsNewTarget(Boolean isNewTarget) {
		this.isNewTarget = isNewTarget;
	}

	@Column(name = "tpl_mobile", nullable = true, length = 150)
	public String getTplMobile() {
		return tplMobile;
	}

	public void setTplMobile(String tplMobile) {
		this.tplMobile = tplMobile;
	}

	@Column(name = "tpl_pc", nullable = true, length = 150)
	public String getTplPc() {
		return tplPc;
	}

	public void setTplPc(String tplPc) {
		this.tplPc = tplPc;
	}

	@Column(name = "wx_media_id", nullable = true, length = 150)
	public String getWxMediaId() {
		return wxMediaId;
	}

	public void setWxMediaId(String wxMediaId) {
		this.wxMediaId = wxMediaId;
	}

	@Column(name = "wb_media_id", nullable = true, length = 50)
	public String getWbMediaId() {
		return wbMediaId;
	}

	public void setWbMediaId(String wbMediaId) {
		this.wbMediaId = wbMediaId;
	}

	@Column(name = "pic_res_id", nullable = true, length = 11)
	public Integer getPicResId() {
		return picResId;
	}

	public void setPicResId(Integer picResId) {
		this.picResId = picResId;
	}

	@Column(name = "views_month", nullable = false, length = 11)
	public Integer getViewsMonth() {
		return viewsMonth;
	}

	public void setViewsMonth(Integer viewsMonth) {
		this.viewsMonth = viewsMonth;
	}

	@Column(name = "comments_month", nullable = false, length = 11)
	public Integer getCommentsMonth() {
		return commentsMonth;
	}

	public void setCommentsMonth(Integer commentsMonth) {
		this.commentsMonth = commentsMonth;
	}

	@Column(name = "downloads_month", nullable = false, length = 11)
	public Integer getDownloadsMonth() {
		return downloadsMonth;
	}

	public void setDownloadsMonth(Integer downloadsMonth) {
		this.downloadsMonth = downloadsMonth;
	}

	@Column(name = "ups_month", nullable = false, length = 11)
	public Integer getUpsMonth() {
		return upsMonth;
	}

	public void setUpsMonth(Integer upsMonth) {
		this.upsMonth = upsMonth;
	}

	@Column(name = "downs_month", nullable = false, length = 11)
	public Integer getDownsMonth() {
		return downsMonth;
	}

	public void setDownsMonth(Integer downsMonth) {
		this.downsMonth = downsMonth;
	}

	@Column(name = "views_week", nullable = false, length = 11)
	public Integer getViewsWeek() {
		return viewsWeek;
	}

	public void setViewsWeek(Integer viewsWeek) {
		this.viewsWeek = viewsWeek;
	}

	@Column(name = "comments_week", nullable = false, length = 11)
	public Integer getCommentsWeek() {
		return commentsWeek;
	}

	public void setCommentsWeek(Integer commentsWeek) {
		this.commentsWeek = commentsWeek;
	}

	@Column(name = "downloads_week", nullable = false, length = 11)
	public Integer getDownloadsWeek() {
		return downloadsWeek;
	}

	public void setDownloadsWeek(Integer downloadsWeek) {
		this.downloadsWeek = downloadsWeek;
	}

	@Column(name = "ups_week", nullable = false, length = 11)
	public Integer getUpsWeek() {
		return upsWeek;
	}

	public void setUpsWeek(Integer upsWeek) {
		this.upsWeek = upsWeek;
	}

	@Column(name = "downs_week", nullable = false, length = 11)
	public Integer getDownsWeek() {
		return downsWeek;
	}

	public void setDownsWeek(Integer downsWeek) {
		this.downsWeek = downsWeek;
	}

	@Column(name = "views_day", nullable = false, length = 11)
	public Integer getViewsDay() {
		return viewsDay;
	}

	public void setViewsDay(Integer viewsDay) {
		this.viewsDay = viewsDay;
	}

	@Column(name = "comments_day", nullable = false, length = 11)
	public Integer getCommentsDay() {
		return commentsDay;
	}

	public void setCommentsDay(Integer commentsDay) {
		this.commentsDay = commentsDay;
	}

	@Column(name = "downloads_day", nullable = false, length = 11)
	public Integer getDownloadsDay() {
		return downloadsDay;
	}

	public void setDownloadsDay(Integer downloadsDay) {
		this.downloadsDay = downloadsDay;
	}

	@Column(name = "ups_day", nullable = false, length = 11)
	public Integer getUpsDay() {
		return upsDay;
	}

	public void setUpsDay(Integer upsDay) {
		this.upsDay = upsDay;
	}

	@Column(name = "downs_day", nullable = false, length = 11)
	public Integer getDownsDay() {
		return downsDay;
	}

	public void setDownsDay(Integer downsDay) {
		this.downsDay = downsDay;
	}

	@MapsId
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "content_id", nullable = false)
	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "content_source_id", insertable = false, updatable = false)
	public ContentSource getContentSource() {
		return contentSource;
	}

	public void setContentSource(ContentSource contentSource) {
		this.contentSource = contentSource;
	}

	@Column(name = "issue_org", length = 11)
	public Integer getIssueOrg() {
		return issueOrg;
	}

	public void setIssueOrg(Integer issueOrg) {
		this.issueOrg = issueOrg;
	}

	@Column(name = "issue_year", length = 11)
	public Integer getIssueYear() {
		return issueYear;
	}

	public void setIssueYear(Integer issueYear) {
		this.issueYear = issueYear;
	}

	@Column(name = "issue_num")
	public String getIssueNum() {
		return issueNum;
	}

	public void setIssueNum(String issueNum) {
		this.issueNum = issueNum;
	}

	@Column(name = "flow_process_id")
	public String getFlowProcessId() {
		return flowProcessId;
	}

	public void setFlowProcessId(String flowProcessId) {
		this.flowProcessId = flowProcessId;
	}

	@Column(name = "flow_start_user_id")
	public Integer getFlowStartUserId() {
		return flowStartUserId;
	}

	public void setFlowStartUserId(Integer flowStartUserId) {
		this.flowStartUserId = flowStartUserId;
	}

	@Column(name = "workflow_id")
	public Integer getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Integer workflowId) {
		this.workflowId = workflowId;
	}

	@Column(name = "curr_node_id")
	public Integer getCurrNodeId() {
		return currNodeId;
	}

	public void setCurrNodeId(Integer currNodeId) {
		this.currNodeId = currNodeId;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pic_res_id", referencedColumnName = "id", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	public ResourcesSpaceData getReData() {
		return reData;
	}

	public void setReData(ResourcesSpaceData reData) {
		this.reData = reData;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "issue_org", referencedColumnName = "id", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	public ContentMark getSueOrg() {
		return sueOrg;
	}

	public void setSueOrg(ContentMark sueOrg) {
		this.sueOrg = sueOrg;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "issue_year", referencedColumnName = "id", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	public ContentMark getSueYear() {
		return sueYear;
	}

	public void setSueYear(ContentMark sueYear) {
		this.sueYear = sueYear;
	}

	@Column(name = "pdf_url")
	public String getPdfUrl() {
		return pdfUrl;
	}

	public void setPdfUrl(String pdfUrl) {
		this.pdfUrl = pdfUrl;
	}

	@Column(name = "doc_resource_id")
	public Integer getDocResourceId() {
		return docResourceId;
	}

	public void setDocResourceId(Integer docResourceId) {
		this.docResourceId = docResourceId;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doc_resource_id", referencedColumnName = "id", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	public ResourcesSpaceData getDocResource() {
		return docResource;
	}

	public void setDocResource(ResourcesSpaceData docResource) {
		this.docResource = docResource;
	}

	@Transient
	public String getRealDescription() {
		if (getContent() != null && getContent().getModel().existItem(CmsModelConstant.FIELD_SYS_DESCRIPTION)) {
			return description;
		}
		return null;
	}

	@Transient
	public ContentSource getRealContentSource() {
		if (getContent() != null && getContent().getModel().existItem(CmsModelConstant.FIELD_SYS_CONTENT_SOURCE)) {
			return contentSource;
		}
		return null;
	}

	@Transient
	public String getRealAuthor() {
		if (getContent() != null && getContent().getModel().existItem(CmsModelConstant.FIELD_SYS_AUTHOR)) {
			return author;
		}
		return null;
	}

	@Transient
	public Boolean getRealIsNewTarget() {
		if (getContent() != null && getContent().getModel().existItem(CmsModelConstant.FIELD_SYS_CONTENT_OUTLINK)) {
			return isNewTarget;
		}
		return false;
	}

	@Transient
	public String getRealOutLink() {
		if (getContent() != null && getContent().getModel().existItem(CmsModelConstant.FIELD_SYS_CONTENT_OUTLINK)) {
			return outLink;
		}

		return null;
	}

	@Transient
	public String getRealKeyWord() {
		if (getContent() != null && getContent().getModel().existItem(CmsModelConstant.FIELD_SYS_KEY_WORD)) {
			return keyWord;
		}
		return null;
	}

	@Transient
	public ResourcesSpaceData getRealDocResource() {
		if (getContent() != null && getContent().getModel().existItem(CmsModelConstant.FIELD_SYS_TEXTLIBRARY)) {
			return docResource;
		}
		return null;
	}

	@Transient
	public ResourcesSpaceData getRealReData() {
		if (getContent() != null && getContent().getModel().existItem(CmsModelConstant.FIELD_SYS_CONTENT_RESOURCE)) {
			return reData;
		}
		return null;
	}

	@Transient
	public String getRealIssueNum() {
		if (getContent() != null
				&& getContent().getModel().existItem(CmsModelConstant.FIELD_SYS_CONTENT_POST_CONTENT)) {
			return issueNum;
		}
		return null;
	}

	@Transient
	public ContentMark getRealSueYear() {
		if (getContent() != null
				&& getContent().getModel().existItem(CmsModelConstant.FIELD_SYS_CONTENT_POST_CONTENT)) {
			return sueYear;
		}
		return null;
	}

	@Transient
	public ContentMark getRealSueOrg() {
		if (getContent() != null
				&& getContent().getModel().existItem(CmsModelConstant.FIELD_SYS_CONTENT_POST_CONTENT)) {
			return sueOrg;
		}
		return null;
	}

}