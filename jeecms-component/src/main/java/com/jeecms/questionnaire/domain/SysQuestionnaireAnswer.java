/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.jeecms.common.base.domain.AbstractDomain;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author xiaohui
 * @version 1.0
 * @date 2019-10-18
 */
@Entity
@Table(name = "jc_sys_vote_answer")
public class SysQuestionnaireAnswer extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final Integer COMPUTER = 1;
	public static final String COMPUTER_DEVICE = "PC";
	public static final Integer MOBILE = 2;
	public static final String MOBILE_DEVICE = "移动设备";

	/**
	 * 创建时间倒序
	 */
	public static final Integer ORDER_BY_CREATE_TIME_DESC = 1;

	/**
	 * 创建时间顺序
	 */
	public static final Integer ORDER_BY_CREATE_TIME_ASC = 2;

	private Integer id;
	/**
	 * 问卷id
	 */
	private Integer questionnaireId;
	/**
	 * 题目id
	 */
	private Integer subjectId;
	/**
	 * 回复人Id
	 */
	private Integer replayId;
	/**
	 * 回复人
	 */
	@Excel(name = "参与人", orderNum = "1", isImportField = "true_st")
	private String replayName;
	/**
	 * 答案
	 */
	@Excel(name = "填写内容", orderNum = "6", width = 40, isImportField = "true_st")
	private String answer;
	/**
	 * 选项类答案Id
	 */
	private String answerId;
	/**
	 * 访问设备（具体）
	 */
	@Excel(name = "网络设备", orderNum = "3", isImportField = "true_st")
	private String device;
	/**
	 * 访问设备(1pc,2手机)
	 */
	private Integer deviceType;
	/**
	 * 访问ip
	 */
	@Excel(name = "参与人ip地址", orderNum = "4", isImportField = "true_st")
	private String ip;
	/**
	 * 省
	 */
	private String province;
	/**
	 * 市
	 */
	private String city;
	/**
	 * 是否有效
	 */
	private Boolean isEffective = true;
	@Excel(name = "参与时间", width = 40, orderNum = "2", databaseFormat = "yyyy-MM-dd HH:mm:ss", isImportField = "true_st")
	private Date createTime;
	/**
	 * 问卷对象
	 */
	private SysQuestionnaire questionnaire;
	/**
	 * 题目对象
	 */
	private SysQuestionnaireSubject subject;
	/**
	 * 省市
	 */
	@Excel(name = "地域", orderNum = "5", width = 35, isImportField = "true_st")
	private String address;

	private Boolean isFile;

	private Map<String, String> attr = new LinkedHashMap<>();

	public SysQuestionnaireAnswer() {
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_sys_vote_answer", pkColumnValue = "jc_sys_vote_answer", initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_vote_answer")
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "questionnaire_id", nullable = false, length = 11)
	public Integer getQuestionnaireId() {
		return questionnaireId;
	}

	public void setQuestionnaireId(Integer questionnaireId) {
		this.questionnaireId = questionnaireId;
	}

	@Column(name = "subject_id", nullable = false, length = 11)
	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	@Column(name = "replay_id", nullable = false, length = 11)
	public Integer getReplayId() {
		return replayId;
	}

	public void setReplayId(Integer replayId) {
		this.replayId = replayId;
	}

	@Column(name = "replay_name", nullable = false, length = 150)
	public String getReplayName() {
		return replayName;
	}

	public void setReplayName(String replayName) {
		this.replayName = replayName;
	}

	@Column(name = "answer", nullable = true, length = 4000)
	public String getAnswer() {
		return answer;
	}


	public void setAnswer(String answer) {
		this.answer = answer;
	}

    @Column(name = "answer_id", nullable = true, length = 100)
    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

	@Column(name = "device", nullable = true, length = 20)
	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	@Column(name = "device_type", nullable = true, length = 6)
	public Integer getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
	}

	@Column(name = "ip", nullable = true, length = 15)
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "province", nullable = true, length = 50)
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Column(name = "city", nullable = true, length = 50)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "is_effective", nullable = true, length = 1)
	public Boolean getIsEffective() {
		return isEffective;
	}

	public void setIsEffective(Boolean isEffective) {
		this.isEffective = isEffective;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@Override
	public Date getCreateTime() {
		return createTime;
	}

	@Override
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinColumn(name = "questionnaire_id", insertable = false, updatable = false)
	public SysQuestionnaire getQuestionnaire() {
		return questionnaire;
	}

	public void setQuestionnaire(SysQuestionnaire questionnaire) {
		this.questionnaire = questionnaire;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinColumn(name = "subject_id", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	public SysQuestionnaireSubject getSubject() {
		return subject;
	}

	public void setSubject(SysQuestionnaireSubject subject) {
		this.subject = subject;
	}

	@Transient
	public Boolean getIsFile() {
		return isFile;
	}

	public void setIsFile(Boolean isFile) {
		this.isFile = isFile;
	}

	@ElementCollection
	@CollectionTable(name = "jc_sys_vote_answer_attr", joinColumns = {
		@JoinColumn(insertable = false, updatable = false, name = "answer_id")})
	@MapKeyColumn(name = "attr_key", length = 255)
	@Column(name = "attr_value", length = 255)
	public Map<String, String> getAttr() {
		return attr;
	}

	public void setAttr(Map<String, String> attr) {
		this.attr = attr;
	}

	@Transient
	public String getAddress() {
		if (address == null) {
			return (getProvince() != null ? getProvince() : "") + (getCity() != null ? getCity() : "");
		}
		return address;
	}

	public void setAddress(String address) {
		if (StringUtils.isNotBlank(address)) {
			this.address = address;
		} else {
			this.address = (getProvince() != null ? getProvince() : "") + (getCity() != null ? getCity() : "");
		}
	}
}