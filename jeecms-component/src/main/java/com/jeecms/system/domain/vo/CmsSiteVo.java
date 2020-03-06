/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.vo;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.system.domain.CmsSite;

/**
 * 默认模板详情
 * 
 * @author: ljw
 * @date: 2019年9月7日 下午4:31:48
 */
public class CmsSiteVo {

	private Integer id;
	/** 手机首页模板 **/
	private String mobileHomePageTemplates;
	/** 手机模板方案 **/
	private String mobileSolution;
	/** PC首页模板 **/
	private String pcHomePageTemplates;
	/** PC模板方案 **/
	private String pcSolution;
	/** 模板集合 **/
	private List<JSONObject> modelTpls;

	/** 构造方法 **/
	public CmsSiteVo() {

	}
	
	/** 有参构造方法 **/
	public CmsSiteVo(CmsSite site) {
		this.id = site.getId();
		this.mobileHomePageTemplates = site.getMobileHomePageTemplates();
		this.mobileSolution = site.getMobileSolution();
		this.pcHomePageTemplates = site.getPcHomePageTemplates();
		this.pcSolution = site.getPcSolution();
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMobileHomePageTemplates() {
		return mobileHomePageTemplates;
	}

	public void setMobileHomePageTemplates(String mobileHomePageTemplates) {
		this.mobileHomePageTemplates = mobileHomePageTemplates;
	}

	public String getMobileSolution() {
		return mobileSolution;
	}

	public void setMobileSolution(String mobileSolution) {
		this.mobileSolution = mobileSolution;
	}

	public String getPcHomePageTemplates() {
		return pcHomePageTemplates;
	}

	public void setPcHomePageTemplates(String pcHomePageTemplates) {
		this.pcHomePageTemplates = pcHomePageTemplates;
	}

	public String getPcSolution() {
		return pcSolution;
	}

	public void setPcSolution(String pcSolution) {
		this.pcSolution = pcSolution;
	}

	public List<JSONObject> getModelTpls() {
		return modelTpls;
	}

	public void setModelTpls(List<JSONObject> modelTpls) {
		this.modelTpls = modelTpls;
	}

}
