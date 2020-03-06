/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.domain.vo;

import java.util.List;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/11/22 15:37
 */
public class QuestionnairePicVo {

	/**
	 * 页眉图
	 */
	public static final Integer TYPE_HEAD_IMG = 1;
	/**
	 * 背景图
	 */
	public static final Integer TYPE_BG_IMG = 2;

	private List<ImgBean> headImg;

	private List<ImgBean> bgImg;

	public List<ImgBean> getHeadImg() {
		return headImg;
	}

	public void setHeadImg(List<ImgBean> headImg) {
		this.headImg = headImg;
	}

	public List<ImgBean> getBgImg() {
		return bgImg;
	}

	public void setBgImg(List<ImgBean> bgImg) {
		this.bgImg = bgImg;
	}

	public static class ImgBean {
		private Integer picId;

		private String picUrl;

		private String thumbnail;

		public Integer getPicId() {
			return picId;
		}

		public void setPicId(Integer picId) {
			this.picId = picId;
		}

		public String getPicUrl() {
			return picUrl;
		}

		public void setPicUrl(String picUrl) {
			this.picUrl = picUrl;
		}

		public String getThumbnail() {
			return thumbnail;
		}

		public void setThumbnail(String thumbnail) {
			this.thumbnail = thumbnail;
		}
	}
}
