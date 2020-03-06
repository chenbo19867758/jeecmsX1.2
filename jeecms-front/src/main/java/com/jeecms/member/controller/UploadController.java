package com.jeecms.member.controller;

import com.jeecms.common.constants.UploadEnum.UploadServerType;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.image.ImageUtils;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.ueditor.ResourceType;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.resource.domain.UploadFtp;
import com.jeecms.resource.domain.UploadOss;
import com.jeecms.resource.domain.dto.UploadResult;
import com.jeecms.resource.service.ResourcesSpaceDataService;
import com.jeecms.resource.service.impl.UploadService;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.util.SystemContextUtils;

import org.apache.commons.lang3.StringUtils;
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
 * @author: tom
 * @date:   2019年8月16日 下午4:46:38
 */
@RestController
public class UploadController  {

	public static final Logger log = LoggerFactory.getLogger(UploadController.class);

	/**
	 * 上传文件
	 *
	 * @param file       文件
	 * @param mark       是否使用水印
	 * @param typeStr    类型 File文件 Flash Flash Image图片 Media视频 Audio音频
	 * @param request    HttpServletRequest
	 * @param response   HttpServletResponse
	 * @throws GlobalException GlobalException
	 * @throws IOException     IOException
	 * @Title: upload
	 * @return: ResponseInfo ResponseInfo
	 */
	@RequestMapping("/upload/o_upload")
	public ResponseInfo upload(@RequestParam(value = "uploadFile", required = false) MultipartFile file, 
			Boolean mark,String typeStr, Integer modelId, String fieldName,Integer spaceId, 
			HttpServletRequest request, HttpServletResponse response) throws GlobalException, IOException {
		/** 为空默认附件类型，如果识别文件是图片则自动赋予图片类型 */
		if (StringUtils.isBlank(typeStr)) {
			typeStr = ResourceType.FILE.getName();
		}
		if (file != null && ImageUtils.isImage(file.getInputStream())) {
			typeStr = ResourceType.IMAGE.getName();
		}
		CmsSite site = SystemContextUtils.getSite(request);
		ResourceType resourceType = ResourceType.getDefaultResourceType(typeStr);
		ResponseInfo info =  uploadService.upload(file, mark, null, resourceType, 
				modelId, fieldName, site);
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
				uploadResult.getResourceType(), false,siteId, duration, ftp, oss);
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
