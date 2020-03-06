/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.resource.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.jeecms.channel.domain.Channel;
import com.jeecms.content.domain.Content;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

/**
 * 模板列表Vo
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/8/17 15:43
 */
public class TplVo {
	private Date lastModifiedDate;
	private String filename;
	private int size;
	private String name;
	private String root;
	private Boolean directory;
	private String sizeUnit;
	private Integer quote;
	private List<Channel> channels;
	private List<Content> contents;

	@Temporal(TemporalType.TIMESTAMP)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public Boolean getDirectory() {
		return directory;
	}

	public void setDirectory(Boolean directory) {
		this.directory = directory;
	}

	public String getSizeUnit() {
		return sizeUnit;
	}

	public void setSizeUnit(String sizeUnit) {
		this.sizeUnit = sizeUnit;
	}

	public List<Channel> getChannels() {
		return channels;
	}

	public void setChannels(List<Channel> channels) {
		this.channels = channels;
	}

	public List<Content> getContents() {
		return contents;
	}

	public void setContents(List<Content> contents) {
		this.contents = contents;
	}

	public Integer getQuote() {
		return quote;
	}

	public void setQuote(Integer quote) {
		this.quote = quote;
	}

	public TplVo() {
		super();
	}

	public TplVo(Date lastModifiedDate, String filename, int size, String name, String root, Boolean directory, String sizeUnit) {
		this.lastModifiedDate = lastModifiedDate;
		this.filename = filename;
		this.size = size;
		this.name = name;
		this.root = root;
		this.directory = directory;
		this.sizeUnit = sizeUnit;
	}
}
