package com.jeecms.resource.base;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.constants.UploadEnum.UploadServerType;
import com.jeecms.common.constants.UploadEnum.WaterMarkSet;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.UploadExceptionInfo;
import com.jeecms.common.exception.error.SysOtherErrorCodeEnum;
import com.jeecms.common.image.ImageScaleService;
import com.jeecms.common.image.ImageUtils;
import com.jeecms.common.ueditor.LocalizedMessages;
import com.jeecms.common.ueditor.ResourceType;
import com.jeecms.common.ueditor.Utils;
import com.jeecms.common.upload.FileRepository;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.common.web.util.ResponseUtils;
import com.jeecms.resource.domain.UploadFtp;
import com.jeecms.resource.domain.UploadOss;
import com.jeecms.resource.service.ImageService;
import com.jeecms.resource.ueditor.PathFormat;
import com.jeecms.resource.ueditor.define.AppInfo;
import com.jeecms.resource.ueditor.define.BaseState;
import com.jeecms.resource.ueditor.define.MultiState;
import com.jeecms.resource.ueditor.define.State;
import com.jeecms.resource.ueditor.hunter.ImageHunter;
import com.jeecms.resource.ueditor.upload.StorageManager;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.CmsSiteConfig;
import com.jeecms.system.domain.GlobalConfig;
import com.jeecms.util.SystemContextUtils;

/**
 * ueditor 上传基类
 * 
 * @author: tom
 * @date: 2018年12月17日 下午3:23:47
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@RestController
public class BaseUeditorUploadAct {

        private static final Logger log = LoggerFactory.getLogger(BaseUeditorUploadAct.class);

        /**
         * 状态
         */
        private static final String STATE = "state";
        /**
         * 上传成功
         */
        private static final String SUCCESS = "SUCCESS";
        /**
         * URL
         */
        private static final String URL = "url";
        /**
         * 文件原名
         */
        private static final String ORIGINAL = "original";
        /**
         * TITLE
         */
        private static final String TITLE = "title";

        /**
         * 上传
         * @Title: upload
         * @param typeStr 类型
         * @param mark 是否水印
         * @param request  HttpServletRequest
         * @param response HttpServletResponse
         * @throws Exception Exception
         */
        public void upload(@RequestParam(value = "Type", required = false) String typeStr, Boolean mark,
                        HttpServletRequest request, HttpServletResponse response) throws Exception {
                responseInit(response);
                if (Utils.isEmpty(typeStr)) {
                        typeStr = "File";
                }
                JSONObject json = new JSONObject();
                JSONObject ob = validateUpload(request, typeStr);
                if (ob == null) {
                        json = doUpload(request, typeStr, mark);
                } else {
                        json = ob;
                }
                ResponseUtils.renderText(response, json.toString());
        }

        /**
         * 获取远程文件
         * @Title: getRemoteImage
         * @param request HttpServletRequest
         * @param response HttpServletResponse
         * @throws Exception  Exception
         */
        public void getRemoteImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
                String[] list = request.getParameterValues("source[]");
                State state = new ImageHunter(imgSvc).captureByApi("", list);
                ResponseUtils.renderJson(response, state.toJSONString());
        }

        /**
         * 在线图片管理（选择最近或站点图片）
         * 
         * @param request
         *                HttpServletRequest
         * @param response
         *                HttpServletResponse
         * @throws Exception
         *                 Exception
         */
        public void imageManager(Integer picNum, Boolean insite, HttpServletRequest request,
                        HttpServletResponse response) throws Exception {
                State state = listFile(request, getStartIndex(request));
                ResponseUtils.renderText(response, state.toJSONString());
        }

        public void scrawlImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
                State state = scrawlImage(request.getParameter("upfile"));
                ResponseUtils.renderText(response, state.toString());
        }
        
        /**
         * 抓取远程图片
         * @Title: scrawlImage
         * @param content 远程图片地址
         * @return: State State
         */
        public State scrawlImage(String content) {
                byte[] data = decode(content);
                String suffix = "jpg";
                HttpServletRequest request = RequestUtils.getHttpServletRequest();
                GlobalConfig config = SystemContextUtils.getGlobalConfig(request);
                CmsSite site = SystemContextUtils.getSite(request);
                String savePath = request.getContextPath() + config.getUploadPath() + "/temp.jpg";
                String physicalPath = (String) realPathResolver.get(savePath);
                State storageState = StorageManager.saveBinaryFile(data, physicalPath);
                File file = new File(physicalPath);
                FileInputStream fileInputStream = null;
                FileInputStream fileInputStream2 = null;
                String fileUrl = "";
                try {
                        fileInputStream = new FileInputStream(file);
                        Boolean mark = null;
                        CmsSiteConfig conf = site.getCmsSiteCfg();
                        if (mark == null) {
                                mark = conf.getWatermarkOpen();
                        }
                        if (UploadServerType.ftp.equals(site.getUploadServerType()) && site.getUploadFtp() != null) {
                                UploadFtp ftp = site.getUploadFtp();
                                if (mark) {
                                        File tempFile = mark(file, site);
                                        fileInputStream2 = new FileInputStream(tempFile);
                                        fileUrl = ftp.storeByExt(config.getUploadPath(), suffix, fileInputStream2);
                                        tempFile.delete();
                                } else {
                                        fileUrl = ftp.storeByExt(config.getUploadPath(), suffix, fileInputStream);
                                }
                                // 加上url前缀
                                fileUrl = ftp.getUrl() + fileUrl;
                        } else if (UploadServerType.oss.equals(site.getUploadServerType())
                                        && site.getUploadOss() != null) {
                                UploadOss oss = site.getUploadOss();
                                if (mark) {
                                        File tempFile = mark(file, site);
                                        fileInputStream2 = new FileInputStream(tempFile);
                                        fileUrl = oss.storeByExt(config.getUploadPath(), suffix, fileInputStream2);
                                        tempFile.delete();
                                } else {
                                        fileUrl = oss.storeByExt(config.getUploadPath(), suffix, fileInputStream);
                                }
                        } else {
                                if (mark) {
                                        File tempFile = mark(file, site);
                                        fileUrl = fileRepository.storeByExt(config.getUploadPath(), suffix, tempFile);
                                        tempFile.delete();
                                } else {
                                        fileUrl = fileRepository.storeByExt(config.getUploadPath(), suffix, file);
                                }
                                // 加上部署路径
                                fileUrl = request.getContextPath() + fileUrl;
                        }
                } catch (Exception e) {
                        log.error(e.getMessage());
                } finally {
                        try {
                                if (fileInputStream != null) {
                                        fileInputStream.close();
                                }
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                        try {
                                if (fileInputStream2 != null) {
                                        fileInputStream2.close();
                                }
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
                if (storageState.isSuccess()) {
                        storageState.putInfo("url", fileUrl);
                        storageState.putInfo("type", suffix);
                        storageState.putInfo("original", "");
                }
                return storageState;
        }

        private JSONObject doUpload(HttpServletRequest request, String typeStr, Boolean mark) throws Exception {
                ResourceType type = ResourceType.getDefaultResourceType(typeStr);
                JSONObject result = new JSONObject();
                InputStream is = null;
                try {
                        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

                        // We upload just one file at the same time
                        MultipartFile uplFile = multipartRequest.getFileMap().entrySet().iterator().next().getValue();
                        if (ImageUtils.isImage(uplFile.getInputStream())) {
                                type = ResourceType.IMAGE;
                        }
                        // Some browsers transfer the entire source path not just the
                        // filename
                        String filename = FilenameUtils.getName(uplFile.getOriginalFilename());
                        log.debug("Parameter NewFile: {}", filename);
                        String ext = FilenameUtils.getExtension(filename);
                        if (type.isDeniedExtension(ext)) {
                                result.put(STATE, LocalizedMessages.getInvalidFileTypeSpecified(request));
                                return result;
                        }
                        if (type.equals(ResourceType.IMAGE) && !ImageUtils.isImage(uplFile.getInputStream())) {
                                result.put(STATE, LocalizedMessages.getInvalidFileTypeSpecified(request));
                                return result;
                        }
                        String fileUrl;
                        GlobalConfig config = SystemContextUtils.getGlobalConfig(request);
                        boolean isImg = type.equals(ResourceType.IMAGE);
                        CmsSite site = SystemContextUtils.getSite(request);
                        /**未手动指定则获取站点统一配置*/
                        if (mark == null) {
                                mark = site.getCmsSiteCfg().getWatermarkOpen();
                        }
                        // 如果选择了优先FTP并且设置了FTP,则使用FTP上传; 否则如果设置了OSS,则使用OSS; 都没设置则使用本地上传
                        if (UploadServerType.ftp.equals(site.getUploadServerType()) && site.getUploadFtp() != null) {
                                UploadFtp ftp = site.getUploadFtp();
                                if (mark && isImg) {
                                        File tempFile = mark(uplFile, site);
                                        is = new FileInputStream(tempFile);
                                        fileUrl = ftp.storeByExt(config.getUploadPath(), ext, is);
                                        tempFile.delete();
                                } else {
                                        is = uplFile.getInputStream();
                                        fileUrl = ftp.storeByExt(config.getUploadPath(), ext, is);
                                }
                                // 加上url前缀
                                fileUrl = ftp.getUrl() + fileUrl;
                        } else if (UploadServerType.oss.equals(site.getUploadServerType())
                                        && site.getUploadOss() != null) {
                                UploadOss oss = site.getUploadOss();
                                if (mark && isImg) {
                                        File tempFile = mark(uplFile, site);
                                        is = new FileInputStream(tempFile);
                                        fileUrl = oss.storeByExt(config.getUploadPath(), ext, is);
                                        tempFile.delete();
                                } else {
                                        is = uplFile.getInputStream();
                                        fileUrl = oss.storeByExt(config.getUploadPath(), ext, is);
                                }
                        } else {
                                if (mark && isImg) {
                                        File tempFile = mark(uplFile, site);
                                        fileUrl = fileRepository.storeByExt(config.getUploadPath(), ext, tempFile);
                                        tempFile.delete();
                                } else {
                                        fileUrl = fileRepository.storeByExt(config.getUploadPath(), ext, uplFile);
                                }

                                fileUrl = request.getContextPath() + fileUrl;
                        }
                        // 需要给页面参数(参考ueditor的/jsp/imageUp.jsp)
                        result.put(URL, fileUrl);
                        result.put(ORIGINAL, filename);
                        result.put("type", ext);
                        // result.put("type", "." + ext);
                        result.put(STATE, SUCCESS);
                        result.put(TITLE, filename);
                        // result.put(FILETYPE, "." + ext);
                        return result;
                } catch (IOException e) {
                        result.put(STATE, LocalizedMessages.getFileUploadWriteError(request));
                        return result;
                } catch (GlobalException e) {
                        throw e;
                } finally {
                        if (is != null) {
                                is.close();
                        }
                }
        }

        /**
         * 文件列表
         * @Title: listFile
         * @param request HttpServletRequest
         * @param index index
         * @return: State
         */
        public State listFile(HttpServletRequest request, int index) {
                GlobalConfig config = SystemContextUtils.getGlobalConfig(request);
                String uploadPath = config.getUploadPath();
                File dir = new File(realPathResolver.get(uploadPath));
                State state = null;
                if (!dir.exists()) {
                        return new BaseState(false, AppInfo.NOT_EXIST);
                }

                if (!dir.isDirectory()) {
                        return new BaseState(false, AppInfo.NOT_DIRECTORY);
                }

                Collection<File> list = FileUtils.listFiles(dir, null, true);

                if (index < 0 || index > list.size()) {
                        state = new MultiState(true);
                } else {
                        Object[] fileList = Arrays.copyOfRange(list.toArray(), index, index + 20);
                        state = getState(realPathResolver.get("/"), request.getContextPath(), fileList);
                }

                state.putInfo("start", index);
                state.putInfo("total", list.size());

                return state;

        }

        /**
         * 获取开始索引
         * @Title: getStartIndex
         * @param request HttpServletRequest 
         * @return: int 
         */
        public int getStartIndex(HttpServletRequest request) {
                String start = request.getParameter("start");
                try {
                        return Integer.parseInt(start);
                } catch (Exception e) {
                        return 0;
                }
        }

        private State getState(String rootPath, String contextPath, Object[] files) {

                MultiState state = new MultiState(true);
                BaseState fileState = null;

                File file = null;

                for (Object obj : files) {
                        if (obj == null) {
                                break;
                        }
                        file = (File) obj;
                        fileState = new BaseState(true);
                        fileState.putInfo("url", PathFormat.format(getPath(rootPath, contextPath, file)));
                        state.addState(fileState);
                }
                return state;
        }

        private String getPath(String rootPath, String contextPath, File file) {
                String path = file.getAbsolutePath();
                if (StringUtils.isNotBlank(contextPath)) {
                        return contextPath + path.replace(rootPath, "/");
                } else {
                        return path.replace(rootPath, "/");
                }

        }

        private void responseInit(HttpServletResponse response) {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/html");
                // response.setContentType("application/json;charset=UTF-8");
                response.setHeader("Cache-Control", "no-cache");
        }

        private JSONObject validateUpload(HttpServletRequest request, String typeStr)
                        throws JSONException, GlobalException, IOException {
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                MultipartFile uplFile = multipartRequest.getFileMap().entrySet().iterator().next().getValue();
                String filename = FilenameUtils.getName(uplFile.getOriginalFilename());
                String ext = FilenameUtils.getExtension(filename).toLowerCase(Locale.ENGLISH);
                CmsSite site = SystemContextUtils.getSite(request);
                /** 文件头格式检查 */
                if (!com.jeecms.common.util.FileUtils.checkFileIsValid(uplFile.getInputStream())) {
                        throw new GlobalException(new UploadExceptionInfo(
                                        SysOtherErrorCodeEnum.UPLOAD_FILE_ERROR.getDefaultMessage(),
                                        SysOtherErrorCodeEnum.UPLOAD_FILE_ERROR.getCode()));
                }
                int num = 1024;
                ResourceType resourceType = ResourceType.getDefaultResourceType(typeStr);
                if (ResourceType.IMAGE.equals(resourceType)) {
                        // 文件格式是否限制
                        if (!site.isAllowPicSuffix(ext)) {
                                throw new GlobalException(new UploadExceptionInfo(
                                                SysOtherErrorCodeEnum.UPLOAD_FORMAT_ERROR.getDefaultMessage(),
                                                SysOtherErrorCodeEnum.UPLOAD_FORMAT_ERROR.getCode()));
                        }
                        // 文件大小是否限制
                        if (!site.isAllowPicMaxFile((int) uplFile.getSize() / num)) {
                                throw new GlobalException(new UploadExceptionInfo(
                                                SysOtherErrorCodeEnum.UPLOAD_LIMIT_ERROR.getDefaultMessage(),
                                                SysOtherErrorCodeEnum.UPLOAD_LIMIT_ERROR.getCode()));
                        }
                } else if (ResourceType.AUDIO.equals(resourceType)) {
                        // 文件格式是否限制
                        if (!site.isAllowAudioSuffix(ext)) {
                                throw new GlobalException(new UploadExceptionInfo(
                                                SysOtherErrorCodeEnum.UPLOAD_FORMAT_ERROR.getDefaultMessage(),
                                                SysOtherErrorCodeEnum.UPLOAD_FORMAT_ERROR.getCode()));
                        }
                        // 文件大小是否限制
                        if (!site.isAllowAudioMaxFile((int) uplFile.getSize() / num)) {
                                throw new GlobalException(new UploadExceptionInfo(
                                                SysOtherErrorCodeEnum.UPLOAD_LIMIT_ERROR.getDefaultMessage(),
                                                SysOtherErrorCodeEnum.UPLOAD_LIMIT_ERROR.getCode()));
                        }
                } else if (ResourceType.FILE.equals(resourceType)) {
                        // 文件格式是否限制
                        if (!site.isAllowAttachmentSuffix(ext)) {
                                throw new GlobalException(new UploadExceptionInfo(
                                                SysOtherErrorCodeEnum.UPLOAD_FORMAT_ERROR.getDefaultMessage(),
                                                SysOtherErrorCodeEnum.UPLOAD_FORMAT_ERROR.getCode()));
                        }
                        // 文件大小是否限制
                        if (!site.isAllowAttachmentMaxFile((int) uplFile.getSize() / num)) {
                                throw new GlobalException(new UploadExceptionInfo(
                                                SysOtherErrorCodeEnum.UPLOAD_LIMIT_ERROR.getDefaultMessage(),
                                                SysOtherErrorCodeEnum.UPLOAD_LIMIT_ERROR.getCode()));
                        }
                } else if (ResourceType.MEDIA.equals(resourceType)) {
                        // 文件格式是否限制
                        if (!site.isAllowVideoSuffix(ext)) {
                                throw new GlobalException(new UploadExceptionInfo(
                                                SysOtherErrorCodeEnum.UPLOAD_FORMAT_ERROR.getDefaultMessage(),
                                                SysOtherErrorCodeEnum.UPLOAD_FORMAT_ERROR.getCode()));
                        }
                        // 文件大小是否限制
                        if (!site.isAllowVideoMaxFile((int) uplFile.getSize() / num)) {
                                throw new GlobalException(new UploadExceptionInfo(
                                                SysOtherErrorCodeEnum.UPLOAD_LIMIT_ERROR.getDefaultMessage(),
                                                SysOtherErrorCodeEnum.UPLOAD_LIMIT_ERROR.getCode()));
                        }
                }
                return null;
        }

        private File mark(File file, CmsSite site) throws Exception {
                String path = System.getProperty("java.io.tmpdir");
                path = java.text.Normalizer.normalize(path, java.text.Normalizer.Form.NFKD);
                File tempFile = new File(path, String.valueOf(System.currentTimeMillis()));
                FileUtils.copyFile(file, tempFile);
                CmsSiteConfig conf = site.getCmsSiteCfg();
                boolean imgMark = WaterMarkSet.img.getValue().equals(conf.getWatermarkSet());
                if (imgMark) {
                        File markImg = new File(realPathResolver.get(site.getWatermarkRes()));
                        imageScale.imageMark(tempFile, tempFile,file.getName(), 0, 0, conf.getWatermarkPosition(), markImg);
                } else {
                        imageScale.imageMark(tempFile, tempFile,file.getName(), 0, 0, conf.getWatermarkPosition(),
                                        conf.getWatermarkTxt(), Color.decode(conf.getWatermarkTxtColour()),
                                        conf.getWatermarkTxtSize(), conf.getWatermarkTxtTransparency());
                }
                return tempFile;
        }

        private File mark(MultipartFile file, CmsSite site) throws Exception  {
                /** 文件头格式检查 */
                if (file != null && !com.jeecms.common.util.FileUtils.checkFileIsValid(file.getInputStream())) {
                        throw new GlobalException(new UploadExceptionInfo(
                                        SysOtherErrorCodeEnum.UPLOAD_FILE_ERROR.getDefaultMessage(),
                                        SysOtherErrorCodeEnum.UPLOAD_FILE_ERROR.getCode()));
                }
                String path = System.getProperty("java.io.tmpdir");
                path = java.text.Normalizer.normalize(path, java.text.Normalizer.Form.NFKD);
                File tempFile = new File(path, String.valueOf(System.currentTimeMillis()));
                file.transferTo(tempFile);
                CmsSiteConfig conf = site.getCmsSiteCfg();
                boolean imgMark = WaterMarkSet.img.getValue().equals(conf.getWatermarkSet());
                if (imgMark) {
                        File markImg = new File(realPathResolver.get(site.getWatermarkRes()));
                        imageScale.imageMark(tempFile, tempFile,file.getOriginalFilename(), 0, 0, conf.getWatermarkPosition(), markImg);
                } else {
                        imageScale.imageMark(tempFile, tempFile,file.getOriginalFilename(), 0, 0, conf.getWatermarkPosition(),
                                        conf.getWatermarkTxt(), Color.decode(conf.getWatermarkTxtColour()),
                                        conf.getWatermarkTxtSize(), conf.getWatermarkTxtTransparency());
                }
                return tempFile;
        }

        

        private static byte[] decode(String content) {
                return Base64.decodeBase64(content);
        }

        private FileRepository fileRepository;
        private ImageScaleService imageScale;
        private RealPathResolver realPathResolver;
        @Autowired
        private ImageService imgSvc;

        @Autowired
        public void setFileRepository(FileRepository fileRepository) {
                this.fileRepository = fileRepository;
        }

        @Autowired
        public void setImageScale(ImageScaleService imageScale) {
                this.imageScale = imageScale;
        }

        @Autowired
        public void setRealPathResolver(RealPathResolver realPathResolver) {
                this.realPathResolver = realPathResolver;
        }

}
