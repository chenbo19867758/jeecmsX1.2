/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.common.base.domain.IBaseFlow;
import com.jeecms.common.base.domain.IBaseSite;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.questionnaire.constants.QuestionnaireConstant;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.system.domain.CmsSite;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 投票调查实体
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-10-18
 */
@Entity
@Table(name = "jc_sys_vote")
public class SysQuestionnaire extends AbstractDomain<Integer> implements Serializable, IBaseSite, IBaseFlow {
	private static final long serialVersionUID = 1L;
	private Integer id;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 描述
	 */
	private String details;
	/**
	 * 答卷数量
	 */
	private Integer answerCount = 0;
	/**
	 * 审核状态
	 */
	private Boolean checkStatus;
	/**
	 * 状态（0未发布1流转中2已驳回3进行中4已结束）
	 */
	private Integer status;
	/**
	 * 二维码
	 */
	private String qrCode;

	/**
	 * 流程实例ID
	 */
	private String flowProcessId;
	/**
	 * 流转发起人
	 */
	private Integer flowStartUserId;
	/**
	 * 流程ID
	 */
	private Integer workflowId;
	/**
	 * 当前工作流节点ID
	 */
	private Integer currNodeId;
	/**
	 * 问卷浏览量
	 */
	private Integer pageViews = 0;
	private String bgConfig;
	private Integer bgImgId;
	private String headConfig;
	private Integer headImgId;
	private String fontConfig;
	private String contConfig;
	private String subConfig;

	private ResourcesSpaceData bgImg;
	private ResourcesSpaceData headImg;

	/**
	 * 站点id
	 */
	private Integer siteId;

	/**
	 * 站点
	 */
	private CmsSite site;

	/**
	 * 问卷设置
	 */
	private SysQuestionnaireConfig questionnaireConfig;

	private List<SysQuestionnaireSubject> subjects;

	private List<SysQuestionnaireAnswer> answers;

	public SysQuestionnaire() {
	}

	public SysQuestionnaire(String title, String details, Integer siteId, CmsSite site) {
		this.title = title;
		this.details = details;
		this.siteId = siteId;
		this.site = site;
	}

	@Override
	@Transient
	public Integer getFlowId() {
		return getWorkflowId();
	}

	@Override
	public void setFlowId(Integer workflowId) {
		this.workflowId = workflowId;
	}

	@Override
	@Column(name = "flow_process_id")
	public String getFlowProcessId() {
		return flowProcessId;
	}

	@Override
	public void setFlowProcessId(String flowProcessId) {
		this.flowProcessId = flowProcessId;
	}

	@Override
	@Column(name = "flow_start_user_id")
	public Integer getFlowStartUserId() {
		return flowStartUserId;
	}

	@Override
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

	@Override
	public void setStatusByChannel(Integer status) {
		this.checkStatus = QuestionnaireConstant.STATUS_NO_REVIEW.equals(status);
	}

	@Override
	@Column(name = "curr_node_id")
	public Integer getCurrNodeId() {
		return currNodeId;
	}

	@Override
	public void setCurrNodeId(Integer currNodeId) {
		this.currNodeId = currNodeId;
	}

	@Override
	@Transient
	public String getMsgPlace() {
		return getTitle();
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_sys_vote", pkColumnValue = "jc_sys_vote", initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_vote")
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "title", nullable = false, length = 150)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "details", nullable = true, length = 450)
	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	@Column(name = "answer_count", nullable = true, length = 6)
	public Integer getAnswerCount() {
		return answerCount;
	}

	public void setAnswerCount(Integer answerCount) {
		this.answerCount = answerCount;
	}

	@Column(name = "check_status", nullable = true, length = 1)
	public Boolean getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(Boolean checkStatus) {
		this.checkStatus = checkStatus;
	}

	@Column(name = "status", nullable = false, length = 1)
	public Integer getStatus() {
		return status;
	}

	@Override
	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "qr_code", nullable = true, length = 255)
	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	@Column(name = "page_views", nullable = true, length = 6)
	public Integer getPageViews() {
		return pageViews;
	}

	public void setPageViews(Integer pageViews) {
		this.pageViews = pageViews;
	}

	@Column(name = "bg_config", nullable = true, length = 4000)
	public String getBgConfig() {
		return bgConfig;
	}

	public void setBgConfig(String bgConfig) {
		this.bgConfig = bgConfig;
	}

	@Column(name = "head_config", nullable = true, length = 4000)
	public String getHeadConfig() {
		return headConfig;
	}

	public void setHeadConfig(String headConfig) {
		this.headConfig = headConfig;
	}

	@Column(name = "font_config", nullable = true, length = 4000)
	public String getFontConfig() {
		return fontConfig;
	}

	public void setFontConfig(String fontConfig) {
		this.fontConfig = fontConfig;
	}

	@Column(name = "cont_config", nullable = true, length = 4000)
	public String getContConfig() {
		return contConfig;
	}

	public void setContConfig(String contConfig) {
		this.contConfig = contConfig;
	}

	@Column(name = "sub_config", nullable = true, length = 4000)
	public String getSubConfig() {
		return subConfig;
	}

	public void setSubConfig(String subConfig) {
		this.subConfig = subConfig;
	}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "questionnaire", cascade = {CascadeType.PERSIST})
	public SysQuestionnaireConfig getQuestionnaireConfig() {
		return questionnaireConfig;
	}

	public void setQuestionnaireConfig(SysQuestionnaireConfig questionnaireConfig) {
		this.questionnaireConfig = questionnaireConfig;
	}

	@Column(name = "site_id")
	@Override
	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "site_id", insertable = false, updatable = false)
	public CmsSite getSite() {
		return site;
	}

	public void setSite(CmsSite site) {
		this.site = site;
	}

	@Column(name = "bg_img_id", nullable = true, length = 11)
	public Integer getBgImgId() {
		return bgImgId;
	}

	public void setBgImgId(Integer bgImgId) {
		this.bgImgId = bgImgId;
	}

	@Column(name = "head_img_id", nullable = true, length = 11)
	public Integer getHeadImgId() {
		return headImgId;
	}

	public void setHeadImgId(Integer headImgId) {
		this.headImgId = headImgId;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bg_img_id", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	public ResourcesSpaceData getBgImg() {
		return bgImg;
	}

	public void setBgImg(ResourcesSpaceData bgImg) {
		this.bgImg = bgImg;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "head_img_id", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	public ResourcesSpaceData getHeadImg() {
		return headImg;
	}

	public void setHeadImg(ResourcesSpaceData headImg) {
		this.headImg = headImg;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "questionnaire", orphanRemoval = true, cascade = CascadeType.MERGE)
	public List<SysQuestionnaireSubject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<SysQuestionnaireSubject> subjects) {
		this.subjects = subjects;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "questionnaire", orphanRemoval = true, cascade = CascadeType.MERGE)
	public List<SysQuestionnaireAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<SysQuestionnaireAnswer> answers) {
		this.answers = answers;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		SysQuestionnaire that = (SysQuestionnaire) o;

		if (id != null ? !id.equals(that.id) : that.id != null) {
			return false;
		}
		if (title != null ? !title.equals(that.title) : that.title != null) {
			return false;
		}
		if (details != null ? !details.equals(that.details) : that.details != null) {
			return false;
		}
		if (answerCount != null ? !answerCount.equals(that.answerCount) : that.answerCount != null) {
			return false;
		}
		if (checkStatus != null ? !checkStatus.equals(that.checkStatus) : that.checkStatus != null) {
			return false;
		}
		if (status != null ? !status.equals(that.status) : that.status != null) {
			return false;
		}
		if (qrCode != null ? !qrCode.equals(that.qrCode) : that.qrCode != null) {
			return false;
		}
		if (flowProcessId != null ? !flowProcessId.equals(that.flowProcessId) : that.flowProcessId != null) {
			return false;
		}
		if (flowStartUserId != null ? !flowStartUserId.equals(that.flowStartUserId) : that.flowStartUserId != null) {
			return false;
		}
		if (workflowId != null ? !workflowId.equals(that.workflowId) : that.workflowId != null) {
			return false;
		}
		if (currNodeId != null ? !currNodeId.equals(that.currNodeId) : that.currNodeId != null) {
			return false;
		}
		if (pageViews != null ? !pageViews.equals(that.pageViews) : that.pageViews != null) {
			return false;
		}
		if (bgConfig != null ? !bgConfig.equals(that.bgConfig) : that.bgConfig != null) {
			return false;
		}
		if (bgImgId != null ? !bgImgId.equals(that.bgImgId) : that.bgImgId != null) {
			return false;
		}
		if (headConfig != null ? !headConfig.equals(that.headConfig) : that.headConfig != null) {
			return false;
		}
		if (headImgId != null ? !headImgId.equals(that.headImgId) : that.headImgId != null) {
			return false;
		}
		if (fontConfig != null ? !fontConfig.equals(that.fontConfig) : that.fontConfig != null) {
			return false;
		}
		if (contConfig != null ? !contConfig.equals(that.contConfig) : that.contConfig != null) {
			return false;
		}
		if (subConfig != null ? !subConfig.equals(that.subConfig) : that.subConfig != null) {
			return false;
		}
		if (bgImg != null ? !bgImg.equals(that.bgImg) : that.bgImg != null) {
			return false;
		}
		if (headImg != null ? !headImg.equals(that.headImg) : that.headImg != null) {
			return false;
		}
		if (siteId != null ? !siteId.equals(that.siteId) : that.siteId != null) {
			return false;
		}
		if (site != null ? !site.equals(that.site) : that.site != null) {
			return false;
		}
		if (questionnaireConfig != null ? !questionnaireConfig.equals(that.questionnaireConfig) : that.questionnaireConfig != null) {
			return false;
		}
		if (subjects != null ? !subjects.equals(that.subjects) : that.subjects != null) {
			return false;
		}
		return answers != null ? answers.equals(that.answers) : that.answers == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (title != null ? title.hashCode() : 0);
		result = 31 * result + (details != null ? details.hashCode() : 0);
		result = 31 * result + (answerCount != null ? answerCount.hashCode() : 0);
		result = 31 * result + (checkStatus != null ? checkStatus.hashCode() : 0);
		result = 31 * result + (status != null ? status.hashCode() : 0);
		result = 31 * result + (qrCode != null ? qrCode.hashCode() : 0);
		result = 31 * result + (flowProcessId != null ? flowProcessId.hashCode() : 0);
		result = 31 * result + (flowStartUserId != null ? flowStartUserId.hashCode() : 0);
		result = 31 * result + (workflowId != null ? workflowId.hashCode() : 0);
		result = 31 * result + (currNodeId != null ? currNodeId.hashCode() : 0);
		result = 31 * result + (pageViews != null ? pageViews.hashCode() : 0);
		result = 31 * result + (bgConfig != null ? bgConfig.hashCode() : 0);
		result = 31 * result + (bgImgId != null ? bgImgId.hashCode() : 0);
		result = 31 * result + (headConfig != null ? headConfig.hashCode() : 0);
		result = 31 * result + (headImgId != null ? headImgId.hashCode() : 0);
		result = 31 * result + (fontConfig != null ? fontConfig.hashCode() : 0);
		result = 31 * result + (contConfig != null ? contConfig.hashCode() : 0);
		result = 31 * result + (subConfig != null ? subConfig.hashCode() : 0);
		result = 31 * result + (bgImg != null ? bgImg.hashCode() : 0);
		result = 31 * result + (headImg != null ? headImg.hashCode() : 0);
		result = 31 * result + (siteId != null ? siteId.hashCode() : 0);
		result = 31 * result + (site != null ? site.hashCode() : 0);
		result = 31 * result + (questionnaireConfig != null ? questionnaireConfig.hashCode() : 0);
		result = 31 * result + (subjects != null ? subjects.hashCode() : 0);
		result = 31 * result + (answers != null ? answers.hashCode() : 0);
		return result;
	}

	/**
	 * 开始时间
	 *
	 * @return Date
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@Transient
	public Date getBeginTime() {
		if (getQuestionnaireConfig() != null) {
			return getQuestionnaireConfig().getBeginTime();
		}
		return null;
	}

	/**
	 * 结束时间
	 *
	 * @return Date
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@Transient
	public Date getEndTime() {
		if (getQuestionnaireConfig() != null) {
			return getQuestionnaireConfig().getEndTime();
		}
		return null;
	}

	/**
	 * 结束日期
	 *
	 * @return String
	 */
	@Transient
	public String getDeadline() {
		if (getQuestionnaireConfig() != null && getQuestionnaireConfig().getEndTime() != null) {
			return MyDateUtils.formatDate(getQuestionnaireConfig().getEndTime(), MyDateUtils.COM_Y_M_D_PATTERN);
		}
		return null;
	}

	/**
	 * 背景图地址
	 *
	 * @return String
	 */
	@Transient
	public String getBgImgUrl() {
		if (getBgImg() != null) {
			return getBgImg().getUrl();
		}
		return "";
	}

	/**
	 * 页眉图地址
	 *
	 * @return String
	 */
	@Transient
	public String getHeadImgUrl() {
		if (getHeadImg() != null) {
			return getHeadImg().getUrl();
		}
		return "";
	}

	/**
	 * 获取预览 URL
	 *
	 * @Title: getPreviewUrl
	 * @return: String
	 */
	@Transient
	public String getPreviewUrl() {
		/*StringBuilder url = new StringBuilder();
		url.append(getSite().getSitePreviewUrl());
		return url.append("?contentId=").append(this.id).append("&type=")
			.append(WebConstants.PREVIEW_TYPE_QUESTIONNAIRE).toString();*/
		return getSite().getUrlWhole() + "interact-view.htm?id=" + getId();
	}


	@Transient
	public String getUrl() {
		return getSite().getUrlWhole() + "interact-vote.htm?id=" + getId();
	}

	/**
	 * 获取二维码
	 *
	 * @return String
	 */
	@Transient
	public String getQrCodeUrl() {
		String siteUrl = getSite().getUrlWhole();
		return siteUrl + "common/qrcode/136" + "?val=" + getUrl();
	}

	/**
	 * 嵌入式部署
	 *
	 * @return String
	 */
	@Transient
	public String getEmbedded() {
		String jsUrl = "";
		jsUrl = "<script charset='UTF-8' defer>(function(h){function n(a){return null===a?null:a.scrollHeight>a.clientHeight?a:n(a.parentNode)}";
		jsUrl += "function t(b){if(b.data){var f=JSON.parse(b.data);!f.height||p||q||(d.style.height=+f.height+'px');";
		jsUrl += "if(f.getter){b={};var f=[].concat(f.getter),k,h=f.length,m,c,g,e;for(k=0;k<h;k++){m=k;c=f[k]||{};";
		jsUrl += "c.n&&(m=c.n);g=null;try{switch(c.t){case 'window':e=window;break;case 'scrollParent':e=n(a)||window;";
		jsUrl += "break;default:e=a}if(c.e)if('rect'===c.v){g={};var l=e.getBoundingClientRect();";
		jsUrl += "g={top:l.top,left:l.left,width:l.width,height:l.height}}else g=e[c.v].apply(e,[].concat(c.e))||!0;";
		jsUrl += "else c.s?(e[c.v]=c.s,g=!0):g=e[c.v]||!1}catch(u){}b[m]=g}b.innerState=!p&&!q;a.contentWindow.postMessage(JSON.stringify({queryRes:b}),'*')}}}";
		jsUrl += "for(var r=h.document,b=r.documentElement;b.childNodes.length&&1==b.lastChild.nodeType;)b=b.lastChild;";
		jsUrl += "var d=b.parentNode,a=r.createElement('iframe');d.style.overflowY='auto';d.style.overflowX='hidden';";
		jsUrl += "var p=d.style.height&&'auto'!==d.style.height,q='absolute'===d.style.position||window.getComputedStyle&&'absolute'===window.getComputedStyle(d,null).getPropertyValue('position')||d.currentStyle&&'absolute'===d.currentStyle.position;";
		jsUrl += "h.addEventListener&&h.addEventListener('message',t,!1);";
		jsUrl += "a.src='" + getUrl() + "';a.onload=function(){a.contentWindow.postMessage(JSON.stringify({cif:1}),'*')};";
		jsUrl += "a.frameBorder=0;a.scrolling='yes';a.style.display='block';a.style.minWidth='100%';";
		jsUrl += "a.style.width='799';a.style.height='800px';a.style.border='none';a.style.overflow='auto';";
		jsUrl += "d.insertBefore(a,b)})(window);";
		jsUrl += "</";
		jsUrl += "script>";

		return jsUrl;
	}

	/**
	 * IFrame代码：不能自适应高度
	 *
	 * @return String
	 */
	@Transient
	public String getIframe() {
		return "<iframe src='" + getUrl() + "' width='799' height='800' frameborder='0' style='overflow:auto'></iframe>";
	}

	/**
	 * 分享logo图
	 *
	 * @return String
	 */
	@Transient
	public String getShareLogoUrl() {
		return getQuestionnaireConfig().getShareLogoUrl();
	}

	@Transient
	public String getCoverPicUrl() {
		return getQuestionnaireConfig().getCoverPicUrl();
	}
}