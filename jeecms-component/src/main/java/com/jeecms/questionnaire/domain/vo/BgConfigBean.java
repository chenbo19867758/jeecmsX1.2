/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.domain.vo;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/11/8 11:58
 */
public class BgConfigBean {
	/**
	 * bgType : 2
	 * bgImage :
	 * alignType : left
	 * opacity : 100
	 * isRepeat : 1
	 * bgColor : #F0F0F0
	 */

	private int bgType = 2;
	private Integer bgImage;
	private String bgImageUrl;
	private String alignType;
	private int opacity = 100;
	private int isRepeat = 1;
	private String bgColor;

	public int getBgType() {
		return bgType;
	}

	public void setBgType(int bgType) {
		this.bgType = bgType;
	}

	public Integer getBgImage() {
		return bgImage;
	}

	public void setBgImage(Integer bgImage) {
		this.bgImage = bgImage;
	}

	public String getBgImageUrl() {
		return bgImageUrl;
	}

	public void setBgImageUrl(String bgImageUrl) {
		this.bgImageUrl = bgImageUrl;
	}

	public String getAlignType() {
		return alignType;
	}

	public void setAlignType(String alignType) {
		this.alignType = alignType;
	}

	public int getOpacity() {
		return opacity;
	}

	public void setOpacity(int opacity) {
		this.opacity = opacity;
	}

	public int getIsRepeat() {
		return isRepeat;
	}

	public void setIsRepeat(int isRepeat) {
		this.isRepeat = isRepeat;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}
}
