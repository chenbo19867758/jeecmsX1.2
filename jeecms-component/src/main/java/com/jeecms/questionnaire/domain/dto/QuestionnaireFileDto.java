/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.domain.dto;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/11/25 15:34
 */
public class QuestionnaireFileDto {

	/**
	 * 图片地址
	 */
	private String url;
	/**
	 * 文件类型(1图片 2视频 3音频 4附件)
	 */
	private Short type;

	/**
	 * 图片名称
	 */
	private String name;

	private String pdfUrl;

	private String width;

	private String height;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPdfUrl() {
		return pdfUrl;
	}

	public void setPdfUrl(String pdfUrl) {
		this.pdfUrl = pdfUrl;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}
}
