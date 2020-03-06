package com.jeecms.admin.controller.resource;

import com.jeecms.common.constants.UploadEnum.UploadServerType;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.ueditor.ResourceType;
import com.jeecms.content.constants.CmsModelConstant;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.resource.domain.UploadFtp;
import com.jeecms.resource.domain.UploadOss;
import com.jeecms.resource.domain.dto.UploadResult;
import com.jeecms.resource.service.ResourcesSpaceDataService;
import com.jeecms.resource.service.impl.UploadService;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.util.SystemContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 资源上传
 *
 * @author: tom
 * @date: 2018年4月14日 下午5:36:38
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@RestController
public class UploadController {

	private static final Logger log = LoggerFactory.getLogger(UploadController.class);

	/**
	 * 上传文件
	 *
	 * @param file
	 *            文件
	 * @param mark
	 *            是否使用水印
	 * @param addToRes
	 *            是否添加到资源库
	 * @param uploadPath
	 *            上传指定目录
	 * @param typeStr
	 *            类型 File文件 Flash Flash Image图片 Media视频 Audio音频
	 * @param spaceId
	 *            资源文件夹
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws GlobalException
	 *             GlobalException
	 * @throws IOException
	 *             IOException
	 * @Title: upload
	 * @return: ResponseInfo ResponseInfo
	 */
	@RequestMapping("/upload/o_upload")
	public ResponseInfo upload(@RequestParam(value = "uploadFile", required = false) MultipartFile file, Boolean mark,
			Boolean addToRes, String uploadPath, Integer spaceId, String typeStr, Integer modelId, String fieldName,
			HttpServletRequest request, HttpServletResponse response) throws GlobalException, IOException {
		if (addToRes == null) {
			addToRes = false;
		}
		ResourceType resourceType = ResourceType.getDefaultResourceType(typeStr);
		CmsSite site = SystemContextUtils.getSite(request);
		ResponseInfo info = uploadService.upload(file, mark, uploadPath, resourceType, modelId, fieldName, site);

		UploadResult uploadResult = (UploadResult) info.getData();
		Integer siteId = SystemContextUtils.getSiteId(request);
		Integer userId = SystemContextUtils.getUserId(request);
		Integer duration = uploadResult.getDuration();
		UploadFtp ftp = null;
		UploadOss oss = null;
		if (UploadServerType.ftp.equals(site.getUploadServerType()) && site.getUploadFtp() != null) {
			ftp = site.getUploadFtp();
		} else if (UploadServerType.oss.equals(site.getUploadServerType()) && site.getUploadOss() != null) {
			oss = site.getUploadOss();
		}
		ResourcesSpaceData resourceData = resourcesService.save(userId, spaceId, uploadResult.getOrigName(),
				uploadResult.getFileSize().intValue(), uploadResult.getFileUrl(), uploadResult.getDimensions(),
				uploadResult.getResourceType(), addToRes,siteId, duration, ftp, oss);
		if (resourceData != null) {
			uploadResult.setResourceId(resourceData.getId());
		}
		return info;
	}

	/**
	 * 上传文库文档
	 *
	 * @param file
	 *            文件
	 * @param request
	 *            HttpServletRequest
	 * @throws GlobalException
	 *             GlobalException
	 * @throws IOException
	 *             IOException
	 * @Title: upload
	 * @return: ResponseInfo ResponseInfo
	 */
	@RequestMapping("/upload/document")
	public ResponseInfo uploadDocument(@RequestParam(value = "uploadFile", required = false) MultipartFile file,
			Integer modelId,Boolean addToRes, HttpServletRequest request) throws GlobalException, IOException {
		// 默认文库文档字段
		String fieldName = CmsModelConstant.FIELD_SYS_TEXTLIBRARY;
		CmsSite site = SystemContextUtils.getSite(request);
		ResponseInfo info = uploadService.uploadDoc(file, modelId, fieldName, site);
		// 默认添加进资源库
		UploadResult uploadResult = (UploadResult) info.getData();
		Integer siteId = SystemContextUtils.getSiteId(request);
		Integer userId = SystemContextUtils.getUserId(request);
		Integer duration = uploadResult.getDuration();
		UploadFtp ftp = null;
		UploadOss oss = null;
		if (UploadServerType.ftp.equals(site.getUploadServerType()) && site.getUploadFtp() != null) {
			ftp = site.getUploadFtp();
		} else if (UploadServerType.oss.equals(site.getUploadServerType()) && site.getUploadOss() != null) {
			oss = site.getUploadOss();
		}
		if (addToRes == null) {
			addToRes = true;
		}
		ResourcesSpaceData resourceData = resourcesService.save(userId, null, uploadResult.getOrigName(),
				uploadResult.getFileSize().intValue(), uploadResult.getFileUrl(), uploadResult.getDimensions(),
				uploadResult.getResourceType(), addToRes,siteId, duration, ftp, oss);
		if (resourceData != null) {
			uploadResult.setResourceId(resourceData.getId());
		}
		return info;
	}

	@Autowired
	private ResourcesSpaceDataService resourcesService;
	@Autowired
	private UploadService uploadService;

}
