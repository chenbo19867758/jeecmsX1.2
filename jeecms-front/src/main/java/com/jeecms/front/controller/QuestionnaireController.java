/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.front.controller;

import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.MathUtil;
import com.jeecms.questionnaire.constants.QuestionnaireConstant;
import com.jeecms.questionnaire.domain.SysQuestionnaire;
import com.jeecms.questionnaire.domain.SysQuestionnaireAnswer;
import com.jeecms.questionnaire.domain.SysQuestionnaireSubjectOption;
import com.jeecms.questionnaire.domain.vo.QuestionnaireFrontListVo;
import com.jeecms.questionnaire.domain.vo.QuestionnaireStatisticsVo;
import com.jeecms.questionnaire.domain.vo.QuestionnaireVo;
import com.jeecms.questionnaire.service.SysQuestionnaireAnswerService;
import com.jeecms.questionnaire.service.SysQuestionnaireService;
import com.jeecms.util.SystemContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 投票调查前台控制层
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/10/25 12:00
 */
@RestController
@RequestMapping("/questionnaire")
public class QuestionnaireController {

	@Autowired
	private SysQuestionnaireService service;
	@Autowired
	private SysQuestionnaireAnswerService answerService;

	/**
	 * 分页列表
	 *
	 * @param request  {@link HttpServletRequest}
	 * @param pageable {@link Pageable}
	 * @return ResponseInfo
	 */
	@RequestMapping("/page")
	@SerializeField(clazz = SysQuestionnaire.class, includes = {"id", "title", "details", "answerCount", "status", "deadline", "coverPicUrl"})
	public ResponseInfo page(HttpServletRequest request, Integer orderBy, Pageable pageable) {
		Page<QuestionnaireFrontListVo> page = service.page(SystemContextUtils.getSiteId(request), orderBy, pageable);
		return new ResponseInfo(page);
	}

	/**
	 * 详情
	 *
	 * @param id      投票调查id
	 * @param request HttpServletRequest
	 * @return ResponseInfo
	 */
	@GetMapping(value = "/preview/{id}")
	public ResponseInfo preview(@PathVariable("id") Integer id, HttpServletRequest request) throws GlobalException {
		QuestionnaireVo vo = service.findById(id, SystemContextUtils.getSiteId(request), true);
		return new ResponseInfo(vo);
	}

	/**
	 * 查看问卷结果
	 *
	 * @param questionnaireId 问卷id
	 * @param showFile        是否显示文件类型题目
	 * @param request         {@link HttpServletRequest}
	 * @return ResponseInfo
	 */
	@GetMapping("/result")
	public ResponseInfo result(@RequestParam("questionnaireId") Integer questionnaireId, Boolean showFile, HttpServletRequest request) {
		if (questionnaireId == null) {
			return new ResponseInfo();
		}
		List<SysQuestionnaireAnswer> list = answerService.findByQuestionnaireId(questionnaireId, showFile, SystemContextUtils.getSiteId(request));
		if (list.size() == 0) {
			return new ResponseInfo();
		}
		//根据题目id分组
		Map<Integer, List<SysQuestionnaireAnswer>> map = list.parallelStream().filter(o -> o.getSubjectId() != null)
			.collect(Collectors.groupingBy(SysQuestionnaireAnswer::getSubjectId));
		//问卷
		QuestionnaireStatisticsVo vo = new QuestionnaireStatisticsVo();
		vo.setNumber(list.get(0).getQuestionnaire().getAnswerCount());
		vo.setTitle(list.get(0).getQuestionnaire().getTitle());
		//题目数组
		List<QuestionnaireStatisticsVo.SubjectsBean> subjectsBeans = new ArrayList<>();
		for (Integer subjectId : map.keySet()) {
			//只处理单选，多选，下拉，级联题，问答题和附件题有分页另调接口
			List<SysQuestionnaireAnswer> beans = map.get(subjectId);
			//题目
			QuestionnaireStatisticsVo.SubjectsBean subjects = new QuestionnaireStatisticsVo.SubjectsBean();
			subjects.setTitle(beans.get(0).getSubject().getTitle());
			subjects.setIndex(beans.get(0).getSubject().getIndex());
			subjects.setId(beans.get(0).getSubjectId());
			subjects.setIsMust(beans.get(0).getSubject().getIsAnswer());
			Short type = beans.get(0).getSubject().getType();
			subjects.setType(type);
			//选项数组
			List<QuestionnaireStatisticsVo.SubjectsBean.OptionsBean> optionsBeans = new ArrayList<QuestionnaireStatisticsVo.SubjectsBean.OptionsBean>();
			//[{"name":"手机","rate":80}]

			//做了该题的人数
			beans = beans.parallelStream().filter(o -> StringUtils.isNotBlank(o.getAnswerId()) ||
				StringUtils.isNotBlank(o.getAnswer())).collect(Collectors.toList());
			int sum = beans.size();
			if (QuestionnaireConstant.SUBJECT_TYPE_RADIO.equals(type)) {
				//单选题的选项
				List<SysQuestionnaireSubjectOption> options = beans.get(0).getSubject().getOptions();
				if (options != null) {
					options = options.parallelStream().sorted(Comparator.comparing(SysQuestionnaireSubjectOption::getSortNum)).collect(Collectors.toList());
					//遍历该题的选项
					for (SysQuestionnaireSubjectOption option : options) {
						QuestionnaireStatisticsVo.SubjectsBean.OptionsBean optionsBean = new QuestionnaireStatisticsVo.SubjectsBean.OptionsBean();
						optionsBean.setName(option.getName());
						long count = beans.parallelStream().filter(o -> String.valueOf(option.getId()).equals(o.getAnswerId())).count();
						try {
							optionsBean.setRate(MathUtil.div(new BigDecimal(count * 100), new BigDecimal(sum), MathUtil.SCALE_LEN_COMMON));
						} catch (IllegalAccessException e) {
							optionsBean.setRate(BigDecimal.ZERO);
						}
						optionsBeans.add(optionsBean);
					}
				}
			} else if (QuestionnaireConstant.SUBJECT_TYPE_CHECKBOX.equals(type) || QuestionnaireConstant.SUBJECT_TYPE_SELECT.equals(type)) {
				//多选题,下拉题
				List<SysQuestionnaireSubjectOption> options = beans.get(0).getSubject().getOptions();
				if (options != null) {
					options = options.parallelStream().sorted(Comparator.comparing(SysQuestionnaireSubjectOption::getSortNum)).collect(Collectors.toList());
					int total = 0;
					for (SysQuestionnaireAnswer bean : beans) {
						if (StringUtils.isNotBlank(bean.getAnswerId())) {
							String s = bean.getAnswerId().replace(WebConstants.ARRAY_SPT, "");
							total += bean.getAnswerId().length() - s.length() + 1;
						}
					}
					//遍历该题的选项
					for (SysQuestionnaireSubjectOption option : options) {
						QuestionnaireStatisticsVo.SubjectsBean.OptionsBean optionsBean = new QuestionnaireStatisticsVo.SubjectsBean.OptionsBean();
						optionsBean.setName(option.getName());
						long count = beans.parallelStream().filter(o -> StringUtils.isNotBlank(o.getAnswerId()))
							.filter(o -> Arrays.asList(o.getAnswerId().split(WebConstants.ARRAY_SPT)).contains(String.valueOf(option.getId()))).count();
						try {
							optionsBean.setRate(MathUtil.div(new BigDecimal(count * 100), new BigDecimal(total), MathUtil.SCALE_LEN_COMMON));
						} catch (IllegalAccessException e) {
							optionsBean.setRate(BigDecimal.ZERO);
						}
						optionsBeans.add(optionsBean);
					}
				}
			} else if (QuestionnaireConstant.SUBJECT_TYPE_CASCADE.equals(type)) {
				//级联题
				Map<String, List<SysQuestionnaireAnswer>> answerMap = beans.parallelStream()
					.filter(o -> StringUtils.isNotBlank(o.getAnswer()))
					.collect(Collectors.groupingBy(SysQuestionnaireAnswer::getAnswer));
				for (String s : answerMap.keySet()) {
					QuestionnaireStatisticsVo.SubjectsBean.OptionsBean optionsBean = new QuestionnaireStatisticsVo.SubjectsBean.OptionsBean();
					optionsBean.setName(s);
					int size = answerMap.get(s).size();
					try {
						optionsBean.setRate(MathUtil.div(new BigDecimal(size * 100), new BigDecimal(sum), MathUtil.SCALE_LEN_COMMON));
					} catch (IllegalAccessException e) {
						optionsBean.setRate(BigDecimal.ZERO);
					}
					optionsBeans.add(optionsBean);
				}
			}
			subjects.setOptions(optionsBeans);
			subjectsBeans.add(subjects);
		}
		subjectsBeans = subjectsBeans.parallelStream().filter(o->o.getIndex() !=null)
			.sorted(Comparator.comparing(QuestionnaireStatisticsVo.SubjectsBean::getIndex)).collect(Collectors.toList());
		vo.setSubjects(subjectsBeans);
		return new ResponseInfo(vo);
	}


	/**
	 * 提交问卷
	 *
	 * @param questionnaireId 问卷id
	 * @param request         {@link HttpServletRequest}
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@RequestMapping()
	public ResponseInfo save(Integer questionnaireId, HttpServletRequest request, HttpServletResponse response) throws GlobalException {
		answerService.save(questionnaireId, request, response);
		return new ResponseInfo(true);
	}

	@SerializeField(clazz = SysQuestionnaireAnswer.class, includes = {"questionnaireId", "subjectId", "answer"})
	@GetMapping("/list")
	public ResponseInfo list(Integer questionnaireId, @RequestParam("subjectId") Integer subjectId, @PageableDefault(
		sort = "createTime", direction = Direction.DESC) Pageable pageable) {
		if (subjectId == null) {
			return new ResponseInfo();
		}
		Page<SysQuestionnaireAnswer> page = answerService.findBySubjectId(subjectId, questionnaireId, pageable);
		return new ResponseInfo(page);
	}

	/**
	 * 统计问卷访问量
	 *
	 * @param questionnaireId 问卷id
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@GetMapping("/statistics")
	public ResponseInfo statisticsQuestionnaire(@RequestParam("questionnaireId") Integer questionnaireId) throws GlobalException {
		SysQuestionnaire q = service.findById(questionnaireId);
		q.setPageViews(q.getPageViews() + 1);
		q = service.update(q);
		return new ResponseInfo(q.getPageViews());
	}

}
