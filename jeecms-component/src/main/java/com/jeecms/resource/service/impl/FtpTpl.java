/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.resource.service.impl;

import com.alibaba.fastjson.annotation.JSONField;
import com.jeecms.common.constants.ContentSecurityConstants;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionInfo;
import com.jeecms.common.exception.error.SysOtherErrorCodeEnum;
import com.jeecms.common.upload.UploadUtils;
import com.jeecms.common.util.DesUtil;
import com.jeecms.resource.domain.UploadFtp;
import com.jeecms.resource.service.Tpl;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Ftp
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/5/11
 */
@ConditionalOnProperty(prefix = "resources", name = "type", havingValue = "ftp", matchIfMissing = true)
public class FtpTpl implements Tpl {
	private static final Logger log = LoggerFactory.getLogger(FtpTpl.class);

	/**
	 * 添加文件
	 *
	 * @param filename 文件名
	 * @param in       输入流
	 * @return 文件名
	 */
	@Transient
	public int storeByFilename(String filename, InputStream in) throws GlobalException {
		return store(filename, in);
	}

	public boolean retrieveFile(String fileName, String root, String localPath) throws GlobalException {
		boolean flag = false;
		OutputStream os = null;
		try {
			FTPClient ftp = getClient();
			if (ftp != null) {
				String filename = uploadFtp.getFtpPath() + root + WebConstants.SPT + fileName;
				String name = FilenameUtils.getName(filename);
				String path = FilenameUtils.getFullPath(filename);
				if (ftp.changeWorkingDirectory(filename)) {
					FTPFile[] fs = ftp.listFiles();
					for (FTPFile ff : fs) {
						if (ff.getType() == 1) {
							retrieveFile(fileName + WebConstants.SPT + ff.getName(), root,
									localPath);
						} else {
							File dir = new File(localPath + WebConstants.SPT + fileName);
							if (!dir.exists()) {
								dir.mkdirs();
							}
							File localFile =
									new File(localPath + WebConstants.SPT + fileName + "/" + ff.getName());
							os = new FileOutputStream(localFile);
							flag = ftp.retrieveFile(ff.getName(), os);
						}
					}
				}
				ftp.logout();
				ftp.disconnect();
			}
		} catch (IOException e) {
			log.error("ftp store error", e);
			flag = false;
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

	/**
	 * 阅读Ftp服务器文件
	 *
	 * @param fileName 文件名
	 * @return 字符串
	 * @throws IOException IO异常
	 */
	public String readFile(String fileName) throws IOException, GlobalException {
		if (StringUtils.isBlank(fileName)) {
			return null;
		}
		InputStream ins = null;
		String builder = "";
		FTPClient ftp = getClient();
		FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
		conf.setServerLanguageCode("zh");
		try {
			fileName = uploadFtp.getFtpPath() + fileName;
			String name = FilenameUtils.getName(fileName);
			String path = FilenameUtils.getFullPathNoEndSeparator(fileName);
			if (StringUtils.isBlank(path) || ftp.changeWorkingDirectory(path)) {
				// 从服务器上读取指定的文件
				//name = new String(name.getBytes(uploadFtp.getEncoding()), "iso-8859-1");
				ins = ftp.retrieveFileStream(name);
				BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
				String line;
				builder = IOUtils.toString(ins, Charsets.toCharset(uploadFtp.getEncoding()));
				reader.close();
				ftp.getReply();
			}
		} finally {
			if (ins != null) {
				ins.close();
			}
		}
		log.info("转码前：" + builder);
		return builder;
	}

	public InputStream getInputStream(String fileName) throws IOException, GlobalException {
		if (StringUtils.isBlank(fileName)) {
			return null;
		}
		InputStream ins = null;
		FTPClient ftp = getClient();
		FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
		conf.setServerLanguageCode("zh");
		try {
			fileName = uploadFtp.getFtpPath() + fileName;
			String name = FilenameUtils.getName(fileName);
			String path = FilenameUtils.getFullPathNoEndSeparator(fileName);
			if (StringUtils.isBlank(path) || ftp.changeWorkingDirectory(path)) {
				// 从服务器上读取指定的文件
				name = new String(name.getBytes(uploadFtp.getEncoding()), "iso-8859-1");
				ins = ftp.retrieveFileStream(name);
				ftp.getReply();
			}
		} finally {
			if (ins != null) {
				ins.close();
			}
		}
		return ins;
	}

	public FTPFile get(String pathname) throws IOException, GlobalException {
		FTPClient ftpClient = getClient();
		FTPFile file = null;
		String name = FilenameUtils.getName(uploadFtp.getFtpPath() + pathname);
		String path = FilenameUtils.getFullPathNoEndSeparator(pathname);
		if (StringUtils.isBlank(path) || ftpClient.changeWorkingDirectory(path)) {
			file = ftpClient.mlistFile(name);
		}
		if (file != null) {
			file.setName(FilenameUtils.getName(file.getName()));
		}
		return file;
	}

	@Transient
	public FTPFile[] list(String path) throws IOException, GlobalException {
		FTPClient ftp = getClient();
		//解决中文乱码问题
		/*if (StringUtils.isNotBlank(path)) {
			path = new String(path.getBytes(uploadFtp.getEncoding()), "iso-8859-1");
		}*/
		path = uploadFtp.getFtpPath() + path;
		FTPFile[] list = ftp.listFiles(path);
		return list;
	}

	@Transient
	public File retrieve(String name, String fileName) throws IOException, GlobalException {
		String path = System.getProperty("java.io.tmpdir");
		File file = new File(path, fileName);
		file = UploadUtils.getUniqueFile(file);
		FTPClient ftp = getClient();
		OutputStream output = new FileOutputStream(file);
		ftp.retrieveFile(uploadFtp.getFtpPath() + name, output);
		output.close();
		ftp.logout();
		ftp.disconnect();
		return file;
	}

	@Transient
	public boolean deleteFile(String fileName) throws IOException, GlobalException {
		if (StringUtils.isNotBlank(uploadFtp.getFtpPath()) && !WebConstants.SPT.equals(uploadFtp.getFtpPath())) {
			fileName = uploadFtp.getFtpPath() + WebConstants.SPT + fileName;
			fileName = fileName.replace("//", "/");
		}
		FTPFile f = get(fileName);
		FTPClient ftp = getClient();
		if (f.getType() == 1) {
			FTPFile[] ftpFiles = list(fileName);
			if (ftpFiles != null && ftpFiles.length > 0) {
				for (FTPFile file : ftpFiles) {
					delete(file, ftp, fileName);
				}
			}
			return ftp.removeDirectory(fileName);
		}
		return ftp.deleteFile(fileName);
	}

	/**
	 * 递归删除文件
	 *
	 * @param file     文件
	 * @param ftp      {@link FTPClient}
	 * @param fileName 上级目录
	 * @throws IOException IO异常
	 */
	private void delete(FTPFile file, FTPClient ftp, String fileName) throws IOException {
		fileName += WebConstants.SPT + file.getName();
		//解决中文乱码问题
		if (file.getType() == 1) {
			FTPFile[] ftpFiles = ftp.listFiles(fileName);
			if (ftpFiles != null && ftpFiles.length > 0) {
				for (FTPFile ftpFile1 : ftpFiles) {
					delete(ftpFile1, ftp, fileName);
				}
			} else {
				ftp.removeDirectory(fileName);
			}
		} else {
			ftp.deleteFile(fileName);
		}
	}

	@Transient
	public boolean restore(String name, File file) throws IOException, GlobalException {
		store(name, FileUtils.openInputStream(file));
		file.deleteOnExit();
		return true;
	}

	public boolean saveDir(String path) throws IOException, GlobalException {
		boolean flag = false;
		FTPClient ftp = getClient();
		path = uploadFtp.getFtpPath() + path;
		if (!ftp.changeWorkingDirectory(path)) {
			String[] ps = StringUtils.split(path, WebConstants.SPT);
			String p = WebConstants.SPT;
			ftp.changeWorkingDirectory(p);
			for (String s : ps) {
				p += s + WebConstants.SPT;
				if (!ftp.changeWorkingDirectory(p)) {
					ftp.makeDirectory(s);
					flag = ftp.changeWorkingDirectory(p);
				}
			}
		}
		return flag;
	}

	/**
	 * 修改文件名
	 *
	 * @param from 原文件名
	 * @param to   修改后文件名
	 * @return
	 */
	@Transient
	public boolean reName(String from, String to) throws IOException, GlobalException {
		FTPClient ftpClient = getClient();
		from = uploadFtp.getFtpPath() + from;
		to = uploadFtp.getFtpPath() + to;
		return ftpClient.rename(from, to);
	}

	@Transient
	private int store(String remote, InputStream in) throws GlobalException {
		try {
			FTPClient ftp = getClient();
			if (ftp != null) {
				String filename = uploadFtp.getFtpPath() + remote;
				String name = FilenameUtils.getName(filename);
				String path = FilenameUtils.getFullPath(filename);
				if (!ftp.changeWorkingDirectory(path)) {
					String[] ps = StringUtils.split(path, WebConstants.SPT);
					String p = WebConstants.SPT;
					ftp.changeWorkingDirectory(p);
					for (String s : ps) {
						p += s + WebConstants.SPT;
						if (!ftp.changeWorkingDirectory(p)) {
							ftp.makeDirectory(s);
							ftp.changeWorkingDirectory(p);
						}
					}
				}
				ftp.storeFile(name, in);
				ftp.logout();
				ftp.disconnect();
			}
			return 0;
		} catch (SocketException e) {
			log.error("ftp store error", e);
			return 3;
		} catch (IOException e) {
			log.error("ftp store error", e);
			return 4;
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
		}
	}

	@Transient
	private FTPClient getClient() throws IOException, GlobalException {
		if (uploadFtp == null) {
			throw new GlobalException(new SystemExceptionInfo(
					SysOtherErrorCodeEnum.FTP_NOT_CONFIGURATION.getDefaultMessage(),
					SysOtherErrorCodeEnum.FTP_NOT_CONFIGURATION.getCode()));
		}
		FTPClient ftp = new FTPClient();
		ftp.addProtocolCommandListener(new PrintCommandListener(
				new PrintWriter(System.out)));
		if (!ftp.getAutodetectUTF8()) {
			ftp.setAutodetectUTF8(true);
		}
		ftp.setDefaultPort(uploadFtp.getPort());
		ftp.connect(uploadFtp.getIp());
		int reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			log.warn("FTP server refused connection: {}", uploadFtp.getIp());
			ftp.disconnect();
			throw new GlobalException(new SystemExceptionInfo(
					SysOtherErrorCodeEnum.FTP_CONNECTION_FAILED.getDefaultMessage(),
					SysOtherErrorCodeEnum.FTP_CONNECTION_FAILED.getCode()));
		}
		String password = DesUtil.decrypt(uploadFtp.getPassword(), ContentSecurityConstants.DES_KEY,
				ContentSecurityConstants.DES_IV);
		if (!ftp.login(uploadFtp.getUsername(), password)) {
			log.warn("FTP server refused login: {}, user: {}", uploadFtp.getIp(),
					uploadFtp.getUsername());
			ftp.logout();
			ftp.disconnect();
			throw new GlobalException(new SystemExceptionInfo(
					SysOtherErrorCodeEnum.FTP_CONNECTION_FAILED.getDefaultMessage(),
					SysOtherErrorCodeEnum.FTP_CONNECTION_FAILED.getCode()));
		}
		ftp.setControlEncoding(uploadFtp.getEncoding());
		ftp.setFileType(FTP.BINARY_FILE_TYPE);
		ftp.enterLocalPassiveMode();
		return ftp;
	}

	private FTPFile ftpFile;
	private UploadFtp uploadFtp;
	private String path;
	private Integer[] models;
	private List<FtpTpl> children;

	/**
	 * 文件名
	 */
	private String fileName;
	/**
	 * 文件路径
	 */
	private String name;
	/**
	 * 文件大小
	 */
	private long size;

	private String sizeUnit;
	/**
	 * 文件所属路径
	 */
	private String root;

	/**
	 * 最后修改时间
	 */
	private long lastModified;
	/**
	 * 最后修改时间
	 */
	private Date lastModifiedDate;

	/**
	 * 是否文件夹
	 */
	private boolean directory;

	public FtpTpl() {
		super();
	}

	public FtpTpl(UploadFtp uploadFtp) {
		this.uploadFtp = uploadFtp;
	}

	public FtpTpl(UploadFtp uploadFtp, String path) {
		this.uploadFtp = uploadFtp;
		this.path = path;
	}

	public FtpTpl(FTPFile ftpFile, String path) {
		this.ftpFile = ftpFile;
		this.path = path;
	}

	public FtpTpl(FTPFile ftpFile, String path, UploadFtp uploadFtp) {
		this.ftpFile = ftpFile;
		this.path = path;
		this.uploadFtp = uploadFtp;
	}

	public List<FtpTpl> getChildren() throws IOException, GlobalException {
		if (this.children != null) {
			return this.children;
		}
		if (ftpFile.getType() == 1) {
			FTPFile[] ftpFiles = list(getName());
			List<FtpTpl> list = new ArrayList<FtpTpl>();
			if (ftpFiles != null) {
				for (FTPFile ftpFile : ftpFiles) {
					FtpTpl ft = new FtpTpl(ftpFile, getName(), uploadFtp);
					list.add(ft);
				}
			}
			return list;
		}
		return null;
	}

	@Override
	public String getName() {
		return (StringUtils.isNotBlank(getRoot()) ? getRoot() : "") + WebConstants.SPT + getFilename();
	}

	@Override
	public String getRoot() {
		if (path != null && path.lastIndexOf(WebConstants.SPT) == path.length() - 1) {
			path = path.substring(0, path.length() - 1);
		}
		return path;
	}

	@Override
	public String getFilename() {
		return ftpFile != null ? ftpFile.getName() : null;
	}

	@Override
	public String getSource() throws GlobalException {
		if (isDirectory()) {
			return null;
		} else {
			try {
				return readFile((StringUtils.isNotBlank(getRoot()) ? getRoot() + WebConstants.SPT : "") + getFilename());
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
		return null;
	}

	@Override
	public long getLastModified() {
		return ftpFile.getTimestamp().getTimeInMillis();
	}

	@Temporal(TemporalType.TIMESTAMP)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@Override
	public Date getLastModifiedDate() {
		return ftpFile.getTimestamp().getTime();
	}

	@Override
	public long getLength() {
		return ftpFile.getSize();
	}

	@Override
	public int getSize() {
		return (int) (getLength() / 1024) + 1;
	}

	@Override
	public String getSizeUnit() {
		//定义GB的计算常量
		int gb = 1024 * 1024;
		//定义MB的计算常量
		int mb = 1024;
		//格式化小数
		DecimalFormat df = new DecimalFormat("0.00");
		String resultSize;
		if (getSize() / gb >= 1) {
			//如果当前Byte的值大于等于1GB
			resultSize = df.format(getSize() / (float) gb) + "GB";
		} else if (getSize() / mb >= 1) {
			//如果当前Byte的值大于等于1MB
			resultSize = df.format(getSize() / (float) mb) + "MB";
		} else {
			resultSize = getSize() + "KB";
		}
		sizeUnit = resultSize;
		return sizeUnit;
	}

	public Integer[] getModels() {
		return models;
	}

	public void setModels(Integer[] models) {
		this.models = models;
	}

	/**
	 * 获取FTP文件
	 *
	 * @return FTPFile
	 */
	public FTPFile getFtpFile() {
		return this.ftpFile;
	}

	@Override
	public boolean isDirectory() {
		return ftpFile.getType() == 1;
	}

	@Override
	public Integer[] models() {
		return models;
	}
}
