package com.jeecms.resource.service.impl;

import com.jeecms.common.constants.TplConstants;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionInfo;
import com.jeecms.common.exception.error.SiteErrorCodeEnum;
import com.jeecms.common.exception.error.SysOtherErrorCodeEnum;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.content.domain.CmsModelTpl;
import com.jeecms.content.service.CmsModelTplService;
import com.jeecms.resource.domain.UploadFtp;
import com.jeecms.resource.domain.dto.TplReSourceDto;
import com.jeecms.resource.domain.dto.TplSaveDirDto;
import com.jeecms.resource.domain.dto.TplUpdateDto;
import com.jeecms.resource.service.Tpl;
import com.jeecms.resource.service.TplResourceService;
import com.jeecms.resource.service.TplService;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.exception.SiteExceptionInfo;
import com.jeecms.system.service.FtpService;
import com.jeecms.system.service.GlobalConfigService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.jeecms.common.constants.WebConstants.SPT;
import static com.jeecms.common.constants.WebConstants.UTF8;

/**
 * 模板Service实现类
 *
 * @author: tom
 * @date: 2019年3月5日 下午4:48:37
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
@Transactional(rollbackFor = Exception.class)
@ConditionalOnProperty(name = "freemarker.resources.type", havingValue = "ftp", matchIfMissing = true)
public class TplFtpServiceImpl implements TplService {
	private static Logger log = LoggerFactory.getLogger(TplFtpServiceImpl.class);

	@Override
	public int delete(String[] names, CmsSite site) throws GlobalException {
		UploadFtp uploadFtp = getUploadFtp();
		FtpTpl ftpTpl = new FtpTpl(uploadFtp);
		int count = 0;
		for (String name : names) {
			try {
				if (ftpTpl.deleteFile(name)) {
					//模板删除成功删除模型关联
					// 模板方案名
					name = name.replace(site.getTplPath() + SPT, "");
					int nameSpt = name.indexOf(SPT);
					String solution = "";
					if (nameSpt != -1) {
						solution = name.substring(0, nameSpt);
					}
					//模板方案名不为空
					if (StringUtils.isNotBlank(solution)) {
						name = name.replace(solution + SPT, "");
					}
					List<CmsModelTpl> list = modelTplService.findByTplPath(site.getId(), name, solution);
					if (list != null) {
						modelTplService.physicalDeleteInBatch(list);
					}
					count++;
				}
			} catch (IOException e) {
				throw new GlobalException(new SiteExceptionInfo(
						SiteErrorCodeEnum.FTP_CONNECTION_ERROR.getDefaultMessage(),
						SiteErrorCodeEnum.FTP_CONNECTION_ERROR.getCode()));
			}
		}
		return count;
	}

	@Override
	public Tpl get(String name, CmsSite site) throws GlobalException {
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
			// 模板方案名
			name = name.replace(site.getTplPath() + SPT, "");
			int nameSpt = name.indexOf(SPT);
			String solution = "";
			if (nameSpt != -1) {
				solution = name.substring(0, nameSpt);
			}
			//模板方案名不为空
			if (StringUtils.isNotBlank(solution)) {
				name = name.replace(solution + SPT, "");
			}
			List<CmsModelTpl> modelTpls = modelTplService.findByTplPath(site.getId(), name, solution);
			Integer[] models = new Integer[modelTpls.size()];
			for (int i = 0; i < modelTpls.size(); i++) {
				models[i] = modelTpls.get(i).getModelId();
			}
			ftpTpl = new FtpTpl(ftpFile, path, uploadFtp);
			ftpTpl.setModels(models);
			return ftpTpl;
		} else {
			return null;
		}
	}

	@Override
	public Tpl getFile(String name, CmsSite site) throws IOException, GlobalException {
		UploadFtp uploadFtp = getUploadFtp();
		FtpTpl ftpTpl = new FtpTpl(uploadFtp);
		FTPFile ftpFile = ftpTpl.get(name);
		if (ftpFile != null) {
			ftpTpl = new FtpTpl(ftpFile, FilenameUtils.getFullPathNoEndSeparator(name));
			return ftpTpl;
		} else {
			return null;
		}
	}

	@Override
	public List<Tpl> getListByPrefix(String prefix, CmsSite site) throws IOException, GlobalException {
		UploadFtp uploadFtp = getUploadFtp();
		FtpTpl ftpTpl = new FtpTpl(uploadFtp);
		FTPFile[] ftpFiles = ftpTpl.list(prefix);
		List<Tpl> tpls = new ArrayList<Tpl>(ftpFiles != null ? ftpFiles.length : 0);
		if (ftpFiles != null) {
			for (FTPFile f : ftpFiles) {
				tpls.add(new FtpTpl(f, prefix, uploadFtp));
			}
			return tpls;
		} else {
			return new ArrayList<Tpl>(0);
		}
	}

	@Override
	public List<String> getIndexBySolutionPath(String prefix, CmsSite site) throws IOException, GlobalException {
		String url = site.getTplIndexPrefix(prefix);
		List<Tpl> list = getListByPrefix(url, site);
		List<String> result = new ArrayList<String>(list.size());
		for (Tpl tpl : list) {
			result.add(tpl.getName());
		}
		return result;
	}

	@Override
	public List<Tpl> getChild(String path, CmsSite site) throws GlobalException {
		UploadFtp uploadFtp = getUploadFtp();
		FtpTpl ftpTpl = new FtpTpl(uploadFtp);
		FTPFile[] ftpFiles = new FTPFile[0];
		try {
			ftpFiles = ftpTpl.list(path);
		} catch (IOException e) {
			throw new GlobalException(new SiteExceptionInfo(
					SiteErrorCodeEnum.FTP_CONNECTION_ERROR.getDefaultMessage(),
					SiteErrorCodeEnum.FTP_CONNECTION_ERROR.getCode()));
		}
		List<Tpl> tpls = new ArrayList<Tpl>(ftpFiles != null ? ftpFiles.length : 0);
		if (ftpFiles != null) {
			for (FTPFile f : ftpFiles) {
				tpls.add(new FtpTpl(f, path, uploadFtp));
			}
		}
		return tpls;

	}

	@Override
	public void rename(String orig, String dist, CmsSite site) throws GlobalException {
		UploadFtp uploadFtp = getUploadFtp();
		FtpTpl ftpTpl = new FtpTpl(uploadFtp);
		boolean flag = false;
		try {
			//模板删除成功删除模型关联
			// 模板方案名
			String name = orig.replace(site.getTplPath() + SPT, "");
			String distRep = dist.replace(site.getTplPath() + SPT, "");
			int nameSpt = name.indexOf(SPT);
			String solution = "";
			if (nameSpt != -1) {
				solution = name.substring(0, nameSpt);
			}
			int distSpt = distRep.indexOf(SPT);
			String distSolution = "";
			if (distSpt != -1) {
				distSolution = distSolution.substring(0, distSpt);
			}
			//模板方案名不为空
			if (StringUtils.isNotBlank(solution)) {
				name = name.replace(solution + SPT, "");
			}
			//模板方案名不为空
			if (StringUtils.isNotBlank(distSolution)) {
				distRep = dist.replace(distSolution + SPT, "");
			}
			List<CmsModelTpl> list = modelTplService.findByTplPath(site.getId(), name, solution);
			if (list != null) {
				for (CmsModelTpl cmsModelTpl : list) {
					cmsModelTpl.setTplPath(distRep);
				}
				modelTplService.batchUpdate(list);
			}
			flag = ftpTpl.reName(orig, dist);
		} catch (IOException e) {
			throw new GlobalException(new SiteExceptionInfo(
					SiteErrorCodeEnum.FTP_CONNECTION_ERROR.getDefaultMessage(),
					SiteErrorCodeEnum.FTP_CONNECTION_ERROR.getCode()));
		}
		if (!flag) {
			log.error("rename template error: " + orig + " to " + dist);
		}
	}

	@Override
	public void saveDir(TplSaveDirDto dto, CmsSite site) throws GlobalException {
		String real = realPathResolver.get(dto.getRoot() + WebConstants.SPT + dto.getDirName());
		File f = new File(real);
		f.mkdirs();
		UploadFtp uploadFtp = getUploadFtp();
		FtpTpl ftpTpl = new FtpTpl(uploadFtp);
		String path = getRoot(dto.getRoot(), site) + WebConstants.SPT + dto.getDirName();
		try {
			ftpTpl.saveDir(path);
		} catch (IOException e) {
			throw new GlobalException(new SiteExceptionInfo(
					SiteErrorCodeEnum.FTP_CONNECTION_ERROR.getDefaultMessage(),
					SiteErrorCodeEnum.FTP_CONNECTION_ERROR.getCode()));
		}
	}

	@Override
	public void save(TplReSourceDto dto, CmsSite site) throws GlobalException {
		String name = getRoot(dto.getRoot(), site) + "/" + dto.getFilename() + TplConstants.TPL_SUFFIX;
		String real = realPathResolver.get(name);
		File f = new File(real);
		String msg = MessageResolver.getMessage(SysOtherErrorCodeEnum.TPL_RESOURCE_CREATE_ERROR.getCode(),
				SysOtherErrorCodeEnum.TPL_RESOURCE_CREATE_ERROR.getDefaultMessage());
		InputStream in = null;
		try {
			FileUtils.writeStringToFile(f, dto.getSource(), UTF8);
			in = new FileInputStream(f);
			UploadFtp uploadFtp = getUploadFtp();
			FtpTpl ftpTpl = new FtpTpl(uploadFtp);
			int flag = ftpTpl.storeByFilename(name, in);
			Integer[] modelIds = dto.getModels();
			modelTplService.saveBatch(dto.getRoot(), site, name, modelIds);
		} catch (FileNotFoundException e) {
			throw new GlobalException(new SystemExceptionInfo(msg,
					SysOtherErrorCodeEnum.TPL_RESOURCE_CREATE_ERROR.getCode()));
		} catch (IOException e) {
			throw new GlobalException(new SystemExceptionInfo(msg,
					SysOtherErrorCodeEnum.TPL_RESOURCE_CREATE_ERROR.getCode()));
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
	public void save(String path, MultipartFile file, CmsSite site, Boolean isCover) throws GlobalException {
		InputStream in = null;
		String msg = MessageResolver.getMessage(SysOtherErrorCodeEnum.TPL_RESOURCE_CREATE_ERROR.getCode(),
				SysOtherErrorCodeEnum.TPL_RESOURCE_CREATE_ERROR.getDefaultMessage());
		String fileName = file.getOriginalFilename();
		try {
			//isCover为空或者isCover为false贼保留原文件
			if (isCover == null || !isCover) {
				fileName = resourceService.filename(path, fileName, 0);
			}
			in = file.getInputStream();
			UploadFtp uploadFtp = getUploadFtp();
			FtpTpl ftpTpl = new FtpTpl(uploadFtp);
			String filename = path + WebConstants.SPT + fileName;
			ftpTpl.storeByFilename(filename, in);
		} catch (IllegalStateException e) {
			log.error("upload template error!", e);
			throw new GlobalException(new SystemExceptionInfo(msg,
					SysOtherErrorCodeEnum.TPL_RESOURCE_CREATE_ERROR.getCode()));
		} catch (IOException e) {
			log.error("upload template error!", e);
			throw new GlobalException(new SystemExceptionInfo(msg,
					SysOtherErrorCodeEnum.TPL_RESOURCE_CREATE_ERROR.getCode()));
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
		}
	}

	@Override
	public void update(TplUpdateDto dto, CmsSite site) throws GlobalException {
		String oldName = dto.getRoot();
		String newName = dto.getFilename();
		//创建新的ftp服务器文件
		TplReSourceDto reSourceDto = new TplReSourceDto();
		reSourceDto.setFilename(FilenameUtils.getBaseName(newName));
		reSourceDto.setModels(dto.getModels());
		reSourceDto.setRoot(FilenameUtils.getFullPathNoEndSeparator(oldName));
		reSourceDto.setSource(dto.getSource());
		//模板删除成功删除模型关联
		String path = oldName.replace(site.getTplPath() + "/", "") + "/";
		// 模板方案名
		String solution = path.substring(0, path.indexOf("/"));
		//模板方案名不为空
		String name;
		if (StringUtils.isNotBlank(solution)) {
			name = oldName.replace(site.getTplPath() + WebConstants.SPT + solution + WebConstants.SPT, "");
		} else {
			name = oldName.replace(site.getTplPath() + WebConstants.SPT, "");
		}
		List<CmsModelTpl> list = modelTplService.findByTplPath(site.getSiteId(), name, solution);
		if (list != null) {
			modelTplService.physicalDeleteInBatch(list);
		}
		save(reSourceDto, site);
		String[] names = {oldName};
		//删除旧的文件，这样就相当于创建了一个修改了原文件
		if (!newName.equals(oldName)) {
			delete(names, site);
		}
	}

	/**
	 * 获取站点路径
	 *
	 * @param root 路径
	 * @param site 站点
	 * @return String
	 */
	private String getRoot(String root, CmsSite site) {
		if (StringUtils.isBlank(root)) {
			return site.getTplPath();
		}
		return root;
	}

	private UploadFtp getUploadFtp() throws GlobalException {
		String uploadFtpId = configService.getGlobalConfig().getConfigAttr().getTemplateFile();
		UploadFtp uploadFtp = null;
		if (StringUtils.isNotBlank(uploadFtpId)) {
			uploadFtp = ftpService.findById(Integer.valueOf(uploadFtpId));
		}
		return uploadFtp;
	}

	private String root;

	private RealPathResolver realPathResolver;

	@Autowired
	public void setRealPathResolver(RealPathResolver realPathResolver) {
		this.realPathResolver = realPathResolver;
		root = realPathResolver.get("");
	}

	private static class PrefixFilter implements FileFilter {
		private String prefix;

		public PrefixFilter(String prefix) {
			this.prefix = prefix;
		}

		@Override
		public boolean accept(File file) {
			String name = file.getName();
			return file.isFile() && name.startsWith(prefix);
		}
	}

	@Autowired
	public CmsModelTplService modelTplService;
	@Autowired
	public GlobalConfigService configService;
	@Autowired
	public FtpService ftpService;
	@Autowired
	private TplResourceService resourceService;

}
