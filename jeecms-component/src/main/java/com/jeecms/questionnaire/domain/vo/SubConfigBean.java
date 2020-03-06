/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.domain.vo;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/11/8 12:00
 */
public class SubConfigBean {
	/**
	 * text :
	 * fontStyle : {"fontSize":14,"fontWigth":400,"fontColor":"#333333"}
	 * bgColor : #ffffff
	 * hasBorder : 1
	 * borderColor : #e8e8e8
	 * borderWidth : 1
	 * borderRadius : 0
	 * btnWidth : 77
	 * btnHeight : 32
	 */

	private String text;
	private FontStyleBean fontStyle = new FontStyleBean();
	private String bgColor;
	private int hasBorder;
	private String borderColor;
	private int borderWidth;
	private int borderRadius;
	private int btnWidth;
	private int btnHeight;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public FontStyleBean getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(FontStyleBean fontStyle) {
		this.fontStyle = fontStyle;
	}

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

	public int getBtnWidth() {
		return btnWidth;
	}

	public void setBtnWidth(int btnWidth) {
		this.btnWidth = btnWidth;
	}

	public int getBtnHeight() {
		return btnHeight;
	}

	public void setBtnHeight(int btnHeight) {
		this.btnHeight = btnHeight;
	}

	public static class FontStyleBean {
		/**
		 * fontSize : 14
		 * fontWigth : 400
		 * fontColor : #333333
		 */

		private int fontSize = 14;
		private int fontWigth = 400;
		private String fontColor = "#333333";

		public int getFontSize() {
			return fontSize;
		}

		public void setFontSize(int fontSize) {
			this.fontSize = fontSize;
		}

		public int getFontWigth() {
			return fontWigth;
		}

		public void setFontWigth(int fontWigth) {
			this.fontWigth = fontWigth;
		}

		public String getFontColor() {
			return fontColor;
		}

		public void setFontColor(String fontColor) {
			this.fontColor = fontColor;
		}
	}
}
