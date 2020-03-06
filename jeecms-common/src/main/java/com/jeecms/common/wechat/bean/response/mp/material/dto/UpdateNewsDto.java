package com.jeecms.common.wechat.bean.response.mp.material.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.jeecms.common.wechat.bean.request.mp.material.UpdateNewsRequest;

/**
 * 修改图文素材dto
 * @author: tom
 * @date:   2019年3月8日 下午4:42:31
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class UpdateNewsDto {
	/** 要修改的图文素材id*/
	private Integer id;
	/** 获取修改图文素材request集合*/
	private List<UpdateNewsRequest> news;

	public List<UpdateNewsRequest> getNews() {
		return news;
	}

	public void setNews(List<UpdateNewsRequest> news) {
		this.news = news;
	}

	@NotNull
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}
