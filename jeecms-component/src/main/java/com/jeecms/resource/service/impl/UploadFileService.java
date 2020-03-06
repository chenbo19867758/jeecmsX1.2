package com.jeecms.resource.service.impl;//package com.jeecms.collect.data.service;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.constants.UploadEnum.UploadServerType;
import com.jeecms.common.ueditor.ResourceType;
import com.jeecms.common.util.FileBase64Utils;
import com.jeecms.common.util.ImageUtil;
import com.jeecms.common.util.TransportUtil;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.resource.domain.UploadFtp;
import com.jeecms.resource.domain.UploadOss;
import com.jeecms.resource.domain.dto.UploadResult;
import com.jeecms.resource.service.ResourcesSpaceDataService;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.util.SystemContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.UUID;

@Component
public class UploadFileService extends UploadService {
    @Autowired
    private ResourcesSpaceDataService resourcesService;
    private static Logger log = LoggerFactory.getLogger(UploadFileService.class);

    /**
     *
     * @param fileUrl 原要采集的地址
     * @param site
     * @param relative 是否使用相对路径，true是 false则是当前站点+上传的全路径
     * @return
     */
    public String uploadFileByUrl(String fileUrl,CmsSite site,boolean relative) {
        log.info(fileUrl);
        String username = UUID.randomUUID().toString();
        String tempFilePath = "/"+username+"_"+System.currentTimeMillis();
        File fileTemp = null;
        File file = null;
        try {
            if(fileUrl.contains("base64,")&&fileUrl.contains("com.jeecms.collect.data:image")){
                int b = fileUrl.indexOf("base64,");
                fileTemp = FileBase64Utils.generateImage(fileUrl.substring(b+"base64,".length()),tempFilePath);
            } else{
                fileTemp = TransportUtil.downloadFromUrl(fileUrl, tempFilePath);
            }
            String imageFormat = ImageUtil.realImgFormat(fileTemp);
            file = new File(fileTemp + "." + imageFormat);
            fileTemp.renameTo(file);
            UploadResult uploadResult  = super.doUpload(file,site.getConfig().getWatermarkOpen(),site.getUploadPath(),ResourceType.IMAGE,site);
            Integer duration = uploadResult.getDuration();
            UploadFtp ftp = null;
			UploadOss oss = null;
			if (UploadServerType.ftp.equals(site.getUploadServerType()) && site.getUploadFtp() != null) {
				ftp = site.getUploadFtp();
			} else if (UploadServerType.oss.equals(site.getUploadServerType()) && site.getUploadOss() != null) {
				oss = site.getUploadOss();
			}
			Integer userId  = 1;
            CoreUser user  = SystemContextUtils.getCoreUser();
            if(user!=null){
                userId = user.getId();
            }
            resourcesService.save(userId, null, uploadResult.getOrigName(),
                    uploadResult.getFileSize().intValue(), uploadResult.getFileUrl(), uploadResult.getDimensions(),
                    uploadResult.getResourceType(), false, site.getId(), duration, ftp, oss);
            fileUrl = uploadResult.getFileUrl();
            /**如果是相对地址则需要拼接否则是oss ftp的地址不需要拼接*/
            if(!relative&& StringUtils.isNotBlank(fileUrl)&&!fileUrl.startsWith("http")){
                /***去除一个/ 多余的 */
                fileUrl = fileUrl.substring(1);
                fileUrl = site.getUrlDynamicForFix(true)+fileUrl;
            }
            return fileUrl;
        }catch (Exception e){
            //e.printStackTrace();
            log.error(e.getMessage());
        }finally {
            if(null != file){
                file.delete();
            }
            if(null != fileTemp){
                fileTemp.delete();
            }
        }
        return "";
    }

    public Integer uploadFileByUrlForId(String fileUrl,CmsSite site) {
        log.info(fileUrl);
        String username = UUID.randomUUID().toString();
        String path = System.getProperty("java.io.tmpdir");
        path = java.text.Normalizer.normalize(path, java.text.Normalizer.Form.NFKD);
        String tempPath = path+"/"+String.valueOf(System.currentTimeMillis());
        File fileTemp =  new File(path, String.valueOf(System.currentTimeMillis()));
        File file = null;
        try {
            fileTemp = TransportUtil.downloadFromUrl(fileUrl, tempPath);
            String imageFormat = ImageUtil.realImgFormat(fileTemp);
           // file = new File(fileTemp + "." + imageFormat);
            file = new File(path, String.valueOf(System.currentTimeMillis()+"."+imageFormat));
            fileTemp.renameTo(file);
            UploadResult uploadResult = super.doUpload(file,site.getConfig().getWatermarkOpen(),site.getUploadPath(),ResourceType.IMAGE,site);
            Integer duration = uploadResult.getDuration();
            UploadFtp ftp = null;
			UploadOss oss = null;
            if (UploadServerType.ftp.equals(site.getUploadServerType()) && site.getUploadFtp() != null) {
                ftp = site.getUploadFtp();
            } else if (UploadServerType.oss.equals(site.getUploadServerType()) && site.getUploadOss() != null) {
                oss = site.getUploadOss();
            }
            Integer userId  = 1;
            CoreUser user  = SystemContextUtils.getCoreUser();
            if(user!=null){
                userId = user.getId();
            }
            ResourcesSpaceData resourceData = resourcesService.save(userId, null, uploadResult.getOrigName(),
                    uploadResult.getFileSize().intValue(), uploadResult.getFileUrl(), uploadResult.getDimensions(),
                    uploadResult.getResourceType(), false, site.getId(), duration, ftp, oss);
            if (resourceData != null) {
                uploadResult.setResourceId(resourceData.getId());
            }
            return resourceData.getId();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            if(null != file){
                file.delete();
                log.info("file删除临时文件");
            }
            if(null != fileTemp){
                fileTemp.delete();
                log.info("fileTemp删除临时文件");
            }
        }

    }
}
