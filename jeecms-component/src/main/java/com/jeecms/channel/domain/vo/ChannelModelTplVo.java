package com.jeecms.channel.domain.vo;

import java.util.List;

/**
 * 栏目查询内容模型末班Vo
 * @author: chenming
 * @date:   2019年7月16日 下午1:54:02
 */
public class ChannelModelTplVo {
	/** 模型名称*/
	private String modelName;
	/** 模型ID*/
	private Integer modelId;
	/** 手机模板*/
	private String tplMobile;
	/** PC模板*/
	private String tplPc;
	/** 手机模板集合*/
	private List<String> mobileTplPaths;
	/** PC模板集合*/
	private List<String> pcTplPaths;

	public ChannelModelTplVo(String modelName, Integer modelId) {
		super();
		this.modelName = modelName;
		this.modelId = modelId;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}

	public String getTplMobile() {
		return tplMobile;
	}

	public void setTplMobile(String tplMobile) {
		this.tplMobile = tplMobile;
	}

	public String getTplPc() {
		return tplPc;
	}

	public void setTplPc(String tplPc) {
		this.tplPc = tplPc;
	}

	public List<String> getMobileTplPaths() {
		return mobileTplPaths;
	}

	public void setMobileTplPaths(List<String> mobileTplPaths) {
		this.mobileTplPaths = mobileTplPaths;
	}

	public List<String> getPcTplPaths() {
		return pcTplPaths;
	}

	public void setPcTplPaths(List<String> pcTplPaths) {
		this.pcTplPaths = pcTplPaths;
	}

}
