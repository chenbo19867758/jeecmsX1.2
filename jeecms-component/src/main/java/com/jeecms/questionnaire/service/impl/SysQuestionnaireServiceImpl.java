/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.constants.WorkflowConstant;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionEnum;
import com.jeecms.common.exception.SystemExceptionInfo;
import com.jeecms.component.listener.WorkflowListener;
import com.jeecms.content.service.FlowService;
import com.jeecms.questionnaire.constants.QuestionnaireConstant;
import com.jeecms.questionnaire.dao.SysQuestionnaireDao;
import com.jeecms.questionnaire.domain.SysQuestionnaire;
import com.jeecms.questionnaire.domain.SysQuestionnaireAnswer;
import com.jeecms.questionnaire.domain.SysQuestionnaireConfig;
import com.jeecms.questionnaire.domain.SysQuestionnaireSubject;
import com.jeecms.questionnaire.domain.SysQuestionnaireSubjectOption;
import com.jeecms.questionnaire.domain.dto.QuestionnaireOptionDto;
import com.jeecms.questionnaire.domain.dto.QuestionnaireSubjectDto;
import com.jeecms.questionnaire.domain.dto.SysQuestionnaireSaveDto;
import com.jeecms.questionnaire.domain.dto.SysQuestionnaireUpdateDto;
import com.jeecms.questionnaire.domain.vo.BgConfigBean;
import com.jeecms.questionnaire.domain.vo.ContConfigBean;
import com.jeecms.questionnaire.domain.vo.FontConfigBean;
import com.jeecms.questionnaire.domain.vo.HeadConfigBean;
import com.jeecms.questionnaire.domain.vo.QuestionnaireFrontListVo;
import com.jeecms.questionnaire.domain.vo.QuestionnaireVo;
import com.jeecms.questionnaire.domain.vo.SubConfigBean;
import com.jeecms.questionnaire.service.SysQuestionnaireAnswerService;
import com.jeecms.questionnaire.service.SysQuestionnaireService;
import com.jeecms.questionnaire.service.SysQuestionnaireSubjectOptionService;
import com.jeecms.questionnaire.service.SysQuestionnaireSubjectService;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.resource.service.ResourcesSpaceDataService;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.SysJob;
import com.jeecms.system.job.factory.JobFactory;
import com.jeecms.system.service.CmsSiteService;
import com.jeecms.system.service.SysJobService;
import com.jeecms.util.SystemContextUtils;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.alibaba.fastjson.JSON.parseArray;
import static com.alibaba.fastjson.JSON.parseObject;
import static com.alibaba.fastjson.JSONObject.toJSONString;

/**
 * 投票调查Service实现类
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-10-18
 */
@Service
@Transactional(rollbackFor = Exception.class)
@ConditionalOnProperty(name = "workflow.support", havingValue = "local", matchIfMissing = true)
public class SysQuestionnaireServiceImpl extends BaseServiceImpl<SysQuestionnaire, SysQuestionnaireDao, Integer>
	implements SysQuestionnaireService, WorkflowListener {

	private static Logger logger = LoggerFactory.getLogger(SysQuestionnaireServiceImpl.class);

	@Autowired
	private SysQuestionnaireSubjectService subjectService;
	@Autowired
	private SysQuestionnaireSubjectOptionService optionService;
	@Autowired
	private ResourcesSpaceDataService dataService;
	@Autowired
	private SysQuestionnaireAnswerService answerService;
	@Autowired
	private FlowService flowService;
	@Autowired
	private SysJobService jobService;
	@Autowired
	private CmsSiteService siteService;

	@Override
	public Page<SysQuestionnaire> page(String title, Integer status, Date beginTime, Date endTime, Integer siteId, Pageable pageable) {
		return dao.page(title, status, beginTime, endTime, siteId, pageable);
	}

	@Override
	public Page<QuestionnaireFrontListVo> page(Integer siteId, Integer orderBy, Pageable pageable) {
		return dao.page(siteId, orderBy, pageable);
	}

	@Override
	public List<QuestionnaireFrontListVo> list(Integer siteId, Integer orderBy, int count) {
		return dao.list(siteId, orderBy, count);
	}

	@Override
	public SysQuestionnaire findByIdAndSiteId(Integer id, Integer siteId) {
		return dao.findByIdAndSiteId(id, siteId);
	}

	@Override
	public QuestionnaireVo findById(Integer id, Integer siteId, Boolean type) throws GlobalException {
		//SysQuestionnaire questionnaire = findByIdAndSiteId(id, siteId);
		SysQuestionnaire questionnaire = findById(id);
		if (questionnaire == null) {
			throw new GlobalException(new SystemExceptionInfo(SystemExceptionEnum.DOMAIN_NOT_FOUND_ERROR.getDefaultMessage(),
				SystemExceptionEnum.DOMAIN_NOT_FOUND_ERROR.getCode()));
		}
		if (type == null || !type) {
			if (QuestionnaireConstant.STATUS_PROCESSING.equals(questionnaire.getStatus()) || QuestionnaireConstant.STATUS_OVER.equals(questionnaire.getStatus())) {
				questionnaire.setStatus(QuestionnaireConstant.STATUS_NO_REVIEW);
				questionnaire = update(questionnaire);
			}
		}

		List<SysQuestionnaireSubject> subjects = subjectService.findByQuestionnaireId(questionnaire.getId());
		QuestionnaireVo vo = QuestionnaireVo.initView(questionnaire, subjects == null ? new ArrayList<SysQuestionnaireSubject>(0) : subjects);
		vo.setPreviewUrl(questionnaire.getPreviewUrl());
		vo.setQrCodeUrl(questionnaire.getQrCodeUrl());
		doFillActions(questionnaire, vo, SystemContextUtils.getCoreUser());
		return getWorkName(vo);
	}

	protected QuestionnaireVo getWorkName(QuestionnaireVo vo) {
		return vo;
	}

	@Override
	public SysQuestionnaire copy(Integer id, String title, CmsSite site) throws GlobalException {
		SysQuestionnaire q = findById(id);
		SysQuestionnaireConfig cfg = q.getQuestionnaireConfig();
		List<SysQuestionnaireSubject> subjects = q.getSubjects();
		List<QuestionnaireSubjectDto> subjectDtos = new ArrayList<QuestionnaireSubjectDto>();
		for (SysQuestionnaireSubject subject : subjects) {
			//选项
			List<QuestionnaireOptionDto.OptionsBean> optionsBeans = new ArrayList<QuestionnaireOptionDto.OptionsBean>();
			if (QuestionnaireConstant.SUBJECT_TYPE_CASCADE.equals(subject.getType())) {
				optionsBeans = parseArray(subject.getCascadeOption(), QuestionnaireOptionDto.OptionsBean.class);
			} else {
				List<SysQuestionnaireSubjectOption> options = subject.getOptions();
				for (SysQuestionnaireSubjectOption option : options) {
					QuestionnaireOptionDto.OptionsBean optionsBean = new QuestionnaireOptionDto.OptionsBean(null, option.getIsDefault(),
						option.getIsRequired(), option.getName(), option.getPicId(), option.getSortNum());
					optionsBeans.add(optionsBean);
				}
			}
			//选项配置Dto
			QuestionnaireOptionDto optionDto = new QuestionnaireOptionDto(subject.getColumn(), subject.getFileCountLimit(), subject.getFileCount(), subject.getFileSizeLimit(),
				subject.getFileSize(), subject.getFileType(), subject.getAllowFile(), subject.getFileTypeLimit(), subject.getFileSizeLimitUnit(), subject.getInputHigh(),
				subject.getIsLimit(), subject.getWordLimit(), subject.getInputNumLimit(), subject.getLimitCondition(), subject.getInputWidth(), subject.getRadio(), optionsBeans);
			//题目Dto
			QuestionnaireSubjectDto subjectDto = new QuestionnaireSubjectDto(null, subject.getDragable(), subject.getEditor(), subject.getGroupIndex(),
				subject.getIcon(), subject.getIndex(), subject.getIsAnswer(), subject.getCustom(), optionDto, subject.getPlaceholder(),
				subject.getPreview(), subject.getPrompt(), subject.getTitle(), subject.getType(), parseObject(subject.getValue(), QuestionnaireSubjectDto.ValueBean.class));
			subjectDtos.add(subjectDto);
		}
		SysQuestionnaireSaveDto dto = new SysQuestionnaireSaveDto(title, q.getDetails(), QuestionnaireConstant.STATUS_NO_REVIEW,
			cfg.getCoverPicId(), cfg.getIsVerification(), cfg.getProcessType(), cfg.getPrompt(), cfg.getWorkflowId(), cfg.getBeginTime(),
			cfg.getEndTime(), cfg.getAnswerLimit(), cfg.getUserAnswerFrequencyLimit(), cfg.getUserAnswerFrequencyLimitUnit(),
			cfg.getIpAnswerFrequencyLimit(), cfg.getIpAnswerFrequencyLimitUnit(), cfg.getDeviceAnswerFrequencyLimit(),
			cfg.getDeviceAnswerFrequencyLimitUnit(), cfg.getIsOnlyWechat(), cfg.getWechatAnswerFrequencyLimit(),
			cfg.getWechatAnswerFrequencyLimitUnit(), cfg.getShareLogoId(), cfg.getDescription(), subjectDtos,
			JSONObject.parseObject(q.getBgConfig(), BgConfigBean.class),
			JSONObject.parseObject(q.getHeadConfig(), HeadConfigBean.class),
			JSONObject.parseObject(q.getContConfig(), ContConfigBean.class),
			JSONObject.parseObject(q.getFontConfig(), FontConfigBean.class),
			JSONObject.parseObject(q.getSubConfig(), SubConfigBean.class)
		);
		SysQuestionnaire questionnaire = save(dto, site);
		questionnaire.setCheckStatus(false);
		questionnaire.setStatus(QuestionnaireConstant.STATUS_NO_REVIEW);
		return questionnaire;
	}

	@Override
	public SysQuestionnaire save(SysQuestionnaireSaveDto dto, CmsSite site) throws GlobalException {
		SysQuestionnaire questionnaire = new SysQuestionnaire(dto.getTitle(), dto.getDetails(), site.getId(), site);
		Integer bgImg = dto.getBgConfig().getBgImage();
		if (bgImg != null) {
			questionnaire.setBgImgId(bgImg);
			ResourcesSpaceData data = dataService.findById(bgImg);
			questionnaire.setBgImg(data);
		}
		Integer headImg = dto.getHeadConfig().getBgImage();
		if (headImg != null) {
			questionnaire.setHeadImgId(headImg);
			ResourcesSpaceData data = dataService.findById(headImg);
			questionnaire.setHeadImg(data);
		}
		questionnaire.setBgConfig(toJSONString(dto.getBgConfig()));
		questionnaire.setHeadConfig(toJSONString(dto.getHeadConfig()));
		questionnaire.setFontConfig(toJSONString(dto.getFontConfig()));
		questionnaire.setContConfig(toJSONString(dto.getContConfig()));
		questionnaire.setSubConfig(toJSONString(dto.getSubConfig()));

		//问卷设置
		SysQuestionnaireConfig config = new SysQuestionnaireConfig(dto.getVerification(), dto.getProcessType(), dto.getPrompt(),
			dto.getWorkflowId(), dto.getAnswerLimit(), dto.getUserAnswerFrequencyLimit(), dto.getUserAnswerFrequencyLimitUnit(), dto.getIpAnswerFrequencyLimit(),
			dto.getIpAnswerFrequencyLimitUnit(), dto.getDeviceAnswerFrequencyLimit(), dto.getDeviceAnswerFrequencyLimitUnit(),
			dto.getOnlyWechat(), dto.getWechatAnswerFrequencyLimit(), dto.getWechatAnswerFrequencyLimitUnit(), dto.getDescription());
		config.setBeginTime(dto.getBeginTime());
		config.setEndTime(dto.getEndTime());
		Integer workflowId = site.getConfig().getSurveyConfigurationId();
		if (workflowId != null) {
			config.setWorkflowId(workflowId);
			questionnaire.setCheckStatus(false);
		}
		if (QuestionnaireConstant.STATUS_PROCESSING.equals(dto.getStatus())) {
			if (config.getWorkflowId() != null) {
				questionnaire.setCheckStatus(false);
				questionnaire.setStatus(QuestionnaireConstant.STATUS_IN_CIRCULATION);
			} else {
				questionnaire.setStatus(dto.getStatus());
				config.setBeginTime(Calendar.getInstance().getTime());
				startUpEndJob(config.getEndTime(), questionnaire.getId());
			}
		} else {
			questionnaire.setStatus(dto.getStatus());
		}
		questionnaire.setQuestionnaireConfig(config);
		Integer shareLogoId = dto.getShareLogo();
		Integer coverPicId = dto.getCoverPic();
		if (coverPicId != null) {
			config.setCoverPicId(coverPicId);
			ResourcesSpaceData data = dataService.findById(coverPicId);
			config.setCoverPic(data);

		}
		if (shareLogoId != null) {
			config.setShareLogoId(shareLogoId);
			ResourcesSpaceData data = dataService.findById(shareLogoId);
			config.setShareLogo(data);

		}
		config.setQuestionnaire(questionnaire);
		//保存
		questionnaire = super.save(questionnaire);
		super.flush();
		//题目
		List<QuestionnaireSubjectDto> subjects = dto.getSubjects();
		subjectOperating(subjects, questionnaire);
		//工作流不为空且是提交审核操作
		if (config.getWorkflowId() != null && QuestionnaireConstant.STATUS_IN_CIRCULATION.equals(questionnaire.getStatus())) {
			doSubmitFlow(questionnaire);
		}
		if (config.getWorkflowId() == null && QuestionnaireConstant.STATUS_NO_REVIEW.equals(questionnaire.getStatus()) && questionnaire.getBeginTime() != null) {
			//开始时间小于等于当前时间则直接发布
			if (questionnaire.getBeginTime().getTime() <= Calendar.getInstance().getTimeInMillis()) {
				questionnaire.setStatus(QuestionnaireConstant.STATUS_PROCESSING);
			} else {
				SysJob job = JobFactory.createQuestionnairePublishJob(config.getBeginTime(), questionnaire.getId());
				try {
					jobService.addJob(job);
				} catch (Exception e) {
					logger.error("启动问卷定时任务失败，问卷id={}", questionnaire.getId());
				}
			}
		}
		return questionnaire;
	}

	@Override
	public SysQuestionnaire update(SysQuestionnaireUpdateDto dto, CmsSite site) throws GlobalException {
		SysQuestionnaire questionnaire = findByIdAndSiteId(dto.getId(), site.getId());
		questionnaire = init(questionnaire, dto, site);
		//保存
		questionnaire = super.updateAll(questionnaire);
		subjectOperatingUpdate(dto.getSubjects(), questionnaire);
		//工作流不为空且是提交审核操作
		if (questionnaire.getQuestionnaireConfig().getWorkflowId() != null && QuestionnaireConstant.STATUS_IN_CIRCULATION.equals(questionnaire.getStatus())) {
			doSubmitFlow(questionnaire);
		}
		//运行中任务需要开启自动结束任务
		if (questionnaire.getEndTime() != null) {
			SysJob job = JobFactory.createQuestionnaireFinishJob(questionnaire.getEndTime(), questionnaire.getId());
			try {
				if (!jobService.checkJobExist(job)) {
					jobService.addJob(job);
				} else {
					jobService.jobDelete(job);
					jobService.addJob(job);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return questionnaire;
	}

	/**
	 * 问卷处理
	 *
	 * @param questionnaire 投票调查实体
	 * @param dto           投票调查修改Dto
	 * @return SysQuestionnaire
	 */
	SysQuestionnaire init(SysQuestionnaire questionnaire, SysQuestionnaireUpdateDto dto, CmsSite site) {
		SysQuestionnaireConfig cfg = questionnaire.getQuestionnaireConfig();
		questionnaire.setTitle(dto.getTitle());
		questionnaire.setDetails(dto.getDetails());
		Integer bgImg = dto.getBgConfig().getBgImage();
		if (bgImg != null) {
			questionnaire.setBgImgId(bgImg);
			ResourcesSpaceData data = dataService.findById(bgImg);
			questionnaire.setBgImg(data);
		} else {
			questionnaire.setBgImgId(null);
			questionnaire.setBgImg(null);
		}
		Integer headImg = dto.getHeadConfig().getBgImage();
		if (headImg != null) {
			questionnaire.setHeadImgId(headImg);
			ResourcesSpaceData data = dataService.findById(headImg);
			questionnaire.setHeadImg(data);
		} else {
			questionnaire.setHeadImgId(null);
			questionnaire.setHeadImg(null);
		}

		questionnaire.setBgConfig(toJSONString(dto.getBgConfig()));
		questionnaire.setHeadConfig(toJSONString(dto.getHeadConfig()));
		questionnaire.setFontConfig(toJSONString(dto.getFontConfig()));
		questionnaire.setContConfig(toJSONString(dto.getContConfig()));
		questionnaire.setSubConfig(toJSONString(dto.getSubConfig()));

		//问卷设置
		cfg.setBeginTime(dto.getBeginTime());
		Integer coverPicId = dto.getCoverPic();
		if (coverPicId != null) {
			cfg.setCoverPicId(coverPicId);
			ResourcesSpaceData data = dataService.findById(coverPicId);
			cfg.setCoverPic(data);
		} else {
			cfg.setCoverPicId(null);
			cfg.setCoverPic(null);
		}
		Integer workflowId = site.getConfig().getSurveyConfigurationId();
		if (workflowId != null) {
			cfg.setWorkflowId(workflowId);
		}
		if (QuestionnaireConstant.STATUS_PROCESSING.equals(dto.getStatus())) {
			if (site.getConfig().getSurveyConfigurationId() != null) {
				if (questionnaire.getCheckStatus()) {
					questionnaire.setStatus(QuestionnaireConstant.STATUS_PROCESSING);
				} else {
					questionnaire.setCheckStatus(false);
					questionnaire.setStatus(QuestionnaireConstant.STATUS_IN_CIRCULATION);
				}
			} else {
				questionnaire.setStatus(dto.getStatus());
				questionnaire.getQuestionnaireConfig().setBeginTime(Calendar.getInstance().getTime());
				startUpEndJob(cfg.getEndTime(), questionnaire.getId());
			}
		} else {
			questionnaire.setStatus(dto.getStatus());
			questionnaire.setCheckStatus(false);
			if (site.getConfig().getSurveyConfigurationId() == null && QuestionnaireConstant.STATUS_NO_REVIEW.equals(questionnaire.getStatus()) && questionnaire.getBeginTime() != null) {
				SysJob job = JobFactory.createQuestionnairePublishJob(cfg.getBeginTime(), questionnaire.getId());
				try {
					jobService.addJob(job);
				} catch (Exception e) {
					logger.error("启动问卷定时任务失败，问卷id={}", questionnaire.getId());
				}
			}
		}
		questionnaire.setQuestionnaireConfig(cfg);
		return initConfig(questionnaire, dto);
	}

	/**
	 * 问卷设置处理
	 *
	 * @param questionnaire 投票调查实体
	 * @param dto           投票调查修改Dto
	 * @return SysQuestionnaire
	 */
	SysQuestionnaire initConfig(SysQuestionnaire questionnaire, SysQuestionnaireUpdateDto dto) {
		SysQuestionnaireConfig cfg = questionnaire.getQuestionnaireConfig();
		cfg.setIsVerification(dto.getVerification());
		cfg.setProcessType(dto.getProcessType());
		cfg.setPrompt(dto.getPrompt());
		cfg.setAnswerLimit(dto.getAnswerLimit());
		cfg.setUserAnswerFrequencyLimit(dto.getUserAnswerFrequencyLimit());
		cfg.setUserAnswerFrequencyLimitUnit(dto.getUserAnswerFrequencyLimitUnit());
		cfg.setIpAnswerFrequencyLimit(dto.getIpAnswerFrequencyLimit());
		cfg.setIpAnswerFrequencyLimitUnit(dto.getIpAnswerFrequencyLimitUnit());
		cfg.setDeviceAnswerFrequencyLimit(dto.getDeviceAnswerFrequencyLimit());
		cfg.setDeviceAnswerFrequencyLimitUnit(dto.getDeviceAnswerFrequencyLimitUnit());
		cfg.setIsOnlyWechat(dto.getOnlyWechat());
		cfg.setWechatAnswerFrequencyLimit(dto.getWechatAnswerFrequencyLimit());
		cfg.setWechatAnswerFrequencyLimitUnit(dto.getWechatAnswerFrequencyLimitUnit());
		Integer shareLogoId = dto.getShareLogo();
		if (shareLogoId != null) {
			cfg.setShareLogoId(shareLogoId);
			ResourcesSpaceData data = dataService.findById(shareLogoId);
			cfg.setShareLogo(data);
		} else {
			cfg.setShareLogoId(null);
			cfg.setShareLogo(null);
		}
		cfg.setDescription(dto.getDescription());
		cfg.setEndTime(dto.getEndTime());
		cfg.setQuestionnaire(questionnaire);
		questionnaire.setQuestionnaireConfig(cfg);
		return questionnaire;
	}

	/**
	 * 新建题目时的处理
	 *
	 * @param subjects      题目集合
	 * @param questionnaire 投票问卷
	 * @throws GlobalException 异常
	 */
	private void subjectOperating(List<QuestionnaireSubjectDto> subjects, SysQuestionnaire questionnaire) throws GlobalException {
		Integer questionnaireId = questionnaire.getId();
		List<SysQuestionnaireSubject> list = subjectService.findByQuestionnaireId(questionnaireId);
		List<SysQuestionnaireSubjectOption> optionList = new ArrayList<SysQuestionnaireSubjectOption>();
		for (SysQuestionnaireSubject sysQuestionnaireSubject : list) {
			optionList.addAll(sysQuestionnaireSubject.getOptions());
		}
		optionService.physicalDeleteInBatch(optionList);
		subjectService.physicalDeleteInBatch(list);
		if (subjects != null && subjects.size() > 0) {
			for (QuestionnaireSubjectDto subject : subjects) {
				saveSubject(subject, questionnaire);
			}
		}
	}

	private void saveSubject(QuestionnaireSubjectDto subject, SysQuestionnaire questionnaire) throws GlobalException {
		Short type = subject.getType();
		SysQuestionnaireSubject questionnaireSubject = new SysQuestionnaireSubject(questionnaire.getId(), subject.getTitle(), type, subject.getIsAnswer(),
			subject.getPrompt(), questionnaire, subject.getDragable(), subject.getEditor(), subject.getGroupIndex(), subject.getIcon(), subject.getIndex(),
			subject.getIsCustom(), subject.getPlaceholder(), subject.getPreview(), toJSONString(subject.getValue()));
		QuestionnaireOptionDto so = subject.getOption();
		if (so == null) {
			return;
		}
		//如果是单选，多选，下拉题则保存选项
		if (QuestionnaireConstant.SUBJECT_TYPE_RADIO.equals(type) || QuestionnaireConstant.SUBJECT_TYPE_CHECKBOX.equals(type) || QuestionnaireConstant.SUBJECT_TYPE_SELECT.equals(type)) {
				//单选 多选 下拉 需要处理选项
				List<QuestionnaireOptionDto.OptionsBean> options = so.getOptions();
				questionnaireSubject.setColumn(so.getColumn());
				questionnaireSubject.setRadio(so.getIsRadio());
				questionnaireSubject = subjectService.save(questionnaireSubject);
				subjectService.flush();
				if (options != null) {
					for (QuestionnaireOptionDto.OptionsBean bean : options) {
						SysQuestionnaireSubjectOption option = new SysQuestionnaireSubjectOption();
						option.setQuestionnaireSubject(questionnaireSubject);
						option.setQuestionnaireSubjectId(questionnaireSubject.getId());
						option.setPicId(bean.getPic());
						option.setSortNum(bean.getSortNum());
						option.setName(bean.getName());
						option.setIsRequired(bean.getIsRequired());
						option.setIsDefault(bean.getIsDefault());
						option.setIsOther(bean.getIsEemty());
						questionnaireSubject.getOptions().add(option);
					}
					subjectService.update(questionnaireSubject);
				}
			} else if (QuestionnaireConstant.SUBJECT_TYPE_CASCADE.equals(type)) {
				//级联题
				questionnaireSubject.setCascadeOption(toJSONString(so.getOptions()));
				subjectService.save(questionnaireSubject);
			} else if (QuestionnaireConstant.SUBJECT_TYPE_QUESTIONS_AND_ANSWERS.equals(type)) {
				//问答题
				questionnaireSubject.setInputHigh(so.getInputHeight());
				questionnaireSubject.setInputWidth(so.getInputWidth());
				questionnaireSubject.setIsLimit(so.getInputLimit());
				questionnaireSubject.setLimitCondition(so.getInputType());
				questionnaireSubject.setWordLimit(so.getInputNum());
				questionnaireSubject.setInputNumLimit(so.getInputNumLimit());
				subjectService.save(questionnaireSubject);
			} else if (QuestionnaireConstant.SUBJECT_TYPE_FILE.equals(type)) {
				//附件题
				questionnaireSubject.setFileSize(so.getFileSizeLimit());
				questionnaireSubject.setFileSizeLimit(so.getFileSize());
				questionnaireSubject.setFileSizeLimitUnit(so.getFileUnit());
				questionnaireSubject.setFileCount(so.getFileNumLimit());
				questionnaireSubject.setFileCountLimit(so.getFileNum());
				questionnaireSubject.setFileType(so.getFileTypeLimit());
				questionnaireSubject.setAllowFile(so.getFileTypeSet());
				questionnaireSubject.setFileTypeLimit(so.getFileTypes());
				subjectService.save(questionnaireSubject);
			}
	}

	/**
	 * 修改题目时的处理
	 *
	 * @param subjects      题目集合
	 * @param questionnaire 投票问卷
	 * @throws GlobalException 异常
	 */
	private void subjectOperatingUpdate(List<QuestionnaireSubjectDto> subjects, SysQuestionnaire questionnaire) throws GlobalException {
		if (subjects != null && subjects.size() > 0) {
			List<Integer> subjectIds = new ArrayList<Integer>();
			List<Integer> optionIds = new ArrayList<Integer>();
			//获取接收到的题目id和选项id
			for (QuestionnaireSubjectDto subject : subjects) {
				if (subject.getId() != null) {
					subjectIds.add(subject.getId());
					for (QuestionnaireOptionDto.OptionsBean option : subject.getOption().getOptions()) {
						if (option.getId() != null) {
							optionIds.add(option.getId());
						}
					}
				}
			}
			Integer questionnaireId = questionnaire.getId();
			//接收到的修改题目
			List<SysQuestionnaireSubject> entitys = subjectService.findAllById(subjectIds);
			//查找到问卷下所有题目
			List<SysQuestionnaireSubject> list = subjectService.findByQuestionnaireId(questionnaireId);
			List<SysQuestionnaireSubjectOption> optionList = new ArrayList<SysQuestionnaireSubjectOption>();
			for (SysQuestionnaireSubject subject : list) {
				optionList.addAll(subject.getOptions());
			}
			//筛选出没有接收到的题目id，进行删除操作
			list = list.parallelStream().filter(o -> !subjectIds.contains(o.getId())).collect(Collectors.toList());
			//筛选出没有接收到的选项id，进行删除操作
			optionList = optionList.parallelStream().filter(o -> !optionIds.contains(o.getId())).collect(Collectors.toList());
			subjectService.physicalDeleteInBatch(list);
			optionService.physicalDeleteInBatch(optionList);
			//遍历接收的题目，分别对已有的题目和新增的题目进行处理
			for (QuestionnaireSubjectDto subject : subjects) {
				Short type = subject.getType();
				//题目id不为空修改题目
				if (subject.getId() != null) {
					for (SysQuestionnaireSubject entity : entitys) {
						if (entity.getId().equals(subject.getId())) {
							entity.setTitle(subject.getTitle());
							entity.setType(type);
							entity.setIsAnswer(subject.getIsAnswer());
							entity.setPrompt(subject.getPrompt());
							entity.setDragable(subject.getDragable());
							entity.setEditor(subject.getEditor());
							entity.setGroupIndex(subject.getGroupIndex());
							entity.setIcon(subject.getIcon());
							entity.setIndex(subject.getIndex());
							entity.setCustom(subject.getIsCustom());
							entity.setPlaceholder(subject.getPlaceholder());
							entity.setPreview(subject.getPreview());
							//接送到的题目选项
							QuestionnaireOptionDto so = subject.getOption();
							if (so == null) {
								break;
							}
							//如果是单选，多选，下拉题则保存选项
							if (QuestionnaireConstant.SUBJECT_TYPE_RADIO.equals(type) || QuestionnaireConstant.SUBJECT_TYPE_CHECKBOX.equals(type) || QuestionnaireConstant.SUBJECT_TYPE_SELECT.equals(type)) {
								entity.setColumn(so.getColumn());
								entity.setRadio(so.getIsRadio());
								//原来的选项
								List<SysQuestionnaireSubjectOption> entityOptions = entity.getOptions();
								//单选 多选 下拉 需要处理选项
								List<QuestionnaireOptionDto.OptionsBean> options = so.getOptions();
								if (options != null) {
									//如果选项不为空，则遍历选项判断进行新增还是修改
									for (QuestionnaireOptionDto.OptionsBean bean : options) {
										if (bean.getId() != null) {
											for (SysQuestionnaireSubjectOption entityOption : entityOptions) {
												if (bean.getId().equals(entityOption.getId())) {
													entityOption.setName(bean.getName());
													entityOption.setPicId(bean.getPic());
													entityOption.setIsDefault(bean.getIsDefault());
													entityOption.setIsRequired(bean.getIsRequired());
													entityOption.setIsOther(bean.getIsEemty());
													entityOption.setSortNum(bean.getSortNum());
													entity.getOptions().add(entityOption);
													break;
												}
											}
										} else {
											SysQuestionnaireSubjectOption option = new SysQuestionnaireSubjectOption(entity.getId(), bean.getName(), bean.getPic(), bean.getIsDefault(),
												bean.getIsRequired(), bean.getIsEemty(), bean.getSortNum(), entity);
											entity.getOptions().add(option);
											//newOptions.add(option);
										}
									}
								}
							} else if (QuestionnaireConstant.SUBJECT_TYPE_CASCADE.equals(type)) {
								//级联题
								entity.setCascadeOption(toJSONString(so.getOptions()));
							} else {
								entity.setInputHigh(so.getInputHeight());
								entity.setInputWidth(so.getInputWidth());
								entity.setIsLimit(so.getInputLimit());
								entity.setLimitCondition(so.getInputType());
								entity.setInputNumLimit(so.getInputNumLimit());
								entity.setWordLimit(so.getInputNum());

								entity.setFileSizeLimit(so.getFileSize());
								entity.setFileSize(so.getFileSizeLimit());
								entity.setFileSizeLimitUnit(so.getFileUnit());
								entity.setFileCount(so.getFileNumLimit());
								entity.setFileCountLimit(so.getFileNum());
								entity.setFileType(so.getFileTypeLimit());
								entity.setAllowFile(so.getFileTypeSet());
								entity.setFileTypeLimit(so.getFileTypes());
							}
							subjectService.update(entity);
							break;
						}
					}
				} else {
					saveSubject(subject, questionnaire);
				}
			}
		} else {
			List<SysQuestionnaireSubject> subjectList = questionnaire.getSubjects();
			subjectService.physicalDeleteInBatch(subjectList);
			questionnaire.getSubjects().clear();
		}
	}

	@Override
	public List<SysQuestionnaire> updateStatus(Integer[] ids, Integer status, Integer siteId) throws GlobalException {
		Integer workflowId = null;
		if (siteId != null) {
			CmsSite site = siteService.findById(siteId);
			if (site != null) {
				workflowId = site.getConfig().getSurveyConfigurationId();
			}
		}
		List<SysQuestionnaire> questionnaires = findAllById(Arrays.asList(ids));
		for (SysQuestionnaire questionnaire : questionnaires) {
			int questionnaireStatus = questionnaire.getStatus();
			SysQuestionnaireConfig cfg = questionnaire.getQuestionnaireConfig();
			//状态相同的不做处理
			if (questionnaireStatus != status) {
				//如果是提交审核 需判断是否有工作流，有工作流则进入流转中，没有工作流则直接发布(进行中)
				if (QuestionnaireConstant.STATUS_IN_CIRCULATION.equals(status)) {
					if (siteId == null) {
						workflowId = cfg.getWorkflowId();
					}
					if (workflowId != null) {
						questionnaire.setStatus(status);
						questionnaire.setCheckStatus(false);
						doSubmitFlow(questionnaire);
					} else {
						questionnaire.setStatus(QuestionnaireConstant.STATUS_PROCESSING);
						cfg.setBeginTime(Calendar.getInstance().getTime());
						//结束时间存在并小于当前时间则制空
						Date endTime = cfg.getEndTime();
						if (endTime != null && endTime.getTime() < Calendar.getInstance().getTimeInMillis()) {
							cfg.setEndTime(null);
						}
					}
				} else if (QuestionnaireConstant.STATUS_PROCESSING.equals(status)) {
					//发布 需要判断是否可以发布
					if (siteId == null) {
						workflowId = cfg.getWorkflowId();
					}
					if (workflowId == null) {
						questionnaire.setStatus(status);
						cfg.setBeginTime(Calendar.getInstance().getTime());
						//结束时间存在并小于当前时间则制空
						Date endTime = cfg.getEndTime();
						if (endTime != null && endTime.getTime() < Calendar.getInstance().getTimeInMillis()) {
							cfg.setEndTime(null);
						}
					} else if (questionnaire.getCheckStatus() != null && questionnaire.getCheckStatus()) {
						questionnaire.setStatus(status);
						cfg.setBeginTime(Calendar.getInstance().getTime());
						//结束时间存在并小于当前时间则制空
						Date endTime = cfg.getEndTime();
						if (endTime != null && endTime.getTime() < Calendar.getInstance().getTimeInMillis()) {
							cfg.setEndTime(null);
						}
					}
					questionnaire.setQuestionnaireConfig(cfg);
				} else {
					if (!QuestionnaireConstant.STATUS_OVER.equals(status)) {
						questionnaire.setCheckStatus(false);
					}
					questionnaire.setStatus(status);
				}
			}
		}
		batchUpdate(questionnaires);
		return questionnaires;
	}

	@Override
	public void publish(Integer id) throws GlobalException {
		SysQuestionnaire questionnaireStatus = findById(id);
		questionnaireStatus.setStatus(QuestionnaireConstant.STATUS_PROCESSING);
		questionnaireStatus.setCheckStatus(true);
		questionnaireStatus.getQuestionnaireConfig().setBeginTime(Calendar.getInstance().getTime());
		super.update(questionnaireStatus);
	}

	@Override
	public List<SysQuestionnaire> review(Integer[] ids, boolean reviewStatus) throws GlobalException {
		List<SysQuestionnaire> questionnaires = findAllById(Arrays.asList(ids));
		//只有流转中的可以审核
		questionnaires = questionnaires.parallelStream()
			.filter(o -> QuestionnaireConstant.STATUS_IN_CIRCULATION.equals(o.getStatus()))
			.collect(Collectors.toList());
		for (SysQuestionnaire questionnaire : questionnaires) {
			questionnaire.setCheckStatus(reviewStatus);
			questionnaire.setStatus(QuestionnaireConstant.STATUS_NO_REVIEW);
			if (reviewStatus && questionnaire.getBeginTime() != null) {
				SysJob job = JobFactory.createQuestionnairePublishJob(questionnaire.getBeginTime(), questionnaire.getId());
				try {
					if (!jobService.checkJobExist(job)) {
						jobService.addJob(job);
					} else {
						jobService.jobDelete(job);
						jobService.addJob(job);
					}
				} catch (Exception e) {
					logger.error("启动问卷定时任务失败，问卷id={}", questionnaire.getId());
				}
			}
		}
		batchUpdate(questionnaires);
		return questionnaires;
	}

	@Override
	public void cleanAnswer(Integer id, Integer siteId) throws GlobalException {
		SysQuestionnaire questionnaire = findByIdAndSiteId(id, siteId);
		if (questionnaire != null) {
			List<SysQuestionnaireAnswer> list = questionnaire.getAnswers();
			answerService.physicalDeleteInBatch(list);
		}
	}

	@Override
	public void startUpEndJob(Date date, Integer questionnaireId) {
		//运行中任务需要开启自动结束任务
		if (date != null) {
			SysJob job = JobFactory.createQuestionnaireFinishJob(date, questionnaireId);
			try {
				if (!jobService.checkJobExist(job)) {
					jobService.addJob(job);
				} else {
					jobService.jobDelete(job);
					jobService.addJob(job);
				}
			} catch (Exception e) {
				logger.error("启动问卷自动结束定时任务失败，问卷id={}", questionnaireId);
			}
		}
	}

	@Override
	public void updateWorkFlow(Integer workflowId, Integer siteId) throws GlobalException {

	}

	@Override
	public List<SysQuestionnaire> deleteBatch(Integer[] ids) throws GlobalException {
		List<SysQuestionnaire> list = delete(ids);
		for (SysQuestionnaire questionnaire : list) {
			SysJob job = JobFactory.createQuestionnairePublishJob(questionnaire.getBeginTime(), questionnaire.getId());
			SysJob endJob = JobFactory.createQuestionnaireFinishJob(questionnaire.getEndTime(), questionnaire.getId());
			try {
				if (jobService.checkJobExist(job)) {
					jobService.jobDelete(job);
					jobService.jobDelete(endJob);
				}
			} catch (SchedulerException e) {
				logger.error("删除已删除的问卷的定时任务");
			}
		}
		return list;
	}

	@Override
	public void publishJob(Integer id) {
		SysQuestionnaire questionnaire = findById(id);
		Integer status = questionnaire.getStatus();
		Integer workflowId = questionnaire.getWorkflowId();
		if (status.equals(QuestionnaireConstant.STATUS_NO_REVIEW) && (workflowId == null || questionnaire.getCheckStatus())) {
			questionnaire.setStatus(QuestionnaireConstant.STATUS_PROCESSING);
			try {
				update(questionnaire);
				SysJob publishJob = JobFactory.createQuestionnairePublishJob(questionnaire.getQuestionnaireConfig().getBeginTime(), id);
				jobService.jobDelete(publishJob);
				//运行中任务需要开启自动结束任务
				if (questionnaire.getEndTime() != null) {
					SysJob job = JobFactory.createQuestionnaireFinishJob(questionnaire.getEndTime(), questionnaire.getId());
					if (!jobService.checkJobExist(job)) {
						jobService.addJob(job);
					}
				}
			} catch (GlobalException e) {
				logger.error("自动发布问卷失败，时间{}", Calendar.getInstance().getTime());
			} catch (Exception e) {

			}
		}
	}

	@Override
	public void offlineJob(Integer id) {
		SysQuestionnaire questionnaire = findById(id);
		Integer status = questionnaire.getStatus();
		if (status.equals(QuestionnaireConstant.STATUS_PROCESSING)) {
			questionnaire.setStatus(QuestionnaireConstant.STATUS_OVER);
			try {
				update(questionnaire);
				SysJob finishJob = JobFactory.createQuestionnaireFinishJob(questionnaire.getQuestionnaireConfig().getEndTime(), id);
				jobService.jobDelete(finishJob);
			} catch (GlobalException e) {
				logger.error("自动结束问卷失败，时间{}", Calendar.getInstance().getTime());
			}
		}
	}

	/**
	 * 提交工作流模板方法
	 *
	 * @param questionnaire 投票对象
	 * @Title: doSubmitFlow
	 * @return: void
	 */
	protected void doSubmitFlow(SysQuestionnaire questionnaire) throws GlobalException {

	}

	/**
	 * 填充当前内容支持的动作
	 */
	protected void doFillActions(SysQuestionnaire questionnaire, QuestionnaireVo vo, CoreUser user) {

	}

	@Override
	public void beforeWorkflowDelete(Integer[] ids) throws GlobalException {
		flowService.doInterruptDataFlow(WorkflowConstant.WORKFLOW_DATA_TYPE_QUESTIONNAIRE,
			Arrays.asList(ids), SystemContextUtils.getCoreUser());
	}

}