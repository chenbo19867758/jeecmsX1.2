/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.interact.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.base.domain.AbstractSortDomain;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.content.domain.Content;
import com.jeecms.system.domain.CmsSite;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户评论实体类
 * 
 * @author: chenming
 * @date: 2019年5月5日 下午5:51:47
 */
@Entity
@Table(name = "jc_user_comment")
public class UserComment extends AbstractSortDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 待审核 */
	public static final Short CHECK_WAIT = 0;
	/** 审核通过 */
	public static final Short CHECK_BY = 1;
	/** 审核不通过 */
	public static final Short CHECK_FAIL = 2;

	/** 排序-最热 */
	public static final Short SORT_HOTTEST = 1;
	/** 排序-最新 */
	public static final Short SORT_LATEST = 2;

	public static final String USER_COMMENT_CACHE_KEY = "USERCOMMENT";

	public static final String USER_COMMENT_TIME_INTERVAL = "timeInterval";

	/** 全局唯一标识符 */
	private Integer id;
	/** 站点ID */
	private Integer siteId;
	/** 评论用户id */
	private Integer userId;
	/** 内容id */
	private Integer contentId;
	/** 评论内容 */
	private String commentText;
	/** 评论ip */
	private String ip;
	/** 是否回复(0-否 1-是) */
	private Boolean isReply;
	/** 状态（0-待审核 1-审核通过 2-审核未通过） */
	private Short status;
	/** 点踩数 */
	private Integer downCount;
	/** 点赞数 */
	private Integer upCount;
	/** 回复时间 */
	private Date replyTime;
	/** 父级ID */
	protected Integer parentId;
	/** 回复的评论的id值 */
	private Integer replyCommentId;
	/** 会员是否被禁止 */
	private Boolean isUserDisable = false;
	/** IP是否被禁止 */
	private Boolean isIpDisable = false;
	/** 是否被推荐 */
	private Boolean isTop;
	/** 是否被举报 */
	private Boolean isReport;
	/** 后台管理员回复的id值 */
	private Integer replyAdminCommentId;
	/** 用户所在的区域(用户IP获取到的区域)*/
	private String visitorArea;

	private Boolean isLike = false;
	
	private Boolean report = true;
	
	/** 全部评论数量 */
	private Integer allNum;
	/** 待审核评论数量 */
	private Integer pendingReview;
	/** 审核成功评论数量 */
	private Integer successReview;
	/** 审核失败评论数量 */
	private Integer errorReview;

	/** 评论会员对象 */
	private CoreUser user;
	/** 该评论的父类评论 */
	private UserComment parent;
	/** 该评论的回复评论集合 */
	private List<UserComment> children = new ArrayList<UserComment>();
	/** 站点对象 */
	private CmsSite site;
	/** 内容主体对象 */
	private Content content;
	/** 回复内容对象 */
	private UserComment replyComment;
	/** 举报集合 */
	private List<UserCommentReport> userCommentReports;
	/** 后台管理员回复的对象*/
	private UserComment replyAdminComment;
	/**是否已加入累计评论数**/
	private Boolean commentFlag;

	public UserComment() {

	}

	@Id
	@TableGenerator(name = "jc_user_comment", pkColumnValue = "jc_user_comment", initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_user_comment")
	@Column(name = "id", unique = true, nullable = false)
	@Override
	public Integer getId() {
		return id;
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

	@Column(name = "user_id", nullable = true, length = 11)
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name = "content_id", nullable = false, length = 11)
	public Integer getContentId() {
		return contentId;
	}

	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}

	@Column(name = "comment_text", nullable = false, length = 500)
	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	@Column(name = "ip", nullable = true, length = 50)
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "status", nullable = false, length = 6)
	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	@Column(name = "is_reply", nullable = false, length = 1)
	public Boolean getIsReply() {
		return isReply;
	}

	public void setIsReply(Boolean isReply) {
		this.isReply = isReply;
	}

	@Column(name = "down_count", nullable = false, length = 11)
	public Integer getDownCount() {
		return downCount;
	}

	public void setDownCount(Integer downCount) {
		this.downCount = downCount;
	}

	@Column(name = "up_count", nullable = false, length = 11)
	public Integer getUpCount() {
		return upCount;
	}

	public void setUpCount(Integer upCount) {
		this.upCount = upCount;
	}

	@Column(name = "reply_time", nullable = true, length = 19)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	public CoreUser getUser() {
		return user;
	}

	public void setUser(CoreUser user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "site_id", insertable = false, updatable = false)
	public CmsSite getSite() {
		return site;
	}

	public void setSite(CmsSite site) {
		this.site = site;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "content_id", insertable = false, updatable = false)
	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	@Column(name = "parent_id", nullable = true, length = 11)
	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	/**
	 * 该用户是否被禁用
	 */
	@Transient
	public Boolean getIsUserDisable() {
		return isUserDisable;
	}

	public void setIsUserDisable(Boolean isUserDisable) {
		this.isUserDisable = isUserDisable;
	}

	/**
	 * 该IP是否被禁用
	 */
	@Transient
	public Boolean getIsIpDisable() {
		return isIpDisable;
	}

	public void setIsIpDisable(Boolean isIpDisable) {
		this.isIpDisable = isIpDisable;
	}

	@Column(name = "is_top", nullable = false, length = 1)
	public Boolean getIsTop() {
		return isTop;
	}

	public void setIsTop(Boolean isTop) {
		this.isTop = isTop;
	}

	@Column(name = "is_report", nullable = false, length = 1)
	public Boolean getIsReport() {
		return isReport;
	}

	public void setIsReport(Boolean isReport) {
		this.isReport = isReport;
	}

	@Column(name = "reply_comment_id", nullable = true, length = 11)
	public Integer getReplyCommentId() {
		return replyCommentId;
	}

	public void setReplyCommentId(Integer replyCommentId) {
		this.replyCommentId = replyCommentId;
	}

	@Column(name = "reply_admin_comment_id", nullable = true, length = 11)
	public Integer getReplyAdminCommentId() {
		return replyAdminCommentId;
	}

	public void setReplyAdminCommentId(Integer replyAdminCommentId) {
		this.replyAdminCommentId = replyAdminCommentId;
	}

	@Transient
	public Integer getAllNum() {
		return allNum;
	}

	public void setAllNum(Integer allNum) {
		this.allNum = allNum;
	}

	@Transient
	public Integer getPendingReview() {
		return pendingReview;
	}

	public void setPendingReview(Integer pendingReview) {
		this.pendingReview = pendingReview;
	}

	@Transient
	public Integer getSuccessReview() {
		return successReview;
	}

	public void setSuccessReview(Integer successReview) {
		this.successReview = successReview;
	}

	@Transient
	public Integer getErrorReview() {
		return errorReview;
	}

	public void setErrorReview(Integer errorReview) {
		this.errorReview = errorReview;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userComment")
	public List<UserCommentReport> getUserCommentReports() {
		return userCommentReports;
	}

	public void setUserCommentReports(List<UserCommentReport> userCommentReports) {
		this.userCommentReports = userCommentReports;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reply_comment_id", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	public UserComment getReplyComment() {
		return replyComment;
	}

	public void setReplyComment(UserComment replyComment) {
		this.replyComment = replyComment;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	public UserComment getParent() {
		return parent;
	}

	public void setParent(UserComment parent) {
		this.parent = parent;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
	public List<UserComment> getChildren() {
		return children;
	}

	public void setChildren(List<UserComment> children) {
		this.children = children;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reply_admin_comment_id", insertable = false, updatable = false)
	public UserComment getReplyAdminComment() {
		return replyAdminComment;
	}

	public void setReplyAdminComment(UserComment replyAdminComment) {
		this.replyAdminComment = replyAdminComment;
	}
	
	@Transient
	public Boolean getReply() {
		if (getReplyAdminCommentId() != null) {
			return true;
		}
		return false;
	}

	@Transient
	public CoreUser getReplyUser() {
		if (getReplyComment() != null) {
			return getReplyComment().getUser();
		}
		return null;
	}
	
	@Transient
	public Boolean getReplyParent() {
		if (getParentId() != null) {
			if (getParentId().equals(getReplyCommentId())) {
				return true;
			}
			return false;
		}
		return false;
	}

	@Transient
	public Boolean getIsLike() {
		return isLike;
	}

	public void setIsLike(Boolean isLike) {
		this.isLike = isLike;
	}

	@Transient
	public Boolean getReport() {
		return report;
	}

	public void setReport(Boolean report) {
		this.report = report;
	}
	
	@Transient
	public String getDistanceTime() {
        return MyDateUtils.getTime(getCreateTime());
	}

	@Transient
	public Integer getChildrenNum() {
		if (getChildren() != null && getChildren().size() > 0) {
			return getChildren().size();
		} else {
			return 0;
		}
	}
	
	@JoinColumn(name = "comment_flag")
	public Boolean getCommentFlag() {
		return commentFlag;
	}

	public void setCommentFlag(Boolean commentFlag) {
		this.commentFlag = commentFlag;
	}

	@Column(name = "visitor_area", nullable = true, length = 150)
	public String getVisitorArea() {
		if (StringUtils.isNoneBlank(visitorArea)) {
			return visitorArea;
		}
		return "其它";
	}

	public void setVisitorArea(String visitorArea) {
		this.visitorArea = visitorArea;
	}

}