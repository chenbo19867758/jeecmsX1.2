/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.system;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.SysOtherErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.SysSensitiveWord;
import com.jeecms.system.domain.dto.SensitiveWordDto;
import com.jeecms.system.service.SysSensitiveWordService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 敏感词Controller
 *
 * @author: xiaohui
 * @version: 1.0
 * @date 2019-04-29
 */
@RequestMapping("/sensitiveWords")
@RestController
public class SysSensitiveWordController extends BaseController<SysSensitiveWord, Integer> {

	@Autowired
	private SysSensitiveWordService service;

	@PostConstruct
	public void init() {
		String[] queryParams = {"sensitiveWord_LIKE"};
		super.setQueryParams(queryParams);
	}


	/**
	 * @Title: 列表分页
	 * @param: @param request
	 * @param: @param pageable
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@GetMapping(value = "/page")
	@SerializeField(clazz = SysSensitiveWord.class, includes = {"id", "sensitiveWord", "replaceWord", "createTime", "createUser"})
	public ResponseInfo page(HttpServletRequest request,
							 @PageableDefault(sort = "createTime", direction = Direction.DESC) Pageable pageable) throws GlobalException {
		return super.getPage(request, pageable, false);
	}

	/**
	 * 校验是否重名
	 *
	 * @param sensitiveWord 敏感词
	 * @param id            敏感词id
	 * @return ResponseInfo
	 */
	@GetMapping("/unique")
	public ResponseInfo checkName(String sensitiveWord, Integer id) {
		return new ResponseInfo(service.checkBySensitiveWord(sensitiveWord, id));
	}

	/**
	 * @Title: 获取详情
	 * @param: @param id
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@GetMapping(value = "/{id}")
	@SerializeField(clazz = SysSensitiveWord.class, includes = {"id", "sensitiveWord", "replaceWord", "createTime", "createUser"})
	@Override
	public ResponseInfo get(@PathVariable("id") Integer id) throws GlobalException {
		return super.get(id);
	}

	/**
	 * @Title: 添加
	 * @param: @param result
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@PostMapping()
	public ResponseInfo save(@RequestBody @Valid SensitiveWordDto dto, BindingResult result) throws GlobalException {
		validateBindingResult(result);
		String username = SystemContextUtils.getCurrentUsername();
		return service.saveBatch(dto, username);
	}


	/**
	 * @Title: 修改
	 * @param: @param result
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@PutMapping()
	@Override
	public ResponseInfo update(@RequestBody @Valid SysSensitiveWord sysSensitiveWord, BindingResult result) throws GlobalException {
		validateBindingResult(result);
		boolean check = service.checkBySensitiveWord(sysSensitiveWord.getSensitiveWord(), sysSensitiveWord.getId());
		if (!check) {
			new ResponseInfo(SysOtherErrorCodeEnum.SENSITIVE_WORD_ALREADY_EXIST.getCode(),
					SysOtherErrorCodeEnum.SENSITIVE_WORD_ALREADY_EXIST.getDefaultMessage());
		}
		return super.update(sysSensitiveWord, result);
	}

	/**
	 * @Title: 删除
	 * @param: @param ids
	 * @param: @return
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@DeleteMapping()
	public ResponseInfo delete(@RequestBody @Valid DeleteDto dels) throws GlobalException {
		return super.physicalDelete(dels.getIds());
	}


	/**
	 * 导入敏感词excel
	 *
	 * @param file         敏感词excel
	 * @param dealWithType 处理方式
	 * @return ResponseInfo
	 */
	@PostMapping(value = "/import")
	public ResponseInfo importExcel(@RequestParam(value = "file") MultipartFile file,
									Integer dealWithType, HttpServletRequest request) throws GlobalException {
		ImportParams params = new ImportParams();
		List<SysSensitiveWord> list = new ArrayList<SysSensitiveWord>();
		try {
			list = ExcelImportUtil.importExcel(file.getInputStream(), SysSensitiveWord.class, params);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseInfo(SysOtherErrorCodeEnum.IMPORT_EXCEL_ERROR.getCode(),
					SysOtherErrorCodeEnum.IMPORT_EXCEL_ERROR.getDefaultMessage());
		}
		service.saveAll(list, dealWithType, SystemContextUtils.getCurrentUsername());
		return new ResponseInfo();
	}

	/**
	 * 导出模板
	 *
	 * @return
	 */
	@PostMapping("/download")
	public ResponseInfo export(HttpServletResponse response) {
		ExportParams exportParams = new ExportParams();
		exportParams.setSheetName("敏感词模板");
		response.setContentType("application/x-download;charset=UTF-8");
		response.addHeader("Content-disposition", "filename=demo.xls");
		List<SysSensitiveWord> list = new ArrayList<>();
		Workbook workbook = ExcelExportUtil.exportExcel(exportParams, SysSensitiveWord.class, list);
		OutputStream fos = null;
		try {
			fos = response.getOutputStream();
			workbook.write(fos);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseInfo(false);
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}



