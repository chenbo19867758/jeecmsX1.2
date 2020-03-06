/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.questionnaire;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.alibaba.fastjson.JSONArray;
import com.google.gson.internal.LinkedHashTreeMap;
import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.common.util.Zipper;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.questionnaire.constants.QuestionnaireConstant;
import com.jeecms.questionnaire.domain.SysQuestionnaireAnswer;
import com.jeecms.questionnaire.domain.dto.QuestionnaireFileDto;
import com.jeecms.questionnaire.domain.vo.QuestionnaireExportListVo;
import com.jeecms.questionnaire.domain.vo.QuestionnairePieVo;
import com.jeecms.questionnaire.domain.vo.QuestionnaireSubjectGroupVo;
import com.jeecms.questionnaire.service.SysQuestionnaireAnswerService;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.util.SystemContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 问卷结果控制层
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-10-18
 */
@RequestMapping("/questionnaireAnswer")
@RestController
public class SysQuestionnaireAnswerController extends BaseController<SysQuestionnaireAnswer, Integer> {

	private static final Logger log = LoggerFactory.getLogger(SysQuestionnaireAnswerController.class);

	private static final int LIST_SIZE = 10000;

	@Autowired
	private SysQuestionnaireAnswerService service;
	@Autowired
	private RealPathResolver realPathResolver;

	@PostConstruct
	public void init() {
		String[] queryParams = {};
		super.setQueryParams(queryParams);
	}

	/**
	 * 结果分析-按用户
	 *
	 * @param isEffective     是否有效
	 * @param province        省
	 * @param city            市
	 * @param device          设备
	 * @param deviceType      设备类型
	 * @param ip              IP
	 * @param replayName      参与人
	 * @param beginTime       开始时间
	 * @param endTime         结束时间
	 * @param questionnaireId 问卷id
	 * @param request         {@link HttpServletRequest}
	 * @param orderBy         排序
	 * @param pageable        {@link Pageable}
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@SerializeField(clazz = SysQuestionnaireAnswer.class, includes = {"id", "replayName", "createTime", "device",
		"province", "city", "ip", "answer", "isEffective", "isFile"})
	@GetMapping(value = "/page")
	public ResponseInfo page(Boolean isEffective, String province, String city, String device,
							 Integer deviceType, String replayName, String ip, Date beginTime,
							 Date endTime, @RequestParam("questionnaireId") Integer questionnaireId,
							 Integer orderBy, HttpServletRequest request, @PageableDefault(
		sort = "createTime", direction = Direction.DESC) Pageable pageable) throws GlobalException {
		Integer siteId = SystemContextUtils.getSiteId(request);
		Page<SysQuestionnaireAnswer> page = service.getPage(isEffective, province, city, orderBy,
			device, deviceType, ip, replayName, beginTime, endTime, siteId, questionnaireId, pageable);
		return new ResponseInfo(page);
	}

	/**
	 * 结果分析-按题目列表
	 *
	 * @param questionnaireId 问卷id
	 * @param request         {@link HttpServletRequest}
	 * @return ResponseInfo
	 */
	@GetMapping("/subject/list")
	public ResponseInfo subjectList(@RequestParam("questionnaireId") Integer questionnaireId,
									HttpServletRequest request) {
		Integer siteId = SystemContextUtils.getSiteId(request);
		List<QuestionnaireSubjectGroupVo> vos = service.groupBySubject(questionnaireId, siteId);
		return new ResponseInfo(vos);
	}

	/**
	 * 结果分析-按题目分页列表
	 *
	 * @param subjectId       题目id
	 * @param province        省
	 * @param city            市
	 * @param device          设备
	 * @param deviceType      设备类型
	 * @param ip              IP
	 * @param replayName      参与人
	 * @param beginTime       开始时间
	 * @param endTime         结束时间
	 * @param questionnaireId 问卷id
	 * @param request         {@link HttpServletRequest}
	 * @param pageable        {@link Pageable}
	 * @return ResponseInfo
	 */
	@SerializeField(clazz = SysQuestionnaireAnswer.class, includes = {"replayName", "createTime",
		"device", "province", "city", "ip", "answer"})
	@GetMapping("/subject/page")
	public ResponseInfo pageBySubject(@RequestParam("subjectId") Integer subjectId, String province, String city,
									  String device, Integer deviceType, String ip, String replayName, Date beginTime,
									  Date endTime, @RequestParam("questionnaireId") Integer questionnaireId,
									  Integer orderBy, String options, HttpServletRequest request, @PageableDefault(
		sort = "createTime", direction = Direction.DESC) Pageable pageable) {
		Integer siteId = SystemContextUtils.getSiteId(request);
		Page<SysQuestionnaireAnswer> page = service.getPageBySubject(province, city, device, deviceType,
			ip, replayName, beginTime, endTime, subjectId, orderBy, siteId, questionnaireId, options, pageable);
		return new ResponseInfo(page);
	}

	/**
	 * 题目-饼图
	 *
	 * @param subjectId       题目id
	 * @param questionnaireId 问卷id
	 * @param request         {@link HttpServletRequest}
	 * @return ResponseInfo
	 */
	@GetMapping("/subject/pieChart")
	public ResponseInfo pieChart(@RequestParam("subjectId") Integer subjectId,
								 @RequestParam("questionnaireId") Integer questionnaireId,
								 HttpServletRequest request) {
		List<QuestionnairePieVo> list = service.pieChart(questionnaireId, subjectId, SystemContextUtils.getSiteId(request));
		return new ResponseInfo(list);
	}

	/**
	 * 获取详情
	 *
	 * @param id 问卷结果id
	 * @return ResponseInfo
	 * @throws GlobalException 全局异常
	 */
	@GetMapping(value = "/{id}")
	@Override
	public ResponseInfo get(@PathVariable("id") Integer id) throws GlobalException {
		validateId(id);
		return new ResponseInfo(service.getById(id));
	}

	/**
	 * 标记有效
	 *
	 * @param deleteDto ids
	 * @param result    {@link BindingResult}
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@PutMapping("/valid")
	public ResponseInfo valid(@RequestBody @Valid DeleteDto deleteDto, BindingResult result) throws GlobalException {
		validateBindingResult(result);
		List<SysQuestionnaireAnswer> list = service.markStatus(deleteDto.getIds(), true);
		return new ResponseInfo(true);
	}

	/**
	 * 标记无效
	 *
	 * @param deleteDto ids
	 * @param result    {@link BindingResult}
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@PutMapping("/invalid")
	public ResponseInfo invalid(@RequestBody @Valid DeleteDto deleteDto, BindingResult result) throws GlobalException {
		validateBindingResult(result);
		service.markStatus(deleteDto.getIds(), false);
		return new ResponseInfo(true);
	}

	/**
	 * 删除
	 *
	 * @param dels 删除ids
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@DeleteMapping
	@Override
	public ResponseInfo delete(@RequestBody @Valid DeleteDto dels, BindingResult result) throws GlobalException {
		validateBindingResult(result);
		service.deleteByIds(dels.getIds());
		return new ResponseInfo(true);
	}

	/**
	 * 导出 已用户为单位
	 *
	 * @param isEffective     是否有效 true 有效
	 * @param province        省
	 * @param city            市
	 * @param device          设备
	 * @param deviceType      设备类型
	 * @param orderBy         排序
	 * @param ip              ip
	 * @param replayName      参与人
	 * @param beginTime       开始时间
	 * @param endTime         结束时间
	 * @param questionnaireId 问卷id
	 * @param request         {@link HttpServletRequest}
	 * @param response        {@link HttpServletResponse}
	 */
	@PostMapping("/export")
	public ResponseInfo export(Boolean isEffective, String province, String city, String device, Integer orderBy,
							   Integer deviceType, String ip, String replayName, Date beginTime, Date endTime,
							   @RequestParam("questionnaireId") Integer questionnaireId, Integer page, Integer size,
							   HttpServletRequest request, HttpServletResponse response) {
		Integer siteId = SystemContextUtils.getSiteId(request);
		List<QuestionnaireExportListVo> list = service.getExportList(isEffective, province, city, device,
			deviceType, ip, replayName, beginTime, endTime, orderBy, siteId, questionnaireId, page, size);
		String name = "问卷结果";
		if (list != null && list.size() > 0) {
			name = list.get(0).getTitle();
		}
		if (list == null) {
			list = new ArrayList<QuestionnaireExportListVo>();
		}
		ExportParams exportParams = new ExportParams();
		exportParams.setSheetName(name);
		response.setContentType("application/x-download;charset=UTF-8");
		FileOutputStream fos = null;
		InputStream input = null;
		OutputStream output = null;
		List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
		List<ExcelExportEntity> entityList = new ArrayList<ExcelExportEntity>();
		List<String> fileUrls = new ArrayList<String>();
		Boolean isFile = false;
		for (QuestionnaireExportListVo answer : list) {
			Map<String, String> map = new LinkedHashTreeMap<>();
			fileUrls.addAll(answer.getFileUrls());
			map.put("参与人", answer.getReplayName());
			map.put("参与时间", MyDateUtils.formatDate(answer.getCreateTime(), MyDateUtils.COM_Y_M_D_H_M_S_PATTERN));
			map.put("网络设备", answer.getDevice());
			map.put("IP地址", answer.getIp());
			map.put("地域", answer.getAddress());
			map.put("是否有效", answer.getEffective() != null && answer.getEffective() ? "是" : "否");
			Map<String, String> attr = answer.getAttr();
			map.putAll(attr);
			mapList.add(map);
			if (answer.getIsFile()) {
				isFile = true;
			}
		}
		for (String key : mapList.get(0).keySet()) {
			ExcelExportEntity exportEntity = new ExcelExportEntity(key, key, 50);
			//设置是否换行
			exportEntity.setWrap(true);
			entityList.add(exportEntity);
		}
		File file = null;
		Workbook workbook = ExcelExportUtil.exportBigExcel(exportParams, entityList, mapList);
		try {
			fos = new FileOutputStream(WebConstants.SPT + name + ".xlsx");
			workbook.write(fos);
			file = new File(WebConstants.SPT + name + ".xlsx");
			if (isFile) {

				List<Zipper.FileEntry> fileEntries = new ArrayList<Zipper.FileEntry>();
				fileEntries.add(new Zipper.FileEntry("", "", file));
				//打包资源
				if (fileUrls.size() > 0) {
					for (String resource : fileUrls) {
						resource = realPathResolver.get(resource);
						File temp = new File(resource);
						fileEntries.add(new Zipper.FileEntry("", "", temp));
					}
				}
				
				RequestUtils.setDownloadHeader(response, new String(name.getBytes(), "ISO8859-1") + ".zip");
				Zipper.zip(response.getOutputStream(), fileEntries, "UTF-8");
			} else {
				//不是附件 直接下载xlsx
				RequestUtils.setDownloadHeader(response, new String(name.getBytes(), "ISO8859-1") + ".xlsx");
				input = new FileInputStream(file);
				output = response.getOutputStream();
				byte[] buff = new byte[1024];
				int len = 0;
				while ((len = input.read(buff)) > -1) {
					output.write(buff, 0, len);
				}
				/*fos = response.getOutputStream();
				workbook.write(fos);*/
			}
		} catch (IOException e) {
			return new ResponseInfo(false);
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage());
			}
			try {
				if (output != null) {
					output.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage());
			}
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage());
			}
			ExcelExportUtil.closeExportBigExcel();
			if (file != null) {
				file.delete();
			}
		}
		return null;
	}

	/**
	 * 导出问卷结果-题目分类
	 *
	 * @param subjectId       题目id
	 * @param province        省
	 * @param city            市
	 * @param device          设备
	 * @param deviceType      设备类型
	 * @param ip              ip
	 * @param replayName      参与人
	 * @param beginTime       开始时间
	 * @param endTime         结束时间
	 * @param questionnaireId 问卷id
	 * @param request         {@link HttpServletRequest}
	 * @param response        {@link HttpServletResponse}
	 * @param pageable        分页组件
	 */
	@RequestMapping("/subject/export")
	public void exportSubject(@RequestParam("subjectId") Integer subjectId, String province, String city,
							  String device, Integer deviceType, String ip, String replayName, Date beginTime,
							  Date endTime, @RequestParam("questionnaireId") Integer questionnaireId, Integer ordderBy,
							  HttpServletRequest request, HttpServletResponse response, String options,
							  @PageableDefault(sort = "createTime", direction = Direction.DESC) Pageable pageable) {
		CmsSite site = SystemContextUtils.getSite(request);
		Page<SysQuestionnaireAnswer> page = service.getPageBySubject(province, city, device, deviceType, ip,
			replayName, beginTime, endTime, subjectId, ordderBy, site.getId(), questionnaireId, options, pageable);
		List<SysQuestionnaireAnswer> list = page.getContent();
		String name = "问卷结果";
		String title = "问卷结果";
		if (list.size() > 0) {
			name = list.get(0).getQuestionnaire().getTitle();
			title = list.get(0).getSubject().getTitle();
		}
		//Workbook workbook = null;
		ExportParams exportParams = new ExportParams();
		exportParams.setSheetName(title);
		List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
		List<ExcelExportEntity> entityList = new ArrayList<ExcelExportEntity>();
		for (SysQuestionnaireAnswer answer : list) {
			Map<String, String> map = new LinkedHashTreeMap<>();
			map.put("参与人", answer.getReplayName());
			map.put("参与时间", MyDateUtils.formatDate(answer.getCreateTime(), MyDateUtils.COM_Y_M_D_H_M_S_PATTERN));
			map.put("网络设备", answer.getDevice());
			map.put("IP地址", answer.getIp());
			map.put("地域", answer.getAddress());
			map.put("是否有效", answer.getIsEffective() != null && answer.getIsEffective() ? "是" : "否");
			if (QuestionnaireConstant.SUBJECT_TYPE_FILE.equals(answer.getSubject().getType())) {
				String s = answer.getAnswer();
				List<String> stringList = new ArrayList<String>();
				List<QuestionnaireFileDto> dtos = JSONArray.parseArray(s, QuestionnaireFileDto.class);
				for (QuestionnaireFileDto fileDto : dtos) {
					stringList.add(fileDto.getName());
				}
				map.put(answer.getSubject().getTitle(), StringUtils.join(stringList, WebConstants.ARRAY_SPT));
			} else {
				map.put(answer.getSubject().getTitle(), answer.getAnswer());
			}
			mapList.add(map);
		}
		for (String key : mapList.get(0).keySet()) {
			ExcelExportEntity exportEntity = new ExcelExportEntity(key, key, 50);
			//设置是否换行
			exportEntity.setWrap(true);
			entityList.add(exportEntity);
		}
		Workbook workbook = ExcelExportUtil.exportBigExcel(exportParams, entityList, mapList);
		response.setContentType("application/x-download;charset=UTF-8");
		FileOutputStream fos = null;
		InputStream input = null;
		OutputStream output = null;
		File file = null;
		try {
			fos = new FileOutputStream(WebConstants.SPT + name + ".xlsx");
			workbook.write(fos);
			file = new File(WebConstants.SPT + name + ".xlsx");

			//如果是附件题 需要下载附件
			if (QuestionnaireConstant.SUBJECT_TYPE_FILE.equals(list.get(0).getSubject().getType())) {
				List<String> resources = new ArrayList<String>();
				for (SysQuestionnaireAnswer answer : list) {
					if (QuestionnaireConstant.SUBJECT_TYPE_FILE.equals(answer.getSubject().getType())) {
						List<QuestionnaireFileDto> fileDtos = JSONArray.parseArray(answer.getAnswer(), QuestionnaireFileDto.class);
						for (QuestionnaireFileDto fileDto : fileDtos) {
							resources.add(fileDto.getUrl());
						}
					}
				}
				List<Zipper.FileEntry> fileEntries = new ArrayList<Zipper.FileEntry>();
				fileEntries.add(new Zipper.FileEntry("", "", file));
				//打包资源
				for (String resource : resources) {
					resource = realPathResolver.get(resource);
					File temp = new File(resource);
					fileEntries.add(new Zipper.FileEntry("", "", temp));
				}
				response.addHeader("Content-disposition", "filename=download.zip");
				Zipper.zip(response.getOutputStream(), fileEntries, "UTF-8");
			} else {
				//不是附件 直接下载xlsx
				RequestUtils.setDownloadHeader(response, new String(name.getBytes(), "ISO8859-1") + ".xlsx");
				input = new FileInputStream(file);
				output = response.getOutputStream();
				byte[] buff = new byte[1024];
				int len = 0;
				while ((len = input.read(buff)) > -1) {
					output.write(buff, 0, len);
				}
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage());
			}
			try {
				if (output != null) {
					output.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage());
			}
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage());
			}
			ExcelExportUtil.closeExportBigExcel();
		}
		if (file != null) {
			file.delete();
		}
	}

	/**
	 * 设备分析
	 *
	 * @param questionnaireId 问卷id
	 * @param request         {@link HttpServletRequest}
	 * @return ResponseInfo
	 */
	@RequestMapping("/chart/device")
	public ResponseInfo deviceChart(Integer questionnaireId, HttpServletRequest request) {
		List<QuestionnairePieVo> vos = service.devicesPieChart(questionnaireId, SystemContextUtils.getSiteId(request));
		return new ResponseInfo(vos);
	}

	/**
	 * 地图
	 *
	 * @param questionnaireId 问卷id
	 * @param request         {@link HttpServletRequest}
	 * @return ResponseInfo
	 */
	@RequestMapping("/chart/map")
	public ResponseInfo mapChart(Integer questionnaireId, HttpServletRequest request) {
		List<QuestionnairePieVo> vos = service.statisticsMapChart(questionnaireId, SystemContextUtils.getSiteId(request));
		return new ResponseInfo(vos);
	}

	/**
	 * 趋势分析
	 *
	 * @param questionnaireId 问卷id
	 * @param device          设备
	 * @param deviceType      设备类型 1pc 2移动
	 * @param province        省
	 * @param city            市
	 * @param showType        显示方式 1时2天3周4月
	 * @param beginTime       开始时间
	 * @param endTime         结束时间
	 * @param request         {@link HttpServletRequest}
	 * @return JSONArray
	 */
	@RequestMapping("/chart/trend")
	public ResponseInfo trend(Integer questionnaireId, String device, Integer deviceType, String province, String city,
							  Integer showType, Date beginTime, Date endTime, HttpServletRequest request) {
		Integer siteId = SystemContextUtils.getSiteId(request);
		showType = showType == null ? 1 : showType;
		JSONArray vos = service.statisticsAreaChart(questionnaireId, device, deviceType,
			province, city, showType, beginTime, endTime, siteId);
		return new ResponseInfo(vos);
	}
}



