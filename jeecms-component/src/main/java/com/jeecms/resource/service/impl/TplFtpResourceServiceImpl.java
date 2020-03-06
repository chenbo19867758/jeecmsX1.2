package com.jeecms.resource.service.impl;

import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionInfo;
import com.jeecms.common.exception.error.SiteErrorCodeEnum;
import com.jeecms.common.exception.error.SysOtherErrorCodeEnum;
import com.jeecms.common.file.FileWrap;
import com.jeecms.common.util.Zipper;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.resource.domain.UploadFtp;
import com.jeecms.resource.service.Tpl;
import com.jeecms.resource.service.TplResourceService;
import com.jeecms.resource.service.TplService;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.exception.SiteExceptionInfo;
import com.jeecms.system.service.FtpService;
import com.jeecms.system.service.GlobalConfigService;
import com.jeecms.util.FrontUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import static com.jeecms.common.constants.WebConstants.SPT;
import static com.jeecms.common.constants.WebConstants.UTF8;
import static com.jeecms.common.util.FrontUtilBase.RES_EXP;

/**
 * @author: tom
 * @date: 2019年3月5日 下午4:48:37
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
@Transactional(rollbackFor = Exception.class)
@ConditionalOnProperty(name = "freemarker.resources.type", havingValue = "ftp", matchIfMissing = false)
public class TplFtpResourceServiceImpl implements TplResourceService {
    private static final Logger log = LoggerFactory.getLogger(TplFtpResourceServiceImpl.class);

    @Autowired
    private FtpService ftpService;
    @Autowired
    private GlobalConfigService configService;
    @Autowired
    private TplService tplService;

    @Override
    public List<FtpTpl> listFile(String path, boolean dirAndEditable, CmsSite site) throws IOException, GlobalException {
        UploadFtp uploadFtp = getUploadFtp();
        FtpTpl ftpTpl = new FtpTpl(uploadFtp);
        FTPFile[] ftpFiles = ftpTpl.list(path);
        List<FtpTpl> list = new ArrayList<FtpTpl>(ftpFiles != null ? ftpFiles.length : 0);
        for (FTPFile f : ftpFiles) {
            list.add(new FtpTpl(f, path, uploadFtp));
        }
        return list;
    }

    @Override
    public void createDir(String path, String dirName, CmsSite site) throws IOException, GlobalException {
        UploadFtp uploadFtp = getUploadFtp();
        FtpTpl ftpTpl = new FtpTpl(uploadFtp);
        ftpTpl.saveDir(path + WebConstants.SPT + dirName);
    }

    @Override
    public void saveFile(String path, MultipartFile file, Boolean isCover) throws GlobalException {
        UploadFtp uploadFtp = getUploadFtp();
        FtpTpl ftpTpl = new FtpTpl(uploadFtp);
        try {
            String fileName = file.getOriginalFilename();
            //isCover为空或者isCover为false贼保留原文件
            if (isCover == null || !isCover) {
                fileName = filename(path, fileName, 0);
            }
            ftpTpl.storeByFilename(path + WebConstants.SPT + fileName, file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            String msg = MessageResolver.getMessage(SysOtherErrorCodeEnum.UPLOAD_FILE_ERROR.getCode(),
                    SysOtherErrorCodeEnum.UPLOAD_FILE_ERROR.getDefaultMessage());
            throw new GlobalException(
                    new SystemExceptionInfo(SysOtherErrorCodeEnum.UPLOAD_FILE_ERROR.getCode(), msg));
        }
    }

    @Override
    public void createFile(String path, String filename, String data, CmsSite site) throws GlobalException {
        InputStream in = null;
        File parent = new File(realPathResolver.get(path));
        parent.mkdirs();
        File file = new File(parent, filename);
        try {
            FileUtils.writeStringToFile(file, data, UTF8);
            UploadFtp uploadFtp = getUploadFtp();
            FtpTpl ftpTpl = new FtpTpl(uploadFtp);
            in = new FileInputStream(file);
            ftpTpl.storeByFilename(path + WebConstants.SPT + filename, in);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Tpl readFile(String name) throws GlobalException {
        UploadFtp uploadFtp = getUploadFtp();
        String path = FilenameUtils.getFullPath(name);
        FtpTpl ftpTpl = new FtpTpl(uploadFtp, path);
        FTPFile ftpFile = null;
        try {
            ftpFile = ftpTpl.get(name);
        } catch (IOException e) {
            throw new GlobalException(new SiteExceptionInfo(
                    SiteErrorCodeEnum.FTP_CONNECTION_ERROR.getDefaultMessage(),
                    SiteErrorCodeEnum.FTP_CONNECTION_ERROR.getCode()));
        }
        if (ftpFile != null) {
            ftpTpl = new FtpTpl(ftpFile, path, uploadFtp);
            return ftpTpl;
        } else {
            return null;
        }
    }

    @Override
    public void updateFile(String root, String name, String newName, String data, CmsSite site) throws IOException, GlobalException {
        createFile(root, newName, data, site);
        String[] names = {root + WebConstants.SPT + name};
        //删除旧的文件，这样旧相当于创建了一个修改了原文件
        if (!newName.equals(name)) {
            delete(names, site);
        }
    }

    @Override
    public int delete(String[] names, CmsSite site) throws IOException, GlobalException {
        int count = 0;
        UploadFtp uploadFtp = getUploadFtp();
        FtpTpl ftpTpl = new FtpTpl(uploadFtp);
        for (String name : names) {
            if (ftpTpl.deleteFile(name)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void rename(String origName, String destName, CmsSite site) throws IOException, GlobalException {
        UploadFtp uploadFtp = getUploadFtp();
        FtpTpl ftpTpl = new FtpTpl(uploadFtp);
        ftpTpl.reName(origName, destName);
    }

    @Override
    public void copyTplAndRes(CmsSite from, CmsSite to) throws IOException {
        UploadFtp ftp = from.getUploadFtp();
        //得到原站点模板路径
        String fromTpl = from.getTplPath();
        //得到资源路径
        String fromRes = from.getResourcePath();
        //得到目标模板路径
        String toTpl = to.getTplPath();
        //得到目标资源路径
        String toRes = to.getResourcePath();
        ftp.copyDirectiory(fromTpl, toTpl);
        ftp.copyDirectiory(fromRes, toRes);
    }

    @Override
    public void delTplAndRes(CmsSite site) throws IOException, GlobalException {
        UploadFtp uploadFtp = getUploadFtp();
        FtpTpl ftpTpl = new FtpTpl(uploadFtp);
        ftpTpl.deleteFile(site.getTplPath());
        ftpTpl.deleteFile(site.getResourcePath());
    }

    @Override
    public String[] getSolutions(String path) throws GlobalException {
        UploadFtp uploadFtp = getUploadFtp();
        FtpTpl ftpTpl = new FtpTpl(uploadFtp);
        String[] solutions = null;
        try {
            FTPFile[] files = ftpTpl.list(path);
            solutions = new String[files.length];
            int i = 0;
            for (FTPFile file : files) {
            	//只过滤文件夹
            	if (file.isDirectory()) {
					solutions[i] = file.getName();
					i++;
				}
            }
        } catch (IOException e) {
            throw new GlobalException(new SiteExceptionInfo(
                    SiteErrorCodeEnum.FTP_CONNECTION_ERROR.getDefaultMessage(),
                    SiteErrorCodeEnum.FTP_CONNECTION_ERROR.getCode()));
        }
        return solutions;
    }

    @Override
    public List<Zipper.FileEntry> export(CmsSite site, String solution) throws GlobalException {
        UploadFtp uploadFtp = getUploadFtp();
        FtpTpl ftpTpl = new FtpTpl(uploadFtp);
        ftpTpl.retrieveFile(solution, site.getTplPath(),
                realPathResolver.get(site.getTplPath()));
        ftpTpl.retrieveFile(solution, site.getResourcePath(),
            realPathResolver.get(site.getResourcePath()));
        List<Zipper.FileEntry> fileEntrys = new ArrayList<Zipper.FileEntry>();
        File tpl = new File(realPathResolver.get(site.getTplPath()), solution);
        fileEntrys.add(new Zipper.FileEntry("", "", tpl));
        File res = new File(realPathResolver.get(site.getResourcePath()), solution);
        if (res.exists()) {
            for (File r : res.listFiles()) {
                fileEntrys.add(new Zipper.FileEntry(FrontUtils.RES_EXP, r));
            }
        }
        return fileEntrys;
    }

    @Override
    public void imoport(File file, CmsSite site, Boolean isCover) throws IOException, GlobalException {
        String resRoot = site.getResourcePath();
        String tplRoot = site.getTplPath();
        ZipFile zip = new ZipFile(file, "GBK");
        ZipEntry entry;
        String name;
        String filename;
        File outFile;
        File pfile;
        byte[] buf = new byte[1024];
        int len;
        InputStream is = null;
        OutputStream os = null;
        String solution = null;
        String deleteUrl = null;
        String deleteResUrl = null;
        Enumeration<ZipEntry> en = zip.getEntries();
        while (en.hasMoreElements()) {
            name = en.nextElement().getName();
            if (!name.startsWith(RES_EXP)) {
                solution = name.substring(0, name.indexOf(SPT));
                deleteUrl = tplRoot + SPT + solution;
                deleteResUrl = site.getResPath() + SPT + solution;
                break;
            }
        }
        String newSolution = solution;
        if (solution == null) {
            if (zip != null) {
                zip.close();
            }
            return;
        } else {
            //isCover 为空或false则表示保存重名的模板
            if (isCover == null || !isCover) {
                File dir = new File(realPathResolver.get(tplRoot + SPT + solution));
                if (dir.exists() && dir.isDirectory()) {
                    newSolution = dirName(tplRoot, solution, 0);
                }
            } else {
                if (deleteUrl != null) {
                    tplService.delete(new String[]{deleteUrl}, site);
                    delete(new String[]{deleteResUrl}, site);
                }
            }
        }
        en = zip.getEntries();
        while (en.hasMoreElements()) {
            entry = en.nextElement();
            if (!entry.isDirectory()) {
                name = entry.getName();
                name = name.replace(solution, newSolution);
                log.debug("unzip file：{}", name);
                // 模板还是资源
                if (name.startsWith(RES_EXP)) {
                    filename = resRoot + SPT + solution + name.substring(RES_EXP.length());
                } else {
                    filename = tplRoot + SPT + name;
                }
                log.debug("解压地址：{}", filename);
                outFile = new File(realPathResolver.get(filename));
                pfile = outFile.getParentFile();
                if (!pfile.exists()) {
                    pfile.mkdirs();
                }
                try {
                    is = zip.getInputStream(entry);
                    os = new FileOutputStream(outFile);
                    while ((len = is.read(buf)) != -1) {
                        os.write(buf, 0, len);
                    }
                    UploadFtp uploadFtp = getUploadFtp();
                    FtpTpl ftpTpl = new FtpTpl(uploadFtp);
                    ftpTpl.storeByFilename(filename, new FileInputStream(outFile));
                } finally {
                    if (is != null) {
                        is.close();
                        is = null;
                    }
                    if (os != null) {
                        os.close();
                        os = null;
                    }
                }
            }
        }
        zip.close();
    }

    @Override
    public String filename(String tplRoot, String filename, int i) throws GlobalException {
        String point = ".";
        String[] file = StringUtils.split(filename, point);
        String fileName = i != 0 ? file[0] + "(" + (i) + ")" + point + file[1]: filename;
        FtpTpl ftpTpl = (FtpTpl) readFile(tplRoot + SPT + fileName);
        //若果存在且不是文件夹
        if (ftpTpl != null && !ftpTpl.isDirectory()) {
            fileName = filename(tplRoot, filename, i + 1);
        }
        return fileName;
    }

    /**
     * 重名处理
     *
     * @param tplRoot  路径
     * @param solution 文件名
     * @param i        序号
     * @return 放回结果 文件名(i)
     */
    public String dirName(String tplRoot, String solution, int i) throws GlobalException {
        String newSolution = i != 0 ? solution + "(" + (i) + ")" : solution;
        FtpTpl file = (FtpTpl) readFile(tplRoot + SPT + newSolution);
        //若果存在且是文件夹
        if (file != null && file.isDirectory()) {
            newSolution = dirName(tplRoot, solution, i + 1);
        }
        return newSolution;
    }

    /**
     * 文件夹和可编辑文件则显示
     */
    private FileFilter filter = new FileFilter() {
        @Override
        public boolean accept(File file) {
            return file.isDirectory() || FileWrap.editableExt(FilenameUtils.getExtension(file.getName()));
        }
    };

    private RealPathResolver realPathResolver;

    @Autowired
    public void setRealPathResolver(RealPathResolver realPathResolver) {
        this.realPathResolver = realPathResolver;
    }

    private UploadFtp getUploadFtp() throws GlobalException {
        String uploadFtpId = configService.getGlobalConfig().getConfigAttr().getTemplateFile();
        UploadFtp uploadFtp = null;
        if (StringUtils.isNotBlank(uploadFtpId)) {
            uploadFtp = ftpService.findById(Integer.valueOf(uploadFtpId));
        }
        return uploadFtp;
    }
}
