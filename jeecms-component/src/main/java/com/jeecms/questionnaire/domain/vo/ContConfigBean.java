/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.domain.vo;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/11/8 11:59
 */
public class ContConfigBean {
	/**
	 * bgColor : #ffffff
	 * hasBorder : 1
	 * borderColor : #e8e8e8
	 * borderWidth : 1
	 * borderRadius : 0
	 */

	private String bgColor = "#ffffff";
	private int hasBorder = 1;
	private String borderColor = "#e8e8e8";
	private int borderWidth = 1;
	private int borderRadius = 0;

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public int getHasBorder() {
		return hasBorder;
	}

	public void setHasBorder(int hasBorder) {
		this.hasBorder = hasBorder;
	}

	public String getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}

	public int getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(int borderWidth) {
		this.borderWidth = borderWidth;
	}

	public int getBorderRadius() {
		return borderRadius;
	}

	public void setBorderRadius(int borderRadius) {
		this.borderRadius = borderRadius;
	}
}
