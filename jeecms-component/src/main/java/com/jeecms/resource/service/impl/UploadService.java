package com.jeecms.resource.service.impl;

import com.jeecms.common.constants.UploadEnum.UploadServerType;
import com.jeecms.common.constants.UploadEnum.WaterMarkSet;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.UploadExceptionInfo;
import com.jeecms.common.exception.error.SysOtherErrorCodeEnum;
import com.jeecms.common.image.ImageScaleService;
import com.jeecms.common.image.ImageUtils;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.ueditor.ResourceType;
import com.jeecms.common.upload.FileRepository;
import com.jeecms.common.util.FileUtils;
import com.jeecms.common.util.mediautil.MediaUtil;
import com.jeecms.common.util.mediautil.MusicMetaInfo;
import com.jeecms.common.util.mediautil.VideoMetaInfo;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.content.domain.CmsModel;
import com.jeecms.content.domain.CmsModelItem;
import com.jeecms.content.service.CmsModelService;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.resource.domain.UploadFtp;
import com.jeecms.resource.domain.UploadOss;
import com.jeecms.resource.domain.dto.UploadResult;
import com.jeecms.resource.service.ResourcesSpaceService;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.CmsSiteConfig;
import com.jeecms.util.SystemContextUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Optional;

/**
 * 上传基类
 *
 * @author: tom
 * @date: 2018年6月21日 下午3:31:55
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Component
public class UploadService {

	private static final Logger log = LoggerFactory.getLogger(UploadService.class);
	private final String crossSp = "..";

	/**
	 * ffmpeg的执行路径
	 */
	@Value("${tool.ffmpeg}")
	private String ffmpegPath;

	@PostConstruct
	private void initFfmpeg() {
		MediaUtil.setFFmpegPath(ffmpegPath);
	}

	/**
	 * 上传
	 *
	 * @param file
	 *            文件
	 * @param mark
	 *            是否添加水印
	 * @param uploadPath
	 *            上传路径
	 * @param resourceType
	 *            UploadResourceType 资源类型
	 * @param modelId
	 *            模型ID
	 * @param fieldName
	 *            字段名称
	 * @param site
	 *            访问站点
	 * @throws GlobalException
	 *             GlobalException
	 * @throws IOException
	 *             IOException
	 * @Title: upload
	 * @return: ResponseInfo ResponseInfo
	 */
	public ResponseInfo upload(@RequestParam(value = "uploadFile", required = false) MultipartFile file, Boolean mark,
			String uploadPath, ResourceType resourceType, Integer modelId, String fieldName, CmsSite site)
					throws GlobalException, IOException {
		validate(file, uploadPath, resourceType, modelId, fieldName, site);
		UploadResult uploadInfo = doUpload(file, mark, uploadPath, resourceType, site);
		return new ResponseInfo(uploadInfo);
	}

	/**
	 * 执行上传（无限制规则）
	 *
	 * @param file
	 *            文件
	 * @param mark
	 *            是否添加水印
	 * @param uploadPath
	 *            上传路径
	 * @param resourceType
	 *            UploadResourceType 资源类型
	 * @param site
	 *            访问站点
	 * @throws GlobalException
	 *             GlobalException
	 * @throws IOException
	 *             IOException
	 * @Title: doUpload
	 * @return: ResponseInfo ResponseInfo
	 */
	public UploadResult doUpload(MultipartFile file, Boolean mark, String uploadPath, ResourceType resourceType,
			CmsSite site) throws GlobalException {
		if (site == null) {
			site = SystemContextUtils.getSite(RequestUtils.getHttpServletRequest());
		}
		File tempFile = null;
		String fileUrl = null;
		String siteUploadPath = WebConstants.UPLOAD_PATH + site.getPath();
		/** 用户未传 ResourceType 若是检查到是图片则也认定是图片 */
		boolean isImg = false;
		if(resourceType!=null&&resourceType.equals(ResourceType.IMAGE)){
			isImg = true;
		}
		InputStream inputStream;
		InputStream markInputStream = null;
		try {
			inputStream = file.getInputStream();
			if (ImageUtils.isImage(file.getInputStream())) {
				isImg = true;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			log.error("上传IO异常->1 "+e1.getMessage());
			throw new GlobalException(new UploadExceptionInfo());
		}
		String origName = file.getOriginalFilename();
		String ext = FilenameUtils.getExtension(origName).toLowerCase(Locale.ENGLISH);
		/** 未手动指定则获取站点统一配置 */
		if (mark == null) {
			mark = site.getCmsSiteCfg().getWatermarkOpen();
		}
		/** 创建临时文件 */
		try {
			if (mark && isImg) {
				tempFile = mark(file, site);
				markInputStream = new FileInputStream(tempFile);
			} else {
				String path = System.getProperty("java.io.tmpdir");
				path = java.text.Normalizer.normalize(path, java.text.Normalizer.Form.NFKD);
				tempFile = new File(path, String.valueOf(System.currentTimeMillis()));
				file.transferTo(tempFile);
			}
			inputStream = new FileInputStream(tempFile);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		/**
		 * 单位KB
		 */
		Long fileSize = tempFile.length() / 1024;
		// 视频或音频时长
		Integer duration = null;
		String dimensions = "";
		/**
		 * inputStream 不可再上传FTP OSS之前消化掉，否则会图片读取不了
		 * （ImageUtils.getImageDimensions 例如会读取流）
		 */
		String filename = "";
		if (StringUtils.isNotBlank(uploadPath)) {
			filename = uploadPath;
			if (!uploadPath.endsWith(WebConstants.SPT)) {
				filename += WebConstants.SPT;
			}
			filename += file.getOriginalFilename();
		}
		try {
			InputStream fileInputStream = new FileInputStream(tempFile);
			/** 读取图片像素尺寸 */
			if (ImageUtils.isImage(fileInputStream)) {
				String tempPath = System.getProperty("java.io.tmpdir");
				File tempFileForDiMen = new File(tempPath, String.valueOf(System.currentTimeMillis() + 1000));
				org.apache.commons.io.FileUtils.copyFile(tempFile, tempFileForDiMen);
				dimensions = ImageUtils.getImageDimensions(tempFileForDiMen);
				fileInputStream.close();
				tempFileForDiMen.delete();
			}
			/** 如果是音频或视频, 提取时长 */
			duration = getDuration(new FileInputStream(tempFile), resourceType, origName);
			/*** 如果选择了优先FTP并且设置了FTP,则使用FTP上传; 否则如果设置了OSS,则使用OSS; 都没设置则使用本地上传 */
			if (UploadServerType.ftp.equals(site.getUploadServerType()) && site.getUploadFtp() != null) {
				UploadFtp ftp = site.getUploadFtp();
				String ftpUrl = ftp.getUrl();
				if (StringUtils.isNotBlank(uploadPath)) {
					fileUrl = ftp.storeByFilename(filename, inputStream);
				} else {
					fileUrl = ftp.storeByExt(siteUploadPath, ext, inputStream);
				}
				// 加上url前缀
				fileUrl = ftpUrl + fileUrl;
			} else if (UploadServerType.oss.equals(site.getUploadServerType()) && site.getUploadOss() != null) {
				UploadOss oss = site.getUploadOss();
				if (StringUtils.isNotBlank(uploadPath)) {
					fileUrl = oss.storeByFilename(filename, inputStream);
				} else {
					fileUrl = oss.storeByExt(siteUploadPath, ext, inputStream);
				}
			} else {
				if (StringUtils.isNotBlank(uploadPath)) {
					fileUrl = fileRepository.storeByFilename(filename, tempFile);
				} else {
					fileUrl = fileRepository.storeByExt(siteUploadPath, ext, tempFile);
				}
				String ctx = site.getContextPath();
				if (StringUtils.isNotBlank(ctx)) {
					fileUrl = ctx + fileUrl;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("上传IO异常->2 "+e.getMessage());
			throw new GlobalException(new UploadExceptionInfo());
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
			if (mark && isImg) {
				if (markInputStream != null) {
					try {
						markInputStream.close();
					} catch (IOException e) {
						log.error(e.getMessage());
					}
				}
				if (tempFile != null) {
					tempFile.delete();
				}
			}
		}
		UploadResult uploadInfo = null;
		if (StringUtils.isNoneBlank(fileUrl)) {
			/** 去除格式后缀 */
			origName = origName.substring(0, origName.lastIndexOf("."));
			uploadInfo = new UploadResult(fileUrl, origName, fileSize, dimensions,
					ResourcesSpaceData.getResourceType(ext));
			uploadInfo.setDuration(duration);
		} else {
			log.error("未知上传错误 ");
			throw new GlobalException(new UploadExceptionInfo());
		}
		return uploadInfo;
	}

	public UploadResult doUpload(File file, Boolean mark, String uploadPath, ResourceType resourceType, CmsSite site)
			throws GlobalException {
		if (site == null) {
			site = SystemContextUtils.getSite(RequestUtils.getHttpServletRequest());
		}
		File tempFile = null;
		String fileUrl = null;
		String siteUploadPath = WebConstants.UPLOAD_PATH + site.getPath();
		/** 用户未传 ResourceType 若是检查到是图片则也认定是图片 */
		boolean isImg = false;
		if(resourceType!=null&&resourceType.equals(ResourceType.IMAGE)){
			isImg = true;
		}
		InputStream inputStream;
		InputStream markInputStream = null;
		FileInputStream fs = null;
		try {
			inputStream = new FileInputStream(file);
			fs  = new  FileInputStream(file);
			if (ImageUtils.isImage(fs)) {
				isImg = true;
			}
		} catch (IOException e1) {
			throw new GlobalException(new UploadExceptionInfo());
		} finally {
			try {
				if(fs!=null){
					fs.close();
				}
			}catch (IOException e){
			}
		}
		String origName = file.getName();
		String ext = FilenameUtils.getExtension(origName).toLowerCase(Locale.ENGLISH);
		/** 未手动指定则获取站点统一配置 */
		if (mark == null) {
			mark = site.getCmsSiteCfg().getWatermarkOpen();
		}
		/** 创建临时文件 */
		try {
			if (mark && isImg) {
				tempFile = mark(file, site);
				markInputStream = new FileInputStream(tempFile);
				inputStream = new FileInputStream(tempFile);
			} else {
				String path = System.getProperty("java.io.tmpdir");
				path = java.text.Normalizer.normalize(path, java.text.Normalizer.Form.NFKD);
				tempFile = new File(path, String.valueOf(System.currentTimeMillis()));
				org.apache.commons.io.FileUtils.copyFile(file, tempFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		/**
		 * 单位KB
		 */
		Long fileSize = tempFile.length() / 1024;
		// 视频或音频时长
		Integer duration = null;
		String dimensions = "";
		/**
		 * inputStream 不可再上传FTP OSS之前消化掉，否则会图片读取不了
		 * （ImageUtils.getImageDimensions 例如会读取流）
		 */
		String filename = "";
		if (StringUtils.isNotBlank(uploadPath)) {
			filename = uploadPath;
			if (!uploadPath.endsWith(WebConstants.SPT)) {
				filename += WebConstants.SPT;
			}
			filename += file.getName();
		}
		try {
			/** 读取图片像素尺寸 */
			FileInputStream forDemenFileInputStream = new FileInputStream(file);
			if (ImageUtils.isImage(forDemenFileInputStream)) {
				String tempPath = System.getProperty("java.io.tmpdir");
				File tempFileForDiMen = new File(tempPath, String.valueOf(System.currentTimeMillis()));
				org.apache.commons.io.FileUtils.copyFile(file, tempFileForDiMen);
				dimensions = ImageUtils.getImageDimensions(tempFileForDiMen);
				tempFileForDiMen.delete();
			}
			forDemenFileInputStream.close();
			/** 如果是音频或视频, 提取时长 */
			duration = getDuration(new FileInputStream(file), resourceType, origName);

			/*** 如果选择了优先FTP并且设置了FTP,则使用FTP上传; 否则如果设置了OSS,则使用OSS; 都没设置则使用本地上传 */
			if (UploadServerType.ftp.equals(site.getUploadServerType()) && site.getUploadFtp() != null) {
				UploadFtp ftp = site.getUploadFtp();
				String ftpUrl = ftp.getUrl();
				if (StringUtils.isNotBlank(uploadPath)) {
					fileUrl = ftp.storeByFilename(filename, inputStream);
				} else {
					fileUrl = ftp.storeByExt(siteUploadPath, ext, inputStream);
				}
				// 加上url前缀
				fileUrl = ftpUrl + fileUrl;
			} else if (UploadServerType.oss.equals(site.getUploadServerType()) && site.getUploadOss() != null) {
				UploadOss oss = site.getUploadOss();
				if (StringUtils.isNotBlank(uploadPath)) {
					fileUrl = oss.storeByFilename(filename, inputStream);
				} else {
					fileUrl = oss.storeByExt(siteUploadPath, ext, inputStream);
				}
			} else {
				if (StringUtils.isNotBlank(uploadPath)) {
					if (mark && isImg) {
						fileUrl = fileRepository.storeByFilename(filename, tempFile);
					} else {
						fileUrl = fileRepository.storeByFilename(filename, file);
					}
				} else {
					if (mark && isImg) {
						fileUrl = fileRepository.storeByExt(siteUploadPath, ext, tempFile);
					} else {
						fileUrl = fileRepository.storeByExt(siteUploadPath, ext, file);
					}
				}
				String ctx = site.getContextPath();
				if (StringUtils.isNotBlank(ctx)) {
					fileUrl = ctx + fileUrl;
				}
			}
		} catch (Exception e) {
			throw new GlobalException(new UploadExceptionInfo());
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
			if (markInputStream != null) {
				try {
					markInputStream.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
			if (tempFile != null) {
				tempFile.delete();
			}
		}
		UploadResult uploadInfo = null;
		if (StringUtils.isNoneBlank(fileUrl)) {
			/** 去除格式后缀 */
			origName = origName.substring(0, origName.lastIndexOf("."));
			uploadInfo = new UploadResult(fileUrl, origName, fileSize, dimensions,
					ResourcesSpaceData.getResourceType(ext));
			uploadInfo.setDuration(duration);
		} else {
			throw new GlobalException(new UploadExceptionInfo());
		}
		return uploadInfo;
	}

	private Integer getDuration(InputStream inputStream, ResourceType resourceType, String fileName) {
		/** 如果是音频或视频, 提取时长 */
		Long millisDuration = null;
		if (ResourceType.AUDIO.equals(resourceType) || MediaUtil.isMusic(fileName)) {
			MusicMetaInfo musicMetaInfo = MediaUtil.getMusicMetaInfo(inputStream);
			if (musicMetaInfo != null) {
				millisDuration = musicMetaInfo.getDuration();
			}
		} else if (ResourceType.MEDIA.equals(resourceType) || MediaUtil.isVideo(fileName)) {
			VideoMetaInfo videoMetaInfo = MediaUtil.getVideoMetaInfo(inputStream);
			if (videoMetaInfo != null) {
				millisDuration = videoMetaInfo.getDuration();
			}
		}
		Integer duration = Optional.ofNullable(millisDuration)
				// 毫秒转秒
				.map(dur -> (int) (dur / 1000)).orElse(null);
		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return duration;
	}

	private void validate(MultipartFile file, String uploadPath, ResourceType resourceType, Integer modelId,
			String fieldName, CmsSite site) throws GlobalException, IOException {
		if (file == null) {
			log.error("upload no file");
			throw new GlobalException(
					new UploadExceptionInfo(SysOtherErrorCodeEnum.UPLOAD_FILE_ERROR.getDefaultMessage(),
							SysOtherErrorCodeEnum.UPLOAD_FILE_ERROR.getCode()));
		}
		String filename = file.getOriginalFilename();
		String ext = FilenameUtils.getExtension(filename);
		if (site == null) {
			site = SystemContextUtils.getSite(RequestUtils.getHttpServletRequest());
		}
		boolean validFileName = filename != null && (filename.contains(File.separator) || filename.indexOf("\0") != -1);
		if (validFileName) {
			log.error("upload  filename error");
			throw new GlobalException(
					new UploadExceptionInfo(SysOtherErrorCodeEnum.UPLOAD_FILE_ERROR.getDefaultMessage(),
							SysOtherErrorCodeEnum.UPLOAD_FILE_ERROR.getCode()));
		}
		if (StringUtils.isNotBlank(uploadPath)) {
			// 上传路径要做检查，不能是规定以为的路径且 且路径不能含有../ ..\\越级等
			if (!uploadPath.startsWith(WebConstants.UPLOAD_PATH) || uploadPath.contains(crossSp + File.separator)
					|| uploadPath.contains(crossSp + WebConstants.SPT)) {
				log.error("upload  uploadPath error");
				throw new GlobalException(
						new UploadExceptionInfo(SysOtherErrorCodeEnum.UPLOAD_FILE_ERROR.getDefaultMessage(),
								SysOtherErrorCodeEnum.UPLOAD_FILE_ERROR.getCode()));
			}
		}
		/** 文件头格式检查 */
		if (!FileUtils.checkFileIsValid(file.getInputStream())) {
			log.error("文件头格式检查不合格");
			throw new GlobalException(
					new UploadExceptionInfo(SysOtherErrorCodeEnum.UPLOAD_FILE_ERROR.getDefaultMessage(),
							SysOtherErrorCodeEnum.UPLOAD_FILE_ERROR.getCode()));
		}
		int num = 1024;
		if(resourceType!=null){
			if (ResourceType.IMAGE.equals(resourceType)) {
				// 文件格式是否限制
				if (!site.isAllowPicSuffix(ext)) {
					throw new GlobalException(
							new UploadExceptionInfo(SysOtherErrorCodeEnum.UPLOAD_FORMAT_ERROR.getDefaultMessage(),
									SysOtherErrorCodeEnum.UPLOAD_FORMAT_ERROR.getCode()));
				}
				// 文件大小是否限制
				if (!site.isAllowPicMaxFile((int) file.getSize() / num)) {
					throw new GlobalException(
							new UploadExceptionInfo(SysOtherErrorCodeEnum.UPLOAD_LIMIT_ERROR.getDefaultMessage(),
									SysOtherErrorCodeEnum.UPLOAD_LIMIT_ERROR.getCode()));
				}
				/** 验证文件头 */
				if (!ImageUtils.isImage(file.getInputStream())) {
					throw new GlobalException(
							new UploadExceptionInfo(SysOtherErrorCodeEnum.UPLOAD_NOT_IMAGE_ERROR.getDefaultMessage(),
									SysOtherErrorCodeEnum.UPLOAD_NOT_IMAGE_ERROR.getCode()));
				}
			} else if (ResourceType.AUDIO.equals(resourceType)) {
				// 文件格式是否限制
				if (!site.isAllowAudioSuffix(ext)) {
					throw new GlobalException(
							new UploadExceptionInfo(SysOtherErrorCodeEnum.UPLOAD_FORMAT_ERROR.getDefaultMessage(),
									SysOtherErrorCodeEnum.UPLOAD_FORMAT_ERROR.getCode()));
				}
				// 文件大小是否限制
				if (!site.isAllowAudioMaxFile((int) file.getSize() / num)) {
					throw new GlobalException(
							new UploadExceptionInfo(SysOtherErrorCodeEnum.UPLOAD_LIMIT_ERROR.getDefaultMessage(),
									SysOtherErrorCodeEnum.UPLOAD_LIMIT_ERROR.getCode()));
				}
			} else if (ResourceType.FILE.equals(resourceType)) {
				// 文件格式是否限制
				if (!site.isAllowAttachmentSuffix(ext)) {
					throw new GlobalException(
							new UploadExceptionInfo(SysOtherErrorCodeEnum.UPLOAD_FORMAT_ERROR.getDefaultMessage(),
									SysOtherErrorCodeEnum.UPLOAD_FORMAT_ERROR.getCode()));
				}
				// 文件大小是否限制
				if (!site.isAllowAttachmentMaxFile((int) file.getSize() / num)) {
					throw new GlobalException(
							new UploadExceptionInfo(SysOtherErrorCodeEnum.UPLOAD_LIMIT_ERROR.getDefaultMessage(),
									SysOtherErrorCodeEnum.UPLOAD_LIMIT_ERROR.getCode()));
				}
			} else if (ResourceType.MEDIA.equals(resourceType)) {
				// 文件格式是否限制
				if (!site.isAllowVideoSuffix(ext)) {
					throw new GlobalException(
							new UploadExceptionInfo(SysOtherErrorCodeEnum.UPLOAD_FORMAT_ERROR.getDefaultMessage(),
									SysOtherErrorCodeEnum.UPLOAD_FORMAT_ERROR.getCode()));
				}
				// 文件大小是否限制
				if (!site.isAllowVideoMaxFile((int) file.getSize() / num)) {
					throw new GlobalException(
							new UploadExceptionInfo(SysOtherErrorCodeEnum.UPLOAD_LIMIT_ERROR.getDefaultMessage(),
									SysOtherErrorCodeEnum.UPLOAD_LIMIT_ERROR.getCode()));
				}
			}
		}
		if (modelId != null && StringUtils.isNoneBlank(fieldName)) {
			CmsModel model = modelService.findById(modelId);
			CmsModelItem modelItem = model.getItem(fieldName);
			// 文件格式是否限制
			if (!modelItem.isAllowVideoSuffix(ext)) {
				throw new GlobalException(
						new UploadExceptionInfo(SysOtherErrorCodeEnum.UPLOAD_FORMAT_ERROR.getDefaultMessage(),
								SysOtherErrorCodeEnum.UPLOAD_FORMAT_ERROR.getCode()));
			}
			// 文件大小是否限制
			if (!modelItem.isAllowVideoMaxFile((int) file.getSize() / num)) {
				throw new GlobalException(
						new UploadExceptionInfo(SysOtherErrorCodeEnum.UPLOAD_LIMIT_ERROR.getDefaultMessage(),
								SysOtherErrorCodeEnum.UPLOAD_LIMIT_ERROR.getCode()));
			}
		}
	}

	private File mark(MultipartFile file, CmsSite site) throws Exception {
		/** 文件头格式检查 */
		if (file != null && !com.jeecms.common.util.FileUtils.checkFileIsValid(file.getInputStream())) {
			log.error("文件头格式检查不合格");
			throw new GlobalException(
					new UploadExceptionInfo(SysOtherErrorCodeEnum.UPLOAD_FILE_ERROR.getDefaultMessage(),
							SysOtherErrorCodeEnum.UPLOAD_FILE_ERROR.getCode()));
		}
		String path = System.getProperty("java.io.tmpdir");
		path = java.text.Normalizer.normalize(path, java.text.Normalizer.Form.NFKD);
		File tempFile = new File(path, String.valueOf(System.currentTimeMillis()));
		file.transferTo(tempFile);
		CmsSiteConfig conf = site.getCmsSiteCfg();
		boolean imgMark = WaterMarkSet.img.getValue().equals(conf.getWatermarkSet());
		Color color;
		try {
			color = Color.decode(conf.getWatermarkTxtColour());
		} catch (NumberFormatException e) {
			color = Color.WHITE;
		}

		try {
			if (imgMark) {
				File markImg = new File(realPathResolver.get(site.getWatermarkRes()));
				imageScale.imageMark(tempFile, tempFile, file.getOriginalFilename(), 0, 0, conf.getWatermarkPosition(),
						markImg);
			} else {
				imageScale.imageMark(tempFile, tempFile, file.getOriginalFilename(), 0, 0, conf.getWatermarkPosition(),
						conf.getWatermarkTxt(), color, conf.getWatermarkTxtSize(), conf.getWatermarkTxtTransparency());
			}
		} catch (Exception e) {
			/** 图片过小可能导致生成水印图片错误，出现这种问题不抛出异常，继续操作 */
			log.error(e.getMessage());
		}

		return tempFile;
	}

	private File mark(File file, CmsSite site) throws Exception {
		/** 文件头格式检查 */
		if (file != null && !com.jeecms.common.util.FileUtils.checkFileIsValid(new FileInputStream(file))) {
			log.error("文件头格式检查不合格");
			throw new GlobalException(
					new UploadExceptionInfo(SysOtherErrorCodeEnum.UPLOAD_FILE_ERROR.getDefaultMessage(),
							SysOtherErrorCodeEnum.UPLOAD_FILE_ERROR.getCode()));
		}
		String path = System.getProperty("java.io.tmpdir");
		path = java.text.Normalizer.normalize(path, java.text.Normalizer.Form.NFKD);
		File tempFile = new File(path, String.valueOf(System.currentTimeMillis()));
		org.apache.commons.io.FileUtils.copyFile(file, tempFile);
		CmsSiteConfig conf = site.getCmsSiteCfg();
		boolean imgMark = WaterMarkSet.img.getValue().equals(conf.getWatermarkSet());
		try {
			if (imgMark) {
				File markImg = new File(realPathResolver.get(site.getWatermarkRes()));
				imageScale.imageMark(tempFile, tempFile, file.getName(), 0, 0, conf.getWatermarkPosition(), markImg);
			} else {
				imageScale.imageMark(tempFile, tempFile, file.getName(), 0, 0, conf.getWatermarkPosition(),
						conf.getWatermarkTxt(), Color.decode(conf.getWatermarkTxtColour()), conf.getWatermarkTxtSize(),
						conf.getWatermarkTxtTransparency());
			}
		} catch (Exception e) {
			/** 图片过小可能导致生成水印图片错误，出现这种问题不抛出异常，继续操作 */
			log.error(e.getMessage());
		}

		return tempFile;
	}

	/**
	 * 上传文库文档
	 *
	 * @param file
	 *            文件
	 * @param modelId
	 *            模型ID
	 * @param fieldName
	 *            字段名称
	 * @param site
	 *            访问站点
	 * @throws GlobalException
	 *             GlobalException
	 * @throws IOException
	 *             IOException
	 * @Title: upload
	 * @return: ResponseInfo ResponseInfo
	 */
	public ResponseInfo uploadDoc(MultipartFile file, Integer modelId, String fieldName, CmsSite site)
			throws GlobalException, IOException {
		if (site == null) {
			site = SystemContextUtils.getSite(RequestUtils.getHttpServletRequest());
		}
		validate(file, null, ResourceType.FILE, modelId, fieldName, site);
		// 得到文件名称+后缀
		String origName = file.getOriginalFilename();
		/**
		 * 单位KB
		 */
		Long fileSize = file.getSize() / 1024;
		String fileUrl = null;
		String ext = null;

		InputStream inputStream = file.getInputStream();
		try {
			// 得到文件后缀
			ext = FilenameUtils.getExtension(origName).toLowerCase(Locale.ENGLISH);
			// 默认文档文库路径
			String siteUploadPath = WebConstants.UPLOAD_WENKU_PATH + site.getPath();
			// 如果选择了优先FTP并且设置了FTP,则使用FTP上传; 否则如果设置了OSS,则使用OSS; 都没设置则使用本地上传
			if (UploadServerType.ftp.equals(site.getUploadServerType()) && site.getUploadFtp() != null) {
				UploadFtp ftp = site.getUploadFtp();
				String ftpUrl = ftp.getUrl();
				fileUrl = ftp.storeByExt(siteUploadPath, ext, inputStream);
				// 加上url前缀
				fileUrl = ftpUrl + fileUrl;
			} else if (UploadServerType.oss.equals(site.getUploadServerType()) && site.getUploadOss() != null) {
				UploadOss oss = site.getUploadOss();
				fileUrl = oss.storeByExt(siteUploadPath, ext, inputStream);
			} else {
				fileUrl = fileRepository.storeByExt(siteUploadPath, ext, file);
				String ctx = site.getContextPath();
				if (StringUtils.isNotBlank(ctx)) {
					fileUrl = ctx + fileUrl;
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			inputStream.close();
		}
		UploadResult uploadInfo = null;
		if (StringUtils.isNoneBlank(fileUrl)) {
			/** 去除格式后缀 */
			origName = origName.substring(0, origName.lastIndexOf("."));
			uploadInfo = new UploadResult(fileUrl, origName, fileSize, "", ResourcesSpaceData.getResourceType(ext));
			uploadInfo.setDuration(0);
		} else {
			throw new GlobalException(new UploadExceptionInfo());
		}
		return new ResponseInfo(uploadInfo);
	}

	@Autowired
	private ImageScaleService imageScale;
	@Autowired
	private RealPathResolver realPathResolver;
	@Autowired
	protected FileRepository fileRepository;
	@Autowired
	protected ResourcesSpaceService resourcesSpaceService;
	@Autowired
	private CmsModelService modelService;
}
