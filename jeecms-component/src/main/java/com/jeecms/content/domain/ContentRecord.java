/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.domain;

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

import com.jeecms.common.base.domain.AbstractDomain;

/**
 * 内容操作记录
 * 
 * @author: chenming
 * @date: 2019年5月16日 上午10:55:40
 */
@Entity
@Table(name = "jc_content_record")
public class ContentRecord extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer contentRecordId;
	/** 内容ID */
	private Integer contentId;
	/** 操作用户 */
	private String userName;
	/** 操作类型 */
	private String operateType;
	/** 备注(审核原因等) */
	private String opreateRemark;
	/** ip */
	private String ip;

	/** 内容对象 */
	private Content content;

	/**
	 * 内容操作时实体
	 */
	public ContentRecord(Integer contentId, String userName, String operateType, String opreateRemark, String ip,
			Content content) {
		super();
		this.contentId = contentId;
		this.userName = userName;
		this.operateType = operateType;
		this.opreateRemark = opreateRemark;
		this.ip = ip;
		this.content = content;
	}

	public ContentRecord() {
	}

	/**
	 * 构造函数
	 * 
	 * @param contentId   内容ID
	 * @param userName    用户名称
	 * @param operateType 操作类型
	 */
	public ContentRecord(Integer contentId, String userName, String operateType) {
		super();
		this.contentId = contentId;
		this.userName = userName;
		this.operateType = operateType;
	}

	@Id
	@Column(name = "content_record_id", nullable = false, length = 11)
	@TableGenerator(name = "jc_content_record", pkColumnValue = "jc_content_record", 
					initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_content_record")
	public Integer getContentRecordId() {
		return this.contentRecordId;
	}

	public void setContentRecordId(Integer contentRecordId) {
		this.contentRecordId = contentRecordId;
	}

	@Column(name = "content_id", nullable = false, length = 11)
	public Integer getContentId() {
		return contentId;
	}

	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}

	@Column(name = "user_name", nullable = false, length = 50)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "operate_type", nullable = false, length = 50)
	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	@Column(name = "opreate_remark", nullable = true, length = 715827882)
	public String getOpreateRemark() {
		return opreateRemark;
	}

	public void setOpreateRemark(String opreateRemark) {
		this.opreateRemark = opreateRemark;
	}

	@Column(name = "ip", nullable = true, length = 50)
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "content_id", insertable = false, updatable = false)
	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

}