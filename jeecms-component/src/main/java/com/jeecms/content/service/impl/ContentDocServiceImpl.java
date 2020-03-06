/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.service.impl;

import com.jeecms.common.constants.UploadEnum.UploadServerType;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.upload.FileRepository;
import com.jeecms.common.upload.UploadUtils;
import com.jeecms.common.util.office.FileUtils;
import com.jeecms.common.util.office.OpenOfficeConverter;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.component.listener.AbstractContentListener;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.ContentExt;
import com.jeecms.content.service.ContentExtService;
import com.jeecms.content.service.ContentService;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.resource.domain.UploadFtp;
import com.jeecms.resource.domain.UploadOss;
import com.jeecms.resource.service.ResourcesSpaceDataService;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.GlobalConfigAttr;
import com.jeecms.system.service.GlobalConfigService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 内容文库服务实现
 *
 * @author: tom
 * @date: 2019年5月11日 下午6:49:19
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ContentDocServiceImpl extends AbstractContentListener {

    private static final Logger log = LoggerFactory.getLogger(ContentDocServiceImpl.class);

    @Autowired
    private ContentService contentService;
    @Autowired(required = false)
    private OpenOfficeConverter openOfficeConverter;
    @Autowired
    private GlobalConfigService globalConfigService;
    @Autowired
    private RealPathResolver realPathResolver;
    @Autowired
    protected FileRepository fileRepository;
    @Autowired
    private ResourcesSpaceDataService resourcesSpaceDataService;
    @Autowired
    private ContentExtService contentExtService;

    /**
     * 得到转换安装路径
     *
     * @throws GlobalException 异常
     **/
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public String getHomePath() throws GlobalException {
        GlobalConfigAttr attr = globalConfigService.get().getConfigAttr();
        return attr.getOpenOfficeCatalog();
    }

    /**
     * 得到转换端口
     *
     * @throws GlobalException 异常
     **/
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public String getOfficePort() throws GlobalException {
        GlobalConfigAttr attr = globalConfigService.get().getConfigAttr();
        return attr.getOpenOfficePort();
    }

    /**
     * 开始服务
     *
     * @throws GlobalException
     **/
    @PostConstruct
    public void startService() throws GlobalException {
        openOfficeConverter.startService(getHomePath(), getOfficePort());
    }

    @Override
    public void afterSave(Content content) throws GlobalException {
        CmsSite site = content.getSite();
        // 如果内容是发布状态，创建pdf文档，存在/u/cms/*下面
        if (content.isPublish()) {
            upload(content.getId(), site);
        }
    }

    @Override
    public void afterChange(Content content, Map<String, Object> map) throws GlobalException {
        // 现在是否发布状态
        boolean curr = content.isPublish();
        CmsSite site = content.getSite();
        /** 之前发布状态，现在非发布状态，不处理，只需处理现在是否发布，如果是，则生成 **/
        if (curr) {
            upload(content.getId(), site);
        }
    }

    /**
     * 删除内容之后的逻辑
     **/
    public void afterDelete(Content content) throws GlobalException {
        // 删除内容之后，直接删除创建的文件
        String url = content.getContentExt().getPdfUrl();
        if (StringUtils.isNotBlank(url)) {
            // 得到doc绝对路径
            String docRealPath = realPathResolver.get(url);
            File file = new File(docRealPath);
            if (file.exists()) {
                file.delete();
            }
            content.getContentExt().setPdfUrl("");
            //修改pdf文件路径
            contentService.update(content);
        }
    }

    /** 将转化后的PDF上传到服务器文件，存在/u/cms/*下面 **/
    /**
     * 如果选择了优先FTP并且设置了FTP,则使用FTP上传; 否则如果设置了OSS,则使用OSS; 都没设置则使用本地上传
     *
     * @throws Exception 异常
     **/
    @Async("asyncServiceExecutor")
    public void upload(Integer contentId, CmsSite site) throws GlobalException {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        String docRealPath = "";
        ContentExt contentExt = contentExtService.findById(contentId);
        String fileUrl = "";
        ResourcesSpaceData space = contentExt.getDocResource();
        fileUrl = this.conversionDoc(space, site);
        contentExt.setPdfUrl(fileUrl);
        //修改pdf文件路径
        contentExtService.updateAll(contentExt);
        contentExtService.flush();
    }

    /**
     * 处理文档
     *
     * @param space       内容文档对象
     * @param site        站点id
     * @param docRealPath
     * @param fileUrl
     * @throws GlobalException
     * @Title: conversionDoc
     * @return: String
     */
    public String conversionDoc(ResourcesSpaceData space, CmsSite site)
            throws GlobalException {
        String docRealPath = "";
        String fileUrl = "";
        // 得到内容原始文档路径
        if (space != null) {
        	//资源文件为PDF，则直接返回
        	if (space.getSuffix().equals(".pdf")) {
        		return space.getUrl();
			}
            // 得到默认上传路径
            String siteUploadPath = WebConstants.UPLOAD_PATH
                    + site.getPath() + "/"
                    + UploadUtils.generateMonthname();
            // 得到资源路径
            String origName = space.getUrl();
            // 得到文件名称
            String fileName = FileUtils.getFileName(origName);
            File docFile = resourcesSpaceDataService.getFile(space);
            // 得到doc绝对路径
            docRealPath = docFile.getAbsolutePath();
            //pdf输入路径
            // pdf输入路径
            String pdfPath = realPathResolver.get(siteUploadPath);
            // 转换pdf
            File pdfFile = openOfficeConverter.convertToPdf(docRealPath,
                    pdfPath + "/", fileName);
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(pdfFile);
                // 默认pdf
                String ext = "pdf";
                if (UploadServerType.ftp.equals(site.getUploadServerType()) && site.getUploadFtp() != null) {
                    siteUploadPath = WebConstants.UPLOAD_PATH + site.getPath();
                    UploadFtp ftp = site.getUploadFtp();
                    String ftpUrl = ftp.getUrl();
                    fileUrl = ftp.storeByExt(siteUploadPath, ext, inputStream);
                    // 加上url前缀
                    fileUrl = ftpUrl + fileUrl;
                } else if (UploadServerType.oss.equals(site.getUploadServerType())
                        && site.getUploadOss() != null) {
                    siteUploadPath = WebConstants.UPLOAD_PATH
                            + site.getPath();
                    UploadOss oss = site.getUploadOss();
                    fileUrl = oss.storeByExt(siteUploadPath, ext, inputStream);
                } else {
                    // 不加水印以及不是图片类型
                    fileUrl = siteUploadPath + "/" + pdfFile.getName();
                }
            } catch (Exception e) {
                log.error("上传失败，原因:" + e.getMessage());
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    //删除临时文件以及文件夹
                    // 删除临时文件以及文件夹
                    if (docFile.exists()) {
                        org.apache.commons.io.FileUtils
                                .deleteDirectory(docFile.getParentFile());
                        org.apache.commons.io.FileUtils.deleteDirectory(docFile.getParentFile());
                    }
                } catch (IOException e) {
                    log.error("上传失败，原因{}" + e.getMessage());
                }
            }
        }
        return fileUrl;
    }

}
