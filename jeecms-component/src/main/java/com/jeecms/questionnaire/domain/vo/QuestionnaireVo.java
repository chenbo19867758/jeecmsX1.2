/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.jeecms.content.domain.vo.ContentFlowActionVo;
import com.jeecms.questionnaire.constants.QuestionnaireConstant;
import com.jeecms.questionnaire.domain.SysQuestionnaire;
import com.jeecms.questionnaire.domain.SysQuestionnaireConfig;
import com.jeecms.questionnaire.domain.SysQuestionnaireSubject;
import com.jeecms.questionnaire.domain.SysQuestionnaireSubjectOption;
import com.jeecms.questionnaire.domain.dto.QuestionnaireOptionDto;
import com.jeecms.questionnaire.domain.dto.QuestionnaireSubjectDto;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.alibaba.fastjson.JSON.parseArray;
import static com.alibaba.fastjson.JSON.parseObject;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/10/23 16:40
 */
public class QuestionnaireVo {

	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 标题
	 */
	private String title;
	/**
	 * 描述
	 */
	private String details;
	/**
	 * 状态
	 */
	private int status = 0;

	private Boolean checkStatus;

	/**
	 * 封面图片id
	 */
	private Integer coverPic;
	/**
	 * 封面图片地址
	 */
	private String coverPicUrl;
	/**
	 * 是否使用验证码
	 */
	private Boolean isVerification = false;
	/**
	 * 提交成功后处理方式（1显示文字信息2跳转到指定页面3显示结果）
	 */
	private Short processType = 1;
	/**
	 * 文字信息/指定页面
	 */
	private String prompt;
	/**
	 * 工作流id
	 */
	private Integer workflowId;
	/**
	 * 工作流名称
	 */
	private String workflowName;
	/**
	 * 开始时间
	 */
	private Date beginTime = Calendar.getInstance().getTime();
	/**
	 * 结束时间
	 */
	private Date endTime;
	/**
	 * 是否登录后才能投票
	 */
	private Boolean answerLimit = false;
	/**
	 * 用户答题次数限制
	 */
	private Integer userAnswerFrequencyLimit;
	/**
	 * 用户答题次数限制单位（1只能2每小时3每天）
	 */
	private Short userAnswerFrequencyLimitUnit;
	/**
	 * ip答题次数限制
	 */
	private Integer ipAnswerFrequencyLimit = 1;
	/**
	 * ip答题次数限制单位（1只能2每小时3每天）
	 */
	private Short ipAnswerFrequencyLimitUnit = 1;
	/**
	 * 设备答题次数限制
	 */
	private Integer deviceAnswerFrequencyLimit = 1;
	/**
	 * 设备答题次数限制单位（1只能2每小时3每天）
	 */
	private Short deviceAnswerFrequencyLimitUnit = 1;
	/**
	 * 是否只能微信使用
	 */
	private Boolean isOnlyWechat = false;
	/**
	 * 微信用户答题次数限制
	 */
	private Integer wechatAnswerFrequencyLimit;
	/**
	 * 微信用户答题次数限制（1只能2每小时3每天）
	 */
	private Short wechatAnswerFrequencyLimitUnit;
	/**
	 * 分享logo图id
	 */
	private Integer shareLogo;
	/**
	 * 分享logo图地址
	 */
	private String shareLogoUrl;
	/**
	 * 说明文字
	 */
	private String description;
	/**
	 * 预览地址
	 */
	private String previewUrl;

	private String qrCodeUrl;

	private List<QuestionnaireSubjectDto> subjects;

	private List<ContentFlowActionVo> actions;

	private Boolean revokeSupport;

	/**
	 * bgConfig : {"bgType":2,"bgImage":"","alignType":1,"opacity":100,"isRepeat":1,"bgColor":"#F0F0F0"}
	 * headConfig : {"bgImage":""}
	 * contConfig : {"bgColor":"#ffffff","hasBorder":1,"borderColor":"#e8e8e8","borderWidth":1,"borderRadius":0}
	 * fontConfig : {"titleStyle":{"fontSize":24,"fontWigth":600,"fontColor":"#333333","fontAlign":"left"},"descStyle":{"fontSize":14,"fontWigth":400,"fontColor":"#333333","fontAlign":"left"},"stemStyle":{"fontSize":14,"fontWigth":400,"fontColor":"#333333","fontAlign":"left"},"optStyle":{"fontSize":14,"fontWigth":400,"fontColor":"#333333","fontAlign":"left"}}
	 * subConfig : {"text":"","fontStyle":{"fontSize":14,"fontWigth":400,"fontColor":"#333333"},"bgColor":"#ffffff","hasBorder":1,"borderColor":"#e8e8e8","borderWidth":1,"borderRadius":0,"btnWidth":77,"btnHeight":32}
	 */

	private BgConfigBean bgConfig;
	private HeadConfigBean headConfig;
	private ContConfigBean contConfig;
	private FontConfigBean fontConfig;
	private SubConfigBean subConfig;

	public QuestionnaireVo() {
		super();
	}

	public QuestionnaireVo(Integer id, String title, String details, int status, Boolean checkStatus, Integer coverPic, String coverPicUrl,
						   Boolean isVerification, Short processType, String prompt, Integer workflowId, String workflowName,
						   Date beginTime, Date endTime, Boolean answerLimit, Integer userAnswerFrequencyLimit,
						   Short userAnswerFrequencyLimitUnit, Integer ipAnswerFrequencyLimit, Short ipAnswerFrequencyLimitUnit,
						   Integer deviceAnswerFrequencyLimit, Short deviceAnswerFrequencyLimitUnit, Boolean isOnlyWechat,
						   Integer wechatAnswerFrequencyLimit, Short wechatAnswerFrequencyLimitUnit, Integer shareLogo, String shareLogoUrl,
						   String description, List<QuestionnaireSubjectDto> subjects, BgConfigBean bgConfig, HeadConfigBean headConfig,
						   ContConfigBean contConfig, FontConfigBean fontConfig, SubConfigBean subConfig) {
		this.id = id;
		this.title = title;
		this.details = details;
		this.status = status;
		this.checkStatus = checkStatus;
		this.coverPic = coverPic;
		this.coverPicUrl = coverPicUrl;
		this.isVerification = isVerification;
		this.processType = processType;
		this.prompt = prompt;
		this.workflowId = workflowId;
		this.workflowName = workflowName;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.answerLimit = answerLimit;
		this.userAnswerFrequencyLimit = userAnswerFrequencyLimit;
		this.userAnswerFrequencyLimitUnit = userAnswerFrequencyLimitUnit;
		this.ipAnswerFrequencyLimit = ipAnswerFrequencyLimit;
		this.ipAnswerFrequencyLimitUnit = ipAnswerFrequencyLimitUnit;
		this.deviceAnswerFrequencyLimit = deviceAnswerFrequencyLimit;
		this.deviceAnswerFrequencyLimitUnit = deviceAnswerFrequencyLimitUnit;
		this.isOnlyWechat = isOnlyWechat;
		this.wechatAnswerFrequencyLimit = wechatAnswerFrequencyLimit;
		this.wechatAnswerFrequencyLimitUnit = wechatAnswerFrequencyLimitUnit;
		this.shareLogo = shareLogo;
		this.shareLogoUrl = shareLogoUrl;
		this.description = description;
		this.subjects = subjects;
		this.bgConfig = bgConfig;
		this.headConfig = headConfig;
		this.contConfig = contConfig;
		this.fontConfig = fontConfig;
		this.subConfig = subConfig;
	}

	public static QuestionnaireVo initView(SysQuestionnaire questionnaire, List<SysQuestionnaireSubject> subjects) {
		List<QuestionnaireSubjectDto> subjectDtos = new ArrayList<QuestionnaireSubjectDto>(subjects.size());
		for (SysQuestionnaireSubject subject : subjects) {
			List<SysQuestionnaireSubjectOption> subjectOption = subject.getOptions();
			List<QuestionnaireOptionDto.OptionsBean> options = new ArrayList<QuestionnaireOptionDto.OptionsBean>();
			if (!QuestionnaireConstant.SUBJECT_TYPE_CASCADE.equals(subject.getType())) {
				for (SysQuestionnaireSubjectOption option : subjectOption) {
					QuestionnaireOptionDto.OptionsBean bean = new QuestionnaireOptionDto.OptionsBean();
					bean.setIsDefault(option.getIsDefault());
					bean.setIsEemty(option.getIsOther());
					bean.setIsRequired(option.getIsRequired());
					bean.setName(option.getName());
					bean.setSortNum(option.getSortNum());
					bean.setPic(option.getPicId());
					bean.setPicUrl(option.getPicUrl());
					bean.setId(option.getId());
					options.add(bean);
				}
				options = options.parallelStream().sorted(Comparator.comparing(QuestionnaireOptionDto.OptionsBean::getSortNum))
					.collect(Collectors.toList());
			} else {
				if (subject.getCascadeOption().startsWith("[")) {
					options = parseArray(subject.getCascadeOption(), QuestionnaireOptionDto.OptionsBean.class);
				}
			}
			QuestionnaireOptionDto optionDto = new QuestionnaireOptionDto(subject.getColumn(), subject.getFileCountLimit(), subject.getFileCount(), subject.getFileSizeLimit(),
				subject.getFileSize(), subject.getFileType(), subject.getAllowFile(), subject.getFileTypeLimit(), subject.getFileSizeLimitUnit(), subject.getInputHigh(),
				subject.getIsLimit(), subject.getWordLimit(), subject.getInputNumLimit(), subject.getLimitCondition(), subject.getInputWidth(), subject.getRadio(), options);

			QuestionnaireSubjectDto dto = new QuestionnaireSubjectDto(subject.getId(), subject.getDragable(), subject.getEditor(), subject.getGroupIndex(), subject.getIcon(), subject.getIndex(), subject.getIsAnswer(),
				subject.getCustom(), optionDto, subject.getPrompt(), subject.getPreview(), subject.getPrompt(), subject.getTitle(), subject.getType(), parseObject(subject.getValue(), QuestionnaireSubjectDto.ValueBean.class));
			dto.setId(subject.getId());
			subjectDtos.add(dto);
		}
		//设置背景图地址
		BgConfigBean bg = parseObject(questionnaire.getBgConfig(), BgConfigBean.class);
		bg.setBgImageUrl(questionnaire.getBgImgUrl());
		HeadConfigBean head = parseObject(questionnaire.getHeadConfig(), HeadConfigBean.class);
		//设置页眉图地址
		head.setBgImageUrl(questionnaire.getHeadImgUrl());
		ContConfigBean cont = parseObject(questionnaire.getContConfig(), ContConfigBean.class);
		FontConfigBean font = parseObject(questionnaire.getFontConfig(), FontConfigBean.class);
		SubConfigBean sub = parseObject(questionnaire.getSubConfig(), SubConfigBean.class);
		SysQuestionnaireConfig cfg = questionnaire.getQuestionnaireConfig();
		return new QuestionnaireVo(questionnaire.getId(), questionnaire.getTitle(), questionnaire.getDetails(), questionnaire.getStatus(), questionnaire.getCheckStatus(),
			cfg.getCoverPicId(), cfg.getCoverPicUrl(), cfg.getIsVerification(), cfg.getProcessType(), cfg.getPrompt(), cfg.getWorkflowId(), null,
			cfg.getBeginTime(), cfg.getEndTime(), cfg.getAnswerLimit(), cfg.getUserAnswerFrequencyLimit(), cfg.getUserAnswerFrequencyLimitUnit(),
			cfg.getIpAnswerFrequencyLimit(), cfg.getIpAnswerFrequencyLimitUnit(), cfg.getDeviceAnswerFrequencyLimit(), cfg.getDeviceAnswerFrequencyLimitUnit(),
			cfg.getIsOnlyWechat(), cfg.getWechatAnswerFrequencyLimit(), cfg.getWechatAnswerFrequencyLimitUnit(), cfg.getShareLogoId(), cfg.getShareLogoUrl(),
			cfg.getDescription(), subjectDtos, bg, head, cont, font, sub);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Boolean getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(Boolean checkStatus) {
		this.checkStatus = checkStatus;
	}

	public Integer getCoverPic() {
		return coverPic;
	}

	public void setCoverPic(Integer coverPic) {
		this.coverPic = coverPic;
	}

	public String getCoverPicUrl() {
		return coverPicUrl;
	}

	public void setCoverPicUrl(String coverPicUrl) {
		this.coverPicUrl = coverPicUrl;
	}

	public Boolean getIsVerification() {
		return isVerification;
	}

	public void setIsVerification(Boolean verification) {
		isVerification = verification;
	}

	public Short getProcessType() {
		return processType;
	}

	public void setProcessType(Short processType) {
		this.processType = processType;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public Integer getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Integer workflowId) {
		this.workflowId = workflowId;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Boolean getAnswerLimit() {
		return answerLimit;
	}

	public void setAnswerLimit(Boolean answerLimit) {
		this.answerLimit = answerLimit;
	}

	public Integer getUserAnswerFrequencyLimit() {
		return userAnswerFrequencyLimit;
	}

	public void setUserAnswerFrequencyLimit(Integer userAnswerFrequencyLimit) {
		this.userAnswerFrequencyLimit = userAnswerFrequencyLimit;
	}

	public Short getUserAnswerFrequencyLimitUnit() {
		return userAnswerFrequencyLimitUnit;
	}

	public void setUserAnswerFrequencyLimitUnit(Short userAnswerFrequencyLimitUnit) {
		this.userAnswerFrequencyLimitUnit = userAnswerFrequencyLimitUnit;
	}

	public Integer getIpAnswerFrequencyLimit() {
		return ipAnswerFrequencyLimit;
	}

	public void setIpAnswerFrequencyLimit(Integer ipAnswerFrequencyLimit) {
		this.ipAnswerFrequencyLimit = ipAnswerFrequencyLimit;
	}

	public Short getIpAnswerFrequencyLimitUnit() {
		return ipAnswerFrequencyLimitUnit;
	}

	public void setIpAnswerFrequencyLimitUnit(Short ipAnswerFrequencyLimitUnit) {
		this.ipAnswerFrequencyLimitUnit = ipAnswerFrequencyLimitUnit;
	}

	public Integer getDeviceAnswerFrequencyLimit() {
		return deviceAnswerFrequencyLimit;
	}

	public void setDeviceAnswerFrequencyLimit(Integer deviceAnswerFrequencyLimit) {
		this.deviceAnswerFrequencyLimit = deviceAnswerFrequencyLimit;
	}

	public Short getDeviceAnswerFrequencyLimitUnit() {
		return deviceAnswerFrequencyLimitUnit;
	}

	public void setDeviceAnswerFrequencyLimitUnit(Short deviceAnswerFrequencyLimitUnit) {
		this.deviceAnswerFrequencyLimitUnit = deviceAnswerFrequencyLimitUnit;
	}

	public Boolean getIsOnlyWechat() {
		return isOnlyWechat;
	}

	public void setIsOnlyWechat(Boolean onlyWechat) {
		isOnlyWechat = onlyWechat;
	}

	public Integer getWechatAnswerFrequencyLimit() {
		return wechatAnswerFrequencyLimit;
	}

	public void setWechatAnswerFrequencyLimit(Integer wechatAnswerFrequencyLimit) {
		this.wechatAnswerFrequencyLimit = wechatAnswerFrequencyLimit;
	}

	public Short getWechatAnswerFrequencyLimitUnit() {
		return wechatAnswerFrequencyLimitUnit;
	}

	public void setWechatAnswerFrequencyLimitUnit(Short wechatAnswerFrequencyLimitUnit) {
		this.wechatAnswerFrequencyLimitUnit = wechatAnswerFrequencyLimitUnit;
	}

	public Integer getShareLogo() {
		return shareLogo;
	}

	public void setShareLogo(Integer shareLogo) {
		this.shareLogo = shareLogo;
	}

	public String getShareLogoUrl() {
		return shareLogoUrl;
	}

	public void setShareLogoUrl(String shareLogoUrl) {
		this.shareLogoUrl = shareLogoUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<QuestionnaireSubjectDto> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<QuestionnaireSubjectDto> subjects) {
		this.subjects = subjects;
	}

	public BgConfigBean getBgConfig() {
		return bgConfig;
	}

	public void setBgConfig(BgConfigBean bgConfig) {
		this.bgConfig = bgConfig;
	}

	public HeadConfigBean getHeadConfig() {
		return headConfig;
	}

	public void setHeadConfig(HeadConfigBean headConfig) {
		this.headConfig = headConfig;
	}

	public ContConfigBean getContConfig() {
		return contConfig;
	}

	public void setContConfig(ContConfigBean contConfig) {
		this.contConfig = contConfig;
	}

	public FontConfigBean getFontConfig() {
		return fontConfig;
	}

	public void setFontConfig(FontConfigBean fontConfig) {
		this.fontConfig = fontConfig;
	}

	public String getQrCodeUrl() {
		return qrCodeUrl;
	}

	public void setQrCodeUrl(String qrCodeUrl) {
		this.qrCodeUrl = qrCodeUrl;
	}

	public SubConfigBean getSubConfig() {
		return subConfig;
	}

	public void setSubConfig(SubConfigBean subConfig) {
		this.subConfig = subConfig;
	}

	public String getPreviewUrl() {
		return previewUrl;
	}

	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
	}

	public List<ContentFlowActionVo> getActions() {
		return actions;
	}

	public void setActions(List<ContentFlowActionVo> actions) {
		this.actions = actions;
	}

	public Boolean getRevokeSupport() {
		return revokeSupport;
	}

	public void setRevokeSupport(Boolean revokeSupport) {
		this.revokeSupport = revokeSupport;
	}
}
