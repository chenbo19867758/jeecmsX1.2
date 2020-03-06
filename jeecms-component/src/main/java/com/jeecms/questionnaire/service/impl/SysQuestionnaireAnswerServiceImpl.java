/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.internal.LinkedHashTreeMap;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.service.CaptchaService;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.exception.CaptchaExceptionInfo;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionInfo;
import com.jeecms.common.exception.error.SysOtherErrorCodeEnum;
import com.jeecms.common.image.ImageUtils;
import com.jeecms.common.ueditor.ResourceType;
import com.jeecms.common.util.MathUtil;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.common.util.office.OpenOfficeConverter;
import com.jeecms.common.web.Location;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.questionnaire.constants.QuestionnaireConstant;
import com.jeecms.questionnaire.dao.SysQuestionnaireAnswerDao;
import com.jeecms.questionnaire.domain.SysQuestionnaire;
import com.jeecms.questionnaire.domain.SysQuestionnaireAnswer;
import com.jeecms.questionnaire.domain.SysQuestionnaireConfig;
import com.jeecms.questionnaire.domain.SysQuestionnaireSubject;
import com.jeecms.questionnaire.domain.SysQuestionnaireSubjectOption;
import com.jeecms.questionnaire.domain.dto.QuestionnaireFileDto;
import com.jeecms.questionnaire.domain.vo.BgConfigBean;
import com.jeecms.questionnaire.domain.vo.ContConfigBean;
import com.jeecms.questionnaire.domain.vo.FontConfigBean;
import com.jeecms.questionnaire.domain.vo.HeadConfigBean;
import com.jeecms.questionnaire.domain.vo.QuestionnaireAnswerViewVo;
import com.jeecms.questionnaire.domain.vo.QuestionnaireExportListVo;
import com.jeecms.questionnaire.domain.vo.QuestionnairePieVo;
import com.jeecms.questionnaire.domain.vo.QuestionnaireSubjectGroupVo;
import com.jeecms.questionnaire.domain.vo.SubConfigBean;
import com.jeecms.questionnaire.service.SysQuestionnaireAnswerService;
import com.jeecms.questionnaire.service.SysQuestionnaireService;
import com.jeecms.questionnaire.service.SysQuestionnaireSubjectService;
import com.jeecms.resource.domain.dto.UploadResult;
import com.jeecms.resource.service.impl.UploadService;
import com.jeecms.system.domain.Area;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.SysAccessRecord;
import com.jeecms.system.service.AddressService;
import com.jeecms.system.service.AreaService;
import com.jeecms.util.SystemContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.alibaba.fastjson.JSON.toJSONString;
import static com.jeecms.questionnaire.domain.SysQuestionnaireAnswer.ORDER_BY_CREATE_TIME_ASC;
import static com.jeecms.questionnaire.domain.SysQuestionnaireAnswer.ORDER_BY_CREATE_TIME_DESC;
import static com.jeecms.questionnaire.domain.SysQuestionnaireSubject.ALLOW;
import static com.jeecms.questionnaire.domain.SysQuestionnaireSubject.PROHIBIT;
import static com.jeecms.questionnaire.domain.SysQuestionnaireSubject.UNIT_KB;
import static com.jeecms.questionnaire.domain.SysQuestionnaireSubject.UNIT_MB;
import static com.jeecms.questionnaire.domain.vo.QuestionnairePieVo.DAY;
import static com.jeecms.questionnaire.domain.vo.QuestionnairePieVo.HOUR;
import static com.jeecms.questionnaire.domain.vo.QuestionnairePieVo.MOUTH;
import static com.jeecms.questionnaire.domain.vo.QuestionnairePieVo.WEEK;

/**
 * 问卷结果Service实现层
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-10-18
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysQuestionnaireAnswerServiceImpl extends BaseServiceImpl<SysQuestionnaireAnswer, SysQuestionnaireAnswerDao, Integer> implements SysQuestionnaireAnswerService {

	@Override
	public Page<SysQuestionnaireAnswer> getPage(Boolean isEffective, String province, String city, Integer orderBy,
												String device, Integer deviceType, String ip, String replayName, Date beginTime,
												Date endTime, Integer siteId, Integer questionnaireId, Pageable pageable) {
		Map<String, String> map = handleArea(province, city);
		List<SysQuestionnaireAnswer> list = dao.getList(null, map.get("province"), map.get("city"), device,
			deviceType, ip, null, replayName, beginTime, endTime, null, siteId, questionnaireId);
		list = getReplayObject(list);
		if (isEffective != null) {
			list = list.parallelStream().filter(o -> o.getIsEffective().equals(isEffective)).collect(Collectors.toList());
		}
		if (orderBy == null || ORDER_BY_CREATE_TIME_DESC.equals(orderBy)) {
			list = list.parallelStream().sorted(Comparator.comparing(SysQuestionnaireAnswer::getCreateTime)
				.reversed()).collect(Collectors.toList());
		} else if (ORDER_BY_CREATE_TIME_ASC.equals(orderBy)) {
			list = list.parallelStream().sorted(Comparator.comparing(SysQuestionnaireAnswer::getCreateTime))
				.collect(Collectors.toList());
		}
		List<SysQuestionnaireAnswer> answers = list.parallelStream()
			.skip(pageable.getPageSize() * (pageable.getPageNumber()))
			.limit(pageable.getPageSize()).collect(Collectors.toList());
		for (SysQuestionnaireAnswer answer : answers) {
			if (answer.getQuestionnaire().getSubjects() != null && answer.getQuestionnaire().getSubjects().size() > 0) {
				for (SysQuestionnaireSubject subject : answer.getQuestionnaire().getSubjects()) {
					if (QuestionnaireConstant.SUBJECT_TYPE_FILE.equals(subject.getType())) {
						answer.setIsFile(true);
						break;
					}
				}
			}
		}
		return new PageImpl<SysQuestionnaireAnswer>(answers, pageable, list.size());
	}

	@Override
	public List<QuestionnaireSubjectGroupVo> groupBySubject(Integer questionnaireId, Integer siteId) {
		List<SysQuestionnaireAnswer> list = getList(true, null, null, null,
			null, null, null, null, null, null, siteId, questionnaireId);
		//根据题目id分组
		Map<Integer, List<SysQuestionnaireAnswer>> map = list.parallelStream().filter(o -> o.getSubjectId() != null)
			.collect(Collectors.groupingBy(SysQuestionnaireAnswer::getSubjectId));
		List<QuestionnaireSubjectGroupVo> vos = new ArrayList<QuestionnaireSubjectGroupVo>();
		for (List<SysQuestionnaireAnswer> answers : map.values()) {
			SysQuestionnaireSubject subject = answers.get(0).getSubject();
			if (subject != null) {
				Integer id = subject.getId();
				String title = subject.getTitle();
				Short type = subject.getType();
				answers = answers.parallelStream().filter(o -> StringUtils.isNotBlank(o.getAnswerId()) ||
					StringUtils.isNotBlank(o.getAnswer())).collect(Collectors.toList());
				Integer number = answers.size();
				Integer index = subject.getIndex();
				List<SysQuestionnaireSubjectOption> options = subject.getOptions();
				options = options.parallelStream().sorted(Comparator.comparing(SysQuestionnaireSubjectOption::getSortNum)).collect(Collectors.toList());
				List<QuestionnaireSubjectGroupVo.Option> optionList = new ArrayList<QuestionnaireSubjectGroupVo.Option>();
				Boolean isFile = false;
				if (QuestionnaireConstant.SUBJECT_TYPE_RADIO.equals(type)) {
					//单选题的选项
					//遍历该题的选项
					for (SysQuestionnaireSubjectOption option : options) {
						QuestionnaireSubjectGroupVo.Option object = new QuestionnaireSubjectGroupVo.Option();
						object.setTitle(option.getName());
						long count = answers.parallelStream().filter(o -> String.valueOf(option.getId()).equals(o.getAnswerId())).count();
						object.setNumber(count);
						try {
							object.setRate(MathUtil.div(new BigDecimal(count * 100), new BigDecimal(number), MathUtil.SCALE_LEN_COMMON));
						} catch (IllegalAccessException e) {
							object.setRate(BigDecimal.ZERO);
						}
						optionList.add(object);
					}
				} else if (QuestionnaireConstant.SUBJECT_TYPE_CHECKBOX.equals(type) || QuestionnaireConstant.SUBJECT_TYPE_SELECT.equals(type)) {
					//多选题,下拉题
					int total = 0;
					for (SysQuestionnaireAnswer bean : answers) {
						if (StringUtils.isNotBlank(bean.getAnswerId())) {
							//把分割符去掉，用原有长度减去现字符长度 + 1就是选项个数
							String s = bean.getAnswerId().replace(WebConstants.ARRAY_SPT, "");
							total += bean.getAnswerId().length() - s.length() + 1;
						}
					}
					number = total;
					//遍历该题的选项
					for (SysQuestionnaireSubjectOption option : options) {
						QuestionnaireSubjectGroupVo.Option object = new QuestionnaireSubjectGroupVo.Option();
						object.setTitle(option.getName());
						long count = answers.parallelStream().filter(o -> StringUtils.isNotBlank(o.getAnswerId()))
							.filter(o -> Arrays.asList(o.getAnswerId().split(WebConstants.ARRAY_SPT)).contains(String.valueOf(option.getId()))).count();
						object.setNumber(count);
						try {
							object.setRate(MathUtil.div(new BigDecimal(count * 100), new BigDecimal(total), MathUtil.SCALE_LEN_COMMON));
						} catch (IllegalAccessException e) {
							object.setRate(BigDecimal.ZERO);
						}
						optionList.add(object);
					}
				} else if (QuestionnaireConstant.SUBJECT_TYPE_CASCADE.equals(type)) {
					//级联题
					Map<String, List<SysQuestionnaireAnswer>> answerMap = answers.parallelStream()
						.filter(o -> StringUtils.isNotBlank(o.getAnswer()))
						.collect(Collectors.groupingBy(SysQuestionnaireAnswer::getAnswer));
					for (String s : answerMap.keySet()) {
						QuestionnaireSubjectGroupVo.Option object = new QuestionnaireSubjectGroupVo.Option();
						object.setTitle(s);
						int size = answerMap.get(s).size();
						object.setNumber((long) size);
						try {
							object.setRate(MathUtil.div(new BigDecimal(size * 100), new BigDecimal(number), MathUtil.SCALE_LEN_COMMON));
						} catch (IllegalAccessException e) {
							object.setRate(BigDecimal.ZERO);
						}
						optionList.add(object);
					}
				} else if (QuestionnaireConstant.SUBJECT_TYPE_FILE.equals(type)) {
					isFile = true;
				}
				QuestionnaireSubjectGroupVo vo = new QuestionnaireSubjectGroupVo(id, title, type, number, index, optionList, isFile);
				vos.add(vo);
			}
		}
		vos = vos.parallelStream().sorted(Comparator.comparing(QuestionnaireSubjectGroupVo::getIndex)).collect(Collectors.toList());
		return vos;
	}

	@Override
	public List<QuestionnairePieVo> pieChart(Integer questionnaireId, Integer subjectId, Integer siteId) {
		List<SysQuestionnaireAnswer> list = dao.getList(subjectId, siteId, questionnaireId);
		List<QuestionnairePieVo> vos = new ArrayList<QuestionnairePieVo>();
		if (list == null || list.size() <= 0) {
			return new ArrayList<QuestionnairePieVo>();
		}
		Short type = list.get(0).getSubject().getType();
		if (QuestionnaireConstant.SUBJECT_TYPE_CASCADE.equals(type)) {
			//级联题处理
			Map<String, List<SysQuestionnaireAnswer>> map = list.parallelStream().filter(o -> o.getAnswer() != null)
				.collect(Collectors.groupingBy(SysQuestionnaireAnswer::getAnswer));
			for (String s : map.keySet()) {
				List<SysQuestionnaireAnswer> answers = map.get(s);
				SysQuestionnaireAnswer answer = answers.get(0);
				QuestionnairePieVo vo = new QuestionnairePieVo(s, answers.size(), answer.getAnswer());
				vos.add(vo);
			}
		} else if (QuestionnaireConstant.SUBJECT_TYPE_SELECT.equals(type) ||
			QuestionnaireConstant.SUBJECT_TYPE_RADIO.equals(type) ||
			QuestionnaireConstant.SUBJECT_TYPE_CHECKBOX.equals(type)) {
			//单选 多选 和 下拉题处理
			//所有选项集合
			List<Map<String, Integer>> answerIdList = new ArrayList<Map<String, Integer>>();
			//循环问卷结果获取所有结果的选项
			for (SysQuestionnaireAnswer answer : list) {
				String answerId = answer.getAnswerId();
				if (StringUtils.isNotBlank(answerId)) {
					//获取单个结果的选项
					String[] strings = answerId.split(WebConstants.ARRAY_SPT);
					//循环单个结果的选项
					for (String string : strings) {
						Map<String, Integer> map = new LinkedHashTreeMap<>();
						List<SysQuestionnaireSubjectOption> options = answer.getSubject().getOptions();
						for (SysQuestionnaireSubjectOption option : options) {
							//判断结果的选项和题目的选项是否一样，是一样获取名字保存到选项集合中
							if (option.getId().equals(Integer.parseInt(string))) {
								map.put(option.getName(), option.getId());
								answerIdList.add(map);
								break;
							}
						}
					}
				}
			}
			SysQuestionnaireSubject subject = subjectService.findById(subjectId);
			List<SysQuestionnaireSubjectOption> opt = subject.getOptions();
			opt = opt.parallelStream().sorted(Comparator.comparing(SysQuestionnaireSubjectOption::getSortNum)).collect(Collectors.toList());
			Map<Set<String>, List<Map<String, Integer>>> collect = answerIdList.parallelStream().collect(Collectors.groupingBy(Map::keySet));
			for (SysQuestionnaireSubjectOption subjectOption : opt) {
				for (Set<String> set : collect.keySet()) {
					String d = set.iterator().next();
					if (subjectOption.getName().equals(d)) {
						List<Map<String, Integer>> maps = collect.get(set);
						Integer value = maps.get(0).get(d);
						QuestionnairePieVo vo = new QuestionnairePieVo(d, maps.size(), String.valueOf(value));
						vos.add(vo);
						break;
					}
				}
			}
		}
		return vos;
	}

	@Override
	public Page<SysQuestionnaireAnswer> getPageBySubject(String province, String city, String device, Integer deviceType, String ip,
														 String replayName, Date beginTime, Date endTime, Integer subjectId, Integer orderBy,
														 Integer siteId, Integer questionnaireId, String options, Pageable pageable) {
		Map<String, String> map = handleArea(province, city);
		List<SysQuestionnaireAnswer> list = dao.getList(true, map.get("province"), map.get("city"), device,
			deviceType, ip, subjectId, replayName, beginTime, endTime, null, siteId, questionnaireId);
		if (StringUtils.isNotBlank(options)) {
			String[] optionArray = options.split(WebConstants.ARRAY_SPT);
			SysQuestionnaireSubject subject = subjectService.findById(subjectId);
			Stream<SysQuestionnaireAnswer> stream = list.parallelStream();
			if (QuestionnaireConstant.SUBJECT_TYPE_RADIO.equals(subject.getType()) ||
				QuestionnaireConstant.SUBJECT_TYPE_SELECT.equals(subject.getType()) ||
				QuestionnaireConstant.SUBJECT_TYPE_CHECKBOX.equals(subject.getType())) {
				//排除答案
				for (String s : optionArray) {
					stream = stream.filter(o -> StringUtils.isNotBlank(o.getAnswerId()))
						.filter(o -> !Arrays.asList(o.getAnswerId().split(WebConstants.ARRAY_SPT)).contains(s));
				}
			} else if (QuestionnaireConstant.SUBJECT_TYPE_CASCADE.equals(subject.getType())) {
				for (String s : optionArray) {
					stream = stream.filter(o -> !o.getAnswer().equals(s));
				}
			}
			list = stream.collect(Collectors.toList());
		}
		if (orderBy == null || ORDER_BY_CREATE_TIME_DESC.equals(orderBy)) {
			list = list.parallelStream().sorted(Comparator.comparing(SysQuestionnaireAnswer::getCreateTime)
				.reversed()).collect(Collectors.toList());
		} else if (ORDER_BY_CREATE_TIME_ASC.equals(orderBy)) {
			list = list.parallelStream().sorted(Comparator.comparing(SysQuestionnaireAnswer::getCreateTime))
				.collect(Collectors.toList());
		}
		List<SysQuestionnaireAnswer> answers = list.stream()
			.skip(pageable.getPageSize() * (pageable.getPageNumber()))
			.limit(pageable.getPageSize()).collect(Collectors.toList());
		return new PageImpl<SysQuestionnaireAnswer>(answers, pageable, list.size());
	}

	@Override
	public QuestionnaireAnswerViewVo getById(Integer id) {
		SysQuestionnaireAnswer answer = findById(id);
		List<SysQuestionnaireAnswer> list = dao.findByReplayNameAndQuestionnaireId(answer.getReplayName(),
			answer.getCreateTime(), answer.getQuestionnaireId());
		QuestionnaireAnswerViewVo viewVo = null;
		if (list != null && list.size() > 0) {
			SysQuestionnaireAnswer bean = list.get(0);
			SysQuestionnaire q = bean.getQuestionnaire();

			BgConfigBean bg = JSONObject.parseObject(q.getBgConfig(), BgConfigBean.class);
			bg.setBgImageUrl(q.getBgImgUrl());
			HeadConfigBean head = JSONObject.parseObject(q.getHeadConfig(), HeadConfigBean.class);
			head.setBgImageUrl(q.getHeadImgUrl());
			ContConfigBean cont = JSONObject.parseObject(q.getContConfig(), ContConfigBean.class);
			FontConfigBean font = JSONObject.parseObject(q.getFontConfig(), FontConfigBean.class);
			SubConfigBean sub = JSONObject.parseObject(q.getSubConfig(), SubConfigBean.class);
			QuestionnaireAnswerViewVo.VoteStyle voteStyle = new QuestionnaireAnswerViewVo.VoteStyle(bg, head, cont, font, sub);
			viewVo = new QuestionnaireAnswerViewVo(q.getTitle(), q.getDetails(), bean.getReplayName(), bean.getCreateTime(),
				bean.getIsEffective(), bean.getIp(), bean.getAddress(), bean.getDevice(), voteStyle);
			JSONArray array = new JSONArray();
			for (SysQuestionnaireAnswer entity : list) {
				JSONObject object = new JSONObject();
				if (entity.getSubject() == null) {
					break;
				}
				object.put("title", entity.getSubject().getTitle());
				Short type = entity.getSubject().getType();
				String str = entity.getAnswer();
				if (QuestionnaireConstant.SUBJECT_TYPE_FILE.equals(type)) {
					try {
						JSONArray strings = JSONArray.parseArray(str);
						object.put("result", strings);
					} catch (Exception e) {
						object.put("result", "");
					}
				} else if (QuestionnaireConstant.SUBJECT_TYPE_CASCADE.equals(type)) {
					if (StringUtils.isNotBlank(str)) {
						str = str.replace(WebConstants.ARRAY_SPT, WebConstants.SALE_ATTR_SPT);
					}
					object.put("result", str);
				} else {
					object.put("result", str);
				}
				array.add(object);
			}
			viewVo.setAnswer(array);
		}
		return viewVo;
	}

	@Override
	public List<SysQuestionnaireAnswer> getList(Boolean isEffective, String province, String city,
												String device, Integer deviceType, String ip,
												String replayName, Date beginTime, Date endTime,
												Integer orderBy, Integer siteId, Integer questionnaireId) {
		Map<String, String> map = handleArea(province, city);
		return dao.getList(isEffective, map.get("province"), map.get("city"), device, deviceType, ip, null,
			replayName, beginTime, endTime, orderBy, siteId, questionnaireId);
	}

	@Override
	public List<QuestionnaireExportListVo> getExportList(Boolean isEffective, String province, String city,
														 String device, Integer deviceType, String ip,
														 String replayName, Date beginTime, Date endTime,
														 Integer orderBy, Integer siteId,
														 Integer questionnaireId, Integer page, Integer size) {
		Map<String, String> map = handleArea(province, city);
		List<SysQuestionnaireAnswer> list = getList(isEffective, map.get("province"), map.get("city"), device, deviceType, ip,
			replayName, beginTime, endTime, orderBy, siteId, questionnaireId);
		//用户列表
		List<SysQuestionnaireAnswer> answers = getReplayObject(list);
		if (orderBy == null || ORDER_BY_CREATE_TIME_DESC.equals(orderBy)) {
			answers = answers.parallelStream().sorted(Comparator.comparing(SysQuestionnaireAnswer::getCreateTime)
				.reversed()).collect(Collectors.toList());
		} else if (ORDER_BY_CREATE_TIME_ASC.equals(orderBy)) {
			answers = answers.parallelStream().sorted(Comparator.comparing(SysQuestionnaireAnswer::getCreateTime))
				.collect(Collectors.toList());
		}
		if(page != null && size !=null) {
			answers = answers.parallelStream().skip(size * (page - 1)).limit(size).collect(Collectors.toList());
		}
		List<QuestionnaireExportListVo> exportListVos = new ArrayList<QuestionnaireExportListVo>();
		for (SysQuestionnaireAnswer answer : answers) {
			QuestionnaireExportListVo exportListVo = new QuestionnaireExportListVo();
			//用户答案列表
			List<SysQuestionnaireAnswer> answerList = list.parallelStream()
				.filter(o -> o.getCreateTime().equals(answer.getCreateTime()))
				.filter(o -> o.getReplayName().equals(answer.getReplayName()))
				.filter(o -> o.getIp().equals(answer.getIp()))
				.filter(o -> o.getDevice().equals(answer.getDevice()))
				.sorted(Comparator.comparing(o -> o.getSubject().getIndex()))
				.collect(Collectors.toList());
			Boolean isFile = false;
			Map<String, String> attr = new LinkedHashTreeMap<String, String>();
			List<String> resourceList = new ArrayList<String>();
			for (SysQuestionnaireAnswer qAnswer : answerList) {
				List<String> resources = new ArrayList<String>();

				if (qAnswer.getSubject() != null) {
					if (QuestionnaireConstant.SUBJECT_TYPE_FILE.equals(qAnswer.getSubject().getType())) {
						isFile = true;
						try {
							List<QuestionnaireFileDto> fileDtos = JSONArray.parseArray(qAnswer.getAnswer(), QuestionnaireFileDto.class);
							for (QuestionnaireFileDto fileDto : fileDtos) {
								resources.add(fileDto.getName());
								resourceList.add(fileDto.getUrl());
							}
							attr.put(qAnswer.getSubject().getTitle(), StringUtils.join(resources, WebConstants.ARRAY_SPT));
						} catch (Exception e) {
							attr.put(qAnswer.getSubject().getTitle(), "");
						}
					} else {
						attr.put(qAnswer.getSubject().getTitle(), qAnswer.getAnswer());
					}
				}
			}
			exportListVo.setFileUrls(resourceList);
			exportListVo.setIsFile(isFile);
			exportListVo.setTitle(answer.getQuestionnaire().getTitle());
			exportListVo.setAttr(attr);
			exportListVo.setAddress(answer.getAddress());
			exportListVo.setCreateTime(answer.getCreateTime());
			exportListVo.setDevice(answer.getDevice());
			exportListVo.setEffective(answer.getIsEffective());
			exportListVo.setIp(answer.getIp());
			exportListVo.setReplayName(answer.getReplayName());
			exportListVos.add(exportListVo);
		}
		return exportListVos;
	}

	@Override
	public List<SysQuestionnaireAnswer> markStatus(Integer[] ids, Boolean status) throws GlobalException {
		List<SysQuestionnaireAnswer> list = dao.findAllById(Arrays.asList(ids));
		List<Date> dates = new ArrayList<Date>(list.size());
		List<String> ips = new ArrayList<String>(list.size());
		List<String> devices = new ArrayList<String>(list.size());
		for (SysQuestionnaireAnswer answer : list) {
			if (!answer.getIsEffective().equals(status)) {
				dates.add(answer.getCreateTime());
				ips.add(answer.getIp());
				devices.add(answer.getDevice());
			}
		}
		List<SysQuestionnaireAnswer> answers = dao.find(dates, ips, devices);
		for (SysQuestionnaireAnswer answer : answers) {
			if (!answer.getIsEffective().equals(status)) {
				answer.setIsEffective(status);
			}
		}
		batchUpdate(list);
		return list;
	}

	@Override
	public void deleteByIds(Integer[] ids) throws GlobalException {
		List<SysQuestionnaireAnswer> list = dao.findAllById(Arrays.asList(ids));
		List<String> ips = new ArrayList<String>(list.size());
		List<Date> dates = new ArrayList<Date>(list.size());
		List<String> devices = new ArrayList<String>(list.size());
		List<SysQuestionnaire> questionnaires = new ArrayList<SysQuestionnaire>(list.size());
		for (SysQuestionnaireAnswer answer : list) {
			dates.add(answer.getCreateTime());
			ips.add(answer.getIp());
			devices.add(answer.getDevice());
			SysQuestionnaire q = answer.getQuestionnaire();
			q.setAnswerCount(q.getAnswerCount() - 1);
			questionnaires.add(q);
		}
		List<SysQuestionnaireAnswer> answers = dao.find(dates, ips, devices);
		super.physicalDeleteInBatch(answers);
		questionnaireService.batchUpdate(questionnaires);
	}

	@Override
	public List<QuestionnairePieVo> devicesPieChart(Integer questionnaireId, Integer siteId) {
		List<SysQuestionnaireAnswer> list = dao.getList(null, siteId, questionnaireId);
		List<SysQuestionnaireAnswer> answers = getReplayObject(list);
		List<String> devices = new ArrayList<String>();
		for (SysQuestionnaireAnswer answer : answers) {
			devices.add(answer.getDevice());
		}
		List<QuestionnairePieVo> vos = new ArrayList<>();

		Map<String, List<String>> collect = devices.parallelStream().collect(Collectors.groupingBy(String::toString));
		for (String s : collect.keySet()) {
			List<String> charts = collect.get(s);
			QuestionnairePieVo vo = new QuestionnairePieVo();
			vo.setItem(s);
			int sum = charts.size();
			vo.setCount(sum);
			vos.add(vo);
		}
		return vos;
	}

	@Override
	public List<QuestionnairePieVo> statisticsMapChart(Integer questionnaireId, Integer siteId) {
		List<SysQuestionnaireAnswer> list = dao.getList(null, siteId, questionnaireId);
		List<SysQuestionnaireAnswer> answers = getReplayObject(list);
		//所有问卷结果
		int sum = answers.size();
		//根据省分组
		Map<String, List<SysQuestionnaireAnswer>> map = answers.parallelStream()
			.filter(o -> StringUtils.isNotBlank(o.getProvince()))
			.collect(Collectors.groupingBy(SysQuestionnaireAnswer::getProvince));
		List<QuestionnairePieVo> vos = new ArrayList<QuestionnairePieVo>();
		for (String s : map.keySet()) {
			QuestionnairePieVo vo = new QuestionnairePieVo();
			vo.setItem(s);
			int count = map.get(s).size();
			vo.setCount(count);
			try {
				vo.setRate(MathUtil.div(new BigDecimal(count * 100), new BigDecimal(sum), MathUtil.SCALE_LEN_COMMON));
			} catch (IllegalAccessException e) {
				vo.setRate(BigDecimal.ZERO);
			}
			vos.add(vo);

		}
		return vos;
	}

	@Override
	public JSONArray statisticsAreaChart(Integer questionnaireId, String device, Integer deviceType, String province, String city,
										 int showType, Date beginTime, Date endTime, Integer siteId) {
		Map<String, String> areaMap = handleArea(province, city);
		beginTime = MyDateUtils.getStartDate(beginTime);
		endTime = MyDateUtils.getFinallyDate(endTime);
		List<SysQuestionnaireAnswer> list = dao.getList(null, areaMap.get("province"), areaMap.get("city"), device,
			deviceType, null, null, null, beginTime, endTime, null, siteId, questionnaireId);
		List<SysQuestionnaireAnswer> answers = getReplayObject(list);
		List<String> times = betweenTime(showType, beginTime, endTime);
		Map<String, List<SysQuestionnaireAnswer>> answerMap = new LinkedHashTreeMap<String, List<SysQuestionnaireAnswer>>();
		JSONArray jsonArray = new JSONArray();
		if (HOUR == showType) {
			Map<String, List<SysQuestionnaireAnswer>> map = answers.parallelStream().filter(o -> o.getCreateTime() != null)
				.collect(Collectors.groupingBy(o -> MyDateUtils.formatDate(o.getCreateTime(), getPattern(showType))));

			for (String time : times) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("time", time);
				if (map.containsKey(time)) {
					jsonObject.put("count", map.get(time).size());
				} else {
					jsonObject.put("count", 0);
				}
				jsonArray.add(jsonObject);
			}
		} else if (DAY == showType) {
			Map<String, List<SysQuestionnaireAnswer>> map = answers.parallelStream().filter(o -> o.getCreateTime() != null)
				.collect(Collectors.groupingBy(o -> MyDateUtils.formatDate(o.getCreateTime(), getPattern(showType))));

			for (String time : times) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("time", time);
				if (map.containsKey(time)) {
					jsonObject.put("count", map.get(time).size());
				} else {
					jsonObject.put("count", 0);
				}
				jsonArray.add(jsonObject);
			}
		} else if (WEEK == showType) {
			beginTime = beginTime != null ? MyDateUtils.getStartDate(beginTime) : Calendar.getInstance().getTime();
			endTime = endTime != null ? MyDateUtils.getFinallyDate(endTime) : Calendar.getInstance().getTime();
			Map<Date, Date> dateMap = groupByWeek(beginTime, endTime);
			for (Date date : dateMap.keySet()) {
				Date value = dateMap.get(date);
				List<SysQuestionnaireAnswer> answerList = answers.parallelStream().filter(
					o -> o.getCreateTime() != null && o.getCreateTime().getTime() >= date.getTime() &&
						o.getCreateTime().getTime() <= value.getTime()).collect(Collectors.toList());
				String key1 = MyDateUtils.formatDate(date);
				String key2 = MyDateUtils.formatDate(value);
				answerMap.put(key1 + "-" + key2, answerList);
			}
			for (String s : answerMap.keySet()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("time", s);

				jsonObject.put("count", answerMap.get(s).size());
				jsonArray.add(jsonObject);
			}
		} else if (MOUTH == showType) {
			beginTime = beginTime != null ? MyDateUtils.getStartDate(beginTime) : Calendar.getInstance().getTime();
			endTime = endTime != null ? MyDateUtils.getFinallyDate(endTime) : Calendar.getInstance().getTime();
			Map<Date, Date> dateMap = groupByMonth(beginTime, endTime);
			for (Date date : dateMap.keySet()) {
				Date value = dateMap.get(date);
				List<SysQuestionnaireAnswer> answerList = answers.parallelStream().filter(
					o -> o.getCreateTime() != null && o.getCreateTime().getTime() >= date.getTime() &&
						o.getCreateTime().getTime() <= value.getTime()).collect(Collectors.toList());
				String key1 = MyDateUtils.formatDate(date);
				String key2 = MyDateUtils.formatDate(value);
				answerMap.put(key1 + "-" + key2, answerList);
			}
			for (String s : answerMap.keySet()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("time", s);

				jsonObject.put("count", answerMap.get(s).size());
				jsonArray.add(jsonObject);
			}
		}
		return jsonArray;
	}

	@Override
	public List<SysQuestionnaireAnswer> findByQuestionnaireId(Integer questionnaireId, Boolean showFile, Integer siteId) {
		return dao.findByQuestionnaireId(questionnaireId, showFile, siteId);
	}

	@Override
	public void save(Integer questionnaireId, HttpServletRequest request, HttpServletResponse response) throws GlobalException {
		SysQuestionnaire q = questionnaireService.findById(questionnaireId);

		String ua = ((HttpServletRequest) request).getHeader("user-agent")
			.toLowerCase();
		// 不是微信浏览器
		if (q.getQuestionnaireConfig().getIsOnlyWechat() != null &&
			q.getQuestionnaireConfig().getIsOnlyWechat() && ua.indexOf("micromessenger") <= 0) {
			throw new GlobalException(new SystemExceptionInfo(
				SysOtherErrorCodeEnum.QUESTIONNAIRE_WECHAT_ONLY.getDefaultMessage(),
				SysOtherErrorCodeEnum.QUESTIONNAIRE_WECHAT_ONLY.getCode()));
		}
		if (q.getQuestionnaireConfig().getIsVerification() != null &&
			q.getQuestionnaireConfig().getIsVerification() &&
			!captchaService.validCaptcha(request, response, request.getParameter("captcha"), request.getParameter("sessionId"))) {
			throw new GlobalException(new CaptchaExceptionInfo());
		}
		if (!QuestionnaireConstant.STATUS_PROCESSING.equals(q.getStatus())) {
			throw new GlobalException(new SystemExceptionInfo(
				SysOtherErrorCodeEnum.QUESTIONNAIRE_CANCELLED.getDefaultMessage(),
				SysOtherErrorCodeEnum.QUESTIONNAIRE_CANCELLED.getCode()));
		}
		CoreUser user = SystemContextUtils.getUser(request);
		Integer userId = null;
		String userName = "匿名";
		String ip = RequestUtils.getRemoteAddr(request);
		Map<String, String> deviceMap = device(request);
		String device = deviceMap.get("device");
		Integer deviceType = Integer.parseInt(deviceMap.get("deviceType"));
		if (user != null) {
			userId = user.getUserId();
			userName = user.getUsername();
		}
		SysQuestionnaireConfig cfg = q.getQuestionnaireConfig();
		List<SysQuestionnaireAnswer> answers = findByQuestionnaireId(questionnaireId, true, SystemContextUtils.getSiteId(request));
		answers = getReplayObject(answers);
		//判断是否需要登录才能投票及登录投票限制
		checkUserLimit(cfg, userId, answers);
		//设备答题次数限制
		checkDeviceLimit(cfg, ip, device, answers);
		//ip答题次数限制
		checkCookieLimit(cfg, ip, answers);

		List<SysQuestionnaireSubject> subjects = q.getSubjects();

		Map<String, String> map = area(request);
		String province = map.get("province");
		String city = map.get("city");

		List<SysQuestionnaireAnswer> answerList = new ArrayList<>(subjects.size());
		for (SysQuestionnaireSubject subject : subjects) {
			SysQuestionnaireAnswer answer = new SysQuestionnaireAnswer();
			answer.setIsEffective(true);
			answer.setCity(city);
			answer.setProvince(province);
			answer.setDevice(device);
			answer.setIp(ip);
			answer.setDeviceType(deviceType);
			answer.setReplayId(userId);
			answer.setReplayName(userName);
			String ans = request.getParameter("attr_" + subject.getId());
			ans = "null".equals(ans) ? "" : ans;
			if (QuestionnaireConstant.SUBJECT_TYPE_RADIO.equals(subject.getType()) ||
				QuestionnaireConstant.SUBJECT_TYPE_SELECT.equals(subject.getType()) ||
				QuestionnaireConstant.SUBJECT_TYPE_CHECKBOX.equals(subject.getType())) {

				List<String> list = new ArrayList<String>();
				//判断是否必填
				if (subject.getIsAnswer() && StringUtils.isBlank(ans)) {
					throw new GlobalException(new SystemExceptionInfo(
						SysOtherErrorCodeEnum.QUESTIONNAIRE_ANSWER_REQUIRED.getDefaultMessage(),
						SysOtherErrorCodeEnum.QUESTIONNAIRE_ANSWER_REQUIRED.getCode()));
				}
				if (StringUtils.isNotBlank(ans)) {
					String[] ansIds = ans.split(WebConstants.ARRAY_SPT);
					for (String s1 : ansIds) {
						for (SysQuestionnaireSubjectOption option : subject.getOptions()) {
							if (String.valueOf(option.getId()).equals(s1)) {
								String name = option.getName();
								if (option.getIsOther() != null && option.getIsOther()) {
									String other = request.getParameter("other_" + option.getId());
									if (StringUtils.isBlank(other) && option.getIsRequired()) {
										throw new GlobalException(new SystemExceptionInfo(
											SysOtherErrorCodeEnum.QUESTIONNAIRE_ANSWER_REQUIRED.getDefaultMessage(),
											SysOtherErrorCodeEnum.QUESTIONNAIRE_ANSWER_REQUIRED.getCode()));
									}
									if (StringUtils.isBlank(name)) {
										name = other;
									} else {
										if (StringUtils.isNotBlank(other)) {
											name = name + WebConstants.SALE_ATTR_SPT + other;
										}
									}
								}
								list.add(name);
								break;
							}
						}
					}
					answer.setAnswer(StringUtils.join(list, WebConstants.ARRAY_SPT));
					answer.setAnswerId(ans);
				}
			} else if (QuestionnaireConstant.SUBJECT_TYPE_FILE.equals(subject.getType())) {

				List<MultipartFile> filess = new ArrayList<MultipartFile>();
				if (request instanceof MultipartHttpServletRequest) {
					MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
					filess = multipartRequest.getFiles("attr_" + subject.getId());
				}
				//判断是否必填
				if (subject.getIsAnswer() && filess.size() == 0) {
					throw new GlobalException(new SystemExceptionInfo(
						SysOtherErrorCodeEnum.QUESTIONNAIRE_ANSWER_REQUIRED.getDefaultMessage(),
						SysOtherErrorCodeEnum.QUESTIONNAIRE_ANSWER_REQUIRED.getCode()));
				}

				List<String> fileNames = new ArrayList<String>();
				long size = 0;
				for (MultipartFile multipartFile : filess) {
					size += multipartFile.getSize();
					String fileName = multipartFile.getOriginalFilename();
					if (fileName != null) {
						fileName = fileName.substring(fileName.lastIndexOf(".") + 1);
						fileNames.add(fileName);
					}
				}

				//文件大小校验
				if (subject.getFileSize() != null && subject.getFileSize()) {
					Integer fileSize = subject.getFileSizeLimit();
					String fileSizeLimit = subject.getFileSizeLimitUnit();
					long fileSizeToByte = fileSizeToByte(fileSize != null ? fileSize : 0, fileSizeLimit != null ? fileSizeLimit : "KB");
					if (size < fileSizeToByte) {
						throw new GlobalException(new SystemExceptionInfo(
							SysOtherErrorCodeEnum.UPLOAD_LIMIT_ERROR.getDefaultMessage(),
							SysOtherErrorCodeEnum.UPLOAD_LIMIT_ERROR.getCode()));
					}
				}
				//文件数量校验
				if (subject.getFileCount() != null && subject.getFileCount()) {
					Integer count = subject.getFileCountLimit();
					if (count < filess.size()) {
						throw new GlobalException(new SystemExceptionInfo(
							SysOtherErrorCodeEnum.UPLOAD_LIMIT_ERROR.getDefaultMessage(),
							SysOtherErrorCodeEnum.UPLOAD_LIMIT_ERROR.getCode()));
					}
				}
				//文件类型校验
				if (subject.getFileType() != null && subject.getFileType()) {
					String fileTypeLimit = subject.getFileTypeLimit();
					if (StringUtils.isNotBlank(fileTypeLimit)) {
						String[] type = fileTypeLimit.split(WebConstants.ARRAY_SPT);
						for (String name : fileNames) {
							if (subject.getAllowFile() != null && PROHIBIT.equals(subject.getAllowFile())) {
								//禁止
								if (StringUtils.containsAny(name, type)) {
									throw new GlobalException(new SystemExceptionInfo(
										SysOtherErrorCodeEnum.UPLOAD_LIMIT_ERROR.getDefaultMessage(),
										SysOtherErrorCodeEnum.UPLOAD_LIMIT_ERROR.getCode()));
								}
							} else if (subject.getAllowFile() != null && ALLOW.equals(subject.getAllowFile())) {
								//允许
								if (!StringUtils.containsAny(name, type)) {
									throw new GlobalException(new SystemExceptionInfo(
										SysOtherErrorCodeEnum.UPLOAD_LIMIT_ERROR.getDefaultMessage(),
										SysOtherErrorCodeEnum.UPLOAD_LIMIT_ERROR.getCode()));
								}
							}
						}
					}
				}

				if (filess.size() > 0) {
					String typeStr = null;
					List<QuestionnaireFileDto> list = new ArrayList<QuestionnaireFileDto>();
					for (MultipartFile file : filess) {
						QuestionnaireFileDto dto = new QuestionnaireFileDto();
						File toFile = null;
						InputStream ins = null;
						try {
							if (file != null && ImageUtils.isImage(file.getInputStream())) {
								typeStr = ResourceType.IMAGE.getName();
							}
							CmsSite site = SystemContextUtils.getSite(request);
							ResourceType resourceType = ResourceType.getDefaultResourceType(typeStr);
							UploadResult result = uploadService.doUpload(file, false, null, resourceType, site);
							dto.setUrl(result.getFileUrl());
							dto.setName(file.getOriginalFilename());
							// 转换pdf
							dto.setType(result.getResourceType());
							String pdfUrl = site.getUploadPath() + "/" + file.getOriginalFilename() + ".pdf";

							if (file.equals("") || file.getSize() <= 0) {
								file = null;
							} else {
								ins = file.getInputStream();
								toFile = new File(realPathResolver.get(site.getUploadPath() + "/" + file.getOriginalFilename()));
								inputStreamToFile(ins, toFile);
								ins.close();
								if (!ImageUtils.isImage(ins)) {
									openOfficeConverter.convertToPdf(toFile.getAbsolutePath(),
										realPathResolver.get(site.getUploadPath()) + "/", file.getOriginalFilename());
									dto.setPdfUrl(pdfUrl);
								}
							}
							String dimensions = result.getDimensions();
							if (StringUtils.isNotBlank(dimensions) && dimensions.contains("*")) {
								dto.setWidth(dimensions.split("\\*")[0]);
								dto.setHeight(dimensions.split("\\*")[1]);
							}
							list.add(dto);
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							if (ins != null) {
								try {
									ins.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}
					answer.setAnswer(toJSONString(list));
				}
			} else if (QuestionnaireConstant.SUBJECT_TYPE_QUESTIONS_AND_ANSWERS.equals(subject.getType())) {
				//判断是否必填
				if (subject.getIsAnswer() && StringUtils.isBlank(ans)) {
					throw new GlobalException(new SystemExceptionInfo(
						SysOtherErrorCodeEnum.QUESTIONNAIRE_ANSWER_REQUIRED.getDefaultMessage(),
						SysOtherErrorCodeEnum.QUESTIONNAIRE_ANSWER_REQUIRED.getCode()));
				}

				//判断是否开启长度限制和长度是否超出限制
				if (subject.getInputNumLimit() != null && subject.getInputNumLimit()) {
					Integer size = subject.getWordLimit();
					size = size == null ? 0 : size;
					if (StringUtils.isNotBlank(ans) && ans.length() > size) {
						throw new GlobalException(new SystemExceptionInfo(
							SysOtherErrorCodeEnum.QUESTIONNAIRE_ANSWER_OTHER_LENGTH.getDefaultMessage(),
							SysOtherErrorCodeEnum.QUESTIONNAIRE_ANSWER_OTHER_LENGTH.getCode()));
					}
				}
				//校验输入限制
				if (subject.getIsLimit() != null && subject.getIsLimit()) {
					Integer limitCondition = subject.getLimitCondition();
					String reg = QuestionnaireConstant.getReg(limitCondition);
					if (StringUtils.isNotBlank(reg)) {
						boolean flag = Pattern.matches(reg, ans);
						if (!flag) {
							throw new GlobalException(new SystemExceptionInfo(
								SysOtherErrorCodeEnum.QUESTIONNAIRE_ANSWER_REGEX_ERROR.getDefaultMessage(),
								SysOtherErrorCodeEnum.QUESTIONNAIRE_ANSWER_REGEX_ERROR.getCode()));
						}
					}
				}
				answer.setAnswer(ans);
			} else if (QuestionnaireConstant.SUBJECT_TYPE_CASCADE.equals(subject.getType())) {
				answer.setAnswer(ans);
			}
			answer.setSubjectId(subject.getId());
			answer.setSubject(subject);
			answer.setQuestionnaire(subject.getQuestionnaire());
			answer.setQuestionnaireId(subject.getQuestionnaireId());
			answerList.add(answer);
		}
		if (answerList.size() == 0) {
			SysQuestionnaireAnswer answer = new SysQuestionnaireAnswer();
			answer.setIsEffective(true);
			answer.setCity(city);
			answer.setProvince(province);
			answer.setDevice(device);
			answer.setIp(ip);
			answer.setDeviceType(deviceType);
			answer.setReplayId(userId);
			answer.setReplayName(userName);
			answer.setQuestionnaire(q);
			answer.setQuestionnaireId(q.getId());
			answerList.add(answer);
		}
		super.saveAll(answerList);
		q.setAnswerCount(q.getAnswerCount() != null ? q.getAnswerCount() + 1 : 1);
		questionnaireService.update(q);
	}

	/**
	 * 校验设备投票限制
	 *
	 * @param cfg     投票配置类
	 * @param ip      ip
	 * @param device  设备
	 * @param answers 结果
	 * @throws GlobalException 异常
	 */
	private void checkDeviceLimit(SysQuestionnaireConfig cfg, String ip, String device, List<SysQuestionnaireAnswer> answers) throws GlobalException {
		//用户设备答题次数单位限制
		Short deviceAnswerFrequencyLimitUnit = cfg.getDeviceAnswerFrequencyLimitUnit();
		//用户设备答题次数限制
		Integer deviceAnswerFrequencyLimit = cfg.getDeviceAnswerFrequencyLimit();
		if (deviceAnswerFrequencyLimit != null && deviceAnswerFrequencyLimitUnit != null) {
			Date date = Calendar.getInstance().getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
			try {
				date = sdf.parse(sdf.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if (QuestionnaireConstant.LIMIT_UNIT_ONLY.equals(deviceAnswerFrequencyLimitUnit)) {
				long count = answers.parallelStream().filter(o -> o.getIp().equals(ip))
					.filter(o -> o.getDevice().equals(device)).count();
				if (count >= deviceAnswerFrequencyLimit) {
					throw new GlobalException(new SystemExceptionInfo(
						SysOtherErrorCodeEnum.QUESTIONNAIRE_EXCEED_COUNT.getDefaultMessage(),
						SysOtherErrorCodeEnum.QUESTIONNAIRE_EXCEED_COUNT.getCode()));
				}
			} else if (QuestionnaireConstant.LIMIT_UNIT_HOUR.equals(deviceAnswerFrequencyLimitUnit)) {
				Date finalDate = date;
				long count = answers.parallelStream().filter(o -> o.getIp().equals(ip))
					.filter(o -> o.getCreateTime().getTime() >= finalDate.getTime())
					.filter(o -> o.getDevice().equals(device)).count();
				if (count >= deviceAnswerFrequencyLimit) {
					throw new GlobalException(new SystemExceptionInfo(
						SysOtherErrorCodeEnum.QUESTIONNAIRE_EXCEED_COUNT.getDefaultMessage(),
						SysOtherErrorCodeEnum.QUESTIONNAIRE_EXCEED_COUNT.getCode()));
				}
			} else if (QuestionnaireConstant.LIMIT_UNIT_DAY.equals(deviceAnswerFrequencyLimitUnit)) {
				//匹配今天开始是否存在该用户的数据
				Date finalDate1 = date;
				long count = answers.parallelStream().filter(o -> o.getIp().equals(ip))
					.filter(o -> o.getCreateTime().getTime() >= MyDateUtils.getStartDate(finalDate1).getTime())
					.filter(o -> o.getDevice().equals(device)).count();
				if (count >= deviceAnswerFrequencyLimit) {
					throw new GlobalException(new SystemExceptionInfo(
						SysOtherErrorCodeEnum.QUESTIONNAIRE_EXCEED_COUNT.getDefaultMessage(),
						SysOtherErrorCodeEnum.QUESTIONNAIRE_EXCEED_COUNT.getCode()));
				}
			}
		}
	}

	/**
	 * 校验IP投票限制
	 *
	 * @param cfg     投票配置类
	 * @param ip      ip
	 * @param answers 结果
	 * @throws GlobalException 异常
	 */
	private void checkCookieLimit(SysQuestionnaireConfig cfg, String ip, List<SysQuestionnaireAnswer> answers) throws GlobalException {
		//用户ip答题次数单位限制
		Short ipAnswerFrequencyLimitUnit = cfg.getIpAnswerFrequencyLimitUnit();
		//用户ip答题次数限制
		Integer ipAnswerFrequencyLimit = cfg.getIpAnswerFrequencyLimit();
		if (ipAnswerFrequencyLimit != null && ipAnswerFrequencyLimitUnit != null) {
			Date date = Calendar.getInstance().getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
			try {
				date = sdf.parse(sdf.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if (QuestionnaireConstant.LIMIT_UNIT_ONLY.equals(ipAnswerFrequencyLimitUnit)) {
				long count = answers.parallelStream().filter(o -> o.getIp().equals(ip)).count();
				if (count >= ipAnswerFrequencyLimit) {
					throw new GlobalException(new SystemExceptionInfo(
						SysOtherErrorCodeEnum.QUESTIONNAIRE_EXCEED_COUNT.getDefaultMessage(),
						SysOtherErrorCodeEnum.QUESTIONNAIRE_EXCEED_COUNT.getCode()));
				}
			} else if (QuestionnaireConstant.LIMIT_UNIT_HOUR.equals(ipAnswerFrequencyLimitUnit)) {
				Date finalDate = date;
				long count = answers.parallelStream().filter(o -> o.getIp().equals(ip))
					.filter(o -> o.getCreateTime().getTime() >= finalDate.getTime()).count();
				if (count >= ipAnswerFrequencyLimit) {
					throw new GlobalException(new SystemExceptionInfo(
						SysOtherErrorCodeEnum.QUESTIONNAIRE_EXCEED_COUNT.getDefaultMessage(),
						SysOtherErrorCodeEnum.QUESTIONNAIRE_EXCEED_COUNT.getCode()));
				}
			} else if (QuestionnaireConstant.LIMIT_UNIT_DAY.equals(ipAnswerFrequencyLimitUnit)) {
				//匹配今天开始是否存在该用户的数据
				Date finalDate1 = date;
				long count = answers.parallelStream().filter(o -> o.getIp().equals(ip))
					.filter(o -> o.getCreateTime().getTime() >= MyDateUtils.getStartDate(finalDate1).getTime()).count();
				if (count >= ipAnswerFrequencyLimit) {
					throw new GlobalException(new SystemExceptionInfo(
						SysOtherErrorCodeEnum.QUESTIONNAIRE_EXCEED_COUNT.getDefaultMessage(),
						SysOtherErrorCodeEnum.QUESTIONNAIRE_EXCEED_COUNT.getCode()));
				}
			}
		}
	}

	/**
	 * 校验登录用户投票限制
	 *
	 * @param cfg     投票配置类
	 * @param userId  用户id
	 * @param answers 结果
	 * @throws GlobalException 异常
	 */
	private void checkUserLimit(SysQuestionnaireConfig cfg, Integer userId, List<SysQuestionnaireAnswer> answers) throws GlobalException {
		if (cfg.getAnswerLimit() != null && cfg.getAnswerLimit()) {
			if (userId == null) {
				throw new GlobalException(new SystemExceptionInfo(
					SysOtherErrorCodeEnum.QUESTIONNAIRE_IS_LOGIN.getDefaultMessage(),
					SysOtherErrorCodeEnum.QUESTIONNAIRE_IS_LOGIN.getCode()));
			}
			//用户答题次数单位限制
			Short userAnswerFrequencyLimitUnit = cfg.getUserAnswerFrequencyLimitUnit();
			//用户答题次数限制
			Integer userAnswerFrequencyLimit = cfg.getUserAnswerFrequencyLimit();
			Date date = Calendar.getInstance().getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
			try {
				date = sdf.parse(sdf.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if (userAnswerFrequencyLimit != null && userAnswerFrequencyLimitUnit != null) {
				if (QuestionnaireConstant.LIMIT_UNIT_ONLY.equals(userAnswerFrequencyLimitUnit)) {
					long count = answers.parallelStream().filter(o -> userId.equals(o.getReplayId())).count();
					if (count >= userAnswerFrequencyLimit) {
						throw new GlobalException(new SystemExceptionInfo(
							SysOtherErrorCodeEnum.QUESTIONNAIRE_EXCEED_COUNT.getDefaultMessage(),
							SysOtherErrorCodeEnum.QUESTIONNAIRE_EXCEED_COUNT.getCode()));
					}
				} else if (QuestionnaireConstant.LIMIT_UNIT_HOUR.equals(userAnswerFrequencyLimitUnit)) {
					Date finalDate = date;
					long count = answers.parallelStream().filter(o -> userId.equals(o.getReplayId()))
						.filter(o -> o.getCreateTime().getTime() >= finalDate.getTime()).count();
					if (count >= userAnswerFrequencyLimit) {
						throw new GlobalException(new SystemExceptionInfo(
							SysOtherErrorCodeEnum.QUESTIONNAIRE_EXCEED_COUNT.getDefaultMessage(),
							SysOtherErrorCodeEnum.QUESTIONNAIRE_EXCEED_COUNT.getCode()));
					}
				} else if (QuestionnaireConstant.LIMIT_UNIT_DAY.equals(userAnswerFrequencyLimitUnit)) {
					//匹配今天开始是否存在该用户的数据
					Date finalDate1 = date;
					long count = answers.parallelStream().filter(o -> userId.equals(o.getReplayId()))
						.filter(o -> o.getCreateTime().getTime() >= MyDateUtils.getStartDate(finalDate1).getTime()).count();
					if (count >= userAnswerFrequencyLimit) {
						throw new GlobalException(new SystemExceptionInfo(
							SysOtherErrorCodeEnum.QUESTIONNAIRE_EXCEED_COUNT.getDefaultMessage(),
							SysOtherErrorCodeEnum.QUESTIONNAIRE_EXCEED_COUNT.getCode()));
					}
				}
			}
		}
	}

	@Override
	public Page<SysQuestionnaireAnswer> findBySubjectId(Integer subjectId, Integer questionnaireId, Pageable pageable) {
		return dao.findBySubjectId(subjectId, questionnaireId, pageable);
	}

	/**
	 * 访客设备系统
	 *
	 * @param request 请求
	 * @Title: device
	 */
	protected Map<String, String> device(HttpServletRequest request) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		// 分析浏览器UserAgent,得到设备信息
		String userAgent = request.getHeader("User-Agent");
		if (!StringUtils.isNotBlank(userAgent)) {
			map.put("device", "移动设备");
			map.put("deviceType", "2");
		}
		if (userAgent.contains("Windows NT 10.0") || userAgent.contains("Windows NT 6.4")) {
			map.put("device", "Windows 10");
			map.put("deviceType", "1");
		} else if (userAgent.contains("Windows NT 6.2")) {
			map.put("device", "Windows 8");
			map.put("deviceType", "1");
		} else if (userAgent.contains("Windows NT 6.1")) {
			map.put("device", "Windows 7");
			map.put("deviceType", "1");
		} else if (userAgent.contains("iPhone OS 12")) {
			map.put("device", "iPhone OS 12");
			map.put("deviceType", "2");
		} else if (userAgent.contains("iPhone OS 11")) {
			map.put("device", "iPhone OS 11");
			map.put("deviceType", "2");
		} else if (userAgent.contains("iPhone OS 10")) {
			map.put("device", "iPhone OS 10");
			map.put("deviceType", "2");
		} else if (userAgent.contains("Android 10")) {
			map.put("device", "Android 10");
			map.put("deviceType", "2");
		} else if (userAgent.contains("Android 9")) {
			map.put("device", "Android 9");
			map.put("deviceType", "2");
		} else if (userAgent.contains("Android 8")) {
			map.put("device", "Android 8");
			map.put("deviceType", "2");
		} else if (userAgent.contains("Android 7")) {
			map.put("device", "Android 7");
			map.put("deviceType", "2");
		} else if (userAgent.contains("Android 6")) {
			map.put("device", "Android 6");
			map.put("deviceType", "2");
		} else if (userAgent.contains(SysAccessRecord.DEVICE_MAC)) {
			map.put("device", "Mac");
			map.put("deviceType", "1");
		} else {
			map.put("device", "PC");
			map.put("deviceType", "1");
		}
		return map;
	}

	/**
	 * 省，市，
	 *
	 * @param request {@link HttpServletRequest}
	 */
	protected Map<String, String> area(HttpServletRequest request) {
		Map<String, String> map = new LinkedHashMap<>(2);
		//省份
		String province = null;
		//市区
		String city = null;
		Location location = null;
		Location.LocationResult.AdInfo adInfo = null;
		// 定位
		String currentIp = RequestUtils.getRemoteAddr(request);
		try {
			location = addressService.getAddressByIP(currentIp);
			request.getSession().setAttribute(Area.CURRENT_ADDRESS_ATTRNAME, location);
			if (location != null && location.getResult() != null) {
				adInfo = location.getResult().getAdInfo();
				city = adInfo.getCity();
				province = adInfo.getProvince();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//应前端需要，将省份，城市中的XX省，XX市去除
		if (StringUtils.isNotBlank(province) && province.contains("省")) {
			province = province.replace("省", "");
		}
		//去除直辖市
		if (StringUtils.isNotBlank(province) && province.contains("市")) {
			province = province.replace("市", "");
		}
		//去除自治区
		if (StringUtils.isNotBlank(province) && province.contains("自治区")) {
			province = province.replace("自治区", "");
			//特殊处理内蒙古
			if (province.contains("内蒙古")) {
				province = province.substring(0, 3);
			} else {
				province = province.substring(0, 2);
			}
		}
		if (StringUtils.isNotBlank(city) && city.contains("市")) {
			city = city.replace("市", "");
		}
		map.put("city", city);
		map.put("province", province);
		return map;
	}

	/**
	 * 处理省和市
	 *
	 * @param provinceId 省
	 * @param cityId     市
	 * @return
	 */
	private Map<String, String> handleArea(String provinceId, String cityId) {
		Map<String, String> map = new LinkedHashMap<>(2);
		String province = "";
		if (StringUtils.isNotBlank(provinceId)) {
			Area provinceArea = areaService.findById(Integer.valueOf(provinceId));
			province = provinceArea.getAreaName();
		}
		String city = "";
		if (StringUtils.isNotBlank(cityId)) {
			Area cityArea = areaService.findById(Integer.valueOf(cityId));
			city = cityArea.getAreaName();
		}
		//应前端需要，将省份，城市中的XX省，XX市去除
		if (StringUtils.isNotBlank(province) && province.contains("省")) {
			province = province.replace("省", "");
		}
		//去除直辖市
		if (StringUtils.isNotBlank(province) && province.contains("市")) {
			province = province.replace("市", "");
		}
		//去除自治区
		if (StringUtils.isNotBlank(province) && province.contains("自治区")) {
			province = province.replace("自治区", "");
			//特殊处理内蒙古
			if (province.contains("内蒙古")) {
				province = province.substring(0, 3);
			} else {
				province = province.substring(0, 2);
			}
		}
		if (StringUtils.isNotBlank(city) && city.contains("市")) {
			city = city.replace("市", "");
		}
		map.put("city", city);
		map.put("province", province);
		return map;
	}

	private List<String> betweenTime(int showType, Date beginTime, Date endTime) {
		List<String> list = new ArrayList<>();
		switch (showType) {
			case DAY:
				list = MyDateUtils.getDays(MyDateUtils.formatDate(beginTime), MyDateUtils.formatDate(endTime));
				break;
			case WEEK:
				int weekLen = 7;
				for (int i = 0; i < weekLen; i++) {
					list.add(String.valueOf(i));
				}
				break;
			case MOUTH:
				list = MyDateUtils.getMonths(MyDateUtils.formatDate(beginTime), MyDateUtils.formatDate(endTime));
				break;
			case HOUR:
			default:
				int len = 24;
				for (int i = 0; i < len; i++) {
					list.add(i > 9 ? String.valueOf(i) : "0" + i);
				}
				break;
		}
		return list;
	}

	private String getPattern(int showType) {
		String s = null;
		switch (showType) {
			case DAY:
			case WEEK:
				s = MyDateUtils.COM_Y_M_D_PATTERN;
				break;
			case MOUTH:
				s = MyDateUtils.COM_Y_M_PATTERN;
				break;
			case HOUR:
				s = MyDateUtils.COM_H_PATTERN;
			default:
				break;
		}
		return s;
	}

	private Map<Date, Date> groupByMonth(Date startDate, Date endDate) {
		Map<Date, Date> dateMap = new LinkedHashMap<Date, Date>();

		Date firstDay = null;
		Date lastDay = null;

		Calendar dd = Calendar.getInstance();// 定义日期实例
		dd.setTime(startDate);// 设置日期起始时间
		Calendar cale = Calendar.getInstance();

		Calendar c = Calendar.getInstance();
		c.setTime(endDate);

		int startDay = startDate.getDate();
		int endDay = endDate.getDate();
		while (dd.getTime().before(endDate)) {// 判断是否到结束日期
			cale.setTime(dd.getTime());
			if (dd.getTime().equals(startDate)) {
				cale.set(Calendar.DAY_OF_MONTH, dd.getActualMaximum(Calendar.DAY_OF_MONTH));
				lastDay = cale.getTime();
				dateMap.put(startDate, lastDay);
			} else if (dd.get(Calendar.MONTH) == endDate.getMonth() && dd.get(Calendar.YEAR) == c.get(Calendar.YEAR)) {
				cale.set(Calendar.DAY_OF_MONTH, 1);// 取第一天
				firstDay = cale.getTime();
				dateMap.put(firstDay, endDate);

			} else {
				cale.set(Calendar.DAY_OF_MONTH, 1);// 取第一天
				firstDay = cale.getTime();
				cale.set(Calendar.DAY_OF_MONTH, dd.getActualMaximum(Calendar.DAY_OF_MONTH));
				lastDay = cale.getTime();
				dateMap.put(firstDay, lastDay);
			}
			dd.add(Calendar.MONTH, 1);// 进行当前日期月份加1

		}
		if (endDay < startDay) {
			cale.setTime(endDate);
			cale.set(Calendar.DAY_OF_MONTH, 1);// 取第一天
			firstDay = cale.getTime();
			dateMap.put(firstDay, endDate);
		}
		return dateMap;

	}

	public Map<Date, Date> groupByWeek(Date startTime, Date endTime) {
		Map<Date, Date> dateMap = new LinkedHashMap<Date, Date>();
		Calendar cl1 = Calendar.getInstance();
		cl1.setTime(startTime);
		Calendar cl2 = Calendar.getInstance();
		cl2.setTime(endTime);
		Calendar cl3 = Calendar.getInstance();
		if (cl1.get(Calendar.DAY_OF_WEEK) - 1 == 1) {
			cl1.add(Calendar.DAY_OF_MONTH, 6);

		} else {
			cl1.add(Calendar.DAY_OF_MONTH, 8 - cl1.get(Calendar.DAY_OF_WEEK));
		}
		dateMap.put(MyDateUtils.getStartDate(startTime), MyDateUtils.getStartDate(cl1.getTime()));
		while (cl3.getTime().getTime() < cl2.getTime().getTime()) {
			cl1.add(Calendar.DAY_OF_MONTH, 1);
			Date s = cl1.getTime();
			cl1.add(Calendar.DAY_OF_MONTH, 6);
			dateMap.put(MyDateUtils.getStartDate(s), MyDateUtils.getFinallyDate(cl1.getTime()));
			cl3.setTime(cl1.getTime());
			cl3.add(Calendar.DAY_OF_MONTH, 7);
		}
		cl1.add(Calendar.DAY_OF_MONTH, 1);
		Date s = cl1.getTime();
		if (cl1.getTime().equals(endTime)) {
			dateMap.put(MyDateUtils.getStartDate(s), MyDateUtils.getFinallyDate(s));
		} else {
			dateMap.put(MyDateUtils.getStartDate(s), MyDateUtils.getFinallyDate(endTime));
		}
		return dateMap;
	}

	/**
	 * 根据参与人，参与时间，问卷id提取
	 *
	 * @param list 问卷结果集合
	 * @return List
	 */
	private List<SysQuestionnaireAnswer> getReplayObject(List<SysQuestionnaireAnswer> list) {
		List<SysQuestionnaireAnswer> answerList = new ArrayList<>();
		Map<Integer, Map<String, Map<Date, Map<String, List<SysQuestionnaireAnswer>>>>> map = list.parallelStream()
			.filter(o -> o.getQuestionnaireId() != null)
			.filter(o -> StringUtils.isNotBlank(o.getReplayName())).filter(o -> o.getCreateTime() != null)
			.collect(Collectors.groupingBy(SysQuestionnaireAnswer::getQuestionnaireId,
				Collectors.groupingBy(SysQuestionnaireAnswer::getReplayName,
					Collectors.groupingBy(SysQuestionnaireAnswer::getCreateTime,
						Collectors.groupingBy(SysQuestionnaireAnswer::getIp)))));
		//根据参与人参与时间和问卷id分组获取单个参与人的问卷
		for (Map<String, Map<Date, Map<String, List<SysQuestionnaireAnswer>>>> value : map.values()) {
			for (Map<Date, Map<String, List<SysQuestionnaireAnswer>>> dateListMap : value.values()) {
				for (Map<String, List<SysQuestionnaireAnswer>> answers : dateListMap.values()) {
					for (List<SysQuestionnaireAnswer> answersList : answers.values()) {
						answerList.add(answersList.get(0));
					}
				}
			}
		}
		return answerList;
	}

	private long fileSizeToByte(Integer size, String unit) {
		long fileSize = 0;
		if (UNIT_KB.equals(unit)) {
			fileSize = size * 1024;
		} else if (UNIT_MB.equals(unit)) {
			fileSize = size * 1024 * 1024;
		}
		return fileSize;
	}

	private void inputStreamToFile(InputStream ins, File file) {
		try {
			OutputStream os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			ins.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Autowired
	private SysQuestionnaireSubjectService subjectService;
	@Autowired
	private UploadService uploadService;
	@Autowired
	private AddressService addressService;
	@Autowired
	private SysQuestionnaireService questionnaireService;
	@Autowired
	private CaptchaService captchaService;
	@Autowired(required = false)
	private OpenOfficeConverter openOfficeConverter;
	@Autowired
	private RealPathResolver realPathResolver;
	@Autowired
	private AreaService areaService;

}