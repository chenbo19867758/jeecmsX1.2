/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.domain.vo;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/11/8 11:59
 */
public class FontConfigBean {
	/**
	 * titleStyle : {"fontSize":24,"fontWigth":600,"fontColor":"#333333","fontAlign":"left"}
	 * descStyle : {"fontSize":14,"fontWigth":400,"fontColor":"#333333","fontAlign":"left"}
	 * stemStyle : {"fontSize":14,"fontWigth":400,"fontColor":"#333333","fontAlign":"left"}
	 * optStyle : {"fontSize":14,"fontWigth":400,"fontColor":"#333333","fontAlign":"left"}
	 */

	private FontConfigBean.TitleStyleBean titleStyle = new FontConfigBean.TitleStyleBean();
	private FontConfigBean.DescStyleBean descStyle = new FontConfigBean.DescStyleBean();
	private FontConfigBean.StemStyleBean stemStyle = new FontConfigBean.StemStyleBean();
	private FontConfigBean.OptStyleBean optStyle = new FontConfigBean.OptStyleBean();

	public FontConfigBean.TitleStyleBean getTitleStyle() {
		return titleStyle;
	}

	public void setTitleStyle(FontConfigBean.TitleStyleBean titleStyle) {
		this.titleStyle = titleStyle;
	}

	public FontConfigBean.DescStyleBean getDescStyle() {
		return descStyle;
	}

	public void setDescStyle(FontConfigBean.DescStyleBean descStyle) {
		this.descStyle = descStyle;
	}

	public FontConfigBean.StemStyleBean getStemStyle() {
		return stemStyle;
	}

	public void setStemStyle(FontConfigBean.StemStyleBean stemStyle) {
		this.stemStyle = stemStyle;
	}

	public FontConfigBean.OptStyleBean getOptStyle() {
		return optStyle;
	}

	public void setOptStyle(FontConfigBean.OptStyleBean optStyle) {
		this.optStyle = optStyle;
	}

	public static class TitleStyleBean {
		/**
		 * fontSize : 24
		 * fontWigth : 600
		 * fontColor : #333333
		 * fontAlign : left
		 */

		private int fontSize = 24;
		private int fontWigth = 600;
		private String fontColor = "#333333";
		private String fontAlign = "left";

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

		public String getFontAlign() {
			return fontAlign;
		}

		public void setFontAlign(String fontAlign) {
			this.fontAlign = fontAlign;
		}
	}

	public static class DescStyleBean {
		/**
		 * fontSize : 14
		 * fontWigth : 400
		 * fontColor : #333333
		 * fontAlign : left
		 */

		private int fontSize = 14;
		private int fontWigth = 400;
		private String fontColor = "#333333";
		private String fontAlign = "left";

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

		public String getFontAlign() {
			return fontAlign;
		}

		public void setFontAlign(String fontAlign) {
			this.fontAlign = fontAlign;
		}
	}

	public static class StemStyleBean {
		/**
		 * fontSize : 14
		 * fontWigth : 400
		 * fontColor : #333333
		 * fontAlign : left
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

	public static class OptStyleBean {
		/**
		 * fontSize : 14
		 * fontWigth : 400
		 * fontColor : #333333
		 * fontAlign : left
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
