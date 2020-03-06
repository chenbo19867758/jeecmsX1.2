package com.jeecms.resource.service.impl;

import com.alibaba.fastjson.annotation.JSONField;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.file.FileWrap.FileComparator;
import com.jeecms.resource.service.Tpl;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author: tom
 * @date: 2019年3月5日 下午4:48:37
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@ConditionalOnProperty(prefix = "resources", name = "type", havingValue = "local", matchIfMissing = true)
public class FileTpl implements Tpl {
	private File file;
	/**
	 * 应用的根目录
	 */
	private String path;
	private Integer[] models;
	private List<FileTpl> children;

	public FileTpl(File file, String path) {
		this.file = file;
		this.path = path;
	}

	/**
	 * 获得下级目录
	 * <p>
	 * 如果没有指定下级目录，则返回文件夹自身的下级文件，并运用FileFilter。
	 *
	 * @return
	 */
	public List<FileTpl> getChildren() {
		if (this.children != null) {
			return this.children;
		}
		File[] files;
		files = getFile().listFiles();
		List<FileTpl> list = new ArrayList<FileTpl>();
		if (files != null) {
			Arrays.sort(files, new FileComparator());
			for (File f : files) {
				FileTpl fw = new FileTpl(f, path);
				list.add(fw);
			}
		}
		return list;
	}

	@Override
	public String getName() {
		String ap = file.getAbsolutePath().substring(path.length());
		ap = ap.replace(File.separatorChar, '/');
		// 在resin里root的结尾是带'/'的，这样会导致getName返回的名称不以'/'开头。
		if (!ap.startsWith(WebConstants.SPT)) {
			ap = WebConstants.SPT + ap;
		}
		return ap;
	}

	@Override
	public String getRoot() {
		String name = getName();
		return name.substring(0, name.lastIndexOf('/'));
	}

	@Override
	public String getFilename() {
		return file.getName();
	}


	@Override
	public String getSource() {
		if (file.isDirectory()) {
			return null;
		}
		try {
			return FileUtils.readFileToString(this.file, WebConstants.UTF8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public long getLastModified() {
		return file.lastModified();
	}

	@Temporal(TemporalType.TIMESTAMP)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@Override
	public Date getLastModifiedDate() {
		return new Timestamp(getLastModified());
	}

	@Override
	public long getLength() {
		return file.length();
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
		return resultSize;
	}

	public Integer[] getModels() {
		return models;
	}

	public void setModels(Integer[] models) {
		this.models = models;
	}

	@Override
	public boolean isDirectory() {
		return file.isDirectory();
	}

	@Override
	public Integer[] models() {
		return models;
	}

	/**
	 * 获得被包装的文件
	 *
	 * @return
	 */
	public File getFile() {
		return this.file;
	}

	/**
	 * 指定下级目录
	 *
	 * @param children
	 */
	public void setChildren(List<FileTpl> children) {
		this.children = children;
	}
}
