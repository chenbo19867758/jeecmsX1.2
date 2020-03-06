/**
 * 
 */
package com.jeecms.resource.domain.dto;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Description:模板、资源重命名Dto
 * @author: tom
 * @date: 2018年11月9日 下午2:20:36
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class TplRenameDto implements Serializable {
	private static final long serialVersionUID = -1634369875648921667L;

	private String fileName;
	private String newName;

	@NotBlank
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@NotBlank
	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

}
