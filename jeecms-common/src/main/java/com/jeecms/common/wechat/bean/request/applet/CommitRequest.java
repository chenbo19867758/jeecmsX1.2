package com.jeecms.common.wechat.bean.request.applet;

/**
 * 
 * @Description: 为授权的小程序帐号上传小程序代码--request
 * @author: chenming
 * @date:   2018年10月31日 下午5:43:19     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class CommitRequest {
	
	/** 代码库中的代码模版ID*/
	private Integer templateId;
	/** 第三方自定义的配置*/
	private String extJson;
	/** 代码版本号*/
	private String userVersion;
	/** 代码描述*/
	private String userDesc;
	
	public Integer getTemplateId() {
		return templateId;
	}
	
	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}
	
	public String getExtJson() {
		return extJson;
	}
	
	public void setExtJson(String extJson) {
		this.extJson = extJson;
	}
	
	public String getUserVersion() {
		return userVersion;
	}
	
	public void setUserVersion(String userVersion) {
		this.userVersion = userVersion;
	}
	
	public String getUserDesc() {
		return userDesc;
	}
	
	public void setUserDesc(String userDesc) {
		this.userDesc = userDesc;
	}
	
	public CommitRequest(Integer templateId, String extJson, String userVersion, String userDesc) {
		super();
		this.templateId = templateId;
		this.extJson = extJson;
		this.userVersion = userVersion;
		this.userDesc = userDesc;
	}
	
	public CommitRequest() {
		super();
	}
	
}
