/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.resource;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.SiteErrorCodeEnum;
import com.jeecms.common.exception.error.SysOtherErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.FileUtils;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.resource.domain.dto.TplDeleteDto;
import com.jeecms.resource.domain.dto.TplReSourceDto;
import com.jeecms.resource.domain.dto.TplRenameDto;
import com.jeecms.resource.domain.dto.TplSaveDirDto;
import com.jeecms.resource.domain.dto.TplUpdateDto;
import com.jeecms.resource.service.Tpl;
import com.jeecms.resource.service.TplResourceService;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.exception.SiteExceptionInfo;
import com.jeecms.system.service.CmsSiteService;
import com.jeecms.util.SystemContextUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;

import static com.jeecms.common.constants.WebConstants.SPT;

/**
 * 模板资源管理
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/5/9
 */
@RequestMapping("/resource")
@RestController
public class CmsResourceController extends AbstractTplAndResourceController {
	private static Logger log = LoggerFactory.getLogger(CmsResourceController.class);
	private static final String RESOURCE_SUFFIX_JS = "js";
	private static final String RESOURCE_SUFFIX_CSS = "css";


	/**
	 * 资源树
	 *
	 * @param request HttpServletRequest
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@RequestMapping(value = "/tree", method = RequestMethod.GET)
	@SerializeField(clazz = Tpl.class, includes = {"lastModifiedDate", "filename", "size", "name", "path",
			"directory", "children"})
	public ResponseInfo tree(HttpServletRequest request) throws GlobalException {
		CmsSite site = SystemContextUtils.getSite(request);
		String root = site.getResourcePath();
		try {
			return new ResponseInfo(resourceService.listFile(root, true, site));
		} catch (IOException e) {
			return new ResponseInfo(SiteErrorCodeEnum.FTP_CONNECTION_ERROR.getCode(),
					SiteErrorCodeEnum.FTP_CONNECTION_ERROR.getDefaultMessage());
		}
	}

	/**
	 * 资源列表
	 *
	 * @param root    路径
	 * @param request HttpServletRequest
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@SerializeField(clazz = Tpl.class, includes = {"lastModifiedDate", "filename", "size", "name", "path",
			"directory", "sizeUnit"})
	public ResponseInfo resourceList(String root, HttpServletRequest request) throws GlobalException {
		CmsSite site = SystemContextUtils.getSite(request);
		if (StringUtils.isBlank(root)) {
			root = site.getResourcePath();
		} else {
			FileUtils.isValidFilename(site.getResourcePath(), root);
		}
		try {
			return new ResponseInfo(resourceService.listFile(root, false, site));
		} catch (IOException e) {
			return new ResponseInfo(SiteErrorCodeEnum.FTP_CONNECTION_ERROR.getCode(),
					SiteErrorCodeEnum.FTP_CONNECTION_ERROR.getDefaultMessage());
		}
	}

	/**
	 * 添加资源目录
	 *
	 * @param dto     资源目录Dto
	 * @param request HttpServletRequest
	 * @param result  BindingResult
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@RequestMapping(value = "/dir", method = RequestMethod.POST)
	public ResponseInfo createDir(@Valid @RequestBody TplSaveDirDto dto, HttpServletRequest request,
								  BindingResult result) throws GlobalException {
		validateBindingResult(result);
		CmsSite site = SystemContextUtils.getSite(request);
		String root = dto.getRoot();
		if (StringUtils.isBlank(root)) {
			root = site.getResourcePath();
		}
		validSave(root + "/" + dto.getDirName(), site.getResourcePath(), site);
		try {
			resourceService.createDir(root, dto.getDirName(), site);
		} catch (IOException e) {
			return new ResponseInfo(SiteErrorCodeEnum.FTP_CONNECTION_ERROR.getCode(),
					SiteErrorCodeEnum.FTP_CONNECTION_ERROR.getDefaultMessage());
		}
		return new ResponseInfo();
	}


	@GetMapping(value = "/unique/dir")
	public ResponseInfo unique(String dirName, String root, HttpServletRequest request) throws GlobalException {
		validBlank(dirName);
		CmsSite site = SystemContextUtils.getSite(request);
		if (StringUtils.isBlank(root)) {
			root = site.getResourcePath();
		} else {
			if (FilenameUtils.getName(root).equalsIgnoreCase(dirName)) {
				return new ResponseInfo(true);
			} else {
				root = FilenameUtils.getFullPathNoEndSeparator(root);
			}
		}
		return new ResponseInfo(!vldExistFile(root + SPT + dirName, site));
	}

	/**
	 * 校验文件是否唯一（修改）
	 *
	 * @param filename 目录名称
	 * @param root     目录路径
	 * @return true 唯一 false 不唯一
	 */
	@GetMapping(value = "/unique/filename")
	public ResponseInfo checkByFilename(@NotBlank @RequestParam String filename, @NotBlank @RequestParam String root,
										HttpServletRequest request) throws GlobalException {
		CmsSite site = SystemContextUtils.getSite(request);
		int num = root.lastIndexOf(SPT);
		if (root.substring(num).equalsIgnoreCase(filename)) {
			return new ResponseInfo(true);
		} else {
			root = root.substring(0, num);
		}
		return new ResponseInfo(!vldExistFile(root + SPT + filename, site));
	}

	/**
	 * 读取资源文件信息
	 *
	 * @param name    资源路径
	 * @param request HttpServletRequest
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@GetMapping()
	@SerializeField(clazz = Tpl.class, includes = {"filename", "root", "source", "sizeUnit"})
	public ResponseInfo get(String name, HttpServletRequest request)
			throws GlobalException {
		CmsSite site = SystemContextUtils.getSite(request);
		validBlank(name);
		validDelete(name, site.getResourcePath(), site);
		return new ResponseInfo(resourceService.readFile(name));
	}

	/**
	 * 添加资源文件
	 *
	 * @param resDto  资源文件Dto
	 * @param request HttpServletRequest
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@PostMapping()
	public ResponseInfo save(@Valid @RequestBody TplReSourceDto resDto,
							 HttpServletRequest request) throws GlobalException {
		validBlank(resDto.getFilename(), resDto.getSource());
		String root = resDto.getRoot();
		CmsSite site = SystemContextUtils.getSite(request);
		if (StringUtils.isBlank(root)) {
			root = site.getResourcePath();
		}
		// 验证参数
		validSave(root + "/" + resDto.getFilename(), site.getResourcePath(), site);
		resourceService.createFile(root, resDto.getFilename(), resDto.getSource(), site);
		return new ResponseInfo();
	}

	/**
	 * 修改资源文件
	 *
	 * @param updateDto 修改文件Dto
	 * @param request   HttpServletRequest
	 * @param result    BindingResult
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@PutMapping()
	public ResponseInfo update(@Valid @RequestBody TplUpdateDto updateDto, HttpServletRequest request,
							   BindingResult result) throws GlobalException {
		validateBindingResult(result);
		CmsSite site = SystemContextUtils.getSite(request);
		String oldName = updateDto.getRoot();
		String root = oldName.substring(0, updateDto.getRoot().lastIndexOf("/"));
		String newName = root + "/" + updateDto.getFilename();
		String oldSuffix = oldName.substring(oldName.lastIndexOf(".") + 1);
		String old = oldName.substring(oldName.lastIndexOf("/") + 1);
		//判断文件名后缀是否js或css结尾
		if (!RESOURCE_SUFFIX_JS.equals(oldSuffix) && !RESOURCE_SUFFIX_CSS.equals(oldSuffix)) {
			return new ResponseInfo(SysOtherErrorCodeEnum.PATH_OR_FILENAME_VALID.getCode(),
					SysOtherErrorCodeEnum.PATH_OR_FILENAME_VALID.getDefaultMessage());
		}
		if (newName.equalsIgnoreCase(oldName)) {
			//如果新名称和旧名称一样，只判断原文件存在与否
			validDelete(oldName, site.getResourcePath(), site);
		} else {
			//判断文件名后缀是否js或css结尾
			String newSuffix = newName.substring(newName.lastIndexOf(".") + 1);
			if (!RESOURCE_SUFFIX_JS.equals(newSuffix) && !RESOURCE_SUFFIX_CSS.equals(newSuffix)) {
				return new ResponseInfo(SysOtherErrorCodeEnum.PATH_OR_FILENAME_VALID.getCode(),
						SysOtherErrorCodeEnum.PATH_OR_FILENAME_VALID.getDefaultMessage());
			}
			//如果新名称和旧名称不一样，需要判断原文件和旧文件存在与否
			validReName(oldName, root + "/" + newName, site.getResourcePath(), site);
		}

		try {
			resourceService.updateFile(root, old, updateDto.getFilename(), updateDto.getSource(), site);
		} catch (IOException e) {
			return new ResponseInfo(SiteErrorCodeEnum.FTP_CONNECTION_ERROR.getCode(),
					SiteErrorCodeEnum.FTP_CONNECTION_ERROR.getDefaultMessage());
		}
		return new ResponseInfo();
	}

	/**
	 * 删除资源文件
	 *
	 * @param nameDto 模板资源删除Dto
	 * @param result  BindingResult
	 * @param request HttpServletRequest
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@DeleteMapping
	public ResponseInfo delete(@Valid @RequestBody TplDeleteDto nameDto, BindingResult result,
							   HttpServletRequest request) throws GlobalException {
		validateBindingResult(result);
		CmsSite site = SystemContextUtils.getSite(request);
		/* 验证名字是否合法路径 */
		for (String n : nameDto.getNames()) {
			validDelete(n, site.getResourcePath(), site);
		}
		try {
			resourceService.delete(nameDto.getNames(), site);
		} catch (IOException e) {
			return new ResponseInfo(SiteErrorCodeEnum.FTP_CONNECTION_ERROR.getCode(),
					SiteErrorCodeEnum.FTP_CONNECTION_ERROR.getDefaultMessage());
		}
		return new ResponseInfo();
	}

	/**
	 * 修改资源文件名称
	 *
	 * @param renameDto 模板、资源重命名Dto
	 * @param request   HttpServletRequest
	 * @param result    BindingResult
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@RequestMapping("/rename")
	public ResponseInfo rename(@Valid @RequestBody TplRenameDto renameDto, HttpServletRequest request,
							   BindingResult result) throws GlobalException {
		validateBindingResult(result);
		CmsSite site = SystemContextUtils.getSite(request);
		validReName(renameDto.getFileName(), renameDto.getFileName(), site.getResourcePath(), site);
		try {
			resourceService.rename(renameDto.getNewName(), renameDto.getNewName(), site);
		} catch (IOException e) {
			return new ResponseInfo(SiteErrorCodeEnum.FTP_CONNECTION_ERROR.getCode(),
					SiteErrorCodeEnum.FTP_CONNECTION_ERROR.getDefaultMessage());
		}
		return new ResponseInfo();
	}

	/**
	 * 上传资源文件
	 *
	 * @param file    文件
	 * @param root    路径
	 * @param request HttpServletRequest
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@RequestMapping("/upload")
	public ResponseInfo upload(@RequestParam(value = "uploadFile", required = false) MultipartFile file,
							   String root, Boolean isCover, HttpServletRequest request) throws GlobalException {
		validBlank(file);
		CmsSite site = SystemContextUtils.getSite(request);
		if (StringUtils.isBlank(root)) {
			root = site.getResourcePath();
		} else {
			FileUtils.isValidFilename(site.getResourcePath(), root);
		}
		try {
			//文件不合法
			if (!FileUtils.checkFileIsValid(file.getInputStream())) {
				return new ResponseInfo(SysOtherErrorCodeEnum.UPLOAD_FILE_ERROR.getCode(),
						SysOtherErrorCodeEnum.UPLOAD_FILE_ERROR.getDefaultMessage());
			}
		} catch (IOException e) {
			log.error(e.getMessage());
			return new ResponseInfo(SysOtherErrorCodeEnum.UPLOAD_IO_ERROR.getCode(),
					SysOtherErrorCodeEnum.UPLOAD_IO_ERROR.getDefaultMessage());
		}
		validateFile(file);
		resourceService.saveFile(root, file, isCover);
		return new ResponseInfo();
	}

	/**
	 * 拷贝资源文件
	 *
	 * @param fromSiteId 资源站点id
	 * @param toSiteId   拷贝到站点id
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@RequestMapping(value = "/copyTplAndRes", method = RequestMethod.GET)
	public ResponseInfo copyTplAndRes(Integer fromSiteId, Integer toSiteId) throws GlobalException {
		validBlank(fromSiteId, toSiteId);
		CmsSite from = siteService.findById(fromSiteId);
		CmsSite to = siteService.findById(toSiteId);
		try {
			resourceService.copyTplAndRes(from, to);
		} catch (IOException e) {
			log.error(e.getMessage());
			String msg = MessageResolver.getMessage(SiteErrorCodeEnum.COPY_TEMPLATE_RES_ERROR.getCode(),
					SiteErrorCodeEnum.COPY_TEMPLATE_RES_ERROR.getDefaultMessage());
			throw new GlobalException(new SiteExceptionInfo(
					SiteErrorCodeEnum.COPY_TEMPLATE_RES_ERROR.getCode(), msg));
		}
		return new ResponseInfo();
	}

	@Autowired
	private TplResourceService resourceService;
	@Autowired
	private CmsSiteService siteService;
}
