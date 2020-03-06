/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.system;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.base.domain.DragSortDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionEnum;
import com.jeecms.common.exception.error.SysOtherErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.ContentMark;
import com.jeecms.system.domain.dto.ContentMarkBatchDto;
import com.jeecms.system.domain.dto.ContentMarkDto;
import com.jeecms.system.service.ContentMarkService;
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
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 发文字号管理Controller
 *
 * @author: xiaohui
 * @version: 1.0
 * @date 2019-05-21
 */
@RequestMapping("/contentMarks")
@RestController
public class ContentMarkController extends BaseController<ContentMark, Integer> {

	@Autowired
	private ContentMarkService service;

	@PostConstruct
	public void init() {
		String[] queryParams = {};
		super.setQueryParams(queryParams);
	}


	/**
	 * 机关代字分页
	 *
	 * @Title: 列表分页
	 * @param: @param request
	 * @param: @param pageable
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@GetMapping(value = "/agency/page")
	@SerializeField(clazz = ContentMark.class, includes = {"id", "markName", "sortNum", "createUser", "createTime"})
	public ResponseInfo pageAgency(HttpServletRequest request, @PageableDefault(sort = "sortNum",
			direction = Direction.ASC) Pageable pageable) {
		Map<String, String[]> params = new HashMap<String, String[]>(2);
		String markName = request.getParameter("markName");
		params.put("EQ_markType_Integer", new String[]{ContentMark.MARK_TYPE_AGENCY.toString()});
		params.put("LIKE_markName_String", new String[]{markName});
		Page<ContentMark> page = service.getPage(params, pageable, false);
		return new ResponseInfo(page);
	}

	/**
	 * 年号列表分页
	 *
	 * @Title: 列表分页
	 * @param: @param request
	 * @param: @param pageable
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@GetMapping(value = "/year/page")
	@SerializeField(clazz = ContentMark.class, includes = {"id", "markName", "sortNum", "createUser", "createTime"})
	public ResponseInfo pageYear(HttpServletRequest request, @PageableDefault(sort = "sortNum",
			direction = Direction.ASC) Pageable pageable) {
		Map<String, String[]> params = new HashMap<String, String[]>(2);
		String markName = request.getParameter("markName");
		params.put("EQ_markType_Integer", new String[]{ContentMark.MARK_TYPE_YEAR.toString()});
		params.put("LIKE_markName_String", new String[]{markName});
		return new ResponseInfo(service.getPage(params, pageable, false));
	}

	/**
	 * 获取机关代字详情
	 *
	 * @Title: 获取详情
	 * @param: @param id
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@GetMapping(value = "/agency/{id}")
	@SerializeField(clazz = ContentMark.class, includes = {"id", "markName", "createUser", "createTime"})
	public ResponseInfo getAgency(@PathVariable("id") Integer id) {
		return new ResponseInfo(service.findByIdAndMarkType(id, ContentMark.MARK_TYPE_AGENCY));
	}

	/**
	 * 获取年号详情
	 *
	 * @Title: 获取详情
	 * @param: @param id
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@GetMapping(value = "/year/{id}")
	@SerializeField(clazz = ContentMark.class, includes = {"id", "markName", "createUser", "createTime"})
	public ResponseInfo getYear(@PathVariable("id") Integer id) {
		return new ResponseInfo(service.findByIdAndMarkType(id, ContentMark.MARK_TYPE_YEAR));
	}

	/**
	 * 校验机关代字是否唯一
	 *
	 * @param markName 机关代字
	 * @param id       id
	 * @return true 唯一 false 不唯一
	 */
	@GetMapping("/agency/unique")
	public ResponseInfo uniqueAgency(String markName, Integer id) {
		if (service.checkByMarkName(markName, ContentMark.MARK_TYPE_AGENCY, id)) {
			return new ResponseInfo(true);
		} else {
			return new ResponseInfo(false);
		}
	}

	/**
	 * 校验年号是否唯一
	 *
	 * @param markName 年号
	 * @param id       id
	 * @return true 唯一 false 不唯一
	 */
	@GetMapping("/year/unique")
	public ResponseInfo uniqueYear(String markName, Integer id) {
		if (service.checkByMarkName(markName, ContentMark.MARK_TYPE_YEAR, id)) {
			return new ResponseInfo(true);
		} else {
			return new ResponseInfo(false);
		}
	}

	/**
	 * 添加机关代字
	 *
	 * @Title: 添加
	 * @param: @param result
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@PostMapping("/agency")
	public ResponseInfo saveAgency(@RequestBody @Valid ContentMarkDto contentMark,
								   BindingResult result) throws GlobalException {
		validateBindingResult(result);
		service.saveBatch(contentMark.getMarkName(), ContentMark.MARK_TYPE_AGENCY);
		return new ResponseInfo();
	}

	/**
	 * 添加年号
	 *
	 * @Title: 添加
	 * @param: @param result
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@PostMapping("/year")
	public ResponseInfo saveYear(@RequestBody @Valid ContentMarkDto contentMark,
								 BindingResult result) throws GlobalException {
		validateBindingResult(result);
		service.saveBatch(contentMark.getMarkName(), ContentMark.MARK_TYPE_YEAR);
		return new ResponseInfo();
	}

	/**
	 * 修改机关代字
	 *
	 * @Title: 修改
	 * @param: @param result
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@PutMapping("/agency")
	public ResponseInfo updateAgency(@RequestBody @Valid ContentMark contentMark,
									 BindingResult result) throws GlobalException {
		validateId(contentMark.getId());
		validateBindingResult(result);
		if (!service.checkByMarkName(contentMark.getMarkName(), ContentMark.MARK_TYPE_AGENCY,
				contentMark.getId())) {
			return new ResponseInfo(SysOtherErrorCodeEnum.AGENCY_WORD_ALREADY_EXIST.getCode(),
					SysOtherErrorCodeEnum.AGENCY_WORD_ALREADY_EXIST.getDefaultMessage());
		}
		ContentMark mark = service.findByIdAndMarkType(contentMark.getId(), ContentMark.MARK_TYPE_AGENCY);
		if (mark == null) {
			return new ResponseInfo(SystemExceptionEnum.DOMAIN_NOT_FOUND_ERROR.getCode(),
					SystemExceptionEnum.DOMAIN_NOT_FOUND_ERROR.getDefaultMessage());
		}
		mark.setMarkName(contentMark.getMarkName());
		service.update(mark);
		return new ResponseInfo();
	}

	/**
	 * 批量录入年份
	 *
	 * @param dto    年号批量录入Dto
	 * @param result BindingResult
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@PostMapping("/year/entry")
	public ResponseInfo entry(@RequestBody @Valid ContentMarkBatchDto dto,
							  BindingResult result) throws GlobalException {
		validateBindingResult(result);
		service.entryBatch(dto);
		return new ResponseInfo();
	}

	/**
	 * 修改年号
	 *
	 * @Title: 修改
	 * @param: @param result
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@PutMapping("/year")
	public ResponseInfo updateYear(@RequestBody @Valid ContentMark contentMark,
								   BindingResult result) throws GlobalException {
		validateId(contentMark.getId());
		validateBindingResult(result);
		if (!service.checkByMarkName(contentMark.getMarkName(), ContentMark.MARK_TYPE_YEAR,
				contentMark.getId())) {
			return new ResponseInfo(SysOtherErrorCodeEnum.YEAR_NUM_ALREADY_EXIST.getCode(),
					SysOtherErrorCodeEnum.YEAR_NUM_ALREADY_EXIST.getDefaultMessage());
		}
		ContentMark mark = service.findByIdAndMarkType(contentMark.getId(), ContentMark.MARK_TYPE_YEAR);
		if (mark != null) {
			mark.setMarkName(contentMark.getMarkName());
			service.update(mark);
			return new ResponseInfo();
		} else {
			return new ResponseInfo(SystemExceptionEnum.DOMAIN_NOT_FOUND_ERROR.getCode(),
					SystemExceptionEnum.DOMAIN_NOT_FOUND_ERROR.getDefaultMessage());
		}
	}

	/**
	 * 机关代字排序
	 *
	 * @Title: 排序
	 * @param: @param sorts
	 * @param: @return
	 * @return: ResponseInfo
	 */
	@PutMapping(value = "/agency/sort")
	public ResponseInfo sortAgency(@RequestBody @Valid DragSortDto sorts, BindingResult result) throws GlobalException {
		validateBindingResult(result);
		service.sort(sorts, ContentMark.MARK_TYPE_AGENCY);
		return new ResponseInfo();
	}

	/**
	 * 年号排序
	 *
	 * @Title: 排序
	 * @param: @param sorts
	 * @param: @return
	 * @return: ResponseInfo
	 */
	@PutMapping(value = "/year/sort")
	public ResponseInfo sortYear(@RequestBody @Valid DragSortDto sorts, BindingResult result) throws GlobalException {
		validateBindingResult(result);
		service.sort(sorts, ContentMark.MARK_TYPE_YEAR);
		return new ResponseInfo();
	}

	/**
	 * 删除机关代字
	 *
	 * @Title: 删除
	 * @param: @param ids
	 * @param: @return
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@DeleteMapping("/agency")
	public ResponseInfo deleteAgency(@RequestBody @Valid DeleteDto dels,
									 BindingResult result) throws GlobalException {
		validateBindingResult(result);
		service.deleteByIdAndMarkType(dels.getIds(), ContentMark.MARK_TYPE_AGENCY);
		return new ResponseInfo();
	}

	/**
	 * 删除年号
	 *
	 * @Title: 删除
	 * @param: @param ids
	 * @param: @return
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@DeleteMapping("/year")
	public ResponseInfo deleteYear(@RequestBody @Valid DeleteDto dels,
								   BindingResult result) throws GlobalException {
		validateBindingResult(result);
		service.deleteByIdAndMarkType(dels.getIds(), ContentMark.MARK_TYPE_YEAR);
		return new ResponseInfo();
	}
}



