/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.vo;

/**
 * 模板列表VO
 * 
 * @author: ljw
 * @date: 2019年4月24日 下午1:54:49
 */
public class CmsModelTplVo {

	/** 模型ID **/
	private Integer modelId;
	/** 模型名称 **/
	private String modelName;
	/** 模板类型(1栏目模板 2内容模板 ) **/
	private Short type;
	/** 模板路径 **/
	private String tplPath;

	public CmsModelTplVo() {
	}

	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}

	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getTplPath() {
		return tplPath;
	}

	public void setTplPath(String tplPath) {
		this.tplPath = tplPath;
	}

}
