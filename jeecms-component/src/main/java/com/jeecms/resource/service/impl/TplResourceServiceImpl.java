package com.jeecms.resource.service.impl;

import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionInfo;
import com.jeecms.common.exception.error.SysOtherErrorCodeEnum;
import com.jeecms.common.file.FileWrap;
import com.jeecms.common.file.FileWrap.FileComparator;
import com.jeecms.common.util.Zipper;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.resource.service.Tpl;
import com.jeecms.resource.service.TplResourceService;
import com.jeecms.resource.service.TplService;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.util.FrontUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
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
import java.util.Arrays;
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
@ConditionalOnProperty(name = "freemarker.resources.type", havingValue = "local", matchIfMissing = true)
public class TplResourceServiceImpl implements TplResourceService {
	private static final Logger log = LoggerFactory.getLogger(TplResourceServiceImpl.class);


	@Autowired
	private TplService tplService;

	@Override
	public List<FileTpl> listFile(String path, boolean dirAndEditable, CmsSite site) {
		File parent = new File(realPathResolver.get(path));
		if (parent.exists()) {
			File[] files;
			if (dirAndEditable) {
				files = parent.listFiles(filter);
			} else {
				files = parent.listFiles();
			}
			Arrays.sort(files, new FileComparator());
			List<FileTpl> list = new ArrayList<FileTpl>(files.length);
			for (File f : files) {
				list.add(new FileTpl(f, realPathResolver.get("")));
			}
			return list;
		} else {
			return new ArrayList<FileTpl>(0);
		}
	}

	@Override
	public void createDir(String path, String dirName, CmsSite site) {
		File parent = new File(realPathResolver.get(path));
		parent.mkdirs();
		File dir = new File(parent, dirName);
		dir.mkdir();
	}

	@Override
	public void saveFile(String path, MultipartFile file, Boolean isCover) throws GlobalException {

		try {
			String fileName = file.getOriginalFilename();
			//isCover为空或者isCover为false贼保留原文件
			if (isCover == null || !isCover) {
				fileName = filename(path, fileName, 0);
			}
			File dest = new File(realPathResolver.get(path), fileName);
			file.transferTo(dest);
		} catch (IOException e) {
			e.printStackTrace();
			String msg = MessageResolver.getMessage(
					SysOtherErrorCodeEnum.UPLOAD_FILE_ERROR.getCode(),
					SysOtherErrorCodeEnum.UPLOAD_FILE_ERROR.getDefaultMessage());
			throw new GlobalException(new SystemExceptionInfo(
					SysOtherErrorCodeEnum.UPLOAD_FILE_ERROR.getCode(), msg));
		}
	}

	@Override
	public void createFile(String path, String filename, String data, CmsSite site) {
		File parent = new File(realPathResolver.get(path));
		parent.mkdirs();
		File file = new File(parent, filename);
		try {
			FileUtils.writeStringToFile(file, data, UTF8);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public Tpl readFile(String name) {
		File file = new File(realPathResolver.get(name));
		return new FileTpl(file, realPathResolver.get(""));
	}

	@Override
	public void updateFile(String root, String name, String newName, String data, CmsSite site) {
		name = root + WebConstants.SPT + name;
		newName = root + WebConstants.SPT + newName;
		File file = new File(realPathResolver.get(name));
		try {
			FileUtils.writeStringToFile(file, data, UTF8);
			rename(name, newName, site);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int delete(String[] names, CmsSite site) {
		int count = 0;
		File f;
		for (String name : names) {
			f = new File(realPathResolver.get(name));
			if (FileUtils.deleteQuietly(f)) {
				count++;
			}
		}
		return count;
	}

	@Override
	public void rename(String origName, String destName, CmsSite site) {
		File orig = new File(realPathResolver.get(origName));
		File dest = new File(realPathResolver.get(destName));
		orig.renameTo(dest);
	}

	@Override
	public void copyTplAndRes(CmsSite from, CmsSite to) throws IOException {
		String fromSol = from.getPath();
		String toSol = to.getPath();
		if (StringUtils.isBlank(toSol)) {
			toSol = fromSol;
		}
		File tplFrom = new File(realPathResolver.get(from.getTplPath()), fromSol);
		File tplTo = new File(realPathResolver.get(to.getTplPath()), toSol);
		if (tplFrom != null 
				&& tplTo != null 
				&& tplFrom.exists() 
				&& !tplFrom.getPath().equals(tplTo.getPath())) {
			FileUtils.copyDirectory(tplFrom, tplTo);
			File resFrom = new File(realPathResolver.get(from.getResourcePath()), fromSol);
			if (resFrom.exists()) {
				File resTo = new File(realPathResolver.get(to.getResourcePath()), toSol);
				FileUtils.copyDirectory(resFrom, resTo);
			}
		}
	}

	@Override
	public void delTplAndRes(CmsSite site) throws IOException {
		File tpl = new File(realPathResolver.get(site.getTplPath()));
		File res = new File(realPathResolver.get(site.getResourcePath()));
		FileUtils.deleteDirectory(tpl);
		FileUtils.deleteDirectory(res);
	}

	@Override
	public String[] getSolutions(String path) {
		File tpl = new File(realPathResolver.get(path));
		return tpl.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return dir.isDirectory();
			}
		});
	}

	@Override
	public List<Zipper.FileEntry> export(CmsSite site, String solution) {
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
				solution = name.substring(0, name.indexOf("/"));
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
					tplService.delete(new String[] {deleteUrl}, site);
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
					filename = resRoot + "/" + solution + name.substring(RES_EXP.length());
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
		String[] files = StringUtils.split(filename, point);
		String fileName = i != 0 ? files[0] + "(" + (i) + ")" + point + files[1]: filename;
		File file = new File(realPathResolver.get(tplRoot + SPT + fileName));
		//若果存在且不是文件夹
		if (file.exists() && !file.isDirectory()) {
			fileName = filename(tplRoot, filename, i + 1);
		}
		return fileName;
	}

	private String dirName(String tplRoot, String solution, int i) {
		String newSolution = i != 0 ? solution + "(" + (i) + ")" : solution;
		File dir = new File(realPathResolver.get(tplRoot + SPT + newSolution));
		//若果存在且是文件夹
		if (dir.exists() && dir.isDirectory()) {
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

}
