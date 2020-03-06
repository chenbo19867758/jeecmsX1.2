package com.jeecms.channel.domain.dto;

import java.util.List;
import java.util.Map;

import com.jeecms.channel.domain.ChannelAttr;
import com.jeecms.content.domain.ChannelTxt;

/**
 * 修改栏目管理SEO信息扩展信息
 * 
 * @author: chenming
 * @date: 2019年5月5日 上午9:10:25
 */
public class ChannelSeoExtDto {

	/** id */
	private Integer id;
	/** SEO标题 */
	private String seoTitle;
	/** SEO关键字 */
	private String seoKeywork;
	/** SEO描述 */
	private String seoDescription;
	/** 自定义模板 */
	private List<ChannelAttr> attrs;
	/** 栏目内容集合*/
	private Map<String,String> txtMap;

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getSeoTitle() {
		return seoTitle;
	}

	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	public String getSeoKeywork() {
		return seoKeywork;
	}

	public void setSeoKeywork(String seoKeywork) {
		this.seoKeywork = seoKeywork;
	}

	public String getSeoDescription() {
		return seoDescription;
	}

	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}

	public List<ChannelAttr> getAttrs() {
		return attrs;
	}

	public void setAttrs(List<ChannelAttr> attrs) {
		this.attrs = attrs;
	}

	public Map<String,String> getTxtMap() {
		return txtMap;
	}

	public void setTxtMap(Map<String,String> txtMap) {
		this.txtMap = txtMap;
	}



}
