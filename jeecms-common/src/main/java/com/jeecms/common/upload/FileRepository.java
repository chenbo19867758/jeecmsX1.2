package com.jeecms.common.upload;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;

/**
 * 本地文件存储
 * 
 * @author: tom
 * @date: 2018年12月26日 上午10:56:26
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Component
public class FileRepository implements ServletContextAware {
	private Logger log = LoggerFactory.getLogger(FileRepository.class);
	final String specialS1 = "\0";
	final String specialS2 = "/";
	final String specialS3 = "\\";

	/**
	 * 上传文件
	 * 
	 * @Title: storeByExt
	 * @param path
	 *            上传路径
	 * @param ext
	 *            文件格式
	 * @param file
	 *            MultipartFile 文件
	 * @throws IOException
	 *             IOException
	 * @return: String
	 */
	public String storeByExt(String path, String ext, MultipartFile file) throws IOException {
		// String filename = UploadUtils.generateFilename(path, ext);
		// File dest = new File(getRealPath(filename));
		String fileName = UploadUtils.generateRamdonFilename(ext);
		String fileUrl = path + fileName;
		File dest = new File(getRealPath(path), fileName);
		dest = UploadUtils.getUniqueFile(dest);
		store(file, dest);
		return fileUrl;
	}

	/**
	 * 上传文件
	 * 
	 * @Title: storeByExt
	 * @param path
	 *            上传路径
	 * @param ext
	 *            文件格式
	 * @param file
	 *            MultipartFile 文件
	 * @throws IOException
	 *             IOException
	 * @return: String
	 */
	public String storeByExt(String path, String ext, File file) throws IOException {
		// String filename = UploadUtils.generateFilename(path, ext);
		// File dest = new File(getRealPath(filename));
		String fileName = UploadUtils.generateRamdonFilename(ext);
		String fileUrl = path + fileName;
		File dest = new File(getRealPath(path), fileName);
		dest = UploadUtils.getUniqueFile(dest);
		store(file, dest);
		return fileUrl;
	}

	/**
	 * 上传文件
	 * 
	 * @Title: storeByExt
	 * @param filename
	 *            上传文件名
	 * @param file
	 *            MultipartFile 文件
	 * @throws IOException
	 *             IOException
	 * @return: String
	 */
	public String storeByFilename(String filename, MultipartFile file) throws IOException {
		// filename.contains("/")||filename.contains("\\")||
		if (filename != null && (filename.indexOf(specialS1) != -1)) {
			return "";
		}
		filename = java.text.Normalizer.normalize(filename, java.text.Normalizer.Form.NFKD);
		File dest = new File(getRealPath(filename));
		store(file, dest);
		return filename;
	}

	/**
	 * 上传文件
	 * 
	 * @Title: storeByExt
	 * @param filename
	 *            文件名
	 * @param file
	 *            MultipartFile 文件
	 * @throws IOException
	 *             IOException
	 * @return: String
	 */
	public String storeByFilename(String filename, File file) throws IOException {
		File dest = new File(getRealPath(filename));
		store(file, dest);
		return filename;
	}

	/**
	 * 上传文件
	 * 
	 * @Title: storeByExt
	 * @param path
	 *            上传路径
	 * @param file
	 *            MultipartFile 文件
	 * @throws IOException
	 *             IOException
	 * @return: String
	 */
	public String storeByFilePath(String path, String filename, MultipartFile file) throws IOException {
		boolean isValid = filename != null
				&& (filename.contains(specialS2) || filename.contains(specialS3) 
						|| filename.indexOf(specialS1) != -1);
		if (isValid) {
			return "";
		}
		File dest = new File(getRealPath(path + filename));
		store(file, dest);
		return path + filename;
	}

	private void store(MultipartFile file, File dest) throws IOException {
		try {
			UploadUtils.checkDirAndCreate(dest.getParentFile());
			file.transferTo(dest);
		} catch (IOException e) {
			log.error("Transfer file error when upload file", e);
			throw e;
		}
	}

	private void store(File file, File dest) throws IOException {
		try {
			UploadUtils.checkDirAndCreate(dest.getParentFile());
			FileUtils.copyFile(file, dest);
		} catch (IOException e) {
			log.error("Transfer file error when upload file", e);
			throw e;
		}
	}

	public File retrieve(String name) {
		return new File(ctx.getRealPath(name));
	}

	private String getRealPath(String name) {
		String realpath = ctx.getRealPath(name);
		if (realpath == null) {
			realpath = ctx.getRealPath(specialS2) + name;
		}
		return realpath;
	}

	private ServletContext ctx;

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.ctx = servletContext;
	}
}
