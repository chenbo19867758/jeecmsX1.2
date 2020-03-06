package com.jeecms.wechat.domain.dto;

/**
 * 
 * @Description: 接受前台提交审核dto
 * @author: chenming
 * @date:   2018年11月2日 下午4:46:54     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class AuditVersionDto {
	/** 获取授权小程序帐号的可选类目dto*/
	private CategoryDto categoryDto;
	/** 获取小程序的第三方提交代码的页面配置*/
	private String address; 
	/** 小程序的标签*/
	private String tag;
	/** 小程序页面的标题*/
	private String title;

	public CategoryDto getCategoryDto() {
		return categoryDto;
	}

	public void setCategoryDto(CategoryDto categoryDto) {
		this.categoryDto = categoryDto;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
