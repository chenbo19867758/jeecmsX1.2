package com.jeecms.common.wechat.bean.request.applet.common;

/**
 * 
 * @Description: 小程序提交代码的对象
 * @author: chenming
 * @date:   2018年11月12日 下午4:03:22     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class AuditList {
	/** 小程序的页面*/
	private String address;
	/** 小程序的标签*/
	private String tag;
	/** 一级类目名称*/
	private String firstClass;
	/** 二级类目名称*/
	private String secondClass;
	/** 三级类目名称*/
	private String thirdClass;
	/** 一级类目的ID*/
	private Integer firstId;
	/** 二级类目的ID*/
	private Integer secondId;
	/** 三级类目的ID*/
	private Integer thirdId;
	/** 小程序页面的标题*/
	private String title;
	
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
	
	public String getFirstClass() {
		return firstClass;
	}
	
	public void setFirstClass(String firstClass) {
		this.firstClass = firstClass;
	}
	
	public String getSecondClass() {
		return secondClass;
	}
	
	public void setSecondClass(String secondClass) {
		this.secondClass = secondClass;
	}
	
	public String getThirdClass() {
		return thirdClass;
	}
	
	public void setThirdClass(String thirdClass) {
		this.thirdClass = thirdClass;
	}
	
	public Integer getFirstId() {
		return firstId;
	}
	
	public void setFirstId(Integer firstId) {
		this.firstId = firstId;
	}
	
	public Integer getSecondId() {
		return secondId;
	}
	
	public void setSecondId(Integer secondId) {
		this.secondId = secondId;
	}
	
	public Integer getThirdId() {
		return thirdId;
	}
	
	public void setThirdId(Integer thirdId) {
		this.thirdId = thirdId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
}
