/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.jeecms.audit.util.AuditUtil;
import com.jeecms.common.base.domain.AbstractDomain;

/**
 * 内容智能审核错误详情
 * 
 * @author: chenming
 * @date: 2019年12月25日 下午2:33:51
 */
@Entity
@Table(name = "jc_content_check_detail")
public class ContentCheckDetail extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/** 审核中*/
	public static final int UNDER_REVIEW = 1;
	/** 审核成功*/
	public static final int REVIEW_SUCCESS = 2;
	/** 审核失败(百度调用失败、余量不足)*/
	public static final int REVIEW_ERROR = 3;
	/** 其它错误(图片格式不对、内容校验出错)*/
	public static final int OTHER_ERROR = 4;
	
	/** 主键值*/
	private Integer id;
	/** 内容ID */
	private Integer contentId;
	/** 内容审核违禁内容 */
	private String checkBanContent;
	/** 该内容字段错误个数 */
	private Integer fieldErrorNum;
	/** 1-审核中、2-审核成功(有违禁词)、3-审核失败(百度调用失败、余量不足)、4-图片格式不对，内容校验出错 */
	private Integer status;
	/** 内容审核失败错误信息(比如:余量不足) */
	private String checkErrorContent;
	/** 内容表中内容标识*/
	private String checkMark;
	
	
	private Integer checkUserId;
	
	
	/** 文本审核场景*/
	private String textScene;
	/** 图片审核场景 */
	private String pictureScene;
	
	private Content content;

	public ContentCheckDetail() {
		
	}

	public ContentCheckDetail(Integer contentId, Integer status, String checkErrorContent, String checkMark,String textScene,String pictureScene,Integer userId) {
		super();
		this.contentId = contentId;
		this.status = status;
		this.checkErrorContent = checkErrorContent;
		this.checkMark = checkMark;
		if (StringUtils.isNotBlank(textScene)) {
			this.textScene = textScene;
		}
		if (StringUtils.isNotBlank(pictureScene)) {
			this.pictureScene = pictureScene;
		}
		this.checkUserId = userId;
	}



	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_content_check_detail", pkColumnValue = "jc_content_check_detail", initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_content_check_detail")
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "content_id", nullable = false, length = 11)
	public Integer getContentId() {
		return contentId;
	}

	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}

	@Column(name = "check_ban_content", nullable = true, length = 715827882)
	public String getCheckBanContent() {
		return checkBanContent;
	}

	public void setCheckBanContent(String checkBanContent) {
		this.checkBanContent = checkBanContent;
	}

	@Column(name = "field_error_num", nullable = true, length = 11)
	public Integer getFieldErrorNum() {
		return fieldErrorNum;
	}

	public void setFieldErrorNum(Integer fieldErrorNum) {
		this.fieldErrorNum = fieldErrorNum;
	}

	@Column(name = "status", nullable = false, length = 11)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "check_error_content", nullable = true, length = 715827882)
	public String getCheckErrorContent() {
		return checkErrorContent;
	}

	public void setCheckErrorContent(String checkErrorContent) {
		this.checkErrorContent = checkErrorContent;
	}

	@Column(name = "check_mark", nullable = false, length = 150)
	public String getCheckMark() {
		return checkMark;
	}

	public void setCheckMark(String checkMark) {
		this.checkMark = checkMark;
	}

	@Column(name = "text_scene", nullable = true, length = 20)
	public String getTextScene() {
		return textScene;
	}

	public void setTextScene(String textScene) {
		this.textScene = textScene;
	}

	@Column(name = "picture_scene", nullable = true, length = 20)
	public String getPictureScene() {
		return pictureScene;
	}

	public void setPictureScene(String pictureScene) {
		this.pictureScene = pictureScene;
	}

	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id", insertable = false, updatable = false)
	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}
	
	/**
	 * 审核策略获得文本审核类型集合
	 * @Title: getTextAuditScenes
	 * @return: List
	 */
	@Transient
	public List<Integer> getTextAuditScenes() {
		List<Integer> auditScenes = new ArrayList<Integer>();
		if (StringUtils.isNotBlank(getTextScene())) {
			auditScenes = AuditUtil.arrayToList(getTextScene());
		}
		return auditScenes;
	}
	
	/**
	 * 审核策略获得图片审核类型集合
	 * @Title: getPictureAuditScenes
	 * @return: List
	 */
	@Transient
	public List<Integer> getPictureAuditScenes() {
		List<Integer> auditScenes = new ArrayList<Integer>();
		if (StringUtils.isNotBlank(getPictureScene())) {
			auditScenes = AuditUtil.arrayToList(getPictureScene());
		}
		return auditScenes;
	}

	@Column(name = "check_user_id", nullable = false, length = 11)
	public Integer getCheckUserId() {
		return checkUserId;
	}

	public void setCheckUserId(Integer checkUserId) {
		this.checkUserId = checkUserId;
	}

}