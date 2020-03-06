/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.resource;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.IllegalParamExceptionInfo;
import com.jeecms.common.exception.IllegalParamValidationUtils;
import com.jeecms.common.exception.SystemExceptionEnum;
import com.jeecms.common.exception.error.SysOtherErrorCodeEnum;
import com.jeecms.common.util.FileUtils;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.resource.service.Tpl;
import com.jeecms.resource.service.TplService;
import com.jeecms.system.domain.CmsSite;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 模板资源抽象父类
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/5/8
 */

public abstract class AbstractTplAndResourceController {
	/**
	 * 校验参数是否为空
	 *
	 * @param names 参数
	 * @throws GlobalException 异常
	 */
	protected void validBlank(Object... names) throws GlobalException {
		for (Object name : names) {
			boolean isNull = false;
			if (name instanceof String) {
				isNull = StringUtils.isBlank((String) name);
			} else if (name == null) {
				isNull = true;
			}
			if (isNull) {
				String msg = MessageResolver.getMessage(SystemExceptionEnum.ILLEGAL_PARAM.getCode(),
						SystemExceptionEnum.ILLEGAL_PARAM.getDefaultMessage());
				throw new GlobalException(new IllegalParamExceptionInfo(
						SystemExceptionEnum.ILLEGAL_PARAM.getCode(), msg, name + " is need"));
			}
		}
	}

	/**
	 * 添加模板校验
	 *
	 * @param path    文件路径（包含文件名）
	 * @param tplPath 站点对应路径
	 * @throws GlobalException 异常
	 */
	protected void validSave(String path, String tplPath, CmsSite site) throws GlobalException {
		String msg = MessageResolver.getMessage(SystemExceptionEnum.ILLEGAL_PARAM.getCode(),
				SystemExceptionEnum.ILLEGAL_PARAM.getDefaultMessage());

		FileUtils.isValidFilename(tplPath, path);

		//文件已存在抛出异常
		if (vldExistFile(path, site)) {
			throw new GlobalException(new IllegalParamExceptionInfo(
					SysOtherErrorCodeEnum.FILE_ALREADY_EXIST.getCode(), msg,
					SysOtherErrorCodeEnum.FILE_ALREADY_EXIST.getDefaultMessage()));
		}
	}

	/**
	 * 文件不存在校验
	 *
	 * @param path    文件路径（包含文件名）
	 * @param tplPath 站点对应路径
	 * @throws GlobalException 异常
	 */
	protected void validDelete(String path, String tplPath, CmsSite site) throws GlobalException {
		String msg = MessageResolver.getMessage(SystemExceptionEnum.ILLEGAL_PARAM.getCode(),
				SystemExceptionEnum.ILLEGAL_PARAM.getDefaultMessage());

		FileUtils.isValidFilename(tplPath, path);

		//文件不存在抛出异常
		if (!vldExistFile(path, site)) {
			throw new GlobalException(new IllegalParamExceptionInfo(
					SysOtherErrorCodeEnum.FILE_NOT_FIND.getCode(), msg,
					SysOtherErrorCodeEnum.FILE_NOT_FIND.getDefaultMessage()));
		}
	}

	/**
	 * 修改模板校验
	 *
	 * @param oldName 原文件名
	 * @param newName 新文件名
	 * @param tplPath 站点对应路径
	 * @throws GlobalException 异常
	 */
	protected void validReName(String oldName, String newName, String tplPath,
							   CmsSite site) throws GlobalException {
		String msg = MessageResolver.getMessage(SystemExceptionEnum.ILLEGAL_PARAM.getCode(),
				SystemExceptionEnum.ILLEGAL_PARAM.getDefaultMessage());

		FileUtils.isValidFilename(tplPath, oldName);

		FileUtils.isValidFilename(tplPath, newName);

		//原文件不存在抛出异常
		if (!vldExistFile(oldName, site)) {
			throw new GlobalException(new IllegalParamExceptionInfo(
					SysOtherErrorCodeEnum.FILE_NOT_FIND.getCode(), msg,
					SysOtherErrorCodeEnum.FILE_NOT_FIND.getDefaultMessage()));
		}
		//修改后的文件名存在抛出异常
		if (vldExistFile(newName, site)) {
			throw new GlobalException(new IllegalParamExceptionInfo(
					SysOtherErrorCodeEnum.FILE_ALREADY_EXIST.getCode(), msg,
					SysOtherErrorCodeEnum.FILE_ALREADY_EXIST.getDefaultMessage()));
		}
	}

	/**
	 * 导入文件校验
	 *
	 * @param file 导入文件
	 * @throws GlobalException 异常
	 */
	protected void validateFile(MultipartFile file) throws GlobalException {
		String msg = MessageResolver.getMessage(SystemExceptionEnum.ILLEGAL_PARAM.getCode(),
				SystemExceptionEnum.ILLEGAL_PARAM.getDefaultMessage());
		//判断文件是否存在
		if (file == null) {
			throw new GlobalException(new IllegalParamExceptionInfo(
					SystemExceptionEnum.ILLEGAL_PARAM.getCode(), msg, "noFile To Upload"));
		}
		String filename = file.getOriginalFilename();
		boolean checkFilename = false;
		String type1 = "/";
		if (type1.contains(filename)) {
			checkFilename = true;
		}
		String type2 = "\\";
		if (type2.contains(filename)) {
			checkFilename = true;
		}
		String type3 = "\0";
		if (type3.contains(filename)) {
			checkFilename = true;
		}
		if (filename != null && checkFilename) {
			throw new GlobalException(new IllegalParamExceptionInfo(
					SystemExceptionEnum.ILLEGAL_PARAM.getCode(), msg, "template invalid Params"));
		}
	}

	/**
	 * 判断文件是否存在
	 *
	 * @param name 文件路径
	 * @param site 站点
	 * @return true 文件存在，false 文件不存在
	 */
	public boolean vldExistFile(String name, CmsSite site) throws GlobalException {
		if (StringUtils.isBlank(name)) {
			return true;
		}
		Tpl entity = null;
		try {
			entity = tplService.getFile(name, site);
		} catch (IOException e) {
			return false;
		}
		if (entity != null) {
			return true;
		}
		return false;
	}

	protected void validateBindingResult(BindingResult result) throws GlobalException {
		/* 对字段进行校验 */
		if (result.hasErrors()) {
			/* 初始化非法参数的提示信息。 */
			IllegalParamValidationUtils
					.initIllegalParamMsg(result);
			/* 获取非法参数异常信息对象，并抛出异常。 */
			throw new GlobalException(IllegalParamValidationUtils.getIllegalParamExceptionInfo());
		}
	}

	@Autowired
	private TplService tplService;
}
