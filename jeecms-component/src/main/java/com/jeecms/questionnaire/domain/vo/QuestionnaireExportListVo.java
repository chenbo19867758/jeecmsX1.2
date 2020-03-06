/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.domain.vo;

import com.google.gson.internal.LinkedHashTreeMap;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/11/8 10:18
 */
public class QuestionnaireExportListVo {

	/**
	 * 问卷标题
	 */
	private String title;
	/**
	 * 回复人
	 */
	private String replayName;
	/**
	 * 参与时间
	 */
	private Date createTime;
	/**
	 * 访问设备
	 */
	private String device;
	/**
	 * 访问ip
	 */
	private String ip;
	/**
	 * 省市
	 */
	private String address;
	/**
	 * 是否有效
	 */
	private Boolean isEffective;

	private Boolean isFile;

	private List<String> fileUrls;

	private Map<String, String> attr = new LinkedHashTreeMap<String, String>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getReplayName() {
		return replayName;
	}

	public void setReplayName(String replayName) {
		this.replayName = replayName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Boolean getEffective() {
		return isEffective;
	}

	public void setEffective(Boolean effective) {
		isEffective = effective;
	}

	public Boolean getIsFile() {
		return isFile;
	}

	public void setIsFile(Boolean file) {
		isFile = file;
	}

	public List<String> getFileUrls() {
		return fileUrls;
	}

	public void setFileUrls(List<String> fileUrls) {
		this.fileUrls = fileUrls;
	}

	public Map<String, String> getAttr() {
		return attr;
	}

	public void setAttr(Map<String, String> attr) {
		this.attr = attr;
	}

}
