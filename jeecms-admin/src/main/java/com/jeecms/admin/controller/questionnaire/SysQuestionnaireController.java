/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.questionnaire;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.google.common.collect.ImmutableSet;
import com.jeecms.admin.controller.BaseAdminController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionInfo;
import com.jeecms.common.exception.error.SysOtherErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.common.util.QrCodeUtil;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.questionnaire.constants.QuestionnaireConstant;
import com.jeecms.questionnaire.domain.QuestionnaireCopyDto;
import com.jeecms.questionnaire.domain.SysQuestionnaire;
import com.jeecms.questionnaire.domain.SysQuestionnaireAnswer;
import com.jeecms.questionnaire.domain.SysQuestionnaireSubject;
import com.jeecms.questionnaire.domain.SysQuestionnaireSubjectOption;
import com.jeecms.questionnaire.domain.dto.SysQuestionnaireSaveDto;
import com.jeecms.questionnaire.domain.dto.SysQuestionnaireUpdateDto;
import com.jeecms.questionnaire.domain.vo.QuestionnaireAnswerVo;
import com.jeecms.questionnaire.domain.vo.QuestionnairePicVo;
import com.jeecms.questionnaire.domain.vo.QuestionnaireViewVo;
import com.jeecms.questionnaire.domain.vo.QuestionnaireVo;
import com.jeecms.questionnaire.service.SysQuestionnaireService;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.resource.service.ResourcesSpaceDataService;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.util.SystemContextUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
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
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.jeecms.questionnaire.domain.vo.QuestionnairePicVo.TYPE_BG_IMG;
import static com.jeecms.questionnaire.domain.vo.QuestionnairePicVo.TYPE_HEAD_IMG;

/**
 * 投票调查Controller
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-10-18
 */
@RequestMapping("/questionnaire")
@RestController
public class SysQuestionnaireController extends BaseAdminController<SysQuestionnaire, Integer> {

	@Autowired
	private SysQuestionnaireService service;
	@Autowired
	private ResourcesSpaceDataService spaceDataService;

	@PostConstruct
	public void init() {
		String[] queryParams = {"title_LIKE", "status_EQ_Integer", "beginTime_GTE_Timestamp",
			"endTime_LTE_Timestamp"};
		super.setQueryParams(queryParams);
	}


	/**
	 * 列表分页
	 *
	 * @param request  HttpServletRequest
	 * @param pageable Pageable
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@GetMapping(value = "/page")
	@MoreSerializeField(@SerializeField(clazz = SysQuestionnaire.class, includes = {"id", "title", "createUser",
		"createTime", "status", "checkStatus", "beginTime", "endTime", "answerCount", "previewUrl"}))
	public ResponseInfo page(String title, Integer status, Date beginTime, Date endTime,
							 HttpServletRequest request, @PageableDefault(sort = "createTime",
		direction = Direction.DESC) Pageable pageable) throws GlobalException {
		return new ResponseInfo(service.page(title, status, beginTime, endTime, SystemContextUtils.getSiteId(request), pageable));
	}

	/**
	 * 预览
	 *
	 * @param id      投票调查id
	 * @param request HttpServletRequest
	 * @return ResponseInfo
	 */
	@GetMapping(value = "/preview/{id}")
	@MoreSerializeField({@SerializeField(clazz = QuestionnaireVo.class, includes = {"title", "details", "subjects",
		"bgConfig", "headConfig", "fontConfig", "subConfig"}),
		@SerializeField(clazz = SysQuestionnaireSubjectOption.class, excludes = {"questionnaireSubject"})})
	public ResponseInfo preview(@PathVariable("id") Integer id, HttpServletRequest request) throws GlobalException {
		QuestionnaireVo vo = service.findById(id, SystemContextUtils.getSiteId(request), null);
		return new ResponseInfo(vo);
	}

	/**
	 * 获取详情
	 *
	 * @param id      详情id
	 * @param request HttpServletRequest
	 * @return ResponseInfo
	 */
	@GetMapping(value = "/{id}")
	public ResponseInfo get(@PathVariable("id") Integer id, Boolean type, HttpServletRequest request) throws GlobalException {
		QuestionnaireVo vo = service.findById(id, SystemContextUtils.getSiteId(request), type);
		return new ResponseInfo(vo);
	}

	/**
	 * 添加
	 *
	 * @param result BindingResult
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@PostMapping
	public ResponseInfo save(@RequestBody @Valid SysQuestionnaireSaveDto dto,
							 HttpServletRequest request, BindingResult result) throws GlobalException {
		if (dto.getIpAnswerFrequencyLimit() == null) {
			throw new GlobalException(new SystemExceptionInfo(
				SysOtherErrorCodeEnum.QUESTIONNAIRE_IP_COUNT_REQUIRED.getDefaultMessage(),
				SysOtherErrorCodeEnum.QUESTIONNAIRE_IP_COUNT_REQUIRED.getCode()));
		}
		if (dto.getDeviceAnswerFrequencyLimit() == null) {
			throw new GlobalException(new SystemExceptionInfo(
				SysOtherErrorCodeEnum.QUESTIONNAIRE_DEVICE_COUNT_REQUIRED.getDefaultMessage(),
				SysOtherErrorCodeEnum.QUESTIONNAIRE_DEVICE_COUNT_REQUIRED.getCode()));
		}
		validateBindingResult(result);
		SysQuestionnaire questionnaire = service.save(dto, SystemContextUtils.getSite(request));
		return new ResponseInfo(new QuestionnaireViewVo(questionnaire.getId(), questionnaire.getPreviewUrl()));
	}

	/**
	 * 获取投票问卷页眉图和背景图列表
	 *
	 * @return ResponseInfo
	 */
	@GetMapping("/pic")
	public ResponseInfo getPic() {
		QuestionnairePicVo pic = new QuestionnairePicVo();
		List<ResourcesSpaceData> dataList = spaceDataService.findByQuestionnairePic();
		List<QuestionnairePicVo.ImgBean> bgImgs = new ArrayList<QuestionnairePicVo.ImgBean>();
		List<QuestionnairePicVo.ImgBean> headImgs = new ArrayList<QuestionnairePicVo.ImgBean>();
		for (ResourcesSpaceData data : dataList) {
			QuestionnairePicVo.ImgBean imgBean = new QuestionnairePicVo.ImgBean();
			imgBean.setPicId(data.getId());
			imgBean.setPicUrl(data.getUrl());
			imgBean.setThumbnail(data.getAlias());
			if (TYPE_BG_IMG.equals(data.getType())) {
				bgImgs.add(imgBean);
			} else if (TYPE_HEAD_IMG.equals(data.getType())) {
				headImgs.add(imgBean);
			}
		}
		pic.setBgImg(bgImgs);
		pic.setHeadImg(headImgs);
		return new ResponseInfo(pic);
	}

	/**
	 * 修改
	 *
	 * @param result BindingResult
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@PutMapping
	public ResponseInfo update(@RequestBody @Valid SysQuestionnaireUpdateDto dto,
							   BindingResult result, HttpServletRequest request) throws GlobalException {
		if (dto.getIpAnswerFrequencyLimit() == null) {
			throw new GlobalException(new SystemExceptionInfo(
				SysOtherErrorCodeEnum.QUESTIONNAIRE_IP_COUNT_REQUIRED.getDefaultMessage(),
				SysOtherErrorCodeEnum.QUESTIONNAIRE_IP_COUNT_REQUIRED.getCode()));
		}
		if (dto.getDeviceAnswerFrequencyLimit() == null) {
			throw new GlobalException(new SystemExceptionInfo(
				SysOtherErrorCodeEnum.QUESTIONNAIRE_DEVICE_COUNT_REQUIRED.getDefaultMessage(),
				SysOtherErrorCodeEnum.QUESTIONNAIRE_DEVICE_COUNT_REQUIRED.getCode()));
		}
		validateBindingResult(result);
		SysQuestionnaire questionnaire = service.update(dto, SystemContextUtils.getSite(request));
		return new ResponseInfo(new QuestionnaireViewVo(questionnaire.getId(), questionnaire.getPreviewUrl()));
	}

	/**
	 * 复制
	 *
	 * @param dto     问卷复制Dto(id和标题)
	 * @param request {@link HttpServletRequest}
	 * @param result  {@link BindingResult}
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@PostMapping("/copy")
	public ResponseInfo copy(@RequestBody @Valid QuestionnaireCopyDto dto, HttpServletRequest request, BindingResult result) throws GlobalException {
		validateBindingResult(result);
		validateId(dto.getId());
		service.copy(dto.getId(), dto.getTitle(), SystemContextUtils.getSite(request));
		return new ResponseInfo();
	}

	/**
	 * 获取问卷链接信息
	 *
	 * @param id 问卷id
	 * @return ResponseInfo
	 */
	@MoreSerializeField({@SerializeField(clazz = SysQuestionnaire.class, includes = {"qrCodeUrl",
		"url", "embedded", "iframe", "title", "details", "shareLogoUrl"})})
	@GetMapping("/link/{id}")
	public ResponseInfo link(@PathVariable("id") Integer id) {
		return new ResponseInfo(service.findById(id));
	}

	/**
	 * 获取问卷统计数据
	 *
	 * @param questionnaireId 问卷id
	 * @return ResponseInfo
	 */
	@GetMapping(value = "/find")
	public ResponseInfo getQuestionnaire(Integer questionnaireId) {
		SysQuestionnaire q = service.findById(questionnaireId);
		QuestionnaireAnswerVo vo = new QuestionnaireAnswerVo(q.getId(), q.getTitle(), q.getPageViews(), q.getAnswerCount(),
			q.getStatus(), q.getBeginTime(), q.getEndTime());
		return new ResponseInfo(vo);
	}

	/**
	 * 删除
	 *
	 * @param dels 删除及批量删除dto
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@DeleteMapping
	@Override
	public ResponseInfo delete(@RequestBody @Valid DeleteDto dels, BindingResult result) throws GlobalException {
		validateBindingResult(result);
		return super.deleteBeatch(dels.getIds());
	}

	/**
	 * 提交审核
	 *
	 * @param deleteDto 投票调查id数组
	 * @param result    {@link BindingResult}
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@PutMapping(value = "/submit")
	public ResponseInfo submit(@RequestBody @Valid DeleteDto deleteDto, BindingResult result, HttpServletRequest request) throws GlobalException {
		validateBindingResult(result);
		Integer siteId = SystemContextUtils.getSiteId(request);
		service.updateStatus(deleteDto.getIds(), QuestionnaireConstant.STATUS_IN_CIRCULATION, siteId);
		return new ResponseInfo(true);
	}

	/**
	 * 发布
	 *
	 * @param deleteDto 投票调查id数组
	 * @param result    {@link BindingResult}
	 * @param request   {@link HttpServletRequest}
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@PutMapping(value = "/release")
	public ResponseInfo release(@RequestBody @Valid DeleteDto deleteDto, BindingResult result, HttpServletRequest request) throws GlobalException {
		validateBindingResult(result);
		Integer siteId = SystemContextUtils.getSiteId(request);
		service.updateStatus(deleteDto.getIds(), QuestionnaireConstant.STATUS_PROCESSING, siteId);
		return new ResponseInfo(true);
	}

	/**
	 * 暂停
	 *
	 * @param deleteDto 投票调查id数组
	 * @param result    {@link BindingResult}
	 * @param request   {@link HttpServletRequest}
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@PutMapping(value = "/pause")
	public ResponseInfo pause(@RequestBody @Valid DeleteDto deleteDto, BindingResult result, HttpServletRequest request) throws GlobalException {
		validateBindingResult(result);
		Integer siteId = SystemContextUtils.getSiteId(request);
		service.updateStatus(deleteDto.getIds(), QuestionnaireConstant.STATUS_NO_REVIEW, siteId);
		return new ResponseInfo(true);
	}

	/**
	 * 清空答案
	 *
	 * @param id      投票调查id
	 * @param request {@link HttpServletRequest}
	 * @return ResponseInfo
	 */
	@GetMapping("/answer/clean")
	public ResponseInfo cleanAnswer(Integer id, HttpServletRequest request) throws GlobalException {
		validateId(id);
		service.cleanAnswer(id, SystemContextUtils.getSiteId(request));
		return null;
	}

	// TODO: 2019/11/8 暂时不需要此接口 ,此接口需要调整

	/**
	 * 导出答案
	 *
	 * @param id       投票调查id
	 * @param request  {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 */
	@RequestMapping("/answer/export")
	public void exportAnswer(@RequestParam("id") Integer id, HttpServletRequest request, HttpServletResponse response) {
		SysQuestionnaire q = service.findByIdAndSiteId(id, SystemContextUtils.getSiteId(request));
		List<SysQuestionnaireAnswer> answers = q.getAnswers();
		List<SysQuestionnaireSubject> subjects = q.getSubjects();
		if (answers == null) {
			return;
		}

		ExportParams exportParams = new ExportParams();
		String taskName = q.getTitle();
		exportParams.setSheetName(taskName);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Set<String> set = ImmutableSet.of("参与人", "参与时间", "网络设备", "IP地址", "地域", "是否有效");
		for (SysQuestionnaireAnswer answer : answers) {
			Map<String, String> map = answer.getAttr();
			map.put("参与人", answer.getReplayName());
			map.put("参与时间", MyDateUtils.formatDate(answer.getCreateTime(), MyDateUtils.COM_Y_M_D_H_M_S_PATTERN));
			map.put("网络设备", answer.getDevice());
			map.put("IP地址", answer.getIp());
			map.put("地域", answer.getProvince() + answer.getCity());
			map.put("是否有效", answer.getIsEffective() != null && answer.getIsEffective() ? "是" : "否");
			list.add(map);
		}
		List<ExcelExportEntity> entityList = new ArrayList<ExcelExportEntity>();
		for (String s : set) {
			for (String key : list.get(0).keySet()) {
				if (s.equals(key)) {
					ExcelExportEntity exportEntity = new ExcelExportEntity(key, key, 50);
					//设置是否换行
					exportEntity.setWrap(true);
					entityList.add(exportEntity);
					break;
				}
			}
		}
		for (SysQuestionnaireSubject subject : subjects) {
			String title = subject.getTitle();
			Integer subjectId = subject.getId();
			ExcelExportEntity exportEntity = new ExcelExportEntity(title, String.valueOf(subjectId), 50);
			//设置是否换行
			exportEntity.setWrap(true);
			entityList.add(exportEntity);
		}

		Workbook workbook = ExcelExportUtil.exportExcel(exportParams, entityList, list);
		response.setContentType("application/x-download;charset=UTF-8");
		try {
			RequestUtils.setDownloadHeader(response, new String(taskName.getBytes(), "ISO8859-1") + ".xls");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		FileOutputStream fos = null;
		InputStream input = null;
		OutputStream output = null;
		try {
			fos = new FileOutputStream("/" + taskName + ".xls");
			workbook.write(fos);
			File file = new File("/" + taskName + ".xls");
			input = new FileInputStream(file);
			output = response.getOutputStream();
			byte[] buff = new byte[1024];
			int len;
			while ((len = input.read(buff)) > -1) {
				output.write(buff, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 下载二维码图片
	 *
	 * @param size     大小
	 * @param id       问卷id
	 * @param response response
	 */
	@PostMapping("/qrcode")
	public void downloadQrcodeImage(@RequestParam("size") Integer size, @RequestParam("id") Integer id,
									HttpServletResponse response) throws GlobalException {
		validateId(id);
		SysQuestionnaire questionnaire = service.findById(id);
		String str = questionnaire.getUrl();
		ServletOutputStream out = null;
		InputStream is = null;
		ImageOutputStream imOut = null;
		ByteArrayOutputStream bs = null;
		try {
			out = response.getOutputStream();
			// 创建二维码
			BufferedImage bi = QrCodeUtil.createQrCode(str, size);
			// write the data out
			bs = new ByteArrayOutputStream();
			imOut = ImageIO.createImageOutputStream(bs);
			ImageIO.write(bi, "jpeg", imOut);

			is = new ByteArrayInputStream(bs.toByteArray());
			// 实现文件下载
			String filename = "二维码.jpeg";
			// 配置文件下载
			response.setHeader("content-type", "application/octet-stream");
			response.setContentType("application/octet-stream");
			// 下载文件能正常显示中文
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
			if (is != null) {
				byte[] buff = new byte[1024];
				int len;
				while ((len = is.read(buff)) != -1) {
					out.write(buff, 0, len);
				}
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (imOut != null) {
				try {
					imOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bs != null) {
				try {
					bs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 判断投票调查是否配置工作流
	 *
	 * @param request {@link HttpServletRequest}
	 * @return ResponseInfo
	 */
	@GetMapping("/workflow")
	public ResponseInfo getWorkflow(HttpServletRequest request) {
		CmsSite site = SystemContextUtils.getSite(request);
		Integer workflow = site.getConfig().getSurveyConfigurationId();
		if (workflow != null) {
			return new ResponseInfo(true);
		} else {
			return new ResponseInfo(false);
		}
	}

}


