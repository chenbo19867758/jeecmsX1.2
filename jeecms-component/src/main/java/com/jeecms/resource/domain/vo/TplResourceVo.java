/**
 * 
 */
package com.jeecms.resource.domain.vo;

import java.io.Serializable;
import java.util.List;

import com.jeecms.common.file.FileWrap;

/**
 * @Description:模板资源Vo
 * @author: tom
 * @date: 2018年11月9日 上午10:55:42
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class TplResourceVo implements Serializable {

	private static final long serialVersionUID = 5005996036297582636L;
	List<FileWrap> files;
	String rootPath;

	public TplResourceVo(List<FileWrap> files, String rootPath) {
		super();
		this.files = files;
		this.rootPath = rootPath;
	}

	public List<FileWrap> getFiles() {
		return files;
	}

	public void setFiles(List<FileWrap> files) {
		this.files = files;
	}

	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

}
